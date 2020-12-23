package com.happs.ximand.ringcontrol.view.fragment;

import android.content.DialogInterface;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
    private OnEventListener<Void> cancelListener;

    public SettingsFragment() {
        super(R.layout.fragment_settings, MENU_NONE);
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    protected void onPreViewModelAttaching(@NonNull SettingsViewModel viewModel) {
        viewModel.getInputIncorrectEvent().observe(
                getViewLifecycleOwner(), this::showDialogWithIncorrectInputError
        );
        viewModel.getDismissDialogEvent().observe(
                getViewLifecycleOwner(), this::dismissDialog
        );
        viewModel.getSetRingDurationEvent().observe(
                getViewLifecycleOwner(), this::makeInputAlertDialog
        );
    }

    private void makeInputAlertDialog(InputAlertDialogDto dialogDto) {
        this.inputCompleteListener = dialogDto.getOnCompleteInput();
        this.cancelListener = dialogDto.getOnCancel();
        inflateInputLayout();
        this.alertDialog = createInputDialogBuilder().create();
        this.alertDialog.show();
        initAlertDialog(dialogDto.getTitleResId());
    }

    private void dismissDialog(Void aVoid) {
        alertDialog.dismiss();
    }

    private void showDialogWithIncorrectInputError(int errorMessageId) {
        alertDialog.show();
        inputLayout.setError(getResources().getString(errorMessageId));
        new Handler().postDelayed(() -> inputLayout.setError(null), 5000);
    }

    private void inflateInputLayout() {
        View inputLayout = getLayoutInflater()
                .inflate(R.layout.alert_input_dialog, (ViewGroup) getView(), false);
        this.editText = inputLayout.findViewById(R.id.edit_text);
        this.inputLayout = (TextInputLayout) inputLayout;
    }

    private void initAlertDialog(int hintResId) {
        inputLayout.setHint(getResources().getString(hintResId));
        initAlertDialogBackground();
        initPositiveButton();
        initNegativeButton();
    }

    private void initPositiveButton() {
        Button positive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positive.setOnClickListener(v -> onPositiveButtonClick());
        initAlertDialogButton(positive);
    }

    private void initNegativeButton() {
        Button negative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        initAlertDialogButton(negative);
    }

    private void onPositiveButtonClick() {
        Editable text = editText.getText();
        String filledLine = null;
        if (text != null) {
            filledLine = editText.getText().toString();
        }
        inputCompleteListener.onEvent(filledLine);
    }

    private void initAlertDialogButton(Button button) {
        button.setAllCaps(false);
        button.setLetterSpacing(0f);
    }

    private void initAlertDialogBackground() {
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().getDecorView().setBackgroundResource(R.drawable.bg_rounded);
        }
    }

    private MaterialAlertDialogBuilder createInputDialogBuilder() {
        return new MaterialAlertDialogBuilder(requireContext())
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, this::onNegativeButtonClick)
                .setOnCancelListener(dialog -> onNegativeButtonClick(dialog, 0))
                .setView(inputLayout);
    }

    private void onNegativeButtonClick(DialogInterface dialog, int witch) {
        cancelListener.onEvent(null);
        dialog.dismiss();
    }
}
