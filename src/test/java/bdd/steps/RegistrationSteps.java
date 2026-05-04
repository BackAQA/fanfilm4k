package bdd.steps;

import com.microsoft.playwright.Page;
import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Когда;

public class RegistrationSteps {

    private final Page page;

    public RegistrationSteps(Page page) {
        this.page = page;
    }

    // ========== GIVEN / ДАНО ==========
    @Дано("пользователь открыл страницу регистрации {string}")
    public void openRegistrationPage(String url) {
        page.navigate(url);
        System.out.println("✅ Страница регистрации открыта: " + url);
    }

    // ========== WHEN / КОГДА ==========
    @Когда("пользователь вводит логин {string}")
    public void enterLogin(String login) {
        page.fill("#login", login);
        System.out.println("✅ Введён логин: " + login);
    }

    @Когда("пользователь вводит пароль {string}")
    public void enterPassword(String password) {
        page.fill("#password", password);
        System.out.println("✅ Введён пароль");
    }

    @Когда("пользователь повторяет пароль {string}")
    public void repeatPassword(String password) {
        page.fill("#confirm_password", password);
        System.out.println("✅ Пароль подтверждён");
    }

    @Когда("пользователь вводит email {string}")
    public void enterEmail(String email) {
        page.fill("#email", email);
        System.out.println("✅ Введён email: " + email);
    }

    @Когда("пользователь решает капчу")
    public void solveCaptcha() {
        // Берём текст капчи (пример: "4 + 4 = ?")
        String captchaText = page.textContent("input[name='question_answer']");

        // Извлекаем числа
        String[] parts = captchaText.split(" ");
        int num1 = Integer.parseInt(parts[0]);
        int num2 = Integer.parseInt(parts[2]);
        int result = num1 + num2;

        // Вводим ответ
        page.fill("input[name='question_answer']", String.valueOf(result));
        System.out.println("✅ Капча решена: " + num1 + " + " + num2 + " = " + result);
    }

    @Когда("пользователь нажимает кнопку {string}")
    public void clickButton() {
        page.click("button.form__btn.form__btn--primary");
        System.out.println("✅ Нажата кнопка");
    }

}
