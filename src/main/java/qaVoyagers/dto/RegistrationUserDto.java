package qaVoyagers.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class RegistrationUserDto {

    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String email;
    private String password;
    private String phone;
    private String photo;
    private GenderDto gender;
}
