package com.happs.ximand.ringcontrol.view.fragment

import android.content.DialogInterface
import android.os.Handler
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.databinding.FragmentSettingsBinding
import com.happs.ximand.ringcontrol.viewmodel.dto.InputAlertDialogDto
import com.happs.ximand.ringcontrol.viewmodel.dto.SelectAlertDialogDto
import com.happs.ximand.ringcontrol.viewmodel.fragment.SettingsViewModel

class SettingsFragment : BaseFragment<SettingsViewModel, FragmentSettingsBinding>(R.layout.fragment_settings, MENU_NONE) {

    private var inputAlertDialog: AlertDialog? = null

    private var inputLayout: TextInputLayout? = null
    private var editText: TextInputEditText? = null
    private var inputCompleteListener: ((input: String) -> Unit)? = null
    private var cancelListener: (() -> Unit)? = null

    companion object {
        @JvmStatic
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    override fun onPreViewModelAttaching(viewModel: SettingsViewModel) {
        viewModel.inputIncorrectEvent.observe(viewLifecycleOwner, Observer { errorMessageId: Int ->
            showDialogWithIncorrectInputError(errorMessageId)
        })
        viewModel.setRingDurationEvent.observe(viewLifecycleOwner, Observer { dialogDto: InputAlertDialogDto ->
            makeInputAlertDialog(dialogDto)
        })
        viewModel.setWeekendModeEvent.observe(viewLifecycleOwner, Observer { dialogDto: SelectAlertDialogDto ->
            makeSelectAlertDialog(dialogDto)
        })
        viewModel.setManualRingDurationEvent.observe(viewLifecycleOwner, Observer { dialogDto: InputAlertDialogDto ->
            makeInputAlertDialog(dialogDto)
        })
        viewModel.dismissDialogEvent.observe(viewLifecycleOwner, Observer { dismissDialog() })
    }

    private fun makeSelectAlertDialog(dialogDto: SelectAlertDialogDto) {
        cancelListener = dialogDto.cancelListener
        MaterialAlertDialogBuilder(requireContext())
                .setTitle(dialogDto.titleResId)
                .setItems(dialogDto.itemsArrayResId
                ) { _: DialogInterface?, which: Int -> dialogDto.selectListener?.invoke(which) }
                .setOnCancelListener { onNegativeButtonClick(it) }
                .show()
    }

    private fun makeInputAlertDialog(dialogDto: InputAlertDialogDto) {
        inputCompleteListener = dialogDto.inputCompleteListener
        cancelListener = dialogDto.cancelListener
        inflateInputLayout()
        inputAlertDialog = createInputDialogBuilder().create()
        inputAlertDialog!!.show()
        initAlertDialog(dialogDto.titleResId)
    }

    private fun dismissDialog() {
        inputAlertDialog!!.dismiss()
    }

    private fun showDialogWithIncorrectInputError(errorMessageId: Int) {
        inputAlertDialog!!.show()
        inputLayout!!.error = resources.getString(errorMessageId)
        Handler().postDelayed({ inputLayout!!.error = null }, 5000)
    }

    private fun inflateInputLayout() {
        val inputLayout = layoutInflater
                .inflate(R.layout.alert_input_dialog, view as ViewGroup?, false)
        editText = inputLayout.findViewById(R.id.edit_text)
        this.inputLayout = inputLayout as TextInputLayout
    }

    private fun initAlertDialog(hintResId: Int) {
        inputLayout!!.hint = resources.getString(hintResId)
        initAlertDialogBackground()
        initPositiveButton()
        initNegativeButton()
    }

    private fun initPositiveButton() {
        val positive = inputAlertDialog!!.getButton(AlertDialog.BUTTON_POSITIVE)
        positive.setOnClickListener { onPositiveButtonClick() }
        initAlertDialogButton(positive)
    }

    private fun initNegativeButton() {
        val negative = inputAlertDialog!!.getButton(AlertDialog.BUTTON_NEGATIVE)
        initAlertDialogButton(negative)
    }

    private fun onPositiveButtonClick() {
        val text = editText!!.text
        var filledLine: String? = null
        if (text != null) {
            filledLine = editText!!.text.toString()
        }
        inputCompleteListener!!.invoke(filledLine!!)
    }

    override fun onSetActionBarTitle(actionBar: ActionBar) {
        actionBar.setTitle(R.string.settings)
    }

    private fun initAlertDialogButton(button: Button) {
        button.isAllCaps = false
        button.letterSpacing = 0f
    }

    private fun initAlertDialogBackground() {
        if (inputAlertDialog!!.window != null) {
            inputAlertDialog!!.window!!.decorView.setBackgroundResource(R.drawable.bg_rounded)
        }
    }

    private fun createInputDialogBuilder(): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(requireContext())
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, _: Int -> onNegativeButtonClick(dialog) }
                .setOnCancelListener { dialog: DialogInterface -> onNegativeButtonClick(dialog) }
                .setView(inputLayout)
    }

    private fun onNegativeButtonClick(dialog: DialogInterface) {
        cancelListener!!.invoke()
        dialog.dismiss()
    }
}