package com.happs.ximand.ringcontrol.view;

import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.happs.ximand.ringcontrol.R;

@SuppressWarnings("UnusedReturnValue")
public class SnackbarBuilder {

    private Snackbar snackbar;

    public SnackbarBuilder(View view) {
        this.snackbar = Snackbar.make(view, R.string.stub, Snackbar.LENGTH_SHORT);
    }

    public SnackbarBuilder setText(int textResId) {
        snackbar.setText(textResId);
        return this;
    }

    public SnackbarBuilder setDuration(int duration) {
        snackbar.setDuration(duration);
        return this;
    }

    public SnackbarBuilder setAction(int actionResId, View.OnClickListener clickListener) {
        snackbar.setAction(actionResId, clickListener);
        return this;
    }

    public SnackbarBuilder setFormatText(int formatStringResId, Object... args) {
        Resources res = snackbar.getContext().getResources();
        String textFormat = res.getString(formatStringResId);
        snackbar.setText(String.format(textFormat, args));
        return this;
    }

    public SnackbarBuilder setIcon(int iconResId) {
        View snackbarLayout = snackbar.getView();
        TextView textView = snackbarLayout
                .findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
        Resources res = snackbar.getContext().getResources();
        textView.setCompoundDrawablePadding(
                res.getDimensionPixelOffset(R.dimen.snackbar_icon_padding));
        return this;
    }

    public Snackbar getSnackbar() {
        return snackbar;
    }
}
