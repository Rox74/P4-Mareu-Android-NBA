package com.lamzone.mareu.ui.addmeeting;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lamzone.mareu.R;
import com.lamzone.mareu.application.AddMeetingViewModel;
import com.lamzone.mareu.di.Injector;
import com.lamzone.mareu.model.Meeting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Fragment to add a new meeting.
 */
public class AddMeetingFragment extends Fragment {

    private EditText mEditTextTitle;
    private Spinner mSpinnerLocation;
    private EditText mEditTextDateTime;
    private EditText mEditTextSubject;
    private EditText mEditTextParticipant;
    private Button mBtnAddParticipant;
    private RecyclerView mRecyclerViewParticipants;
    private Button mBtnValidate;

    private List<String> mParticipantsList = new ArrayList<>();
    private ParticipantAdapter mParticipantAdapter;

    private AddMeetingViewModel mViewModel;
    private Injector mInjector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_meeting, container, false);

        // Initialize the Injector instance
        mInjector = Injector.getInstance();

        // Initialize the ViewModel using the Injector
        mViewModel = mInjector.provideAddMeetingViewModel(this);

        mEditTextTitle = view.findViewById(R.id.editTextTitle);
        mSpinnerLocation = view.findViewById(R.id.spinnerLocation);
        mEditTextDateTime = view.findViewById(R.id.editTextDateTime);
        mEditTextSubject = view.findViewById(R.id.editTextSubject);
        mEditTextParticipant = view.findViewById(R.id.editTextParticipant);
        mBtnAddParticipant = view.findViewById(R.id.btnAddParticipant);
        mRecyclerViewParticipants = view.findViewById(R.id.recyclerViewParticipants);
        mBtnValidate = view.findViewById(R.id.btnValidate);

        setupSpinner();
        setupRecyclerView();

        mEditTextDateTime.setOnClickListener(v -> showDateTimePicker());
        mBtnAddParticipant.setOnClickListener(v -> addParticipant());
        mBtnValidate.setOnClickListener(v -> saveMeeting());

        return view;
    }


    // Private methods to initialize UI components

    /**
     * Initializes the Spinner with the list of meeting rooms.
     */
    private void setupSpinner() {
        // Use the MeetingRepository to get the list of meeting rooms
        mViewModel.getMeetingRooms().observe(getViewLifecycleOwner(), meetingRooms -> {
            ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(requireContext(),
                    android.R.layout.simple_spinner_item, meetingRooms);
            locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinnerLocation.setAdapter(locationAdapter);
        });
    }

    /**
     * Initializes the RecyclerView to display the list of participants.
     */
    private void setupRecyclerView() {
        mParticipantAdapter = new ParticipantAdapter(mParticipantsList);
        mRecyclerViewParticipants.setLayoutManager(new LinearLayoutManager(requireContext()));
        mRecyclerViewParticipants.setAdapter(mParticipantAdapter);
    }


    // Private methods for handling date and time selection

    /**
     * Displays a DatePickerDialog to select the date.
     */
    private void showDateTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Show the DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    calendar.set(selectedYear, selectedMonth, selectedDayOfMonth);
                    showTimePicker(calendar);
                },
                year, month, dayOfMonth);

        datePickerDialog.show();
    }

    /**
     * Displays a TimePickerDialog to select the time.
     *
     * @param calendar Instance of Calendar containing the selected date.
     */
    private void showTimePicker(Calendar calendar) {
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Show the TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                (view, selectedHourOfDay, selectedMinute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, selectedHourOfDay);
                    calendar.set(Calendar.MINUTE, selectedMinute);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                    mEditTextDateTime.setText(dateFormat.format(calendar.getTime()));
                },
                hourOfDay, minute, true);

        timePickerDialog.show();
    }


    // Private methods for managing the participants list

    /**
     * Adds a participant to the list.
     */
    private void addParticipant() {
        String participant = mEditTextParticipant.getText().toString().trim();
        if (!participant.isEmpty()) {
            mParticipantsList.add(participant);
            mParticipantAdapter.notifyDataSetChanged();
            mEditTextParticipant.setText("");
        } else {
            Toast.makeText(requireContext(), "Entrez le nom du participant", Toast.LENGTH_SHORT).show();
        }
    }


    // Method to save the meeting

    /**
     * Saves the meeting using the data entered by the user.
     */
    private void saveMeeting() {
        String title = mEditTextTitle.getText().toString().trim();
        String location = mSpinnerLocation.getSelectedItem().toString(); // Use the Spinner to get the selected meeting room
        String subject = mEditTextSubject.getText().toString().trim();
        String dateTimeString = mEditTextDateTime.getText().toString().trim();

        if (!title.isEmpty() && !subject.isEmpty() && !mParticipantsList.isEmpty() && !dateTimeString.isEmpty()) {

            // Parse the date and time string to Calendar object
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            Calendar dateTime = Calendar.getInstance();
            try {
                dateTime.setTime(Objects.requireNonNull(dateFormat.parse(dateTimeString)));
            } catch (ParseException e) {
                e.printStackTrace();
                return; // Exit if parsing fails
            }

            Meeting meeting = new Meeting(title, dateTime, location, subject, mParticipantsList);

            // Add the meeting using the ViewModel
            mViewModel.addMeeting(meeting);

            // Clear the current Fragment to go back to the previous screen
            requireActivity().getOnBackPressedDispatcher().onBackPressed();

        } else {
            Toast.makeText(requireContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
        }

    }
}