package com.happs.ximand.ringcontrol.viewmodel.util;

import android.os.Handler;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

public final class BindingAdapters {

    private BindingAdapters() {

    }

    @BindingAdapter("adapter")
    public static void setRecyclerViewAdapter(@NonNull RecyclerView recyclerView,
                                              @Nullable RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        if (adapter != null) {
            recyclerView.setAdapter(adapter);
        }
    }

    @BindingAdapter("textWatcher")
    public static void setTextWatcher(@NonNull EditText editText,
                                      @Nullable TextWatcher textWatcher) {
        if (textWatcher != null) {
            editText.addTextChangedListener(textWatcher);
        }
    }

    @BindingAdapter("errorText")
    public static void setErrorText(@NonNull TextInputLayout layout, @Nullable String error) {
        if (layout.getError() == null && error == null) {
            return;
        }
        layout.setError(error);
        new Handler().postDelayed(() -> layout.setError(""), 5000);
    }


}
