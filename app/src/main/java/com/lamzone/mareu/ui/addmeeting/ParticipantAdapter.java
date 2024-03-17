package com.lamzone.mareu.ui.addmeeting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lamzone.mareu.R;

import java.util.List;

/**
 * Adapter to display the list of participants in a RecyclerView.
 */
public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ParticipantViewHolder> {

    private final List<String> mParticipantsList;

    /**
     * Initializes a new adapter with the provided list of participants.
     *
     * @param participantsList List of participants to display.
     */
    public ParticipantAdapter(List<String> participantsList) {
        mParticipantsList = participantsList;
    }

    @NonNull
    @Override
    public ParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.participant_item, parent, false);
        return new ParticipantViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantViewHolder holder, int position) {
        String participant = mParticipantsList.get(position);
        holder.bind(participant);

        // Add an OnClickListener for the delete button
        holder.mBtnDeleteParticipant.setOnClickListener(v -> {
            // Remove the participant from the list
            mParticipantsList.remove(position);
            // Notify the adapter of the change
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mParticipantsList.size());
        });
    }

    @Override
    public int getItemCount() {
        return mParticipantsList.size();
    }

    /**
     * ViewHolder for each item in the participants list.
     */
    static class ParticipantViewHolder extends RecyclerView.ViewHolder {
        private final TextView mParticipantTextView;
        private ImageButton mBtnDeleteParticipant;

        /**
         * Initializes a new ViewHolder.
         *
         * @param itemView Item view.
         */
        public ParticipantViewHolder(@NonNull View itemView) {
            super(itemView);
            mParticipantTextView = itemView.findViewById(R.id.participantTextView);
            mBtnDeleteParticipant = itemView.findViewById(R.id.btnDeleteParticipant); // Add reference to the delete button
        }

        /**
         * Binds participant data to the view.
         *
         * @param participant Name of the participant to display.
         */
        public void bind(String participant) {
            mParticipantTextView.setText(participant);
        }
    }
}