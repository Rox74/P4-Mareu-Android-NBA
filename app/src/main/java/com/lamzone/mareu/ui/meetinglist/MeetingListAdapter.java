package com.lamzone.mareu.ui.meetinglist;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.lamzone.mareu.R;
import com.lamzone.mareu.model.Meeting;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Adapter to display the list of meetings in a RecyclerView.
 */
public class MeetingListAdapter extends RecyclerView.Adapter<MeetingListAdapter.MeetingViewHolder> {

    private List<Meeting> mMeetings;
    private List<Meeting> mFilteredMeetings; // Adding reference to filtered list

    /**
     * Initializes a new adapter with the provided filtered meetings list.
     *
     * @param filteredMeetings Filtered meetings list to display.
     */
    public MeetingListAdapter(List<Meeting> filteredMeetings) {
        mFilteredMeetings = filteredMeetings;
    }

    /**
     * Sets the complete list of meetings to display.
     *
     * @param meetings Complete list of meetings.
     */
    public void setMeetings(List<Meeting> meetings) {
        mMeetings = meetings;
        Collections.sort(mMeetings); // Sorting meetings by date and time
        notifyDataSetChanged(); // Refreshing the list

        // Log.d("MeetingListAdapter", "Meetings set: " + meetings.size());
    }

    @NonNull
    @Override
    public MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.meeting_item, parent, false);
        return new MeetingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingViewHolder holder, int position) {
        if (mMeetings != null) {
            Meeting meeting = mMeetings.get(position);
            holder.bind(meeting);

            holder.btnDelete.setOnClickListener(v -> {
                // Delete the meeting when the button is clicked
                Context context = v.getContext();
                if (context instanceof FragmentActivity) {
                    MeetingListFragment fragment = (MeetingListFragment) ((FragmentActivity) context).getSupportFragmentManager().findFragmentById(R.id.container);
                    if (fragment != null) {
                        fragment.deleteMeeting(meeting);
                    }
                }
            });

            // Log.d("MeetingListAdapter", "Meeting bound: " + meeting.getTitle()); // Meeting binding log
        }
    }

    @Override
    public int getItemCount() {
        return mMeetings != null ? mMeetings.size() : 0;
    }

    /**
     * ViewHolder for each item in the meetings list.
     */
    static class MeetingViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;
        private TextView mDetailsTextView;
        private View mViewColor;
        public View btnDelete;

        /**
         * Initializes a new ViewHolder.
         *
         * @param itemView Item view.
         */
        MeetingViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleTextView = itemView.findViewById(R.id.textTitle);
            mDetailsTextView = itemView.findViewById(R.id.textDetails);
            mViewColor = itemView.findViewById(R.id.viewColor); // Initialize mViewColor view
            btnDelete = itemView.findViewById(R.id.btnDelete); // Initialize btnDelete
        }

        /**
         * Binds meeting data to the view.
         *
         * @param meeting Meeting to display.
         */
        void bind(Meeting meeting) {
            // Format date and time
            Calendar calendar = meeting.getDateTime();
            DateFormat dateFormat;
            if (isToday(calendar)) {
                dateFormat = new SimpleDateFormat("HH'h'mm", Locale.getDefault());
            } else {
                dateFormat = new SimpleDateFormat("HH'h'mm dd-MM-yyyy", Locale.getDefault());
            }
            String formattedDateTime = dateFormat.format(calendar.getTime());

            // Building meeting information
            String meetingInfo = String.format("%s - %s - %s", meeting.getTitle(), formattedDateTime, meeting.getLocation());
            mTitleTextView.setText(meetingInfo);

            // Building participants list
            StringBuilder participantsInfo = new StringBuilder();
            for (String participant : meeting.getParticipants()) {
                participantsInfo.append(participant).append("\n");
            }
            mDetailsTextView.setText(participantsInfo.toString());

            // Set viewColor view color based on meeting proximity
            setViewColor(meeting);
        }

        // Check if the date is today
        private boolean isToday(Calendar date) {
            Calendar today = Calendar.getInstance();
            return date.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                    date.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                    date.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
        }

        // Set viewColor view color based on meeting proximity
        private void setViewColor(Meeting meeting) {
            Calendar currentTime = Calendar.getInstance();
            Calendar meetingTime = meeting.getDateTime();

            long timeDifferenceMillis = meetingTime.getTimeInMillis() - currentTime.getTimeInMillis();
            long timeDifferenceMinutes = TimeUnit.MILLISECONDS.toMinutes(timeDifferenceMillis);

            int colorResId = timeDifferenceMinutes <= 30 ? R.color.colorCircle1 :
                    timeDifferenceMinutes <= 60 ? R.color.colorCircle2 :
                            isToday(meetingTime) ? R.color.colorCircle3 : R.color.colorCircle4;

            // Set oval shape Drawable as background of mViewColor view
            mViewColor.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.circle));
            // Change circle fill color based on the color defined in circle_background.xml file
            mViewColor.getBackground().setColorFilter(ContextCompat.getColor(itemView.getContext(), colorResId), PorterDuff.Mode.SRC_IN);
        }
    }
}