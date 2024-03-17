package com.lamzone.mareu.application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.lamzone.mareu.di.Injector;
import com.lamzone.mareu.model.Meeting;
import com.lamzone.mareu.repository.MeetingRepository;

import java.util.List;

/**
 * ViewModel for adding new meetings.
 */
public class AddMeetingViewModel extends ViewModel {

    private final MeetingRepository mRepository;
    private final LiveData<List<String>> mMeetingRooms;

    /**
     * Initializes a new AddMeetingViewModel.
     */
    public AddMeetingViewModel() {
        // Initialize these values with default or null data as necessary
        mRepository = Injector.getInstance().provideMeetingRepository();
        mMeetingRooms = mRepository.getMeetingRoomsLiveData();
    }


    // Public methods for adding meetings and retrieving data

    /**
     * Adds a new meeting.
     *
     * @param meeting Meeting to add.
     */
    public void addMeeting(Meeting meeting) {
        mRepository.addMeeting(meeting);
    }

    /**
     * Retrieves the list of meetings.
     *
     * @return LiveData containing the list of meetings.
     */
    public LiveData<List<Meeting>> getMeetings() {
        // Log.d("AddMeetingViewModel", "Getting meetings...");
        return mRepository.getMeetingsLiveData();
    }

    /**
     * Retrieves the list of meeting rooms.
     *
     * @return LiveData containing the list of meeting rooms.
     */
    public LiveData<List<String>> getMeetingRooms() {
        // Log.d("AddMeetingViewModel", "Getting meeting rooms...");
        return mMeetingRooms;
    }
}