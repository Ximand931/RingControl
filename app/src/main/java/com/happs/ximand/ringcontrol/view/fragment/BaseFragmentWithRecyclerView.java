package com.happs.ximand.ringcontrol.view.fragment;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.happs.ximand.ringcontrol.view.BaseFragment;
import com.happs.ximand.ringcontrol.view.adapter.BaseRecyclerViewAdapter;
import com.happs.ximand.ringcontrol.viewmodel.fragment.BaseViewModel;

import java.util.List;

public abstract class BaseFragmentWithRecyclerView<VM extends BaseViewModel,
        D extends ViewDataBinding, T, A extends BaseRecyclerViewAdapter<T, ? extends ViewHolder>>
        extends BaseFragment<VM, D> {

    private RecyclerView recyclerView;

    public BaseFragmentWithRecyclerView(int layoutId, int menuResId) {
        super(layoutId, menuResId);
    }

    @Override
    protected void onViewDataBindingCreated(@NonNull D binding) {
        this.recyclerView = getRecyclerViewFromBinding(binding);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    protected abstract RecyclerView getRecyclerViewFromBinding(D binding);

    protected void initAdapter(List<T> items) {
        if (items == null) {
            throw new IllegalStateException();
        }
        A adapter = getAdapter();
        A newAdapter = createNewAdapter(items);
        onPreAttachRecyclerViewAdapter(newAdapter);
        recyclerView.setAdapter(newAdapter);
        if (adapter != null) {
            recyclerView.invalidate();
        }
    }

    protected abstract A createNewAdapter(List<T> items);

    protected void onPreAttachRecyclerViewAdapter(A adapter) {

    }

    @SuppressWarnings("unchecked")
    protected A getAdapter() {
        return (A) recyclerView.getAdapter();
    }

}
