package com.happs.ximand.ringcontrol.view.adapter;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.happs.ximand.ringcontrol.BR;
import com.happs.ximand.ringcontrol.viewmodel.item.BaseItemViewModel;

public class BindingViewHolder extends RecyclerView.ViewHolder {
    private ViewDataBinding binding;

    BindingViewHolder(@NonNull ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Object o) {
        binding.setVariable(BR.viewModel, o);
    }
}
