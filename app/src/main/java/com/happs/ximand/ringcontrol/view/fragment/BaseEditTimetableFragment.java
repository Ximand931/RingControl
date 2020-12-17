package com.happs.ximand.ringcontrol.view.fragment;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.model.object.Lesson;
import com.happs.ximand.ringcontrol.view.adapter.EditTimetableRecyclerViewAdapter;
import com.happs.ximand.ringcontrol.viewmodel.fragment.BaseEditTimetableViewModel;

import java.util.List;

public abstract class BaseEditTimetableFragment<VM extends BaseEditTimetableViewModel,
        D extends ViewDataBinding>
        extends BaseFragmentWithRecyclerView<VM, D, Lesson, EditTimetableRecyclerViewAdapter> {

    public BaseEditTimetableFragment(int layoutId) {
        super(layoutId, R.menu.menu_toolbar_editing);
    }

    @Override
    protected void onPreViewModelAttaching(@NonNull VM viewModel) {
        viewModel.getLessonsLiveData().observe(
                getViewLifecycleOwner(), this::initAdapter
        );
        viewModel.getDetailEditingLiveData().observe(
                getViewLifecycleOwner(), this::updateDetailEditingStatus
        );
        viewModel.setCorrectnessCheck(this::isAdapterDataCorrect);
    }

    @Override
    protected EditTimetableRecyclerViewAdapter createNewAdapter(List<Lesson> items) {
        return new EditTimetableRecyclerViewAdapter(items);
    }

    @Override
    protected void onPreAttachRecyclerViewAdapter(EditTimetableRecyclerViewAdapter adapter) {
        getViewModel().getAddLessonEvent().observe(getViewLifecycleOwner(),
                aVoid -> adapter.addEmptyLesson()
        );
        getViewModel().getRemoveLessonEvent().observe(getViewLifecycleOwner(),
                aVoid -> adapter.removeLastLesson()
        );
    }

    private void updateDetailEditingStatus(boolean status) {
        EditTimetableRecyclerViewAdapter adapter = getAdapter();
        if (adapter != null) {
            adapter.setDetailEditingStatus(status);
        }
    }

    private boolean isAdapterDataCorrect() {
        EditTimetableRecyclerViewAdapter adapter = getAdapter();
        if (adapter != null) {
            return adapter.isAllLinesCorrect();
        }
        return false;
    }

}
