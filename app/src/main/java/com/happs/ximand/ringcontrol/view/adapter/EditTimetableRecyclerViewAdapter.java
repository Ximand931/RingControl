package com.happs.ximand.ringcontrol.view.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import com.happs.ximand.ringcontrol.OnEventListener;
import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.ItemEditLessonBinding;
import com.happs.ximand.ringcontrol.model.object.Lesson;
import com.happs.ximand.ringcontrol.model.object.Time;
import com.happs.ximand.ringcontrol.view.MaskWatcher;
import com.happs.ximand.ringcontrol.viewmodel.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class EditTimetableRecyclerViewAdapter extends BaseRecyclerViewAdapter<Lesson,
        EditTimetableRecyclerViewAdapter.EditTimetableViewHolder> {

    private static final String START_END_TIME_DIVIDER = " – ";

    private final List<ObservableField<String>> inputs = new ArrayList<>();
    private final List<MaskWatcher> maskWatchers = new ArrayList<>();
    private final List<ObservableBoolean> errorList = new ArrayList<>();
    private final OnEventListener<Integer> lineFilledEvent;
    private boolean detailEditing;

    public EditTimetableRecyclerViewAdapter(List<Lesson> lessons) {
        super(lessons);
        this.detailEditing = !lessons.isEmpty();
        this.lineFilledEvent = this::onLineFilled;
        initByLessonList(lessons);
    }

    private void initByLessonList(List<Lesson> lessons) {
        for (Lesson lesson : lessons) {
            inputs.add(new ObservableField<>(
                    getLessonScopes(lesson))
            );
            maskWatchers.add(new MaskWatcher(
                    TimeUtils.DETAILED_TIME_MASK
            ));
            errorList.add(new ObservableBoolean(false));
        }
    }

    public boolean isAllLinesCorrect() {
        for (ObservableBoolean errorObservable : errorList) {
            if (!errorObservable.get()) {
                return false;
            }
        }
        return true;
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
        holder.bind(
                inputs.get(position), errorList.get(position),
                maskWatchers.get(position), lineFilledEvent
        );
    }

    private void onLineFilled(int num) {
        String input = inputs.get(num).get();
        if (isInputCorrect(input)) {
            updateErrorIdStatusForCorrectLine(num);
            Lesson lesson = getLessonForInput(num, input);
            getItems().set(num, lesson);
        } else {
            errorList.get(num).set(true);
        }
    }

    private void updateErrorIdStatusForCorrectLine(int num) {
        ObservableBoolean errorObservable = errorList.get(num);
        if (errorObservable.get()) {
            errorObservable.set(false);
        }
    }

    @Override
    public List<Lesson> getItems() {
        return super.getItems();
    }

    private Lesson getLessonForInput(int num, String input) {
        Time startTime = new Time(getDetailedStartTime(input));
        Time endTime = new Time(getDetailedEndTime(input));
        return new Lesson(num + 1, startTime, endTime);
    }

    public void addEmptyLesson() {
        getItems().add(new Lesson(getItemCount() + 1));
        inputs.add(new ObservableField<>());
        maskWatchers.add(createMaskWatcher());
        errorList.add(new ObservableBoolean(false));
        notifyItemRangeInserted(getItems().size() - 1, 1);
    }

    private MaskWatcher createMaskWatcher() {
        final String mask = getMaskForCurrentDetailEditingStatus();
        return new MaskWatcher(mask);
    }

    public void removeLastLesson() {
        int lastItemIndex = getItems().size() - 1;
        getItems().remove(lastItemIndex);
        inputs.remove(lastItemIndex);
        maskWatchers.remove(lastItemIndex);
        errorList.remove(lastItemIndex);
        notifyItemRemoved(lastItemIndex);
    }

    public void changeDetailEditingStatus() {
        for (MaskWatcher mask : maskWatchers) {
            mask.setMask(getMaskForInverseDetailEditingStatus());
        }
        updateInputsForNewDetailEditingStatus();
        detailEditing = !detailEditing;
    }

    private void updateInputsForNewDetailEditingStatus() {
        if (detailEditing) {
            updateInputsFromDetailedToSimple();
        } else {
            updateInputsFromSimpleToDetailed();
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

    private String getLessonScopes(Lesson lesson) {
        return lesson.getStartTime().toString() + START_END_TIME_DIVIDER
                + lesson.getEndTime().toString();
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
        return !TextUtils.isEmpty(input)
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

    private String getMaskForInverseDetailEditingStatus() {
        return detailEditing ?
                TimeUtils.SIMPLE_TIME_MASK : TimeUtils.DETAILED_TIME_MASK;
    }

    static class EditTimetableViewHolder extends RecyclerView.ViewHolder {

        private ItemEditLessonBinding binding;
        private EditText timeEditText;

        EditTimetableViewHolder(@NonNull ItemEditLessonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.timeEditText = binding.input;
        }

        void bind(ObservableField<String> input,
                  ObservableBoolean error, MaskWatcher maskWatcher,
                  OnEventListener<Integer> lineFilledEvent) {
            binding.setViewPosition(getAdapterPosition());
            binding.setInput(input);
            binding.setError(error);
            attachMaskWatcher(maskWatcher, lineFilledEvent);
            timeEditText.setOnFocusChangeListener((view, hasFocus) -> {
                if (!hasFocus) {
                    lineFilledEvent.onEvent(getAdapterPosition());
                }
            });
            binding.executePendingBindings();
        }

        private void attachMaskWatcher(MaskWatcher maskWatcher,
                                       OnEventListener<Integer> lineFilledEvent) {
            maskWatcher.setLineFilledListener(
                    () -> lineFilledEvent.onEvent(getAdapterPosition())
            );
            binding.input.addTextChangedListener(maskWatcher);
        }

    }

}
