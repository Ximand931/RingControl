package com.happs.ximand.ringcontrol.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;

import com.happs.ximand.ringcontrol.OnEventListener;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.ItemTimetableBinding;
import com.happs.ximand.ringcontrol.model.object.timetable.Timetable;

import java.util.List;

public class AllTimetablesRecyclerViewAdapter extends BaseRecyclerViewAdapter<Timetable, AllTimetablesRecyclerViewAdapter.AllTimetablesViewHolder> {

    private ObservableBoolean applyingPossible;
    private ObservableInt appliedTimetableId;
    @Nullable
    private OnEventListener<Timetable> applyTimetableClickListener;
    @Nullable
    private OnEventListener<Timetable> detailsTimetableClickListener;

    public AllTimetablesRecyclerViewAdapter(List<Timetable> timetables) {
        super(timetables);
        this.applyingPossible = new ObservableBoolean(false);
        this.appliedTimetableId = new ObservableInt(-1);
    }

    public void setApplyingPossible(boolean applyingPossible) {
        this.applyingPossible.set(applyingPossible);
    }

    public void setAppliedTimetableId(int id) {
        this.appliedTimetableId.set(id);
    }

    public void setApplyTimetableClickListener(@Nullable OnEventListener<Timetable>
                                                       applyTimetableClickListener) {
        this.applyTimetableClickListener = applyTimetableClickListener;
    }

    public void setDetailsTimetableClickListener(@Nullable OnEventListener<Timetable>
                                                         detailsTimetableClickListener) {
        this.detailsTimetableClickListener = detailsTimetableClickListener;
    }

    @NonNull
    @Override
    public AllTimetablesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemTimetableBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_timetable, parent, false);
        return new AllTimetablesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AllTimetablesViewHolder holder, int position) {
        holder.bind(getItems().get(position), applyingPossible, appliedTimetableId,
                applyTimetableClickListener, detailsTimetableClickListener);
    }

    static class AllTimetablesViewHolder extends RecyclerView.ViewHolder {

        private ItemTimetableBinding binding;

        AllTimetablesViewHolder(@NonNull ItemTimetableBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Timetable timetable, ObservableBoolean applyingPossible, ObservableInt appliedTimetableId,
                  OnEventListener<Timetable> applyTimetableClickListener,
                  OnEventListener<Timetable> detailsTimetableClickListener) {
            binding.setTimetable(timetable);
            binding.setApplyingPossible(applyingPossible);
            binding.setAppliedTimetableId(appliedTimetableId);
            binding.setApplyClickListener(applyTimetableClickListener);
            binding.setDetailsClickListener(detailsTimetableClickListener);
            binding.executePendingBindings();
        }
    }
}
