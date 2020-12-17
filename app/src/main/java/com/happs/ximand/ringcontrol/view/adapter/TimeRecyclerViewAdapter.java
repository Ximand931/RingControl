package com.happs.ximand.ringcontrol.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.ItemTimeBinding;
import com.happs.ximand.ringcontrol.model.object.Lesson;

import java.util.List;

public class TimeRecyclerViewAdapter
        extends BaseRecyclerViewAdapter<Lesson, TimeRecyclerViewAdapter.TimeViewHolder> {

    public TimeRecyclerViewAdapter(List<Lesson> lessons) {
        super(lessons);
    }

    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemTimeBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.item_time, parent, false);
        return new TimeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeViewHolder holder, int position) {
        holder.bind(getItems().get(position));
    }

    static class TimeViewHolder extends RecyclerView.ViewHolder {

        private ItemTimeBinding binding;

        TimeViewHolder(ItemTimeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Lesson lesson) {
            binding.setLesson(lesson);
            binding.executePendingBindings();
        }
    }
}
