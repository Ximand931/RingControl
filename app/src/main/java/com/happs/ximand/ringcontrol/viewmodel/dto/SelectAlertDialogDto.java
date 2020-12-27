package com.happs.ximand.ringcontrol.viewmodel.dto;

import com.happs.ximand.ringcontrol.OnEventListener;

public class SelectAlertDialogDto {

    private int titleResId;
    private int itemsArrayResId;
    private OnEventListener<Integer> selectListener;
    private OnEventListener<Void> cancelListener;

    public int getTitleResId() {
        return titleResId;
    }

    public SelectAlertDialogDto setTitleResId(int titleResId) {
        this.titleResId = titleResId;
        return this;
    }

    public int getItemsArrayResId() {
        return itemsArrayResId;
    }

    public SelectAlertDialogDto setItemsArrayResId(int itemsArrayResId) {
        this.itemsArrayResId = itemsArrayResId;
        return this;
    }

    public OnEventListener<Integer> getSelectListener() {
        return selectListener;
    }

    public SelectAlertDialogDto setSelectListener(OnEventListener<Integer> selectListener) {
        this.selectListener = selectListener;
        return this;
    }

    public OnEventListener<Void> getCancelListener() {
        return cancelListener;
    }

    public SelectAlertDialogDto setCancelListener(OnEventListener<Void> cancelListener) {
        this.cancelListener = cancelListener;
        return this;
    }
}
