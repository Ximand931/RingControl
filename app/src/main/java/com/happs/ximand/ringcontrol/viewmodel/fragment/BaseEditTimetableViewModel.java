package com.happs.ximand.ringcontrol.viewmodel.fragment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;

import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.model.object.exception.IncorrectInputException;
import com.happs.ximand.ringcontrol.model.object.Lesson;
import com.happs.ximand.ringcontrol.view.adapter.EditTimetableRecyclerViewAdapter;
import com.happs.ximand.ringcontrol.viewmodel.item.EditItemViewModel;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseEditTimetableViewModel extends BaseFragmentViewModel {

    private EditTimetableRecyclerViewAdapter adapter;
    protected boolean detailEditing;

    BaseEditTimetableViewModel(@NonNull Application application) {
        super(application);
        this.detailEditing = true;
    }

    @Bindable
    public EditTimetableRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    void setAdapter(EditTimetableRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    public void onAddLessonClick() {
        String hint = getApplication().getString(R.string.lesson)
                + " " + (adapter.getItemCount() + 1);
        EditItemViewModel viewModel =
                new EditItemViewModel(hint, detailEditing);
        adapter.addItem(viewModel);
    }

    public void onRemoveLastLessonClick() {
        adapter.removeLastItem();
    }

    public void onDetailEditingClick() {
        this.detailEditing = !detailEditing;
        List<EditItemViewModel> viewModels = adapter.getViewModels();
        for (EditItemViewModel viewModel : viewModels) {
            viewModel.invertDetailedEdited();
        }
    }

    List<Lesson> getLessonsFromRecyclerViewAdapter() throws IncorrectInputException {
        List<Lesson> lessons = new ArrayList<>();
        List<EditItemViewModel> viewModels = adapter.getViewModels();
        for (int i = 0; i < viewModels.size(); i++) {
            EditItemViewModel viewModel = viewModels.get(i);
            lessons.add(getLessonFromEditItemViewModel(i, viewModel));
        }
        return lessons;
    }

    private Lesson getLessonFromEditItemViewModel(int index, EditItemViewModel viewModel)
            throws IncorrectInputException {

        if (!viewModel.isInputCorrect()) {
            showIncorrectInputMessageForItem(viewModel);
            throw new IncorrectInputException();
        }

        int num = index + 1;
        return new Lesson(
                num, viewModel.getDetailedStartTime(), viewModel.getDetailedEndTime()
        );
    }

    private void showIncorrectInputMessageForItem(EditItemViewModel itemViewModel) {
        itemViewModel.setError(
                getApplication().getString(R.string.incorrect_input_time)
        );
    }

}
