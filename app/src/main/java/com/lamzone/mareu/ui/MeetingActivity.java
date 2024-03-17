package com.lamzone.mareu.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.lamzone.mareu.R;
import com.lamzone.mareu.ui.addmeeting.AddMeetingFragment;
import com.lamzone.mareu.ui.meetinglist.MeetingListFragment;
import com.lamzone.mareu.application.MeetingListViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Main activity of the application, responsible for managing fragments and UI.
 */
public class MeetingActivity extends AppCompatActivity {

    private MeetingListViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        // Initialize ViewModel using ViewModelProvider
        mViewModel = new ViewModelProvider(this).get(MeetingListViewModel.class);

        // Access AddMeetingFragment when FAB button is clicked
        ImageButton fabAddMeeting = findViewById(R.id.fabAddMeeting);
        fabAddMeeting.setOnClickListener(view -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new AddMeetingFragment())
                    .addToBackStack(null) // Add transaction to the back stack
                    .commit();

            toggleButtonsVisibility(false); // Hide buttons when AddMeetingFragment is added
        });

        // Handle click on Options button
        ImageButton btnOptions = findViewById(R.id.btnOptions);
        btnOptions.setOnClickListener(view -> showOptionsDialog());

        // Add default meeting_list fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new MeetingListFragment())
                    .commit();
        }

        // Add a callback to handle back press from AddMeetingFragment
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Go back when back button is pressed
                getSupportFragmentManager().popBackStack();
                toggleButtonsVisibility(true); // Restore button visibility when back from AddMeetingFragment
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    // Method to display a UI for selecting filtering options
    private void showOptionsDialog() {
        // Create options interface view from resource file
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_options, null);

        // Initialize options interface elements
        CheckBox checkBoxDate = dialogView.findViewById(R.id.checkBoxDate);
        CheckBox checkBoxLocation = dialogView.findViewById(R.id.checkBoxLocation);
        Spinner spinnerLocations = dialogView.findViewById(R.id.spinnerLocations);
        EditText editTextDate = dialogView.findViewById(R.id.editTextDate);

        // Show DatePicker to select date
        editTextDate.setOnClickListener(view -> {
            Calendar currentDate = Calendar.getInstance();
            int year = currentDate.get(Calendar.YEAR);
            int month = currentDate.get(Calendar.MONTH);
            int day = currentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        // Create Calendar object with selected date
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(selectedYear, selectedMonth, selectedDay);

                        // Display selected date in EditText
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        editTextDate.setText(sdf.format(selectedDate.getTime()));

                        // Save selected date to use when applying filters
                        editTextDate.setTag(selectedDate);
                    }, year, month, day);

            datePickerDialog.show();
        });

        // Load available meeting rooms
        mViewModel.getMeetingRooms().observe(this, meetingRooms -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, meetingRooms);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerLocations.setAdapter(adapter);
        });

        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Options de Filtrage")
                .setPositiveButton("Filtrer", (dialog, which) -> {
                    // Handle actions when user clicks "Filter" button
                    // Get checkbox states and apply filters accordingly
                    boolean filterByDate = checkBoxDate.isChecked();
                    boolean filterByLocation = checkBoxLocation.isChecked();
                    String selectedLocation = spinnerLocations.getSelectedItem().toString();
                    Calendar selectedDate = (Calendar) editTextDate.getTag(); // Get selected date

                    // Get an instance of your MeetingListFragment
                    MeetingListFragment meetingListFragment = (MeetingListFragment) getSupportFragmentManager().findFragmentById(R.id.container);

                    // Apply filters based on selected options in the fragment
                    if (meetingListFragment != null) {
                        meetingListFragment.applyFilters(filterByDate, filterByLocation, selectedLocation, selectedDate);
                    }
                })
                .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Method to toggle visibility of + and Options buttons
    private void toggleButtonsVisibility(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        findViewById(R.id.btnOptions).setVisibility(visibility);
        findViewById(R.id.fabAddMeeting).setVisibility(visibility);
    }
}