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
  @DisplayName("Проверка получения списка пршкдших - архивных events без авторизации")
  void getArchivedEvents() {
    Response archivedEvents = getResponse(HttpUtils.HttpMethods.GET, GET_ARCHIVE_EVENTS_ENDPOINT, null, 200, EventsDto.class);

    Assertions.assertNotNull( archivedEvents, "List of archived events is empty");
    Assertions.assertNotNull(archivedEvents.getBody(), "List of active events is empty");

  }



  @Test
  @DisplayName("Проверка создания event у авторизованного пользователя")
  void testCreateEvent() {
    token = postResponse(getTestUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getAccessToken();
    // Создание объекта события
    EventDto newEvent = EventDto.builder()
            .title("Travel to the Moon")
            .addressStart("Cosmodrom, Planet Earth")
            .startDateTime(LocalDateTime.parse("2024-10-10T10:00:00"))
            .addressEnd("Cosmodrom, Satellite Moon")
            .endDateTime(LocalDateTime.parse("2024-10-10T17:00:00"))
            .maxNnumberOfParticipants(10)
            .build();

    // Отправка запроса на создание события
    EventDto createdEvent = postResponse(token, CREATE_EVENT_ENDPOINT, 200, EventDto.class);

    Assertions.assertNotNull(createdEvent, "Event creation failed");
    Assertions.assertEquals("Travel to the Moon", createdEvent.getTitle(), "Event has other title");

  }
  @Test
  @DisplayName("Проверка обновления данных в event у авторизованного пользователя")
  void testUpdateEvent() {
    // Авторизация и получение токена
      token = postResponse(getTestUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getAccessToken();

    // ID существующего события, которое мы будем обновлять
    String eventId = "1";

    // Поиск существующего с
    EventDto existingEvent = getResponse(token, LIST_OF_MY_EVENTS.replace("{1}", eventId), 200, EventDto.class);
    Assertions.assertNotNull(existingEvent, "Event not found");
    Assertions.assertEquals("Travel to the Moon", existingEvent.getTitle(), "Event has other title");

    // Обновление Title of event
    existingEvent.setTitle("Travel to the Sun");

    // Отправка запроса на обновление event
    EventDto updatedEvent= putResponse(existingEvent,UPDATE_EVENT_ENDPOINT.replace("{id_event}", eventId), 200, token, EventDto.class);
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

