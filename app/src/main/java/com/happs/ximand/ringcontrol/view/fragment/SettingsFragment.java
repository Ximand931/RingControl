package com.happs.ximand.ringcontrol.view.fragment;

import android.content.DialogInterface;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.happs.ximand.ringcontrol.OnEventListener;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.FragmentSettingsBinding;
import com.happs.ximand.ringcontrol.view.BaseFragment;
import com.happs.ximand.ringcontrol.viewmodel.InputAlertDialogDto;
import com.happs.ximand.ringcontrol.viewmodel.fragment.SettingsViewModel;

public class SettingsFragment extends BaseFragment<SettingsViewModel, FragmentSettingsBinding> {

    private AlertDialog alertDialog;
    private TextInputLayout inputLayout;
    private TextInputEditText editText;
    private OnEventListener<String> inputCompleteListener;

    public SettingsFragment() {
        super(R.layout.fragment_settings, MENU_NONE);
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    protected void onPreViewModelAttaching(@NonNull SettingsViewModel viewModel) {
        viewModel.getSetRingDurationEvent().observe(
                getViewLifecycleOwner(), this::makeInputAlertDialog
        );
    }

    private void makeInputAlertDialog(InputAlertDialogDto dialogDto) {
        this.inputCompleteListener = dialogDto.getOnCompleteInput();
        inflateInputLayout();
        MaterialAlertDialogBuilder dialogBuilder =
                new MaterialAlertDialogBuilder(requireContext());
        dialogBuilder.setView(inputLayout);
        this.alertDialog = dialogBuilder
                .setPositiveButton(dialogDto.getPositiveButtonResId(), this::onPositiveButtonClick)
                .setNegativeButton(
                        dialogDto.getNegativeButtonResId(), (dialog, which) -> dialog.dismiss()
                )
                .create();
        alertDialog.show();
    }

    private void inflateInputLayout() {
        View inputLayout = getLayoutInflater()
                .inflate(R.layout.alert_input_dialog, (ViewGroup) getView(), false);
        this.editText = inputLayout.findViewById(R.id.edit_text);
        this.inputLayout = (TextInputLayout) inputLayout;
    }

    private void onPositiveButtonClick(DialogInterface dialog, int witch) {
        Editable text = editText.getText();
        String filledLine = null;
        if (text != null) {
            filledLine = editText.getText().toString();
        }
        inputCompleteListener.onEvent(filledLine);
    }
}
