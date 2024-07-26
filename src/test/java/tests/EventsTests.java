package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qaVoyagers.dto.CommentsEventRqDto;
import qaVoyagers.dto.CommentsEventRsDto;
import qaVoyagers.dto.EventDto;
import qaVoyagers.dto.EventsDto;
import qaVoyagers.dto.TokenDto;
import qaVoyagers.utils.HttpUtils;

import java.time.LocalDateTime;

import static qaVoyagers.utils.HttpUtils.ADD_EVENT_COMMENTS_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.CREATE_EVENT_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.GET_ACTIVE_EVENTS_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.GET_ARCHIVE_EVENTS_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.GET_EVENT_COMMENTS_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.GET_INFO_ABOUT_EVENT_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.LOGIN_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.UPDATE_EVENT_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.getResponse;
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
    @DisplayName("Проверка получения списка пршедших - архивных events без авторизации")
    void getArchivedEvents() {
//    Response archivedEvents = getResponse(HttpUtils.HttpMethods.GET, GET_ARCHIVE_EVENTS_ENDPOINT, null, 200, EventsDto.class);
        EventsDto archivedEvents = getResponse(null, GET_ARCHIVE_EVENTS_ENDPOINT, 200, EventsDto.class);

        Assertions.assertNotNull(archivedEvents, "List of archived events is empty");
        Assertions.assertNotNull(archivedEvents.getEvents(), "List of active events is empty");

    }

    @Test
    @DisplayName("Проверка получения инфо о конкретном event без авторизации")
    void getInfoAboutEvent() {
        String eventId = "1";

        // Отправка запроса на получение информации о событии
//    EventDto eventInfo = (EventDto) getResponse(HttpUtils.HttpMethods.GET, GET_INFO_ABOUT_EVENT_ENDPOINT.replace("1", eventId), null, 200, EventDto.class);
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
//    EventDto eventInfo = (EventDto) getResponse(HttpUtils.HttpMethods.GET, GET_INFO_ABOUT_EVENT_ENDPOINT.replace("1", eventId), null, 200, EventDto.class);
        CommentsEventRsDto[] commentsToEvent = getResponse(null, GET_EVENT_COMMENTS_ENDPOINT.replace("{eventId}", eventId), 200, CommentsEventRsDto[].class);

        // Проверка ответа
        Assertions.assertNotNull(commentsToEvent, "No comments to  event");
        Assertions.assertNotNull(commentsToEvent.length > 0, "No comments to event");

     //   if (commentsToEvent.length > 0) {
          //  CommentsEventRsDto firstComment = commentsToEvent[0];

            //  Assertions.assertNotNull(firstComment.getCommentId(), "Comment ID is null");
//            Assertions.assertNotNull(firstComment. (), "Comment`s text is null");
//            Assertions.assertNotNull(firstComment.getAuthor(), "Author of comments has other name");
//            Assertions.assertNotNull(firstComment.getTimestamp(), "Timestamp to comment of event doesn`t match");

           /* "firstName": "Ben",
                    "lastName": "Benovski",
                    "comments": "I was happy to partisipate to this voyage",
                    "eventTitle": "Meeting Conference"*/
      //  }
    }


    @Test
    @DisplayName("Проверка создания event у авторизованного пользователя")
    void testCreateEvent() {
        token = postResponse(getTestUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getAccessToken();
        // Создание объекта события
        EventDto newEvent = EventDto.builder()
                .title("Travel to the Moon")
                .addressStart("Cosmodrom, Planet Earth")
                .startDateTime("2024-10-10T10:00:00")
                .addressEnd("Cosmodrom, Satellite Moon")
                .endDateTime("2024-10-10T17:00:00")
                .cost(null)
                .maximal_number_of_participants(10)
                .build();


        // Отправка запроса на создание события
        EventDto createdEvent = postResponseWithToken(newEvent, CREATE_EVENT_ENDPOINT, 200, token, EventDto.class);


//    Assertions.assertNotNull(createdEvent, "Event creation failed");
//    Assertions.assertEquals("Travel to the Moon", createdEvent.getTitle(), "Event has other title");

    }

    @Test
    @DisplayName("Проверка обновления данных в event у авторизованного пользователя")
    void testUpdateEvent() {
        // Авторизация и получение токена
        token = postResponse(getTestUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getAccessToken();

        // ID существующего события, которое мы будем обновлять
        String eventId = "19";

        // Поиск существующего с
        EventDto existingEvent = getResponse(token, UPDATE_EVENT_ENDPOINT.replace("{eventId}", eventId), 200, EventDto.class);
        Assertions.assertEquals(19, existingEvent.getId(), "Event ID does not match");
        //Assertions.assertNotNull(existingEvent, "Event not found");
        Assertions.assertEquals("Travel to the Moon", existingEvent.getTitle(), "Event has other title");

        existingEvent.setTitle("Travel to the Sun");


        // Отправка запроса на обновленеи Event
        EventDto updatedEvent = putResponseWithToken(existingEvent, UPDATE_EVENT_ENDPOINT.replace("{eventId}", eventId), 200, token, EventDto.class);


        // Проверка успешного обновления event

        Assertions.assertNotNull(updatedEvent, "Event update failed");
        Assertions.assertEquals("Travel to the Sun", updatedEvent.getTitle(), "Event has other title");
    }

    @Test
    @DisplayName("Проверка добавления комментария к архивному event  авторизованным пользователем")
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



/*
  @Test
  @Tag("@DELETE")
  @DisplayName("Проверка удаления контакта у авторизованного пользователя по id")
  void test4() throws Exception {
    ContactsDto contacts = getResponse(token, CONTACTS_ENDPOINT, 200, ContactsDto.class);
    Assertions.assertTrue(contacts.getContacts().size() == 1);
    String id = contacts.getContacts().get(0).getId();

    ResponseMessageDto responseMessageDto = deleteResponse(token, CONTACTS_ENDPOINT + "/" + id, 200, ResponseMessageDto.class);
    Assertions.assertEquals("Contact was deleted!", responseMessageDto.getMessage(), "Сообщение об удалении не соответствует ожидаемому");

    contacts = getResponse(token, CONTACTS_ENDPOINT, 200, ContactsDto.class);
    //TODO написать вторую проверку по списку
    Assertions.assertTrue(contacts.getContacts().size() == 0);

    //Альтернативная проверка
//        for (ContactDto contact : contacts.getContacts()) {
//            if (contact.getId().equals(id)) {
////                throw new Exception("Контакт не удалён");
//                //AutotestExc
//                Assertions.assertFalse(contact.getId().equals(id), "Контакт не удалён");
//            }
//        }
  }*/




}
