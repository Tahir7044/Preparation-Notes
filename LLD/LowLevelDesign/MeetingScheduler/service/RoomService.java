package service;

import exceptions.RoomNotAvailableException;
import model.Meeting;
import model.Room;
import model.RoomCalendar;
import model.RoomConflictChecker;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoomService {
    private Map<String, Room> rooms;
    private ConcurrentHashMap<String, RoomCalendar> roomCalendars;

    public RoomService(Map<String, Room> rooms) {
        this.rooms = rooms;
        this.roomCalendars = new ConcurrentHashMap<>();
        rooms.keySet().forEach(id-> roomCalendars.put(id, new RoomCalendar(id, new RoomConflictChecker())));
    }

    public Room getRoom(String roomId){
        return rooms.get(roomId);
    }

    public RoomCalendar getRoomCalendar(String roomId){
        return roomCalendars.get(roomId);
    }

    public void bookRoom(String roomId, Meeting meeting){
        RoomCalendar roomCalendar = roomCalendars.get(roomId);
        roomCalendar.book(meeting);
    }

    public void releaseRoom(String roomId, Meeting meeting){
        RoomCalendar roomCalendar = roomCalendars.get(roomId);
        roomCalendar.release(meeting);
    }

    public void reschedule(Meeting meeting, String newRoomId, LocalDateTime start, LocalDateTime end){
        RoomCalendar oldRoomCalendar = roomCalendars.get(meeting.getRoom().getRoomId());
        RoomCalendar newRoomCalendar = roomCalendars.get(newRoomId);

        RoomCalendar first = oldRoomCalendar.getRoomId().compareTo(newRoomCalendar.getRoomId()) <=0 ? oldRoomCalendar : newRoomCalendar;
        RoomCalendar second = first == oldRoomCalendar ? newRoomCalendar : oldRoomCalendar;

        first.lock();
        if(first!=second) second.lock();
        try {
            if(!newRoomCalendar.isAvailable(start, end, meeting.getMeetingId())){
                throw new RoomNotAvailableException(newRoomId); 
            }
            oldRoomCalendar.release(meeting);
            meeting.setStart(start);
            meeting.setEnd(end);
            meeting.setRoom(rooms.get(newRoomId));
            newRoomCalendar.book(meeting);
        }

        finally {
            if(first!=second) second.unlock();
            first.unlock();
        }
    }

}
