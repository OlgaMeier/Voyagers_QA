package qaVoyagers.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class CommentsEventRsDto {
    // private Long commentId;
    private String firstName;
    private String lastName;
    private String comments;
    public String eventTitle;
}

