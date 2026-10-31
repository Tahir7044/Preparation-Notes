package model;

import enums.MeetingStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Meeting {
    private final String title;
    private final String meetingId;
    private LocalDateTime start;
    private LocalDateTime end;
    private final Set<String> participants;
    private Room room;
    private final String organizer;
    private MeetingStatus meetingStatus;

    public Meeting(String title, String meetingId, LocalDateTime start, LocalDateTime end, Set<String> participants, String organizer, Room room) {
        this.title = title;
        this.meetingId = meetingId;
        this.start = start;
        this.end = end;
        this.participants = ConcurrentHashMap.newKeySet();
        this.participants.addAll(participants);
        this.organizer = organizer;
        this.meetingStatus = MeetingStatus.SCHEDULED;
        this.room = room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public void setMeetingStatus(MeetingStatus meetingStatus) {
        this.meetingStatus = meetingStatus;
    }

    public String getTitle() {
        return title;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public Set<String> getParticipants() {
        return participants;
    }

    public void setParticipant(String userId) {
        participants.add(userId);
    }

    public Room getRoom() {
        return room;
    }

    public String getOrganizer() {
        return organizer;
    }

    public MeetingStatus getMeetingStatus() {
        return meetingStatus;
    }
}
