package com.lamzone.mareu.di;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.lamzone.mareu.data_sources.FakeApi;
import com.lamzone.mareu.repository.MeetingRepository;
import com.lamzone.mareu.application.AddMeetingViewModel;

/**
 * The Injector class is responsible for dependency injection for the application.
 */
public class Injector {

    private static Injector instance;

    private final MeetingRepository mMeetingRepository;

    /**
     * Private constructor to create an instance of Injector.
     * Initializes the meeting repository with an instance of FakeApi.
     */
    private Injector() {
        FakeApi fakeApi = new FakeApi();
        mMeetingRepository = new MeetingRepository(fakeApi);
    }

    /**
     * Get the singleton instance of Injector.
     * If no instance exists, creates a new one.
     *
     * @return The instance of Injector.
     */
    public static Injector getInstance() {
        if (instance == null) {
            instance = new Injector();
        }
        return instance;
    }

    /**
     * Provides a ViewModel for creating new meetings.
     *
     * @param fragment The fragment associated with the ViewModel.
     * @return The ViewModel for adding a meeting.
     */
    public AddMeetingViewModel provideAddMeetingViewModel(Fragment fragment) {
        return new ViewModelProvider(fragment, new ViewModelProvider.NewInstanceFactory()).get(AddMeetingViewModel.class);
    }

    /**
     * Provides the meeting repository.
     *
     * @return The meeting repository.
     */
    public MeetingRepository provideMeetingRepository() {
        return mMeetingRepository;
    }
}