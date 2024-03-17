package com.lamzone.mareu.ui.meetinglist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lamzone.mareu.R;
import com.lamzone.mareu.application.MeetingListViewModel;
import com.lamzone.mareu.di.Injector;
import com.lamzone.mareu.model.Meeting;
import com.lamzone.mareu.repository.MeetingRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Fragment displaying the list of meetings.
 */
public class MeetingListFragment extends Fragment {

    /**
     * Adapter for the list of meetings.
     */
    public MeetingListAdapter mAdapter;

    /**
     * List of displayed meetings.
     */
    public List<Meeting> mMeetings = new ArrayList<>();

    /**
     * List of filtered meetings.
     */
    public List<Meeting> mFilteredMeetings = new ArrayList<>();

    /**
     * Reference to the meeting repository.
     */
    public MeetingRepository mMeetingRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_meeting_list, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewMeetings);
        mAdapter = new MeetingListAdapter(mFilteredMeetings);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Instantiation of MeetingRepository
        mMeetingRepository = Injector.getInstance().provideMeetingRepository();

        // Instantiation and binding of the ViewModel
        MeetingListViewModel viewModel = new ViewModelProvider(requireActivity()).get(MeetingListViewModel.class);
        viewModel.getMeetings().observe(getViewLifecycleOwner(), meetings -> {
            mMeetings.clear(); // Clears the current meetings
            mMeetings.addAll(meetings); // Adds the new meetings
            updateMeetingsList(); // Update the list of meetings to display
            // Log.d("MeetingListFragment", "Meeting List updated: " + mMeetings.size() + " meetings");
        });

        return root;
    }

    /**
     * Applies filters using the repository method.
     *
     * @param filterByDate     Indicates if filtering by date is enabled.
     * @param filterByLocation Indicates if filtering by location is enabled.
     * @param selectedLocation Selected location for filtering.
     * @param selectedDate     Selected date for filtering.
     */
    public void applyFilters(boolean filterByDate, boolean filterByLocation, String selectedLocation, Calendar selectedDate) {
        // Reset the list of filtered meetings
        mFilteredMeetings.clear();

        // Apply filters using the repository
        mFilteredMeetings.addAll(mMeetingRepository.filterMeetings(mMeetings, filterByDate, filterByLocation, selectedLocation, selectedDate));

        // Update the list of meetings to display
        updateMeetingsList();
    }

    /**
     * Deletes a meeting.
     *
     * @param meeting Meeting to delete.
     */
    public void deleteMeeting(Meeting meeting) {
        // Check if the repository is initialized
        if (mMeetingRepository != null) {

            // Call the deleteMeeting method of MeetingRepository to delete the meeting
            mMeetingRepository.deleteMeeting(meeting);

            // Remove the meeting from the lists in the fragment
            mMeetings.remove(meeting);
            mFilteredMeetings.remove(meeting);

            // Inform the list adapter to update the view
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Updates the list of meetings to display.
     */
    private void updateMeetingsList() {
        mAdapter.setMeetings(mFilteredMeetings.isEmpty() ? mMeetings : mFilteredMeetings);
        mAdapter.notifyDataSetChanged();
    }
}