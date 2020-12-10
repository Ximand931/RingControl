package com.happs.ximand.ringcontrol.viewmodel.item;

import androidx.databinding.BaseObservable;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public abstract class ItemViewModelFactory<VM extends BaseItemViewModel, T> {

    public abstract VM createViewModelByItem(T item);

    public List<VM> createViewModelsByItemList(List<T> items) {
        List<VM> viewModels = new ArrayList<>();
        for (T item : items) {
            viewModels.add(createViewModelByItem(item));
        }
        return viewModels;
    }

}
