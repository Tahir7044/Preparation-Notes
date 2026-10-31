package service;

import enums.MeetingStatus;
import exceptions.*;
import model.Meeting;
import model.Room;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MeetingService {
    private Map<String, Meeting> meetings;
    private final UserService userService;
    private final RoomService roomService;
    private final ConcurrentHashMap<String, Set<Meeting>> userMeetings;

    public MeetingService(Map<String, Meeting> meetings, UserService userService, RoomService roomService) {
        this.meetings = meetings;
        this.userService = userService;
        this.roomService = roomService;
        this.userMeetings =new ConcurrentHashMap<>();
    }

    public Meeting createMeeting(String organizerId, String title, LocalDateTime start, LocalDateTime end, Set<String> participants, String roomId){
        if(start == null || end == null){
            throw new InvalidMeetingException("start and end time are required");
        }
        if(!start.isBefore(end)){
            throw new InvalidMeetingException("start time must be before end time");
        }

        Room room = roomService.getRoom(roomId);
        if(room == null){
            throw new RoomNotFoundException(roomId);
        }
        if(userService.getUser(organizerId) == null){
            throw new UserNotFoundException(organizerId);
        }

        for(String participant: participants){
            if(userService.getUser(participant) == null){
                throw new UserNotFoundException(participant);
            }
        }

        Meeting meeting = new Meeting(title, UUID.randomUUID().toString(), start, end, participants, organizerId, room);

        roomService.bookRoom(roomId, meeting);
        meetings.put(meeting.getMeetingId(), meeting);

        for(String userId: participants){
            boolean clash = userMeetings.getOrDefault(userId, Set.of()).stream()
                            .anyMatch(m -> m.getMeetingStatus()==MeetingStatus.SCHEDULED
                                        && m.getStart().isBefore(end) && start.isBefore(m.getEnd()));
            if(clash){
                System.out.println("Warning: " + userId + " has a conflicting meeting");
            }
        }

        for(String userId: participants){
            userMeetings.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet()).add(meeting);
        }
        userMeetings.computeIfAbsent(organizerId, k -> ConcurrentHashMap.newKeySet()).add(meeting);
        return meeting;
    }

    public void cancelMeeting(String meetingId, String RequestorId) {

        Meeting meeting = meetings.get(meetingId);
        if (meeting == null) {
            throw new MeetingNotFoundException(meetingId);
        }
        if (userService.getUser(RequestorId) == null) {
            throw new UserNotFoundException(RequestorId);
        }

        if (!meeting.getOrganizer().equals(RequestorId)) {
            throw new UnauthorizedOperationException("Only the organizer can cancel meeting " + meetingId);
        }

        synchronized (meeting) {
            if (meeting.getMeetingStatus() == MeetingStatus.CANCELLED) {
                throw new InvalidMeetingException("Meeting already cancelled: " + meetingId);
            }
            meeting.setMeetingStatus(MeetingStatus.CANCELLED);
            roomService.releaseRoom(meeting.getRoom().getRoomId(), meeting);
        }
    }

    public List<Meeting> getMeetings(String userId, LocalDateTime from, LocalDateTime to){
        if(to==null || from ==null || !from.isBefore(to)){
            throw new InvalidMeetingException("invalid time window");
        }
        return userMeetings.getOrDefault(userId, Set.of()).stream()
                .filter(m -> m.getMeetingStatus() == MeetingStatus.SCHEDULED)
                .filter(m -> m.getStart().isBefore(to) && from.isBefore(m.getEnd()))
                .sorted(Comparator.comparing(Meeting::getStart))
                .toList();
    }

    public void addParticipant(String meetingId, String userId, String requesterId){
        Meeting meeting = meetings.get(meetingId);
        if (meeting == null) {
            throw new MeetingNotFoundException(meetingId);
        }
        if(!meeting.getOrganizer().equals(requesterId)){
            throw new UnauthorizedOperationException("Only the organizer can add participants to " + meetingId);
        }
        if (userService.getUser(userId) == null) {
            throw new UserNotFoundException(userId);
        }
        synchronized (meeting){
            if(meeting.getMeetingStatus()==MeetingStatus.CANCELLED){
                throw new InvalidMeetingException("Cannot add participant to cancelled meeting " + meetingId);
            }
            meeting.setParticipant(userId);
            userMeetings.computeIfAbsent(userId, k-> ConcurrentHashMap.newKeySet()).add(meeting);
        }

    }

    public Meeting rescheduleMeeting(String meetingId, String requesterId, LocalDateTime start, LocalDateTime end, String roomId){
        
        if(start == null || end == null){
            throw new InvalidMeetingException("start and end time are required");
        }

        if(!start.isBefore(end)){
            throw new InvalidMeetingException("start time must be before end time");
        }

        Room room = roomService.getRoom(roomId);
        if(room == null){
            throw new RoomNotFoundException(roomId);
        }

        Meeting meeting = meetings.get(meetingId);
        if(meeting == null){
            throw new MeetingNotFoundException(meetingId);
        }

        if (!meeting.getOrganizer().equals(requesterId)){
            throw new UnauthorizedOperationException("Only the organizer can reschedule meeting " + meetingId);
        }


        synchronized (meeting) {
            if (meeting.getMeetingStatus() == MeetingStatus.CANCELLED) {
                throw new InvalidMeetingException("Cannot reschedule cancelled meeting " + meetingId);
            }
            // TODO capacity check if the room changes
            roomService.reschedule(meeting, roomId, start, end);
        }
        return meeting;


    }

}
