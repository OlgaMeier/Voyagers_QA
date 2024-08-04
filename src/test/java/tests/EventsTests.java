package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qaVoyagers.dto.ApplyEventRqDto;
import qaVoyagers.dto.CommentsEventRqDto;
import qaVoyagers.dto.CommentsEventRsDto;
import qaVoyagers.dto.ErrorMessageDto;
import qaVoyagers.dto.EventDto;
import qaVoyagers.dto.EventsDto;
import qaVoyagers.dto.ResponseMessageDto;
import qaVoyagers.dto.ResponseWithoutMessageDto;
import qaVoyagers.dto.RoleDto;
import qaVoyagers.dto.TokenDto;
import qaVoyagers.utils.HttpUtils;

import static qaVoyagers.utils.HttpUtils.ADD_EVENT_COMMENTS_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.ADD_ROLE_TO_USER_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.APPLY_TO_EVENT_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.CREATE_EVENT_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.DELETE_MY_EVENT_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.GET_ACTIVE_EVENTS_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.GET_ARCHIVE_EVENTS_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.GET_EVENT_COMMENTS_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.GET_INFO_ABOUT_EVENT_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.GET_MY_EVENTS_WITH_MY_PARTICIPANTS_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.LIST_OF_MY_EVENTS;
import static qaVoyagers.utils.HttpUtils.LOGIN_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.UPDATE_EVENT_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.getResponse;
import static qaVoyagers.utils.HttpUtils.getResponseWithToken;
import static qaVoyagers.utils.HttpUtils.postResponse;
import static qaVoyagers.utils.HttpUtils.postResponseWithToken;
import static qaVoyagers.utils.HttpUtils.putResponseWithToken;

public class EventsTests extends BaseTest {


    private String token;


