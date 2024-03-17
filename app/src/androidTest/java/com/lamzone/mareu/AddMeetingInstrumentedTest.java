package com.lamzone.mareu;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.lamzone.mareu.ui.MeetingActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test class to validate the process of adding a meeting using the UI.
 */
@RunWith(AndroidJUnit4.class)
public class AddMeetingInstrumentedTest {

    /**
     * Launches the MeetingActivity before each test.
     */
    @Before
    public void launchActivity() {
        ActivityScenario.launch(MeetingActivity.class);
    }

    /**
     * Tests the process of adding a meeting using the UI.
     * It performs the following steps:
     * 1. Clicks on the "+" button to add a meeting.
     * 2. Fills in the meeting details:
     *    - Enters the meeting title.
     *    - Selects a location from the spinner.
     *    - Chooses the date and time.
     *    - Enters the subject of the meeting.
     * 3. Adds a participant.
     * 4. Closes the virtual keyboard to ensure button visibility.
     * 5. Clicks on the validation button to save the meeting.
     */
    @Test
    public void addMeeting() {
        // Clicks on the "+" button to add a meeting
        Espresso.onView(ViewMatchers.withId(R.id.fabAddMeeting)).perform(ViewActions.click());

        // Fills in the meeting details
        Espresso.onView(ViewMatchers.withId(R.id.editTextTitle)).perform(ViewActions.typeText("Reunion Test"));
        Espresso.onView(ViewMatchers.withId(R.id.spinnerLocation)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("Peach")).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.editTextDateTime)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withText("OK")).perform(ViewActions.click()); // Select current date
        Espresso.onView(ViewMatchers.withText("OK")).perform(ViewActions.click()); // Select current time
        Espresso.onView(ViewMatchers.withId(R.id.editTextSubject)).perform(ViewActions.typeText("Discussion sur les tests du projet"));

        // Adds a participant
        Espresso.onView(ViewMatchers.withId(R.id.editTextParticipant)).perform(ViewActions.typeText("duck.test@example.com"));
        Espresso.onView(ViewMatchers.withId(R.id.btnAddParticipant)).perform(ViewActions.click());

        // Closes the virtual keyboard to ensure button visibility
        Espresso.closeSoftKeyboard();

        // Clicks on the validation button
        Espresso.onView(ViewMatchers.withId(R.id.btnValidate)).perform(ViewActions.click());
    }
}