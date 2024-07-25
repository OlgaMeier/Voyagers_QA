package qaVoyagers.utils;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static qaVoyagers.utils.HttpUtils.HttpMethods.DELETE;
import static qaVoyagers.utils.HttpUtils.HttpMethods.GET;
import static qaVoyagers.utils.HttpUtils.HttpMethods.POST;
import static qaVoyagers.utils.HttpUtils.HttpMethods.PUT;
import static qaVoyagers.utils.Utils.isNullOrEmpty;


public class HttpUtils {

    public static Properties properties = TestProperties.getINSTANCE().getProperties();
    public static final String LOGIN_ENDPOINT = properties.getProperty("login.endpoint");
    public static final String REGISTRATION_ENDPOINT = properties.getProperty("registration.endpoint");
    public static final String GET_ACTIVE_EVENTS_ENDPOINT = properties.getProperty("getActiveEvents.endpoint");
    public static final String GET_ALL_EVENTS_ENDPOINT = properties.getProperty("getAllEvents.endpoint");
    public static final String GET_INFO_ABOUT_EVENT_ENDPOINT = properties.getProperty("getInfoAboutEvent.endpoint");
    public static final String GET_ARCHIVE_EVENTS_ENDPOINT = properties.getProperty("getArchiveEvents.endpoint");
    public static final String GET_EVENT_COMMENTS_ENDPOINT = properties.getProperty("getEventComments.endpoint");
    public static final String APPLY_TO_EVENT_ENDPOINT = properties.getProperty("applyToEvent.endpoint");
    // public static final String GET_APPLICANTS_ENDPOINT = properties.getProperty("getApplicants.endpoint"); пока нет такого запроса в userstory
    public static final String DELETE_MY_APPLICATION_TO_EVENT_ENDPOINT = properties.getProperty("deleteMyApplicationToEvent.endpoint");
    public static final String GET_MY_EVENTS_WITH_MY_PARTICIPANTS_ENDPOINT = properties.getProperty("getMyEventsWithMyParticipants.endpoint");
    public static final String CREATE_EVENT_ENDPOINT = properties.getProperty("createEvent.endpoint");
    public static final String DELETE_MY_EVENT_ENDPOINT = properties.getProperty("deleteMyEvent.endpoint");
    public static final String UPDATE_EVENT_ENDPOINT = properties.getProperty("updateEvent.endpoint");
    public static final String ADD_EVENT_COMMENTS_ENDPOINT = properties.getProperty("addEventComments.endpoin");
    public static final String LIST_OF_MY_EVENTS = properties.getProperty("listOfMyEvents.endpoint");

    public static <T> T postResponse(Object body, String endpoint, int statusCode, Class<T> responseClass) {
        return getResponse(POST, endpoint, null, statusCode, body).as(responseClass);
    }

    public static <T> T postResponseWithToken(Object body, String endpoint, int statusCode, String token, Class<T> responseClass) {
        return getResponse(POST, endpoint, token, statusCode, body).as(responseClass);
    }

    public static <T> T putResponse(Object body, String endpoint, int statusCode, String token, Class<T> responseClass) {
        return getResponse(PUT, endpoint, token, statusCode, body).as(responseClass);
    }

    public static <T> T getResponse(String token, String endpoint, int statusCode, Class<T> responseClass) {
        return getResponse(GET, endpoint, token, statusCode, null).as(responseClass);
    }

    public static <T> T deleteResponse(String token, String endpoint, int statusCode, Class<T> responseClass) {
        return getResponse(DELETE, endpoint, token, statusCode, null).as(responseClass);
    }

    @Step("Отправляет запрос")
    public static Response getResponse(HttpMethods method, String endpoint, String token, int statusCode, Object body) {
        //RequestSpecification - это сам запрос, через него можно выделить общую часть запроса
        RequestSpecification requestSpecification = given().spec(getRequestSpecBuilder(token).build()).when().log().all();

        if (method.equals(POST)) { // - POST запрос
            return requestSpecification
                    .body(body)// Тело запроса (POST & PUT)
                    .post(endpoint)
                    .then()
                    .log().all()
                    .assertThat().statusCode(statusCode)
                    .extract().response();
        } else if (method.equals(PUT)) { // - PUT запрос
            return requestSpecification
                    .body(body)// Тело запроса (POST & PUT)
                    .put(endpoint)
                    .then()
                    .log().all()
                    .assertThat().statusCode(statusCode)
                    .extract().response();
        } else if (method.equals(GET)) { // - GET запрос
            return requestSpecification
                    .get(endpoint)
                    .then()
                    .log().all()
                    .assertThat().statusCode(statusCode)
                    .extract().response();
        } else if (method.equals(DELETE)) {// - DELETE запрос
            return requestSpecification
                    .delete(endpoint)
                    .then()
                    .log().all()
                    .assertThat().statusCode(statusCode)
                    .extract().response();
        }
        return null;
    }

    //Помогает нам установить параметры общие для наших запросов/ В данном случае мы добавляем хэдэры к нашим запросам
    private static RequestSpecBuilder getRequestSpecBuilder(String token) {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        if (!isNullOrEmpty(token)) {
            headers.put("Authorization", "Bearer " + token);
        }
        requestSpecBuilder.addHeaders(headers);
        return requestSpecBuilder;
    }

    public enum HttpMethods {
        GET,
        POST,
        PUT,
        DELETE
    }
}
