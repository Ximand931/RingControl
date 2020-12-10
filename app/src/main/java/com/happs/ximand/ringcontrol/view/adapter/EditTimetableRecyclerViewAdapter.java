package com.happs.ximand.ringcontrol.view.adapter;

import androidx.databinding.ObservableField;

import com.happs.ximand.ringcontrol.model.object.Lesson;

import java.util.List;

public class EditTimetableRecyclerViewAdapter extends BaseEditTimetableRecyclerViewAdapter {

    public EditTimetableRecyclerViewAdapter(List<Lesson> lessons) {
        super(lessons, true);
        for (Lesson lesson : lessons) {
            getInputs().add(new ObservableField<>(lesson.getStartTime().toString()));
        }
    }

}
