package com.happs.ximand.ringcontrol.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.ItemEditLessonBinding;
import com.happs.ximand.ringcontrol.viewmodel.item.EditItemViewModel;

import java.util.List;

public class EditTimetableRecyclerViewAdapter extends RecyclerView.Adapter<BindingViewHolder> {

    private final List<EditItemViewModel> viewModels;

    public EditTimetableRecyclerViewAdapter(List<EditItemViewModel> viewModels) {
        this.viewModels = viewModels;
    }

    public List<EditItemViewModel> getViewModels() {
        return viewModels;
    }

    public void addItem(EditItemViewModel viewModel) {
        this.viewModels.add(viewModel);
        notifyItemRangeInserted(viewModels.size() - 1, 1);
    }

    public void removeLastItem() {
        int lastItemIndex = viewModels.size() - 1;
        this.viewModels.remove(lastItemIndex);
        notifyItemRemoved(lastItemIndex);
    }

    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemEditLessonBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.item_edit_lesson, parent, false);
        return new BindingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
        holder.bind(viewModels.get(position));
    }

    @Override
    public int getItemCount() {
        return viewModels.size();
    }
}
