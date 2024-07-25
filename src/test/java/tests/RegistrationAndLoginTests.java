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

import static qaVoyagers.utils.HttpUtils.LOGIN_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.REGISTRATION_ENDPOINT;
import static qaVoyagers.utils.Utils.isNullOrEmpty;

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
    }

    @Test
    @DisplayName("Проверка авторизации с некорректным password")
    void test6() {
        LoginBodyDto loginRqBody = getTestUserLoginBody();
        loginRqBody.setPassword("noPass!");

        ErrorMessageDto errorMessageDto = HttpUtils.postResponse(loginRqBody, LOGIN_ENDPOINT, 401, ErrorMessageDto.class);

        Assertions.assertEquals("Password is incorrect", errorMessageDto.getMessage(), "Текст ошибки не соответствует ожидаемому");
    }

    @Test
    @DisplayName("Проверка успешной регистрации")
    void test3() {
        RegistrationUserDto registrationBody = RegistrationUserDto.builder()
                .firstName("Dragon")
                .lastName("Dragon")
                .email("dragonqq@example.com")
                .password("Dragon111!")
                .dateOfBirth("1995-01-01")
                .phone("+123456789")
                .photo("photo")
                .gender(GenderDto.builder().id(1).build())
                .build();

        HttpUtils.getResponse(HttpUtils.HttpMethods.POST, REGISTRATION_ENDPOINT, null, 200, registrationBody);
    }

    @Test
    @DisplayName("Проверка регистрации с уже существующим email")
    void test4() {
        RegistrationUserDto registrationRqBody = getRegistrationUserBody();

        ErrorMessageDto errorMessageDto = HttpUtils.postResponse(registrationRqBody, REGISTRATION_ENDPOINT, 409, ErrorMessageDto.class);

        Assertions.assertEquals("User with that name already exists", errorMessageDto.getMessage(), "Текст ошибки не соответствует ожидаемому");
    }


    @Test
    @DisplayName("Проверка Login с некорректным email")
    void test5() {
        LoginBodyDto loginRqBody = LoginBodyDto.builder()
                .email("dragon@example")
                .password("Dragon111!")
                .build();

        ErrorMessageDto errorMessageDto = HttpUtils.postResponse(loginRqBody, LOGIN_ENDPOINT, 401, ErrorMessageDto.class);
        Assertions.assertEquals("User with this name not found", errorMessageDto.getMessage(), "Тип ошибки не соответствует ожидаемому");
    }
}
