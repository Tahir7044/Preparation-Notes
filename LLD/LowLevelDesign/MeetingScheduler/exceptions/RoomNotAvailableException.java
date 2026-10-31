package exceptions;

public class RoomNotAvailableException extends RuntimeException {
    public RoomNotAvailableException(String roomId) {
        super("Room not available for the requested time: " + roomId);
    }
}
