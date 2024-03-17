package com.lamzone.mareu.data_sources;

import com.lamzone.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * The FakeApi class simulates a fictional API providing meeting and meeting room data.
 */
public class FakeApi {

    private List<String> meetingRooms;
    private List<Meeting> meetingList;

    /**
     * Constructor for FakeApi.
     * Initializes the lists of fictional meeting rooms and meetings.
     */
    public FakeApi() {
        // Initializing lists of fictional meeting rooms and meetings
        meetingRooms = Arrays.asList("Peach", "Mario", "Luigi", "Toad", "Daisy");
        meetingList = new ArrayList<>(Arrays.asList(
                new Meeting("Réunion A", createDate(20, 2, 2024, 10, 0), "Peach", "Présentation du projet", Arrays.asList("theo.johnson@example.com", "may.smith@example.com")),
                new Meeting("Réunion B", createDate(22, 2, 2024, 13, 50), "Mario", "Planification des tâches", Arrays.asList("theo.johnson@example.com", "jack.smith@example.com")),
                new Meeting("Réunion C", createDate(22, 2, 2024, 15, 0), "Luigi", "Brainstorming", Arrays.asList("theo.johnson@example.com", "may.smith@example.com", "jack.smith@example.com")),
                new Meeting("Réunion D", createDate(23, 2, 2024, 10, 0), "Mario", "Revue", Arrays.asList("theo.johnson@example.com", "jack.smith@example.com")),
                new Meeting("Réunion E", createDate(23, 2, 2024, 11, 30), "Luigi", "Revue", Arrays.asList("theo.johnson@example.com", "may.smith@example.com", "jack.smith@example.com"))
        ));
    }

    /**
     * Retrieves the list of meetings.
     *
     * @return The list of meetings.
     */
    public List<Meeting> getMeetings() {
        return new ArrayList<>(meetingList);
    }

    /**
     * Retrieves the list of meeting rooms.
     *
     * @return The list of meeting rooms.
     */
    public List<String> getMeetingRooms() {
        return new ArrayList<>(meetingRooms);
    }

    /**
     * Utility method to create a Calendar instance with specific date and time.
     *
     * @param dayOfMonth  The day of the month.
     * @param month       The month (1-12).
     * @param year        The year.
     * @param hourOfDay   The hour of the day (0-23).
     * @param minute      The minutes (0-59).
     * @return            A Calendar instance representing the specified date and time.
     */
    private Calendar createDate(int dayOfMonth, int month, int year, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, dayOfMonth, hourOfDay, minute); // Calendar uses a month index starting from 0
        return calendar;
    }
}