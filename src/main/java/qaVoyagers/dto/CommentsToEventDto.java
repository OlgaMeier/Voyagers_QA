package qaVoyagers.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class CommentsToEventDto {
        private Long commentId;
        private String text;
        private String author;
        private String timestamp;
    }

