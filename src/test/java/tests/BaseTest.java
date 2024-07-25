package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import qaVoyagers.dto.EventDto;
import qaVoyagers.dto.GenderDto;
import qaVoyagers.dto.LoginBodyDto;
import qaVoyagers.dto.RegistrationUserDto;
import qaVoyagers.utils.TestProperties;

import java.time.LocalDateTime;
import java.util.Properties;

public class BaseTest {
    public static Properties properties = TestProperties.getINSTANCE().getProperties();
    private static String authToken;

    @BeforeAll
    public static void load() {
        //Указываем в настройках RestAssured наш адрес для запросов
        //https://contactapp-telran-backend.herokuapp.com
        RestAssured.baseURI = properties.getProperty("base.url");
        //Указываем в настройках RestAssured наш путь для запросов
        //v1
        RestAssured.basePath = properties.getProperty("base.version");
        //https://contactapp-telran-backend.herokuapp.com/v1  - дальше это эндпоинты для работы /user/login/usernamepassword";


    }

    static LoginBodyDto getTestUserLoginBody() {
        return LoginBodyDto.builder()
                .email(properties.getProperty("testuser.email"))
                .password(properties.getProperty("testuser.password"))
                .build();
    }


    static RegistrationUserDto getRegistrationUserBody() {

        return RegistrationUserDto.builder()
                .firstName(properties.getProperty(" testuser.firstname"))
                .lastName(properties.getProperty("testuser.lastname"))
                .dateOfBirth(properties.getProperty("testuser.dateOfBirth"))

                .email(properties.getProperty("testuser.email"))
                .password(properties.getProperty("testuser.password"))
                .phone(properties.getProperty("testuser.phone"))
                .photo(properties.getProperty("testuser.photo"))
                .gender(GenderDto.builder()
                        .id(Integer.parseInt(properties.getProperty("testuser.gender")))
                        .build())
                .build();
    }
    /*как удалить евент афторизованному юзеру
   static LoginBodyDto getDeleteTestUserLoginBody() {
        return LoginBodyDto.builder()
                .email(properties.getProperty("delete_user.name"))
                .password(properties.getProperty("testuser.pass"))
                .build();
    }*/

    static EventDto getEventBody() {
        return EventDto.builder()
                .title(properties.getProperty("event.title"))
                .addressStart(properties.getProperty("event.addressStart"))
                .startDateTime(properties.getProperty("event.startDateTime"))
                .addressEnd(properties.getProperty("event.addressEnd"))
                .endDateTime(properties.getProperty("event.endDateTime"))
                .maximal_number_of_participants(Integer.valueOf(properties.getProperty("event.maxNnumberOfParticipants")))
                .build();
    }
}
