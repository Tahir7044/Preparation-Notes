package model;

import exceptions.RoomNotAvailableException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class RoomCalendar {
    private final String roomId;
    private final TreeMap<LocalDateTime, Meeting> meetings = new TreeMap<>();
    private final ReentrantLock lock;
    private final ConflictChecker conflictChecker; 

    public RoomCalendar(String roomId, ConflictChecker conflictChecker) {
        this.roomId = roomId;
        this.lock = new ReentrantLock();
        this.conflictChecker = conflictChecker;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isAvailable(LocalDateTime start, LocalDateTime end, String excludeMeetingId){

       lock.lock();
       try{
            return !conflictChecker.hasConflict(meetings.values(),excludeMeetingId, start, end);
       } finally{
        lock.unlock();
       }

    }

    public void book(Meeting meeting){

        lock.lock(); // Explicitly acquire the lock
        try {
            if(!isAvailable(meeting.getStart(), meeting.getEnd(), meeting.getMeetingId())){
                throw new RoomNotAvailableException(roomId);
            }
            meetings.put(meeting.getStart(), meeting);
        } finally {
            lock.unlock(); // Always release in a finally block
        }
    }

    public void release(Meeting meeting){
        lock.lock(); // Explicitly acquire the lock
        try {
            meetings.remove(meeting.getStart(), meeting);
        } finally {
            lock.unlock(); // Always release in a finally block
        }
    }

    public void lock()   { lock.lock(); }
    public void unlock() { lock.unlock(); }

}
