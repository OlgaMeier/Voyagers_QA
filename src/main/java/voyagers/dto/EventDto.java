package voyagers.dto;

import java.time.LocalDateTime;
import java.util.Collection;

public class EventDto {
    private Long id;
    private String title;
    private String addressStart;
    private LocalDateTime startDateTime;
    private String addressEnd;
    private LocalDateTime endDateTime;

    public Collection<Object> getEvents() {
        return null;
    }
}
