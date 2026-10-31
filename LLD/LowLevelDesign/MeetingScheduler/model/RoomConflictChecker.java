package model;

import enums.MeetingStatus;

import java.time.LocalDateTime;
import java.util.Collection;

public class RoomConflictChecker implements ConflictChecker {

    public boolean hasConflict(Collection<Meeting> existingMeetings, String excludeMeetingId, LocalDateTime start, LocalDateTime end){
        for(Meeting m: existingMeetings){
            if(!m.getMeetingId().equals(excludeMeetingId) &&
                    m.getStart().isBefore(end) &&
                    start.isBefore(m.getEnd())
            ) {
                return true;
            }
        }
        return false;
    }
}
