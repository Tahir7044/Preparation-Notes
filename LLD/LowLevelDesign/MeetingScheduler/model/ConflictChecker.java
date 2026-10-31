package model;

import java.time.LocalDateTime;
import java.util.Collection;

public interface ConflictChecker {
    boolean hasConflict(Collection<Meeting> existing, String excludeMeetingId, LocalDateTime start, LocalDateTime end);
}
