package com.happs.ximand.ringcontrol.viewmodel.util;

import com.happs.ximand.ringcontrol.R;
import com.happs.ximand.ringcontrol.model.object.exception.BluetoothException;
import com.happs.ximand.ringcontrol.model.object.exception.BluetoothIsDisabledException;
import com.happs.ximand.ringcontrol.model.object.exception.BluetoothNotSupportedException;
import com.happs.ximand.ringcontrol.model.object.exception.DeviceNotFoundException;
import com.happs.ximand.ringcontrol.model.object.exception.DiscoveryModeIsNotStartedException;
import com.happs.ximand.ringcontrol.model.object.exception.FailedToConnectException;
import com.happs.ximand.ringcontrol.model.object.exception.LocationPermissionDeniedException;
import com.happs.ximand.ringcontrol.model.object.timetable.Lesson;
import com.happs.ximand.ringcontrol.model.object.timetable.Time;

import java.util.List;

public final class BindingConverters {

    private BindingConverters() {

    }

    public static String convertTimeToPreviewTimeString(Time time) {
        StringBuilder simplifiedTimeBuilder = new StringBuilder();
        simplifiedTimeBuilder.append(time.getHours());
        simplifiedTimeBuilder.append(":");
        if (time.getMinutes() < 10) {
            simplifiedTimeBuilder.append("0");
        }
        simplifiedTimeBuilder.append(time.getMinutes());
        if (time.getSeconds() != 0) {
            simplifiedTimeBuilder.append(":");
            if (time.getSeconds() < 10) {
                simplifiedTimeBuilder.append("0");
            }
            simplifiedTimeBuilder.append(time.getSeconds());
        }
        return simplifiedTimeBuilder.toString();
    }

    public static String convertListToLessonsDetails(List<Lesson> lessons) {
        StringBuilder previewBuilder = new StringBuilder();
        for (Lesson lesson : lessons) {
            if (previewBuilder.length() > 86) {
                break;
            }
            previewBuilder
                    .append(convertTimeToPreviewTimeString(lesson.getStartTime()))
                    .append(" - ")
                    .append(convertTimeToPreviewTimeString(lesson.getEndTime()))
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

    public static int convertBluetoothExceptionToMessageResId(BluetoothException e) {
        if (e instanceof BluetoothIsDisabledException) {
            return R.string.bluetooth_is_disabled;
        } else if (e instanceof DeviceNotFoundException) {
            return R.string.device_not_found;
        } else if (e instanceof LocationPermissionDeniedException) {
            return R.string.location_permission_denied;
        } else if (e instanceof DiscoveryModeIsNotStartedException) {
            return R.string.bluetooth_was_not_initialized;
        } else if (e instanceof BluetoothNotSupportedException) {
            return R.string.bluetooth_not_supported;
        } else if (e instanceof FailedToConnectException) {
            return R.string.failed_to_connect;
        }
        return 0;
    }
}
