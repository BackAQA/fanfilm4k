package bdd.config;

public class Config {

    private Config() {
        // приватный конструктор, чтобы нельзя было создать объект
    }

    // Базовый URL сайта
    public static final String BASE_URL = "https://fanfilm4k/registraciya";

    // Конкретные страницы
    public static final String REGISTRATION_PAGE = BASE_URL + "/registraciya";
    public static final String LOGIN_PAGE = BASE_URL + "/login";
    public static final String HOME_PAGE = BASE_URL + "/";

    // Тестовые данные
    public static final String TEST_USER_LOGIN = "fanuser123";
    public static final String TEST_USER_PASSWORD = "Qwerty123!";
    public static final String TEST_USER_EMAIL = "fanuser123@example.com";
}