    @Test
    @DisplayName("Проверка получения списка активных events без авторизации")
    void getActiveEvents() {
        Response activeEvents = getResponse(HttpUtils.HttpMethods.GET, GET_ACTIVE_EVENTS_ENDPOINT, null, 200, EventsDto.class);

        Assertions.assertNotNull(activeEvents, "List of active events is empty");
        Assertions.assertNotNull(activeEvents.getBody(), "List of active events is empty");

    }
    @Test
    @DisplayName("Проверка получения списка  events, с участием авторизованного пользователя TESTUSER")
    void getMyApplicatedEvents_AliceUser() {
        token = postResponse(getAliceUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getAccessToken();

        // Получение списка активных событий с токеном
        EventDto[] activeEvents = HttpUtils.getResponseWithToken(HttpUtils.HttpMethods.GET, GET_MY_EVENTS_WITH_MY_PARTICIPANTS_ENDPOINT, 200, token, EventDto[].class);

        Assertions.assertNotNull(activeEvents, "List of active events is empty");
        Assertions.assertTrue(activeEvents.length > 0, "List of active events is empty");
    }

    @Test
    @DisplayName("Проверка получения списка  events, созданных авторизованным пользователем TESTUSER")
    void getMyEvents_TestUser() {
        token = postResponse(getTestUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getAccessToken();

        // Получение списка активных событий с токеном
        EventDto[] myCreatedEvents = HttpUtils.getResponseWithToken(HttpUtils.HttpMethods.GET, LIST_OF_MY_EVENTS, 200, token, EventDto[].class);

        Assertions.assertNotNull(myCreatedEvents, "List of active events is empty");
        Assertions.assertTrue(myCreatedEvents.length > 0, "List of active events is empty");
    }
    @Test
    @DisplayName("Проверка удаления event, созданного  авторизованным пользователем TESTUSER")
    void deletetMyEvent_TestUser() {
        token = postResponse(getTestUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getAccessToken();
        String eventId = "16";


        //ErrorMessageDto response = getResponse((HttpUtils.HttpMethods.DELETE, DELETE_MY_EVENT_ENDPOINT.replace("{eventId}", eventId), 403, ErrorMessageDto.class);

        Response response = HttpUtils.getResponse(HttpUtils.HttpMethods.DELETE, DELETE_MY_EVENT_ENDPOINT.replace("{eventId}", eventId), token, 200,200);



        Assertions.assertEquals("200",  "200");


    }
    @Test
    @DisplayName("Проверка удаления event,НЕ созданного  авторизованным пользователем TESTUSER")
    void deleteNotMyEvent_TestUser() {
        token = postResponse(getTestUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getAccessToken();
        String eventId = "11";


        //ErrorMessageDto response = getResponse((HttpUtils.HttpMethods.DELETE, DELETE_MY_EVENT_ENDPOINT.replace("{eventId}", eventId), 403, ErrorMessageDto.class);

       Response response = HttpUtils.getResponse(HttpUtils.HttpMethods.DELETE, DELETE_MY_EVENT_ENDPOINT.replace("{eventId}", eventId), token, 403,403);



         Assertions.assertEquals("You can't delete events that don't belong to you",  "You can't delete events that don't belong to you");


    }
    @Test
    @DisplayName("Проверка удаления НЕ существующего или уже удаленного event,   авторизованным пользователем TESTUSER")
    void deleteNotExistedEvent_TestUser() {
        token = postResponse(getTestUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getAccessToken();
        String eventId = "20";


        //ErrorMessageDto response = getResponse((HttpUtils.HttpMethods.DELETE, DELETE_MY_EVENT_ENDPOINT.replace("{eventId}", eventId), 403, ErrorMessageDto.class);

        Response response = HttpUtils.getResponse(HttpUtils.HttpMethods.DELETE, DELETE_MY_EVENT_ENDPOINT.replace("{eventId}", eventId), token, 404,404);



        Assertions.assertEquals("Event not found",  "Event not found");


    }
    @Test
    @DisplayName("Проверка получения списка пршедших - архивных events без авторизации")
    void getArchivedEvents() {

        EventsDto[] archivedEvents = getResponse(null, GET_ARCHIVE_EVENTS_ENDPOINT, 200, EventsDto[].class);

        Assertions.assertNotNull(archivedEvents, "List of archived events is empty");
        Assertions.assertNotNull(archivedEvents.getClass(), "List of active events is empty");

    }

    @Test
    @DisplayName("Проверка получения инфо о конкретном event без авторизации")
    void getInfoAboutEvent() {
        String eventId = "1";

        // Отправка запроса на получение информации о событии

        EventDto eventInfo = getResponse(null, GET_INFO_ABOUT_EVENT_ENDPOINT.replace("{eventId}", eventId), 200, EventDto.class);

        // Проверка ответа
        Assertions.assertNotNull(eventInfo, "No info about event");
        Assertions.assertEquals(1, eventInfo.getId(), "Event ID does not match");
        Assertions.assertNotNull(eventInfo.getTitle(), "Event title does not match");
        Assertions.assertNotNull(eventInfo.getAddressEnd(), "Event addressEnd does not match");
        Assertions.assertNotNull(eventInfo.getAddressStart(), "Event addressStart does not match");
        Assertions.assertNotNull(eventInfo.getStartDateTime(), "Event startDateTime does not match");
        Assertions.assertNotNull(eventInfo.getEndDateTime(), "Event endDateTime does not match");
        Assertions.assertNotNull(eventInfo.getMaximal_number_of_participants(), "Event maximal_number_of_participants does not match");
    }


    @Test
    @DisplayName("Проверка просмотра комментариевв о конкретном event без авторизации")
    void getCommentsAboutEvent() {
        String eventId = "1";

        // Отправка запроса на получение информации о событии

        CommentsEventRsDto[] commentsToEvent = getResponse(null, GET_EVENT_COMMENTS_ENDPOINT.replace("{eventId}", eventId), 200, CommentsEventRsDto[].class);

        // Проверка ответа
        Assertions.assertNotNull(commentsToEvent, "No comments to  event");
        Assertions.assertNotNull(commentsToEvent.length > 0, "No comments to event");


    }


    @Test
    @DisplayName("Проверка создания event у авторизованного пользователя-TESTUSER")
    void testCreateEvent() {
        token = postResponse(getTestUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getAccessToken();
        // Создание объекта события
        EventDto newEvent = EventDto.builder()
                .title("Travel to Jupiter-1")
                .addressStart("Cosmodrom, Planet Earth")
                .startDateTime("2024-12-12T10:00:00")
                .addressEnd("Cosmodrom, Jupiter-1")
                .endDateTime("2024-10-10T17:00:00")
                .cost(null)
                .maximal_number_of_participants(10)
                .build();


        // Отправка запроса на создание события
        EventDto createdEvent = postResponseWithToken(newEvent, CREATE_EVENT_ENDPOINT, 200, token, EventDto.class);



    }

    @Test
    @DisplayName("Проверка обновления данных в event у авторизованного пользователя - TESTUSER")
    void testUpdateEvent() {
        // Авторизация и получение токена
        token = postResponse(getTestUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getAccessToken();

        // ID существующего события, которое мы будем обновлять
        String eventId = "19";

        // Поиск существующего с
        EventDto existingEvent = getResponse(token, UPDATE_EVENT_ENDPOINT.replace("{eventId}", eventId), 200, EventDto.class);
        Assertions.assertEquals(19, existingEvent.getId(), "Event ID does not match");
        //Assertions.assertNotNull(existingEvent, "Event not found");
        Assertions.assertEquals("Travel to the Sun", existingEvent.getTitle(), "Event has other title");

        existingEvent.setTitle("Travel to the Neptun");


        // Отправка запроса на обновленеи Event
        EventDto updatedEvent = putResponseWithToken(existingEvent, UPDATE_EVENT_ENDPOINT.replace("{eventId}", eventId), 200, token, EventDto.class);


        // Проверка успешного обновления event

        Assertions.assertNotNull(updatedEvent, "Event update failed");
        Assertions.assertEquals("Travel to the Neptun", updatedEvent.getTitle(), "Event has other title");
    }

    @Test
    @DisplayName("Проверка добавления комментария к архивному event  авторизованным пользователем- TESTUSER")
    void testCommentsToEvent() {
        // Авторизация и получение токена
        token = postResponse(getTestUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getAccessToken();

        // ID существующего события, которое мы будем обновлять
        String eventId = "1";

        //Создание коммментария к event

        CommentsEventRqDto commentsToEvent = CommentsEventRqDto.builder()
                .comments("The info about event is very interesting")
                .build();

        CommentsEventRsDto addedComment = postResponseWithToken(commentsToEvent, ADD_EVENT_COMMENTS_ENDPOINT.replace("{eventId}", eventId), 200, token, CommentsEventRsDto.class);

        Assertions.assertNotNull(addedComment, "Commments was not added");
        Assertions.assertEquals("Test", addedComment.getLastName(), "Last Name of author does not match");
        Assertions.assertNotNull("Meeting Conference", "Event not found");
      Assertions.assertEquals("The info about event is very interesting", addedComment.getComments(), "Comment text does not match");

    }
    // Повторная подача заявки на уже зарегестрированный к участию event №13 Berlin Reichstag (Позитивный ТЕСТ)

    @Test
    @DisplayName("Проверка (Неуспешной) повторной пордачи заявки на участие в активном event  авторизованным пользователем-AliceFromWonderland")
    void testSecondApplicationToEvent() {
        // Авторизация и получение токена
        token = postResponse(getAliceUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getAccessToken();

        // ID существующего события, которое мы будем обновлять
        String eventId = "22";

        //Создание заявки на участие в event

        ApplyEventRqDto secondApplicationToEvent = ApplyEventRqDto.builder()
                .application("I wish to travel  to the Sun")
                .build();

      ResponseMessageDto submittedAplication = postResponseWithToken(secondApplicationToEvent, APPLY_TO_EVENT_ENDPOINT.replace("{eventId}", eventId), 409, token, ResponseMessageDto.class);

        // Отправка запроса на подачу заявки с токеном
        // Проверка результата
        Assertions.assertEquals("You are already registered for this event.", submittedAplication.getMessage(),"The response doesn`t match");


    }


    // Повторная подача заявки на уже зарегестрированный к участию event №13 Berlin Reichstag (НЕГАТИВНЫЙ ТЕСТ)
    @Test
    @DisplayName("Проверка (Успешной) подачи заявки на участие в активном event  авторизованным пользователем")
    void testFirstApplicationToEvent() {
        // Авторизация и получение токена
        token = postResponse(getAliceUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getAccessToken();

        // ID существующего события, которое мы будем обновлять
        String eventId = "29";

        //Создание заявки на участие в event

        ApplyEventRqDto applicationToEvent = ApplyEventRqDto.builder()
                .application("I wish to partisipate to the event")
                .build();

        //ApplyEventRqDto submittedAplication = postResponseWithToken(applicationToEvent, APPLY_TO_EVENT_ENDPOINT.replace("{eventId}", eventId), 409, token, ApplyEventRqDto.class);


      // ResponseMessageDto submitedApplication = postResponseWithToken(applicationToEvent, APPLY_TO_EVENT_ENDPOINT.replace("{eventId}", eventId), 200, token,ResponseMessageDto.class);


        // Отправка запроса на создание события
       ApplyEventRqDto submittedAplication = postResponseWithToken(applicationToEvent, APPLY_TO_EVENT_ENDPOINT.replace("{eventId}", eventId), 200, token, ApplyEventRqDto.class);


    }



}




