package com.happs.ximand.ringcontrol.view.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;

import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.ItemEditLessonBinding;
import com.happs.ximand.ringcontrol.model.object.Lesson;
import com.happs.ximand.ringcontrol.view.MaskWatcher;
import com.happs.ximand.ringcontrol.viewmodel.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class EditTimetableRecyclerViewAdapter extends BaseRecyclerViewAdapter<Lesson,
        EditTimetableRecyclerViewAdapter.EditTimetableViewHolder> {

    private static final String START_END_TIME_DIVIDER = " – ";

    private final List<ObservableField<String>> inputs;
    private final List<MaskWatcher> maskWatchers;
    private final ObservableInt errorId = new ObservableInt(-1);
    private boolean detailEditing = true;

    public EditTimetableRecyclerViewAdapter(List<Lesson> lessons) {
        super(lessons);
        this.inputs = new ArrayList<>();
        this.maskWatchers = new ArrayList<>();
    }

    public void addItem(Lesson lesson) {
        getItems().add(lesson);
        inputs.add(new ObservableField<>());
        maskWatchers.add(createMaskWatcher());
        notifyItemRangeInserted(getItems().size() - 1, 1);
    }

    private MaskWatcher createMaskWatcher() {
        final String mask = getMaskForCurrentDetailEditingStatus();
        return new MaskWatcher(mask);
    }

    public void removeLastItem() {
        int lastItemIndex = getItems().size() - 1;
        this.getItems().remove(lastItemIndex);
        notifyItemRemoved(lastItemIndex);
    }

    public void setErrorId(int id) {
        errorId.set(id);
    }

    public void changeDetailEdititngStatus() {
        detailEditing = !detailEditing;
        for (MaskWatcher mask : maskWatchers) {
            mask.setMask(getMaskForCurrentDetailEditingStatus());
        }
        updateInputsForNewDetailEditingStatus();
    }

    private void updateInputsForNewDetailEditingStatus() {
        if (detailEditing) {
            updateInputsFromSimpleToDetailed();
        } else {
            updateInputsFromDetailedToSimple();
        }
    }

    private void updateInputsFromSimpleToDetailed() {
        for (ObservableField<String> input : inputs) {
            String inputValue = input.get();
            if (isInputCorrect(inputValue)) {
                String startTime = getDetailedStartTime(inputValue);
                String endTime = getDetailedEndTime(inputValue);
                input.set(startTime + START_END_TIME_DIVIDER + endTime);
            }
        }
    }

    private void updateInputsFromDetailedToSimple() {
        for (ObservableField<String> input : inputs) {
            String inputValue = input.get();
            if (isInputCorrect(inputValue)) {
                String startTime = getDetailedStartTime(inputValue);
                String endTime = getDetailedEndTime(inputValue);
                input.set(
                        startTime.substring(0, startTime.lastIndexOf(":"))
                                + START_END_TIME_DIVIDER
                                + endTime.substring(0, endTime.lastIndexOf(":"))
                );
            }
        }
    }

    private String getDetailedStartTime(String input) {
        if (detailEditing) {
            return input.substring(0, 8);
        } else {
            return input.substring(0, 5) + ":00";
        }
    }

    private String getDetailedEndTime(String input) {
        if (detailEditing) {
            return input.substring(11);
        } else {
            return input.substring(8) + ":00";
        }
    }

    private boolean isInputCorrect(String input) {
        return TextUtils.isEmpty(input)
                && input.matches(getPatternForCurrentDetailedEditingStatus());
    }

    private String getPatternForCurrentDetailedEditingStatus() {
        return detailEditing ?
                TimeUtils.DETAILED_TIME_PATTERN : TimeUtils.SIMPLE_TIME_PATTERN;
    }

    private String getMaskForCurrentDetailEditingStatus() {
        return detailEditing ?
                TimeUtils.DETAILED_TIME_MASK : TimeUtils.SIMPLE_TIME_MASK;
    }

    public List<ObservableField<String>> getInputs() {
        return inputs;
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
        holder.bind(getItems().get(position),
                inputs.get(position), errorId, maskWatchers.get(position));
    }

    static class EditTimetableViewHolder extends RecyclerView.ViewHolder {

        private ItemEditLessonBinding binding;

        EditTimetableViewHolder(@NonNull ItemEditLessonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Lesson lesson, ObservableField<String> input,
                  ObservableInt errorId, MaskWatcher maskWatcher) {
            binding.setNumber(lesson.getNumber());
            binding.setInput(input);
            binding.setErrorId(errorId);
            binding.input.addTextChangedListener(maskWatcher);
        }

    }

}
