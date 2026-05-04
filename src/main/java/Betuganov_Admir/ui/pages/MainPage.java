package Betuganov_Admir.ui.pages;

import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainPage {

    private final Page page;

    // ✅ Просто передаём page и всё! Локаторы не храним!
    public MainPage(Page page) {
        this.page = page;
    }

    // ✅ ВСЕ ДЕЙСТВИЯ ПРЯМО В МЕТОДАХ
    public RegistrationPage clickRegistrationButton() {
        page.locator("a.header__link").click();  // ← локатор прямо здесь!
        log.info("Клик осуществлён успешно!!!");
        return new RegistrationPage(page);
    }
}
