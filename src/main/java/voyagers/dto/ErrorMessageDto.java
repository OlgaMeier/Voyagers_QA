package voyagers.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ErrorMessageDto {
    private Object message;
    private String error;
    private String path;
    private String timestamp;
    private int status;
}
