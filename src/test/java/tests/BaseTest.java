package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import voyagers.dto.AuthentificationBodyDto;
import voyagers.dto.UserDto;
import voyagers.utils.TestProperties;

import java.util.Date;
import java.util.Properties;

public class BaseTest {
    public static Properties properties = TestProperties.getINSTANCE().getProperties();

    @BeforeAll
    public static void load() {
        //Указываем в настройках RestAssured наш адрес для запросов
        //https://contactapp-telran-backend.herokuapp.com как на тренировочном сайте
        RestAssured.baseURI = properties.getProperty("base.url");
        //Указываем в настройках RestAssured наш путь для запросов
        //v1
        RestAssured.basePath = properties.getProperty("base.version");
        //https://contactapp-telran-backend.herokuapp.com/v1  - дальше это эндпоинты для работы /user/login/usernamepassword";
    }

    static AuthentificationBodyDto getTestUserLoginBody() {
        return AuthentificationBodyDto.builder()
                .email(properties.getProperty("testuser.email"))
                .password(properties.getProperty("testuser.password"))
                .build();
    }

    static AuthentificationBodyDto getDeleteTestUserLoginBody() {
        return AuthentificationBodyDto.builder()
                .email(properties.getProperty("delete_user.email"))
                .password(properties.getProperty("testuser.password"))
                .build();
    }

    static UserDto getUserBody() {
        return UserDto.builder()
                .firstName(properties.getProperty("user.name"))
                .lastName(properties.getProperty("user.lastname"))
                .dateOfBirth(properties.getProperty("user.dateOfBirth"))
                .email(properties.getProperty("user.email"))
                .password(properties.getProperty("user.password"))
                .phone(properties.getProperty("user.phone"))
                .photo(properties.getProperty("user.photo"))
                .build();

    }
}
