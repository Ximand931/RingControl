package com.happs.ximand.ringcontrol.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.ItemEditLessonBinding;
import com.happs.ximand.ringcontrol.model.object.Lesson;
import com.happs.ximand.ringcontrol.viewmodel.item.EditItemViewModel;

import java.util.List;

public class EditTimetableRecyclerViewAdapter extends BaseRecyclerViewAdapter<Lesson,
        EditTimetableRecyclerViewAdapter.EditTimetableViewHolder> {

    public EditTimetableRecyclerViewAdapter(List<Lesson> lessons) {
        super(lessons);
    }

    public void addItem(Lesson lesson) {
        getItems().add(lesson);
        notifyItemRangeInserted(getItems().size() - 1, 1);
    }

    public void removeLastItem() {
        int lastItemIndex = getItems().size() - 1;
        this.getItems().remove(lastItemIndex);
        notifyItemRemoved(lastItemIndex);
    }

    @NonNull
    @Override
    public EditTimetableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemEditLessonBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.item_edit_lesson, parent, false);
        return new EditTimetableViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EditTimetableViewHolder holder, int position) {
        holder.bind(getItems().get(position));
    }

    static class EditTimetableViewHolder extends RecyclerView.ViewHolder {

        private ItemEditLessonBinding binding;

        EditTimetableViewHolder(@NonNull ItemEditLessonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Lesson lesson) {
            binding.setNumber(lesson.getNumber());
            //binding.setInput();
        }
    }

}
