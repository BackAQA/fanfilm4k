package bdd.steps;

import Betuganov_Admir.bdd.BaseCucumberPage;
import com.microsoft.playwright.Page;
import io.cucumber.java.en.Then;
import io.cucumber.java.ru.*;
import org.junit.jupiter.api.Assertions;

public class MainPageSteps {

    // ✅ Просто получаем page через статический метод
    private com.microsoft.playwright.Page getPage() {
        return BaseCucumberPage.getPage();
    }


    @Дано("Пользователь открывает главную страницу {string}")
    public void openMainPage(String url) {
        getPage().navigate(url);
        System.out.println("✅ Главная страница открыта: " + url);
    }

    @Когда("пользователь нажимает на кнопку {string}")
    public void clickButton(String buttonText) {
        getPage().click("a.header__link");
        System.out.println("✅ Нажата кнопка: " + buttonText);
    }

    @Then("Пользователь успешно перешёл на страницу регистраций")
    public RegistrationSteps pageRegistration(){
        System.out.println("На следующую страницу перешли");
        String currentUrl = getPage().url();
        System.out.println("Текущий URL: " + currentUrl);
        Assertions.assertTrue(currentUrl.contains("register"), "Ожидалась страница регистрации, но текущий URL: " + currentUrl);
        return new RegistrationSteps(getPage());
    }
}
