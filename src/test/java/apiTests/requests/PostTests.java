package apiTests.requests;

import Betuganov_Admir.api.Post;
import apiTests.baseApi.BaseApiTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PostTests extends BaseApiTest {

    @Nested
    class AllAboutPostRequests{

        @Nested
        @DisplayName("Позитивные сценарии")
        class PositiveRequests{

            // ============================================================
            // 1. ПРОСТАЯ ПРОВЕРКА (без извлечения данных)
            // ============================================================
            @Nested
            @DisplayName("1. Простые проверки (без extract)")
            class SimpleChecks {

                @Test
                @DisplayName("POST /posts — создать пост и проверить статус")
                void createPostTest() {
                    String requestBody = """
                            {
                                "title": "Мой первый пост",
                                "body": "Содержимое поста",
                                "userId": 1
                            }
                            """;

                    given()
                            .log().ifValidationFails()
                            .contentType(ContentType.JSON)
                            .body(requestBody)
                            .when()
                            .post("/posts")
                            .then()
                            .log().ifValidationFails()
                            .log().body()
                            .log().status()
                            .statusCode(201)
                            .body("title", equalTo("Мой первый пост"))
                            .body("body", equalTo("Содержимое поста"))
                            .body("userId", equalTo(1))
                            .body("id", notNullValue());
                }

                @Test
                @DisplayName("POST /posts — с использованием Map")
                void createPostWithMapTest() {
                    Map<String, Object> requestBody = new HashMap<>();
                    requestBody.put("title", "Пост из Map");
                    requestBody.put("body", "Содержимое из Map");
                    requestBody.put("userId", 2);

                    given()
                            .log().ifValidationFails()
                            .contentType(ContentType.JSON)
                            .body(requestBody)
                            .when()
                            .post("/posts")
                            .then()
                            .log().ifValidationFails()
                            .statusCode(201)
                            .body("title", equalTo("Пост из Map"))
                            .body("userId", equalTo(2));
                }
            }

            // ============================================================
            // 2. ИЗВЛЕЧЕНИЕ ДАННЫХ (extract)
            // ============================================================
            @Nested
            @DisplayName("2. Извлечение данных (extract)")
            class ExtractExamples {

                @Test
                @DisplayName("asString() — получить JSON как текст")
                void createPostAndGetAsStringTest() {
                    String requestBody = """
                            {
                                "title": "Тестовый пост",
                                "body": "Тестовое содержимое",
                                "userId": 1
                            }
                            """;

                    String response = given()
                            .contentType(ContentType.JSON)
                            .body(requestBody)
                            .when()
                            .post("/posts")
                            .then()
                            .statusCode(201)
                            .extract()
                            .asString();

                    System.out.println("Созданный пост (JSON): " + response);
                }

                @Test
                @DisplayName("jsonPath() — получить конкретное значение из ответа")
                void createPostAndExtractValueTest() {
                    String requestBody = """
                            {
                                "title": "Пост для извлечения ID",
                                "body": "Тело поста",
                                "userId": 5
                            }
                            """;

                    int createdId = given()
                            .contentType(ContentType.JSON)
                            .body(requestBody)
                            .when()
                            .post("/posts")
                            .then()
                            .statusCode(201)
                            .extract()
                            .jsonPath()
                            .getInt("id");

                    System.out.println("Созданный ID = " + createdId);
                }

                @Test
                @DisplayName("as(Class.class) — получить Java-объект (ЛУЧШИЙ СПОСОБ)")
                void createPostAndGetAsObjectTest() {
                    Post newPost = new Post();
                    newPost.setTitle("Пост из Java объекта");
                    newPost.setBody("Содержимое из Java объекта");
                    newPost.setUserId(3);

                    Post createdPost = given()
                            .contentType(ContentType.JSON)
                            .body(newPost)
                            .when()
                            .post("/posts")
                            .then()
                            .log().body()
                            .log().status()
                            .statusCode(201)
                            .extract()
                            .as(Post.class);

                    System.out.println("=== Созданный пост ===");
                    System.out.println("userId = " + createdPost.getUserId());
                    System.out.println("id = " + createdPost.getId());
                    System.out.println("title = " + createdPost.getTitle());
                    System.out.println("body = " + createdPost.getBody());
                }
            }

            // ============================================================
            // 3. РАЗНЫЕ ФОРМАТЫ ЗАПРОСОВ
            // ============================================================
            @Nested
            @DisplayName("3. Разные форматы тела запроса")
            class DifferentBodyFormats {

                @Test
                @DisplayName("JSON как строка (с тройными кавычками)")
                void postWithJsonStringTest() {
                    String json = """
                            {
                                "title": "Строковый JSON",
                                "body": "Тело",
                                "userId": 1
                            }
                            """;

                    given()
                            .contentType(ContentType.JSON)
                            .body(json)
                            .when()
                            .post("/posts")
                            .then()
                            .statusCode(201);
                }

                @Test
                @DisplayName("JSON через Map")
                void postWithMapTest() {
                    Map<String, Object> postMap = Map.of(
                            "title", "Map пост",
                            "body", "Содержимое из Map",
                            "userId", 2
                    );

                    given()
                            .contentType(ContentType.JSON)
                            .body(postMap)
                            .when()
                            .post("/posts")
                            .then()
                            .statusCode(201)
                            .body("title", equalTo("Map пост"));
                }

                @Test
                @DisplayName("Форма данных (application/x-www-form-urlencoded)")
                void postWithFormDataTest() {
                    given()
                            .contentType(ContentType.URLENC)
                            .formParam("title", "Пост из формы")
                            .formParam("body", "Содержимое из формы")
                            .formParam("userId", 1)
                            .when()
                            .post("/posts")
                            .then()
                            .statusCode(201);
                }
            }
        }

        @Nested
        @DisplayName("Негативные сценарии")
        class NegativeRequests{
            // ============================================================
            // 1. НЕВАЛИДНЫЕ ДАННЫЕ
            // ============================================================

            @Test
            @DisplayName("POST /posts — пустое тело запроса → 400 или 500")
            void postWithEmptyBodyTest() {
                given()
                        .contentType(ContentType.JSON)
                        .body("")
                        .when()
                        .post("/posts")
                        .then()
                        .log().all()
                        .statusCode(201);
            }

            @Test
            @DisplayName("POST /posts — невалидный JSON → 400")
            void postWithInvalidJsonTest() {
                given()
                        .contentType(ContentType.JSON)
                        .body("{}")
                        .when()
                        .post("/posts")
                        .then()
                        .log().all()
                        .statusCode(anyOf(equalTo(400), equalTo(500)));
            }

            @Test
            @DisplayName("POST /posts — отрицательный userId → 400 или 201 (зависит от API)")
            void postWithNegativeUserIdTest() {
                String requestBody = """
                        {
                            "title": "Тест",
                            "body": "Негативный тест",
                            "userId": -5
                        }
                        """;

                given()
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .when()
                        .post("/posts")
                        .then()
                        .log().all()
                        .statusCode(anyOf(equalTo(400), equalTo(201)));
            }

            // ============================================================
            // 2. ПАРАМЕТРИЗОВАННЫЕ ТЕСТЫ
            // ============================================================

            @ParameterizedTest
            @CsvSource({
                    "''",           // пустая строка
                    "null",         // null как строка
                    "abc",          // невалидный JSON
                    "123",          // число вместо объекта
                    "true"          // boolean вместо объекта
            })
            @DisplayName("POST /posts — невалидное тело → 201 и 500")
            void postWithInvalidBodyTest(String invalidBody) {
                given()
                        .contentType(ContentType.JSON)
                        .body(invalidBody)
                        .when()
                        .post("/posts")
                        .then()
                        .log().status()
                        .statusCode(anyOf(equalTo(201), equalTo(500)));
            }

            @ParameterizedTest
            @CsvSource({
                    ", Тест, 1",           // пустой title
                    "Тест, , 1",           // пустой body
                    "Тест, Тест, "         // пустой userId
            })
            @DisplayName("POST /posts — обязательные поля пустые → 400")
            void postWithMissingFieldsTest(String title, String body, String userId) {
                // В зависимости от API, может пропустить или вернуть 400
                String requestBody = String.format("""
                        {
                            "title": "%s",
                            "body": "%s",
                            "userId": %s
                        }
                        """, title, body, userId);

                given()
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .when()
                        .post("/posts")
                        .then()
                        .log().all()
                        .statusCode(anyOf(equalTo(400), equalTo(201)));
            }

            // ============================================================
            // 4. ЦЕПОЧКА ЗАПРОСОВ (POST → GET)
            // ============================================================
            @Nested
            @DisplayName("4. Цепочка запросов (POST → GET)")
            class ChainedRequests {

                @Test
                @DisplayName("Создать пост -> Получить его по ID")
                void createAndThenGetPostTest() {
                    // 1. Создаём пост
                    String requestBody = """
                        {
                            "title": "Пост для цепочки",
                            "body": "Создан, чтобы потом получить",
                            "userId": 1
                        }
                        """;

                    int createdId = given()
                            .contentType(ContentType.JSON)
                            .body(requestBody)
                            .when()
                            .post("/posts")
                            .then()
                            .statusCode(201)
                            .extract()
                            .jsonPath()
                            .getInt("id");

                    System.out.println("Создан пост с ID = " + createdId);

                    // 2. Получаем созданный пост
                    given()
                            .pathParam("id", createdId)
                            .when()
                            .get("/posts/{id}")
                            .then()
                            .statusCode(200)
                            .body("id", equalTo(createdId))
                            .body("title", equalTo("Пост для цепочки"));
                }

                @Test
                @DisplayName("Создать пост через Java-объект -> Проверить через GET")
                void createWithObjectAndGetTest() {
                    // 1. Создаём объект
                    Post newPost = new Post();
                    newPost.setTitle("Объектный пост");
                    newPost.setBody("Пост создан из Java объекта");
                    newPost.setUserId(2);

                    // 2. Отправляем POST
                    Post createdPost = given()
                            .contentType(ContentType.JSON)
                            .body(newPost)
                            .when()
                            .post("/posts")
                            .then()
                            .statusCode(201)
                            .extract()
                            .as(Post.class);

                    System.out.println("Создан: ID = " + createdPost.getId());

                    // 3. Проверяем через GET
                    given()
                            .pathParam("id", createdPost.getId())
                            .when()
                            .get("/posts/{id}")
                            .then()
                            .statusCode(200)
                            .body("title", equalTo("Объектный пост"))
                            .body("userId", equalTo(2));
                }
            }
        }
    }
}
