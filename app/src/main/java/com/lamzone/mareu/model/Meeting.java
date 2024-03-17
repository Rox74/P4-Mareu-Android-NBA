package com.lamzone.mareu.model;

import java.util.Calendar;
import java.util.List;

/**
 * The Meeting class represents a meeting.
 */
public class Meeting implements Comparable<Meeting> {
    private String title;
    private Calendar dateTime;
    private String location;
    private String subject;
    private List<String> participants;

    /**
     * Constructor to create an instance of Meeting.
     *
     * @param title       The title of the meeting.
     * @param dateTime    The date and time of the meeting.
     * @param location    The location of the meeting.
     * @param subject     The subject of the meeting.
     * @param participants The list of participants in the meeting.
     */
    public Meeting(String title, Calendar dateTime, String location, String subject, List<String> participants) {
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
        this.subject = subject;
        this.participants = participants;
    }

    /**
     * Compare this meeting to another meeting to determine their relative order based on date and time.
     *
     * @param other The other meeting to compare.
     * @return A negative integer, zero, or a positive integer if this meeting is before, equal to, or after the other meeting.
     */
    @Override
    public int compareTo(Meeting other) {
        return this.getDateTime().compareTo(other.getDateTime());
    }


    // Getters

    /**
     * Get the title of the meeting.
     *
     * @return The title of the meeting.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the date and time of the meeting.
     *
     * @return The date and time of the meeting.
     */
    public Calendar getDateTime() {
        return dateTime;
    }

    /**
     * Get the location of the meeting.
     *
     * @return The location of the meeting.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Get the subject of the meeting.
     *
     * @return The subject of the meeting.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Get the list of participants in the meeting.
     *
     * @return The list of participants in the meeting.
     */
    public List<String> getParticipants() {
        return participants;
    }
}