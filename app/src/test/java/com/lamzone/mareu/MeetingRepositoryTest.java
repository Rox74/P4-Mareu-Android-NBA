package com.lamzone.mareu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.lamzone.mareu.data_sources.FakeApi;
import com.lamzone.mareu.model.Meeting;
import com.lamzone.mareu.repository.MeetingRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Unit tests for the MeetingRepository class.
 */
@RunWith(MockitoJUnitRunner.class)
public class MeetingRepositoryTest {

    @Mock
    private FakeApi fakeApi;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MeetingRepository repository;

    /**
     * Sets up the test environment before each test case.
     */
    @Before
    public void setUp() {
        repository = new MeetingRepository(fakeApi);
    }

    /**
     * Tests the addition of a meeting to the repository.
     * It performs the following steps:
     * 1. Adds a meeting to the repository.
     * 2. Checks if the meeting has been added successfully.
     */
    @Test
    public void testAddMeeting() {
        // Add a meeting
        Meeting meetingToAdd = new Meeting("Test Meeting", Calendar.getInstance(), "Peach", "Test Subject", Arrays.asList("theo.johnson@example.com", "may.smith@example.com"));
        repository.addMeeting(meetingToAdd);

        // Check if the meeting has been added successfully
        List<Meeting> meetings = repository.getMeetingsLiveData().getValue();
        assertNotNull(meetings);
        assertTrue(meetings.contains(meetingToAdd));
    }

    /**
     * Tests the deletion of a meeting from the repository.
     * It performs the following steps:
     * 1. Adds a meeting to the repository.
     * 2. Checks if the meeting has been added successfully.
     * 3. Deletes the added meeting.
     * 4. Checks if the meeting has been deleted successfully.
     */
    @Test
    public void testDeleteMeeting() {
        // Add a meeting
        Meeting meetingToAdd = new Meeting("Test Meeting", Calendar.getInstance(), "Luigi", "Test Subject", Arrays.asList("theo.johnson@example.com", "may.smith@example.com", "jack.smith@example.com"));
        repository.addMeeting(meetingToAdd);

        // Check if the meeting has been added successfully
        List<Meeting> meetingsAfterAddition = repository.getMeetingsLiveData().getValue();
        assertNotNull(meetingsAfterAddition);
        assertTrue(meetingsAfterAddition.contains(meetingToAdd));

        // Delete the added meeting
        repository.deleteMeeting(meetingToAdd);

        // Check if the meeting has been deleted successfully
        List<Meeting> meetingsAfterDeletion = repository.getMeetingsLiveData().getValue();
        assertNotNull(meetingsAfterDeletion);
        assertFalse(meetingsAfterDeletion.contains(meetingToAdd));
    }

    /**
     * Tests the deletion of a non-existent meeting from the repository.
     * It performs the following steps:
     * 1. Attempts to delete a meeting that does not exist.
     * 2. Checks if the meeting has not been deleted.
     */
    @Test
    public void testDeleteNonExistentMeeting() {
        // Attempt to delete a meeting that does not exist
        Meeting meetingToDelete = new Meeting("Nonexistent Meeting", Calendar.getInstance(), "Room X", "Test Subject", Arrays.asList("theo.johnson@example.com", "may.smith@example.com"));
        repository.deleteMeeting(meetingToDelete);

        // Check if the meeting has not been deleted
        List<Meeting> meetings = repository.getMeetingsLiveData().getValue();
        assertNotNull(meetings);
        assertFalse(meetings.contains(meetingToDelete));
    }

    /**
     * Tests the initial loading of meetings from the fake API.
     * It performs the following steps:
     * 1. Sets up initial meetings.
     * 2. Mocks the getMeetings() method of FakeApi.
     * 3. Checks if the initial meetings are loaded successfully.
     */
    @Test
    public void testInitialMeetingsLoading() {
        // Set up initial meetings
        List<Meeting> initialMeetings = new ArrayList<>();
        initialMeetings.add(new Meeting("Meeting 1", Calendar.getInstance(), "Room A", "Subject 1", Arrays.asList("theo.johnson@example.com", "may.smith@example.com")));
        initialMeetings.add(new Meeting("Meeting 2", Calendar.getInstance(), "Room B", "Subject 2", Arrays.asList("theo.johnson@example.com", "may.smith@example.com")));

        // Mock the getMeetings() method of FakeApi
        when(fakeApi.getMeetings()).thenReturn(initialMeetings);
        repository = new MeetingRepository(fakeApi);

        // Check if the initial meetings are loaded successfully
        List<Meeting> meetings = repository.getMeetingsLiveData().getValue();
        assertNotNull(meetings);
        assertEquals(initialMeetings.size(), meetings.size());
        assertTrue(meetings.containsAll(initialMeetings));
    }

    /**
     * Tests the initial loading of meeting rooms from the fake API.
     * It performs the following steps:
     * 1. Sets up initial meeting rooms.
     * 2. Mocks the getMeetingRooms() method of FakeApi.
     * 3. Checks if the initial meeting rooms are loaded successfully.
     */
    @Test
    public void testInitialMeetingRoomsLoading() {
        // Set up initial meeting rooms
        List<String> initialMeetingRooms = Arrays.asList("Room A", "Room B", "Room C");

        // Mock the getMeetingRooms() method of FakeApi
        when(fakeApi.getMeetingRooms()).thenReturn(initialMeetingRooms);
        repository = new MeetingRepository(fakeApi);

        // Check if the initial meeting rooms are loaded successfully
        List<String> meetingRooms = repository.getMeetingRoomsLiveData().getValue();
        assertNotNull(meetingRooms);
        assertEquals(initialMeetingRooms.size(), meetingRooms.size());
        assertTrue(meetingRooms.containsAll(initialMeetingRooms));
    }
}