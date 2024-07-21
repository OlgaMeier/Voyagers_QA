package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import voyagers.dto.EventDto;
import voyagers.dto.ResponseMessageDto;
import voyagers.dto.TokenDto;
import voyagers.utils.HttpUtils;

import java.util.Set;

import static voyagers.utils.HttpUtils.EVENTS_ENDPOINT;
import static voyagers.utils.HttpUtils.HttpMethods.GET;
import static voyagers.utils.HttpUtils.LOGIN_ENDPOINT;
import static voyagers.utils.HttpUtils.deleteResponse;
import static voyagers.utils.HttpUtils.getResponse;
import static voyagers.utils.HttpUtils.postResponseWithToken;
import static voyagers.utils.Utils.isNullOrEmpty;

public class EventsTests extends BaseTest {
    private String token;

    @BeforeEach
    void precondition(TestInfo testInfo) {
        Set<String> tags = testInfo.getTags();
        if (tags.contains("@ADD")) {
            token = HttpUtils.postResponse(getDeleteTestUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getToken();
            deleteResponse(token, EVENTS_ENDPOINT + "/clear", 200, ResponseMessageDto.class);
        } else if (tags.contains("@DELETE")) {
            token = HttpUtils.postResponse(getDeleteTestUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getToken();
            postResponseWithToken(getEventBody(), EVENTS_ENDPOINT, 200, token, ResponseMessageDto.class);
        } else {
            token = HttpUtils.postResponse(getTestUserLoginBody(), LOGIN_ENDPOINT, 200, TokenDto.class).getToken();
        }
    }

    @Test
    @DisplayName("Проверка получения списка events у авторизованного пользователя")
    void test1() {
        EventDto event = getResponse(token, EVENTS_ENDPOINT, 200, EventDto.class);
        Assertions.assertFalse(event.getEvent().isEmpty());
    }

    @Test
    @DisplayName("Проверка получения списка event без авторизации")
    void test2() {
        getResponse(GET, EVENTS_ENDPOINT, null, 403, null);
    }

    @Test
    @Tag("@ADD")
    @DisplayName("Проверка добавления комментариев на event у авторизованного пользователя")
    void test3() {
        ResponseMessageDto responseMessageDto = postResponseWithToken(getEventBody(), EVENTS_ENDPOINT, 200, token, ResponseMessageDto.class);
        Assertions.assertFalse(isNullOrEmpty(responseMessageDto.getMessage()));
        String id = responseMessageDto.getMessage().replace("Comment was added! ID: ", "");

        EventDto events = getResponse(token, EVENTS_ENDPOINT, 200, EventDto.class);
        Assertions.assertTrue(events.getEvents().size() == 1);
        Assertions.assertEquals(id, events.getEvents().get(0).getId(), "Added comments to Event with Id");
        deleteResponse(token, EVENTS_ENDPOINT + "/clear", 200, ResponseMessageDto.class);
    }

    @Test
    @Tag("@DELETE")
    @DisplayName("Проверка удаления event у авторизованного пользователя по id")
    void test4() throws Exception {
        EventDto events = getResponse(token, EVENTS_ENDPOINT, 200, EventDtoDto.class);
        Assertions.assertTrue(events.getEvents().size() == 1);
        String id = events.getEvents().get(0).getId();

        ResponseMessageDto responseMessageDto = deleteResponse(token, EVENTS_ENDPOINT + "/" + id, 200, ResponseMessageDto.class);
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
    }
}
