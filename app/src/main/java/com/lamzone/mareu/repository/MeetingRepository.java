package com.lamzone.mareu.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lamzone.mareu.data_sources.FakeApi;
import com.lamzone.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * The MeetingRepository class manages access to meeting data.
 */
public class MeetingRepository {

    private final MutableLiveData<List<Meeting>> mMeetingsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<String>> mMeetingRoomsLiveData = new MutableLiveData<>();
    private final FakeApi mFakeApi;

    /**
     * Constructor to create an instance of MeetingRepository.
     *
     * @param fakeApi The fake API used to retrieve meeting and meeting room data.
     */
    public MeetingRepository(FakeApi fakeApi) {
        mFakeApi = fakeApi;
        loadMeetings(); // Load meetings on repository startup
        loadMeetingRooms(); // Load meeting rooms on repository startup
    }

    /**
     * Gets a LiveData containing the list of meetings.
     *
     * @return LiveData List Meeting containing the list of meetings.
     */
    public LiveData<List<Meeting>> getMeetingsLiveData() {
        // Log.d("MeetingRepository", "Getting meetings from LiveData..." + mMeetingsLiveData);
        return mMeetingsLiveData;
    }

    /**
     * Gets a LiveData containing the list of meeting rooms.
     *
     * @return LiveData List String containing the list of meeting rooms.
     */
    public LiveData<List<String>> getMeetingRoomsLiveData() {
        // Log.d("MeetingRepository", "Getting meeting rooms from LiveData..." + mMeetingRoomsLiveData);
        return mMeetingRoomsLiveData;
    }

    /**
     * Adds a new meeting to the list of meetings.
     *
     * @param meeting Meeting to add.
     */
    public void addMeeting(Meeting meeting) {
        // Get a copy of the current list of meetings
        List<Meeting> meetings = new ArrayList<>(mMeetingsLiveData.getValue());

        // Add the new meeting to the list
        meetings.add(meeting);

        // Update the LiveData with the new list of meetings
        mMeetingsLiveData.postValue(meetings);

        // Log.d("MeetingRepository", "Meeting added: " + meeting.getTitle());
        // Log.d("MeetingRepository", "Meetings LiveData updated: " + mMeetingsLiveData.getValue().size());
    }

    /**
     * Deletes a meeting from the list of meetings.
     *
     * @param meeting Meeting to delete.
     */
    public void deleteMeeting(Meeting meeting) {
        // Get a copy of the current list of meetings
        List<Meeting> meetings = new ArrayList<>(mMeetingsLiveData.getValue());

        // Remove the meeting from the list
        meetings.remove(meeting);

        // Update the LiveData with the new list of meetings
        mMeetingsLiveData.postValue(meetings);

        // Log.d("MeetingRepository", "Meeting deleted: " + meeting.getTitle());
        // Log.d("MeetingRepository", "Meetings LiveData updated: " + mMeetingsLiveData.getValue().size());
    }


    // Private methods for initial data loading

    /**
     * Loads initial meetings from FakeApi.
     */
    private void loadMeetings() {
        mMeetingsLiveData.setValue(mFakeApi.getMeetings()); // Retrieve meetings from FakeApi
    }

    /**
     * Loads initial meeting rooms from FakeApi.
     */
    private void loadMeetingRooms() {
        mMeetingRoomsLiveData.setValue(mFakeApi.getMeetingRooms()); // Retrieve meeting rooms from FakeApi
    }

    /**
     * Filters meetings based on specified criteria.
     *
     * @param meetings          Full list of meetings.
     * @param filterByDate      Indicates if filtering by date is enabled.
     * @param filterByLocation  Indicates if filtering by location is enabled.
     * @param selectedLocation  Selected location for filtering.
     * @param selectedDate      Selected date for filtering.
     * @return Filtered list of meetings.
     */
    public List<Meeting> filterMeetings(List<Meeting> meetings, boolean filterByDate, boolean filterByLocation, String selectedLocation, Calendar selectedDate) {
        List<Meeting> filteredMeetings = new ArrayList<>();

        for (Meeting meeting : meetings) {
            boolean passesDateFilter = !filterByDate || isSameDay(meeting.getDateTime(), selectedDate);
            boolean passesLocationFilter = !filterByLocation || meeting.getLocation().equals(selectedLocation);

            if (passesDateFilter && passesLocationFilter) {
                filteredMeetings.add(meeting);
            }
        }

        return filteredMeetings;
    }

    /**
     * Checks if two dates are identical (same day, month, and year).
     *
     * @param date1 First date.
     * @param date2 Second date.
     * @return true if the dates are identical, false otherwise.
     */
    private boolean isSameDay(Calendar date1, Calendar date2) {
        return date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR) &&
                date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH) &&
                date1.get(Calendar.DAY_OF_MONTH) == date2.get(Calendar.DAY_OF_MONTH);
    }
}