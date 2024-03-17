package com.lamzone.mareu;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import android.widget.DatePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.lamzone.mareu.ui.MeetingActivity;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test class for testing the functionality of the MeetingListFragment.
 */
@RunWith(AndroidJUnit4.class)
public class MeetingListInstrumentedTest {

    /**
     * Launches the MeetingActivity before each test.
     */
    @Before
    public void launchActivity() {
        ActivityScenario.launch(MeetingActivity.class);
    }

    /**
     * Tests filtering meetings by date.
     * It performs the following steps:
     * 1. Clicks on the Options button to display the filtering options.
     * 2. Selects the filter by date option.
     * 3. Clicks on the date field to open the calendar.
     * 4. Selects a date (e.g., 20-02-2024).
     * 5. Clicks on the "OK" or "Validate" button to confirm the selection.
     * 6. Closes the virtual keyboard.
     * 7. Clicks on the "Filter" button.
     * 8. Verifies that the text "20-02-2024" is displayed in the list of meetings.
     */
    @Test
    public void filterMeetingsByDate() {

        // Clicks on the Options button to display the filtering options
        Espresso.onView(withId(R.id.btnOptions)).perform(click());

        // Selects the filter by date option
        Espresso.onView(withId(R.id.checkBoxDate)).perform(click());

        // Clicks on the date field to open the calendar
        Espresso.onView(withId(R.id.editTextDate)).perform(click());

        // Selects a date (e.g., 20-02-2024)
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2024, 2, 20));

        // Clicks on the "OK" or "Validate" button to confirm the selection
        Espresso.onView(withText("OK")).perform(click());

        // Closes the virtual keyboard
        Espresso.closeSoftKeyboard();

        // Clicks on the "Filter" button
        Espresso.onView(withText("Filtrer")).perform(click());

        // Verifies that the text "20-02-2024" is displayed in the list of meetings
            Espresso.onView(allOf(
                            ViewMatchers.withId(R.id.textTitle),
                            ViewMatchers.withText(containsString("20-02-2024"))
                    ))
                    .check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Tests filtering meetings by room.
     * It performs the following steps:
     * 1. Clicks on the Options button to display the filtering options.
     * 2. Selects the filter by room option.
     * 3. Clicks on the "Filter" button.
     * 4. Verifies that the displayed meetings correspond to the selected room.
     */
    @Test
    public void filterMeetingsByRoom() {

        // Clicks on the Options button to display the filtering options
        Espresso.onView(withId(R.id.btnOptions)).perform(click());

        // Selects the filter by room option
        Espresso.onView(withId(R.id.checkBoxLocation)).perform(click());

        // Clicks on the "Filter" button
        Espresso.onView(withText("Filtrer")).perform(click());

        // Verifies that the displayed meetings correspond to the selected room
        Espresso.onView(allOf(
                        ViewMatchers.withId(R.id.textTitle),
                        ViewMatchers.withText(containsString("Peach"))
                ))
                .check(ViewAssertions.matches(isDisplayed()));
    }

    /**
     * Tests deleting a meeting.
     * It performs the following steps:
     * 1. Clicks on the Options button to display the filtering options.
     * 2. Selects the filter by date option.
     * 3. Clicks on the date field to open the calendar.
     * 4. Selects a date (e.g., 20-02-2024).
     * 5. Clicks on the "OK" or "Validate" button to confirm the selection.
     * 6. Closes the virtual keyboard.
     * 7. Clicks on the "Filter" button.
     * 8. Clicks on the first found meeting deletion button.
     * 9. Verifies that the meeting A is no longer present after deletion.
     */
    @Test
    public void deleteMeeting() {

        // Clicks on the Options button to display the filtering options
        Espresso.onView(withId(R.id.btnOptions)).perform(click());

        // Selects the filter by date option
        Espresso.onView(withId(R.id.checkBoxDate)).perform(click());

        // Clicks on the date field to open the calendar
        Espresso.onView(withId(R.id.editTextDate)).perform(click());

        // Selects a date (e.g., 20-02-2024)
        Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2024, 2, 20));

        // Clicks on the "OK" or "Validate" button to confirm the selection
        Espresso.onView(withText("OK")).perform(click());

        // Closes the virtual keyboard
        Espresso.closeSoftKeyboard();

        // Clicks on the "Filter" button
        Espresso.onView(withText("Filtrer")).perform(click());

        // Clicks on the first found meeting deletion button
        Espresso.onView(withId(R.id.btnDelete)).perform(click());

        // Verifies that the meeting A is no longer present after deletion
        Espresso.onView(allOf(
                        ViewMatchers.withId(R.id.textTitle),
                        ViewMatchers.withText(containsString("Réunion A"))
                ))
                .check(ViewAssertions.doesNotExist());
    }

}