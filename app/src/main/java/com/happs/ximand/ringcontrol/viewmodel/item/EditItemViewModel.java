package com.happs.ximand.ringcontrol.viewmodel.item;

import androidx.databinding.Bindable;

import com.happs.ximand.ringcontrol.BR;
import com.happs.ximand.ringcontrol.view.MaskWatcher;

@Deprecated
public class EditItemViewModel extends BaseItemViewModel {

    private static final String SIMPLE_TIME_PATTERN =
            "(?:[01]\\d|2[0123]):(?:[012345]\\d) – (?:[01]\\d|2[0123]):(?:[012345]\\d)";
    private static final String DETAILED_TIME_PATTERN =
            "(?:[01]\\d|2[0123]):(?:[012345]\\d):(?:[012345]\\d) " +
                    "– (?:[01]\\d|2[0123]):(?:[012345]\\d):(?:[012345]\\d)";

    private static final String SIMPLE_TIME_MASK = "ss:ss – ss:ss";
    private static final String DETAILED_TIME_MASK = "ss:ss:ss – ss:ss:ss";

    private final MaskWatcher maskWatcher;

    private boolean detailEditing;

    private String hint;
    private String input;
    private String error;

    public EditItemViewModel(String hint, boolean detailEditing) {
        this.hint = hint;
        this.detailEditing = detailEditing;
        this.maskWatcher = createRequiredMaskWatcher();
    }

    public EditItemViewModel(String hint, String start, String end) {
        this.hint = hint;
        this.detailEditing = isDetailedTime(start) && isDetailedTime(end);
        this.maskWatcher = createRequiredMaskWatcher();
        this.input = start + " – " + end;
    }

    private MaskWatcher createRequiredMaskWatcher() {
        if (detailEditing) {
            //return new MaskWatcher(DETAILED_TIME_MASK);
        } else {
            //return new MaskWatcher(SIMPLE_TIME_MASK);
        }
        return null;
        //TODO
    }

    private boolean isDetailedTime(String time) {
        if (time != null) {
            return !time.matches(SIMPLE_TIME_PATTERN);
        } else {
            return true;
        }
    }

    @Bindable
    public MaskWatcher getMaskWatcher() {
        return maskWatcher;
    }

    @Bindable
    public String getHint() {
        return hint;
    }

    @Bindable
    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
        notifyPropertyChanged(BR.input);
    }

    @Bindable
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
        notifyPropertyChanged(BR.error);
    }

    public String getDetailedStartTime() {
        if (input == null) {
            return null;
        }

        if (detailEditing) {
            return input.substring(0, 8);
        } else {
            return input.substring(0, 5) + ":00";
        }
    }

    public String getDetailedEndTime() {
        if (input == null) {
            return null;
        }

        if (detailEditing) {
            return input.substring(11);
        } else {
            return input.substring(8) + ":00";
        }
    }

    public void invertDetailedEdited() {
        if (detailEditing) {
            updateInputFromDetailedToSimple();
        } else {
            updateInputFromSimpleToDetailed();
        }
        detailEditing = !detailEditing;
    }

    private void updateInputFromSimpleToDetailed() {
        if (!detailEditing && isInputCorrect()) {
            setInput(getDetailedStartTime() + " – " + getDetailedEndTime());
        }
        //maskWatcher.setMask(DETAILED_TIME_MASK);
    }

    private void updateInputFromDetailedToSimple() {
        if (detailEditing && isInputCorrect()) {
            String startTime = getDetailedStartTime();
            String endTime = getDetailedEndTime();
            setInput(startTime.substring(0, startTime.lastIndexOf(":"))
                    + " – " + endTime.substring(0, endTime.lastIndexOf(":")));
        }
        //maskWatcher.setMask(SIMPLE_TIME_MASK);
    }

    public boolean isInputCorrect() {
        if (input == null) {
            return false;
        }

        if (detailEditing) {
            return input.matches(DETAILED_TIME_PATTERN);
        } else {
            return input.matches(SIMPLE_TIME_PATTERN);
        }

    }
}
