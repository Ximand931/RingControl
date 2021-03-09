package com.happs.ximand.ringcontrol.view.fragment

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import com.happs.ximand.ringcontrol.R
import com.happs.ximand.ringcontrol.databinding.FragmentExceptionBinding
import com.happs.ximand.ringcontrol.model.`object`.exception.BaseException
import com.happs.ximand.ringcontrol.viewmodel.fragment.ExceptionViewModel

class ExceptionFragment : BaseFragment<ExceptionViewModel, FragmentExceptionBinding>(R.layout.fragment_exception, MENU_NONE) {

    companion object {
        @JvmStatic
        fun newInstance(exception: BaseException): ExceptionFragment {
            val fragment = ExceptionFragment()
            fragment.exception = exception
            return fragment
        }
    }

    private var exception: BaseException? = null

    override fun onSetActionBarTitle(actionBar: ActionBar) {
        actionBar.setTitle(R.string.error_reasons)
    }

    override fun onPreViewModelAttaching(viewModel: ExceptionViewModel) {
        if (exception != null) {
            viewModel.attachException(exception!!)
        }
        viewModel.navigateToSupportLiveEvent.observe(viewLifecycleOwner, Observer {
            navigateToSendEmailActivity(it)
        })
        viewModel.navigateToPermissionSettingsLiveEvent.observe(viewLifecycleOwner, Observer {
            navigateToPermissionSettings()
        })
        viewModel.restartApplicationLiveEvent.observe(viewLifecycleOwner, Observer {
            restartApplication()
        })
    }

    private fun navigateToPermissionSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun navigateToSendEmailActivity(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_EMAIL, email)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            requireViewModel().onNoOneEmailClient()
        }
    }

    private fun restartApplication() {
        val intent = getLaunchIntent()
        if (intent != null) {
            val componentName = intent.component
            val mainIntent = Intent.makeRestartActivityTask(componentName)
            requireContext().startActivity(mainIntent)
        }
        Runtime.getRuntime().exit(0)
    }

    private fun getLaunchIntent(): Intent? {
        val packageManager = requireContext().packageManager
        return packageManager.getLaunchIntentForPackage(requireContext().packageName)
    }

}