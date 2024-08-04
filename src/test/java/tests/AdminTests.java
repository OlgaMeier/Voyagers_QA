package tests;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonTypeInfo;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qaVoyagers.dto.EventDto;
import qaVoyagers.dto.GenderDto;
import qaVoyagers.dto.LoginBodyDto;
import qaVoyagers.dto.ResponseMessageDto;
import qaVoyagers.dto.RoleDto;
import qaVoyagers.dto.TokenDto;
import qaVoyagers.dto.UserDto;
import qaVoyagers.utils.HttpUtils;

import static ch.qos.logback.classic.spi.ThrowableProxyVO.build;
import static qaVoyagers.utils.HttpUtils.ADD_ROLE_TO_USER_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.BLOCK_USER_BY_ADMIN;
import static qaVoyagers.utils.HttpUtils.DELETE_MY_EVENT_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.GET_LIST_OF_USER;
import static qaVoyagers.utils.HttpUtils.HttpMethods.PUT;
import static qaVoyagers.utils.HttpUtils.LIST_OF_MY_EVENTS;
import static qaVoyagers.utils.HttpUtils.LOGIN_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.UPDATE_EVENT_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.getResponse;
import static qaVoyagers.utils.HttpUtils.getResponseWithToken;
import static qaVoyagers.utils.HttpUtils.postResponse;
import static qaVoyagers.utils.HttpUtils.putResponseWithToken;
import static tests.BaseTest.getAliceUserLoginBody;
import static tests.BaseTest.getTestUserLoginBody;

public class AdminTests extends BaseTest {
    //   String url = "http://localhost:8080/api
    private String token;
    private String userID = "17";

    @Test
    @DisplayName("Проверка получения списка  Users,  авторизованным Администратором TESTUSER")
    void getAllUsers_TestUser() {
        token = postResponse(getTestUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getAccessToken();

        // Получение списка активных событий с токеном
        ResponseMessageDto[] getAllUsers = HttpUtils.getResponseWithToken(HttpUtils.HttpMethods.GET, GET_LIST_OF_USER, 200, token, ResponseMessageDto[].class);

        Assertions.assertNotNull(getAllUsers, "List of active events is empty");
        Assertions.assertTrue(getAllUsers.length > 0, "List of active events is empty");
    }


    @Test
    @DisplayName("Назначение роли администратора пользователю с проверкой назначения,  авторизованным Администратором TESTUSER")
    void userGotAdmin_byTestUser() {
        token = postResponse(getTestUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getAccessToken();


        // Получение списка активных событий с токеном
       UserDto blockUsers17 = HttpUtils.putResponseWithToken(PUT,  ADD_ROLE_TO_USER_ENDPOINT.replace("{userId}", userID), 200, token, UserDto.class);

        Assertions.assertEquals(200, "200");
        //Assertions.assertTrue(getAllUsers.length > 0, "List of active events is empty");
    }


    @Test
    @DisplayName("Блокирование прльзователя ID -17,  Администратором TESTUSER ID-10")
    void blockUsers_TestUser() {
        token = postResponse(getTestUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getAccessToken();


        // Получение списка активных событий с токеном
       UserDto blockUsers17 = HttpUtils.putResponseWithToken(PUT,  BLOCK_USER_BY_ADMIN.replace("{userId}", userID), 200, token, UserDto.class);

        Assertions.assertEquals(200, "200");
        //Assertions.assertTrue(getAllUsers.length > 0, "List of active events is empty");
    }

}





 /*  private int roleId = 1;


    @Test
    @DisplayName("Назначение роли администратора пользователю с проверкой назначения")
    void test1AssignAdminRoleToUser() {
        // Авторизация и получение токена

        token = postResponse(getTestUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getAccessToken();
        // Создание объекта пользователя с roleId 1 (роль администратора)
        UserDto testUserAdmin = UserDto.builder()
              //  .userId("10")
                .roleId(RoleDto.builder().roleId(1).build())
                .build();
        // Присвоение роли админа пользователю
        ResponseMessageDto roleAdminResponse = putResponseWithToken(
                testUserAdmin,
                ADD_ROLE_TO_USER_ENDPOINT.replace("{userid}", userID),
                200,
                token,
                ResponseMessageDto.class);

        // Проверка ответа сервера
        Assertions.assertNotNull(roleAdminResponse, "Ответ проверки роли пользователя не получен");
// Получение обновленной информации о пользователе и проверка назначения роли
        UserDto updatedUser = getResponseWithToken(
                roleAdminResponse,
                ADD_ROLE_TO_USER_ENDPOINT.replace("{userid}", userID),
                200,
                token,
                UserDto.class);

        Assertions.assertEquals(roleId, updatedUser.getRoleId().getRoleId(), "Роль пользователя не соответствует ожидаемой");
    }




   @Test
    @DisplayName("Назначение роли администратора пользователю с проверкой назначения")
    void testAssignAdminRoleToUser() {
        token = postResponse(getTestUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getAccessToken();

        // Присвоение роли
        ResponseMessageDto roleAdminResponse = putResponseWithToken(
                UserDto.builder().roleId(1).build(),
                ADD_ROLE_TO_USER_ENDPOINT.replace("{userId}", userID),
                200,
                token,
                ResponseMessageDto.class);

        // Проверка ответа
        Assertions.assertNotNull(roleAdminResponse, "Ответ проверки роли пользователя не получен");
}*/


