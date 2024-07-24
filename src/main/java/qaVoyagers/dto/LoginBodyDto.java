package qaVoyagers.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder

public class LoginBodyDto {

    private String email;
    private String password;
}


