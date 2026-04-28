package apiTests.requests;

import Betuganov_Admir.api.Get;
import apiTests.baseApi.BaseApiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetTests extends BaseApiTest {

    @Nested
    class AllAboutGetRequests{

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
                @DisplayName("GET /posts — проверить статус и количество постов")
                void getAllPostsTest() {
                    given()
                            .log().ifValidationFails()
                            .when()
                            .get("/posts")
                            .then()
                            .log().ifValidationFails()
                            .statusCode(200)
                            .body("size()", equalTo(100));
                }

                @Test
                @DisplayName("GET /posts/1 — проверить поля конкретного поста")
                void getPostByIdTest() {
                    given()
                            .log().ifValidationFails()
                            .when()
                            .get("/posts/1")
                            .then()
                            .log().ifValidationFails()
                            .statusCode(200)
                            .body("id", equalTo(1))
                            .body("userId", equalTo(1))
                            .body("title", notNullValue())
                            .body("body", notNullValue());
                }

                @Test
                @DisplayName("GET /posts/1/comments — комментарии к посту")
                void getCommentsByPathTest() {
                    given()
                            .pathParam("postId", 1)
                            .when()
                            .get("/posts/{postId}/comments")
                            .then()
                            .statusCode(200)
                            .body("size()", greaterThan(0))
                            .body("postId", everyItem(equalTo(1)));
                }

                @Test
                @DisplayName("GET /comments?postId=1 — фильтрация через query param")
                void getCommentsByQueryTest() {
                    given()
                            .queryParam("postId", 1)
                            .when()
                            .get("/comments")
                            .then()
                            .statusCode(200)
                            .body("size()", greaterThan(0))
                            .body("postId", everyItem(equalTo(1)));
                }
            }

            // ============================================================
            // 2. ИЗВЛЕЧЕНИЕ ДАННЫХ (extract)
            // ============================================================
            @Nested
            @DisplayName("2. Извлечение данных (extract)")
            class ExtractExamples {

                @Test
                @DisplayName("asString() — получить JSON как текст (для отладки)")
                void getPostAsStringTest() {
                    String response = given()
                            .when()
                            .get("/posts/1")
                            .then()
                            .statusCode(200)
                            .extract()
                            .asString();

                    // Для демонстрации — первые 100 символов
                    System.out.println("JSON ответ (первые 100 символов): " + response.substring(0, Math.min(100, response.length())));
                }

                @Test
                @DisplayName("jsonPath() — получить одно значение (userId)")
                void getPostValueWithJsonPathTest() {
                    int userId = given()
                            .pathParam("id", 1)
                            .when()
                            .get("/posts/{id}")
                            .then()
                            .statusCode(200)
                            .extract()
                            .jsonPath()
                            .getInt("userId");

                    System.out.println("userId = " + userId);
                }

                @Test
                @DisplayName("as(Class.class) — получить Java-объект (ЛУЧШИЙ СПОСОБ)")
                void getPostAsObjectTest() {
                    Get post = given()
                            .pathParam("id", 1)
                            .when()
                            .get("/posts/{id}")
                            .then()
                            .statusCode(200)
                            .extract()
                            .as(Get.class);

                    System.out.println("=== Объект Get ===");
                    System.out.println("userId = " + post.getUserId());
                    System.out.println("id = " + post.getId());
                    System.out.println("title = " + post.getTitle());
                    System.out.println("body = " + post.getBody());
                }
            }

            // ============================================================
            // 3. РАЗНИЦА МЕЖДУ PATH PARAM И QUERY PARAM
            // ============================================================
            @Nested
            @DisplayName("3. pathParam vs queryParam")
            class PathVsQuery {

                @Test
                @DisplayName("pathParam: /posts/{id} — для КОНКРЕТНОГО ресурса")
                void pathParamExample() {
                    given()
                            .pathParam("id", 5)
                            .when()
                            .get("/posts/{id}")
                            .then()
                            .statusCode(200)
                            .body("id", equalTo(5));
                }

                @Test
                @DisplayName("queryParam: /posts?userId=1 — для ФИЛЬТРАЦИИ")
                void queryParamExample() {
                    given()
                            .queryParam("userId", 1)
                            .when()
                            .get("/posts")
                            .then()
                            .statusCode(200)
                            .body("userId", everyItem(equalTo(1)));
                }

                @Test
                @DisplayName("Комбинация: pathParam + queryParam")
                void combinationExample() {
                    given()
                            .pathParam("postId", 1)
                            .queryParam("postId", 1)  // избыточно, но демонстрирует
                            .when()
                            .get("/posts/{postId}/comments")
                            .then()
                            .statusCode(200);
                }
            }
        }

        @Nested
        @DisplayName("Негативные сценарии")
        class NegativeRequests{

            // ============================================================
            // 1. НЕСУЩЕСТВУЮЩИЙ РЕСУРС
            // ============================================================

            @Test
            @DisplayName("GET /posts/99999 — несуществующий пост → 404")
            void getNonExistentPostTest() {
                given()
                        .log().ifValidationFails()
                        .log().uri()
                        .pathParam("id", 99999)
                        .when()
                        .get("/posts/{id}")
                        .then()
                        .log().ifValidationFails()
                        .log().body()
                        .statusCode(404);
            }

            @Test
            @DisplayName("GET /comments?postId=99999 — несуществующий пост → пустой список")
            void getCommentsForNonExistentPostTest() {
                given()
                        .log().ifValidationFails()
                        .log().uri()
                        .pathParam("id", 99999)
                        .when()
                        .get("/posts/{id}/comments")
                        .then()
                        .log().ifValidationFails()
                        .log().body()
                        .statusCode(200)
                        .body("size()", equalTo(0));  // ← не ошибка, а пустой массив!

                ;
            }

            // ============================================================
            // 2. НЕВАЛИДНЫЙ ID (отрицательный, ноль, буквы)
            // ============================================================

            @Test
            @DisplayName("GET /posts/0 — ID = 0 → 404 или 400")
            void getPostWithZeroIdTest() {
                given()
                        .pathParam("id", 0)
                        .when()
                        .get("/posts/{id}")
                        .then()
                        .log().all()
                        .statusCode(anyOf(equalTo(404), equalTo(400)));
            }

            @ParameterizedTest
            @CsvSource({
                    "0",      // ID = 0
                    "-1",     // ID = -1
                    "-100",   // ID = -100
                    "abc",    // строка вместо числа
                    "99999"   // несуществующий ID
            })
            @DisplayName("GET /posts/{id} — невалидные ID → 404")
            void getPostWithInvalidIdTest(String invalidId) {
                given()
                        .pathParam("id", invalidId)
                        .when()
                        .get("/posts/{id}")
                        .then()
                        .log().all()
                        .statusCode(404);
            }

            // ============================================================
            // 3. НЕСУЩЕСТВУЮЩИЙ ПУТЬ
            // ============================================================

            // 1 способ
            /*@Test
            @DisplayName("GET /invalid — несуществующий эндпоинт → 404")
            void getInvalidEndpointTest() {
                given()
                        .when()
                        .get("/invalid-path-12345")
                        .then()
                        .log().all()
                        .statusCode(404);
            }*/

            // 2 способ
            /*@ParameterizedTest
            @ValueSource(strings = {"/invalid-path-12345", "/not-exist", "/fake/endpoint", "/api/v1/wrong"})
            @DisplayName("GET /invalid — несуществующий эндпоинт → 404")
            void getInvalidEndpointTest(String path) {
                given()
                        .when()
                        .log().all()
                        .get(path)
                        .then()
                        .log().all()
                        .statusCode(404);
            }*/

            // 3 способ
            @ParameterizedTest
            @MethodSource("invalidIdProvider")
            @DisplayName("GET /posts/{id} — невалидные ID → 404")
            void getPostWithInvalidId(String invalidId) {
                given()
                        .pathParam("id", invalidId)
                        .when()
                        .get("/posts/{id}")
                        .then()
                        .log().all()
                        .statusCode(404);
            }

            // ⭐ Метод-поставщик данных (может быть в этом же классе)
            static Stream<Arguments> invalidIdProvider() {
                return Stream.of(
                        Arguments.of("99999"),
                        Arguments.of("0"),
                        Arguments.of("-1"),
                        Arguments.of("abc"),
                        Arguments.of("-100"),
                        Arguments.of("invalid")
                );
            }

            /**
             * ┌─────────────────────────────────────────────────────────────────────────┐
             * │                    ИЗВЛЕЧЕНИЕ ДАННЫХ ИЗ RESPONSE                        │
             * │                                                                          │
             * │  .extract().asString()              → всё тело как текст                 │
             * │  .extract().jsonPath()              → JSON-значение                      │
             * │  .extract().as(Класс.class)         → Java-объект                        │
             * │  .extract().header("name")          → заголовок                          │
             * │  .extract().headers()               → все заголовки                      │
             * │  .extract().statusCode()            → статус-код                         │
             * │  .extract().response()              → весь Response объект               │
             * │  .extract().cookie("name")          → куку                               │
             * │  .extract().cookies()               → все куки                           │
             * │  .extract().body().asString()       → тело как текст (альтернатива)      │
             * └─────────────────────────────────────────────────────────────────────────┘
             *
             * ┌─────────────────────────────────────────────────────────────────────────┐
             * │  Нужно                → Используй                                       │
             * │  ──────────────────────────────────────────────────────────────────────│
             * │  Весь JSON как текст  → .extract().asString()                           │
             * │  Одно значение        → .extract().jsonPath().getInt("field")          │
             * │  Весь Java-объект     → .extract().as(MyClass.class)  ← ЛУЧШЕЕ          │
             * │  Заголовок            → .extract().header("name")                        │
             * │  Все заголовки        → .extract().headers()                             │
             * │  Статус код           → .extract().statusCode()                          │
             * │  Всё сразу            → .extract().response()                            │
             * │  Cookie               → .extract().cookie("name")                        │
             * └─────────────────────────────────────────────────────────────────────────┘*/
        }
    }
}