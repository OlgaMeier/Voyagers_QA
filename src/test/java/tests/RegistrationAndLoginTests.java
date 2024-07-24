package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qaVoyagers.dto.ErrorMessageDto;
import qaVoyagers.dto.GenderDto;
import qaVoyagers.dto.LoginBodyDto;
import qaVoyagers.dto.RegistrationUserDto;
import qaVoyagers.dto.TokenDto;
import qaVoyagers.utils.HttpUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static qaVoyagers.utils.HttpUtils.LOGIN_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.REGISTRATION_ENDPOINT;
import static qaVoyagers.utils.Utils.isNullOrEmpty;
import static tests.BaseTest.getTestUserLoginBody;

public class RegistrationAndLoginTests extends BaseTest {
    @Test
    @DisplayName("Проверка успешной авторизации")
    void test1() {
        TokenDto tokenDto = HttpUtils.postResponse(getTestUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class);

        Assertions.assertFalse(isNullOrEmpty(tokenDto.getAccessToken()), "Пришел пустой токен");
    }

    @Test
    @DisplayName("Проверка авторизации с некорректным email")
    void test2() {
        LoginBodyDto loginRqBody = getTestUserLoginBody();
        loginRqBody.setEmail("error@gm.com");

        ErrorMessageDto errorMessageDto = HttpUtils.postResponse(loginRqBody, LOGIN_ENDPOINT, 401, ErrorMessageDto.class);

        Assertions.assertEquals("User with this name not found", errorMessageDto.getMessage(), "Текст ошибки не соответствует ожидаемому");
        Assertions.assertEquals("UNAUTHORIZED", errorMessageDto.getError(), "Тип ошибки не соответствует ожидаемому");
    }

    @Test
    @DisplayName("Проверка авторизации с некорректным password")
    void test6() {
        LoginBodyDto loginRqBody = getTestUserLoginBody();
        loginRqBody.setPassword("noPass!");

        ErrorMessageDto errorMessageDto = HttpUtils.postResponse(loginRqBody, LOGIN_ENDPOINT, 401, ErrorMessageDto.class);

        Assertions.assertEquals("Password is incorrect", errorMessageDto.getMessage(), "Текст ошибки не соответствует ожидаемому");
        Assertions.assertEquals("UNAUTHORIZED", errorMessageDto.getError(), "Тип ошибки не соответствует ожидаемому");
    }

    @Test
    @DisplayName("Проверка успешной регистрации")
    void test3() {


        RegistrationUserDto registrationBody = RegistrationUserDto.builder()
                .firstName("Dragon")
                .lastName("Dragon")
                .email("dragon@example.com")
                .password("Dragon111!")
                .dateOfBirth("1995-01-01")
                .phone("+123456789")
                .photo("photo")
                .gender(GenderDto.builder().gender(1).build())
                .build();

        TokenDto tokenDto = HttpUtils.postResponse(registrationBody, REGISTRATION_ENDPOINT, 200, TokenDto.class);
        Assertions.assertFalse(isNullOrEmpty(tokenDto.getAccessToken()), "Пришел пустой токен");
    }

    @Test
    @DisplayName("Проверка регистрации с уже существующим email")
    void test4() {
        RegistrationUserDto registrationRqBody= getRegistrationUserBody();

        ErrorMessageDto errorMessageDto = HttpUtils.postResponse(registrationRqBody, REGISTRATION_ENDPOINT, 409, ErrorMessageDto.class);

        Assertions.assertEquals("User already exists", errorMessageDto.getMessage(), "Текст ошибки не соответствует ожидаемому");
        Assertions.assertEquals("Con flict", errorMessageDto.getError(), "Тип ошибки не соответствует ожидаемому");
    }


    @Test
    @DisplayName("Проверка Login с некорректным email")
    void test5() {
        LoginBodyDto loginRqBody = LoginBodyDto.builder()
                .email("dragon@example")
                .password("Dragon111!")
                .build();

        ErrorMessageDto errorMessageDto = HttpUtils.postResponse(loginRqBody, LOGIN_ENDPOINT, 401, ErrorMessageDto.class);
        String msg = errorMessageDto.getMessage().toString();
        Assertions.assertTrue(msg.contains("must be a well-formed email address"), "Текст ошибки не соответствует ожидаемому");
        Assertions.assertEquals("Bad Request", errorMessageDto.getError(), "Тип ошибки не соответствует ожидаемому");
    }
}
