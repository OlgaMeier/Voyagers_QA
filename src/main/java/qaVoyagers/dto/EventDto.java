package qaVoyagers.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder

public class EventDto {
    private int id;
    private String title;
    private String addressStart;
    private String startDateTime;
    private String addressEnd;
    private String endDateTime;
    private String cost;
    private int maximal_number_of_participants;

}
