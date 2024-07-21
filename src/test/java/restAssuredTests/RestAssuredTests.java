package restAssuredTests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import voyagers.dto.AuthentificationBodyDto;
import voyagers.dto.TokenDto;

import static io.restassured.RestAssured.given;

public class RestAssuredTests {
    AuthentificationBodyDto authenticationBodyDto = AuthentificationBodyDto.builder()
            .email("Test @example.com")
            .password("Test1234!").
            build();


    @Test
    void test1() {
        String url = "http://localhost:8080/api/auth/login";
/**
 * Четыре основных REST-assured метода
 *
 * Given — позволяет узнать, что было передано в запросе.
 * When — с каким методом и на какой эндпойнт отправляем запрос.
 * Then — как проверяется пришедший ответ.
 *
 */

        TokenDto tokenDto = given()//Начало запроса
                .contentType(ContentType.JSON)//Указываем тип контента
                .body(authenticationBodyDto)// Тело запроса (POST & PUT)
                .when()// с каким методом и на какой эндпойнт отправляем запрос.
                .log().all()// логирование в консоль
                .post(url)// на какой адрес (или endpoint) отправляем запрос
                .then()// Что делаем когда получаем ответ
                .log().all()//Логируем ответ
                .assertThat().statusCode(200)//Проверяем статус код пришедшего ответа
                .extract().response().as(TokenDto.class);//извлекаем ответ.

        System.out.println();
        System.out.println(tokenDto.getToken());
    }
}
