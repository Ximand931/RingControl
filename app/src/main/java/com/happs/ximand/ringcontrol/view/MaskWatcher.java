package com.happs.ximand.ringcontrol.view;

import android.text.Editable;
import android.text.TextWatcher;

public class MaskWatcher implements TextWatcher {

    private static final char INPUT_CHAR = 's';
    private String mask;
    private boolean adding;

    MaskWatcher(String mask) {
        this.mask = mask;
    }

    void setMask(String mask) {
        this.mask = mask;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        this.adding = after > count;
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        int length = editable.length();
        if (mask.length() > length) {
            char currentMaskChar = mask.charAt(length);
            if (adding) {
                if (currentMaskChar != INPUT_CHAR) {
                    editable.append(currentMaskChar);
                }
            } else {
                if (currentMaskChar != INPUT_CHAR) {
                    editable.delete(editable.length() - 1, editable.length());
                }
            }
        }
    }
}
