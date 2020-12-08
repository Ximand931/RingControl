package com.happs.ximand.ringcontrol.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;

import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.databinding.ItemEditLessonBinding;
import com.happs.ximand.ringcontrol.model.object.Lesson;
import com.happs.ximand.ringcontrol.view.MaskWatcher;
import com.happs.ximand.ringcontrol.viewmodel.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class EditTimetableRecyclerViewAdapter extends BaseRecyclerViewAdapter<Lesson,
        EditTimetableRecyclerViewAdapter.EditTimetableViewHolder> {

    private final List<ObservableField<String>> inputs;
    private final ObservableInt errorId = new ObservableInt(-1);
    private final ObservableBoolean detailEditing = new ObservableBoolean();

    public EditTimetableRecyclerViewAdapter(List<Lesson> lessons) {
        super(lessons);
        this.inputs = new ArrayList<>();
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

    public void setErrorId(int id) {
        errorId.set(id);
    }

    public void changeDetailEdititngStatus() {
        detailEditing.set(!detailEditing.get());
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
        holder.bind(getItems().get(position), inputs.get(position), errorId);
    }

    static class EditTimetableViewHolder extends RecyclerView.ViewHolder {

        private ItemEditLessonBinding binding;
        private MaskWatcher maskWatcher;

        EditTimetableViewHolder(@NonNull ItemEditLessonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.maskWatcher = new MaskWatcher(TimeUtil.DETAILED_TIME_MASK);
        }

        void bind(Lesson lesson, ObservableField<String> input, ObservableInt errorId) {
            binding.setNumber(lesson.getNumber());
            binding.setInput(input);
            binding.setErrorId(errorId);
            binding.input.addTextChangedListener(maskWatcher);
        }

    }

}
