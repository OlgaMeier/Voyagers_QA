package qaVoyagers.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder

public class EventDto {
    private Long eventId;
    private String title;
    private String addressStart;
    private LocalDateTime startDateTime;
    private String addressEnd;
    private LocalDateTime endDateTime;
    private Integer maxNnumberOfParticipants;

}
