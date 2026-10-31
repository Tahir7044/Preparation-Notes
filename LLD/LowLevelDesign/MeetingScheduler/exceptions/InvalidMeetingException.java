package exceptions;

public class InvalidMeetingException extends RuntimeException {
    public InvalidMeetingException(String message) {
        super(message);
    }
}
