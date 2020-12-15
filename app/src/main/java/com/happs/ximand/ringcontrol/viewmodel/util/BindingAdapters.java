package com.happs.ximand.ringcontrol.viewmodel.util;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.google.android.material.textfield.TextInputLayout;

public final class BindingAdapters {

    private static final int ERROR_MESSAGE_SHOW_DURATION = 5000;

    private BindingAdapters() {

    }

    @BindingAdapter("constantErrorText")
    public static void setConstantErrorText(@NonNull TextInputLayout layout, @Nullable String error) {
        setErrorText(layout, error);
    }

    @BindingAdapter("temporaryErrorText")
    public static void setTemporaryErrorText(@NonNull TextInputLayout layout,
                                             @Nullable String error) {
        boolean success =
                setErrorText(layout, error);
        if (success) {
            new Handler().postDelayed(() -> layout.setError(null), ERROR_MESSAGE_SHOW_DURATION);
        }
    }

    private static boolean setErrorText(@NonNull TextInputLayout layout, @Nullable String error) {
        if (layout.getError() == null && error == null) {
            return false;
        }
        layout.setError(error);
        return true;
    }


}
