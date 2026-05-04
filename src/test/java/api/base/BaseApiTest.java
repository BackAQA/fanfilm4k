package api.base;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import static api.config.Config.BASE_URL;

public class BaseApiTest {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = BASE_URL;                                    // ← 1. Базовый URL
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();  // ← 2. Логи только при падении
    }
}