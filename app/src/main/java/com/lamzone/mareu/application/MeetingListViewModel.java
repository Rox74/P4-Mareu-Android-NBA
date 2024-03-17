package com.lamzone.mareu.application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.lamzone.mareu.di.Injector;
import com.lamzone.mareu.model.Meeting;
import com.lamzone.mareu.repository.MeetingRepository;

import java.util.List;

/**
 * ViewModel for the list of meetings.
 */
public class MeetingListViewModel extends ViewModel {

    private final MeetingRepository mRepository;

    /**
     * Initializes a new MeetingListViewModel object.
     */
    public MeetingListViewModel() {
        mRepository = Injector.getInstance().provideMeetingRepository();
        // Log.d("MeetingListViewModel", "ViewModel initialized"); // ViewModel initialization log
    }

    /**
     * Retrieves the list of meetings.
     *
     * @return LiveData List Meeting representing the list of meetings.
     */
    public LiveData<List<Meeting>> getMeetings() {
        // Log.d("MeetingListViewModel", "Getting meetings..." + mRepository.getMeetingsLiveData());
        return mRepository.getMeetingsLiveData();
    }

    /**
     * Retrieves the list of meeting rooms.
     *
     * @return LiveData List String representing the list of meeting rooms.
     */
    public LiveData<List<String>> getMeetingRooms() {
        // Log.d("MeetingListViewModel", "Getting meeting rooms..." + mRepository.getMeetingRoomsLiveData());
        return mRepository.getMeetingRoomsLiveData();
    }
}