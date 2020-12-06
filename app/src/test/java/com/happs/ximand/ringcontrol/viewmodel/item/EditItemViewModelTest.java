package com.happs.ximand.ringcontrol.viewmodel.item;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EditItemViewModelTest {

    @Test
    public void getStartTimeTest() {
        EditItemViewModel viewModel =
                new EditItemViewModel("", "08:30:00", "09:15:00");

        String expected = "08:30:00";
        String actual = viewModel.getDetailedStartTime();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getEndTimeTest() {
        EditItemViewModel viewModel =
                new EditItemViewModel("", "08:30:00", "09:15:00");

        String expected = "09:15:00";
        String actual = viewModel.getDetailedEndTime();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void isCorrectInputTestWithIncorrectHours() {
        EditItemViewModel viewModel;
        viewModel = new EditItemViewModel("", "30:00:00", "30:00:01");
        Assert.assertFalse(viewModel.isInputCorrect());

        viewModel = new EditItemViewModel("", "25:00:00", "25:00:00");
        Assert.assertFalse(viewModel.isInputCorrect());
    }

    @Test
    public void isCorrectInputTestWithIncorrectMinutes() {
        EditItemViewModel viewModel =
                new EditItemViewModel("", "00:60:00", "00:60:01");
        Assert.assertFalse(viewModel.isInputCorrect());
    }

    @Test
    public void isCorrectInputTestWithIncorrectSeconds() {
        EditItemViewModel viewModel =
                new EditItemViewModel("", "00:00:61", "00:00:61");
        Assert.assertFalse(viewModel.isInputCorrect());
    }

    @Test
    public void isCorrectInputTestWithCorrectData() {
        EditItemViewModel viewModel =
                new EditItemViewModel("", "23:59:59", "00:00:00");
        Assert.assertTrue(viewModel.isInputCorrect());
    }

}