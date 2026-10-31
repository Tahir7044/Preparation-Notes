package exceptions;

public class MeetingNotFoundException extends RuntimeException {
    public MeetingNotFoundException(String meetingId) {
        super("Meeting not found: " + meetingId);
    }
}
