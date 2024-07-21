package voyagers.dto;
import lombok.Builder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder

//the same as RegistrationDto
public class AuthentificationBodyDto {
    private String email;
    private String password;
}
