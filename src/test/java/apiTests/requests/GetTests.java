package apiTests.requests;

import Betuganov_Admir.api.Get;
import apiTests.baseApi.BaseApiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetTests extends BaseApiTest {

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