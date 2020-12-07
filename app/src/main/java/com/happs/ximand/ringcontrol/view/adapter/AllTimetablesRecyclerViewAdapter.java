package com.happs.ximand.ringcontrol.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.ItemTimetableBinding;
import com.happs.ximand.ringcontrol.model.object.Lesson;
import com.happs.ximand.ringcontrol.viewmodel.util.TimeHelper;
import com.happs.ximand.ringcontrol.viewmodel.item.NewTimetableItemViewModel;

import java.util.List;

public class AllTimetablesRecyclerViewAdapter extends RecyclerView.Adapter<BindingViewHolder> {

    private List<NewTimetableItemViewModel> timetables;

    public AllTimetablesRecyclerViewAdapter(List<NewTimetableItemViewModel> timetables) {
        this.timetables = timetables;
    }

    public void updateTimetables(List<NewTimetableItemViewModel> timetables) {
        int size = this.timetables.size();
        int newSize = timetables.size();
        this.timetables = timetables;
        if (size == newSize) {
            notifyDataSetChanged();
        } else if (size > newSize) {
            notifyItemRangeRemoved(newSize, size);
        } else {
            notifyItemRangeInserted(size, newSize);
        }
    }

    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemTimetableBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_timetable, parent, false);
        return new BindingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
        holder.bind(timetables.get(position));
    }

    @Override
    public int getItemCount() {
        return timetables.size();
    }

    @Deprecated
    public static String mapLessonListToString(List<Lesson> lessons) {
        StringBuilder previewBuilder = new StringBuilder();
        for (Lesson lesson : lessons) {
            if (previewBuilder.length() > 86) {
                break;
            }
            previewBuilder
                    .append(TimeHelper.getPreviewTime(lesson.getStartTimeDep()))
                    .append(" - ")
                    .append(TimeHelper.getPreviewTime(lesson.getEndTimeDep()))
                    .append(", ");
        }
        return prunePreviewString(previewBuilder.toString());
    }

    private static String prunePreviewString(String s) {
        if (s.length() > 86) {
            return s.substring(
                    0, s.substring(0, 86).lastIndexOf(',')
            ) + "...";
        } else {
            return s.substring(0, s.length() - 2);
        }
    }

}
