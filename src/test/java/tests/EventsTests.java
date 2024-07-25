package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import qaVoyagers.dto.EventDto;
import qaVoyagers.dto.EventsDto;
import qaVoyagers.dto.GenderDto;
import qaVoyagers.dto.RegistrationUserDto;
import qaVoyagers.dto.TokenDto;
import qaVoyagers.utils.HttpUtils;

import java.time.LocalDateTime;

import static qaVoyagers.utils.HttpUtils.CREATE_EVENT_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.GET_ACTIVE_EVENTS_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.GET_ARCHIVE_EVENTS_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.GET_INFO_ABOUT_EVENT_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.GET_MY_EVENTS_WITH_MY_PARTICIPANTS_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.LIST_OF_MY_EVENTS;
import static qaVoyagers.utils.HttpUtils.LOGIN_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.UPDATE_EVENT_ENDPOINT;
import static qaVoyagers.utils.HttpUtils.getResponse;
import static qaVoyagers.utils.HttpUtils.postResponse;
import static qaVoyagers.utils.HttpUtils.postResponseWithToken;
import static qaVoyagers.utils.HttpUtils.putResponse;

public class EventsTests extends BaseTest{


  private String token;

//    @BeforeEach
//    void precondition(TestInfo testInfo) {
//        Set<String> tags = testInfo.getTags();
//        if (tags.contains("@ADD")) {
//            token = HttpUtils.postResponse(getDeleteTestUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getToken();
//            HttpUtils.deleteResponse(token, CONTACTS_ENDPOINT + "/clear", 200, ResponseMessageDto.class);
//        } else if (tags.contains("@DELETE")) {
//            token = HttpUtils.postResponse(getDeleteTestUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getToken();
//            postResponseWithToken(getContactBody(), CONTACTS_ENDPOINT, 200, token, ResponseMessageDto.class);
//        } else {
//            token = HttpUtils.postResponse(getTestUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getToken();
//        }
//    }

  @Test
  @DisplayName("Проверка получения всех events  c участием авторизованного пользователя")
  void test1() {
    token = postResponse(getTestUserLoginBody(),LOGIN_ENDPOINT , 200, TokenDto.class).getAccessToken();
    EventsDto myPartisipationInEvevnts = getResponse(token,  GET_MY_EVENTS_WITH_MY_PARTICIPANTS_ENDPOINT, 200, EventsDto.class);
    Assertions.assertNotNull(myPartisipationInEvevnts, "Impossible to get events with your partisipation");
    Assertions.assertFalse(myPartisipationInEvevnts.getEvents().isEmpty(), "Impossible to get events with your partisipation");
  }

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
    String eventId= "1";

    // Отправка запроса на получение информации о событии
//    EventDto eventInfo = (EventDto) getResponse(HttpUtils.HttpMethods.GET, GET_INFO_ABOUT_EVENT_ENDPOINT.replace("1", eventId), null, 200, EventDto.class);
    EventDto eventInfo = getResponse(null, GET_INFO_ABOUT_EVENT_ENDPOINT.replace("{eventId}", eventId), 200, EventDto.class);

    // Проверка ответа
    Assertions.assertNotNull(eventInfo, "No info about event");
    Assertions.assertEquals( 1, eventInfo.getId(), "Event ID does not match");
    Assertions.assertNotNull(eventInfo.getTitle(), "Event title does not match");
    Assertions.assertNotNull(eventInfo.getAddressEnd(), "Event addressEnd does not match");
    Assertions.assertNotNull(eventInfo.getAddressStart(), "Event addressStart does not match");
    Assertions.assertNotNull(eventInfo.getStartDateTime(), "Event startDateTime does not match");
    Assertions.assertNotNull(eventInfo.getEndDateTime(), "Event endDateTime does not match");
    Assertions.assertNotNull(eventInfo.getMaximal_number_of_participants(), "Event maximal_number_of_participants does not match");
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
    EventDto createdEvent = postResponseWithToken(newEvent, CREATE_EVENT_ENDPOINT, 200, token,EventDto.class);


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
    EventDto existingEvent = getResponse(token, LIST_OF_MY_EVENTS.replace("{eventId}", eventId), 200, EventDto.class);
    Assertions.assertEquals( 19, existingEvent.getId(), "Event ID does not match");
    //Assertions.assertNotNull(existingEvent, "Event not found");
    Assertions.assertEquals("Travel to the Moon", existingEvent.getTitle(), "Event has other title");

    existingEvent.setTitle("Travel to the Sun");


    // Отправка запроса на обновленеи Event
    EventDto updatedEvent = postResponseWithToken(existingEvent, CREATE_EVENT_ENDPOINT, 200, token,EventDto.class);



    // Отправка запроса на обновление event
    //EventDto updatedEvent= putResponse(existingEvent,UPDATE_EVENT_ENDPOINT.replace("{{eventId}}", eventId), 200, token, EventDto.class);
   // Проверка успешного обновления event

    Assertions.assertNotNull(updatedEvent, "Event update failed");
    Assertions.assertEquals("Travel to the Sun", updatedEvent.getTitle(), "Event has other title");
  }}



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

