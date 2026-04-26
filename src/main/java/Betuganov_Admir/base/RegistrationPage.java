package Betuganov_Admir.base;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class RegistrationPage {

    private final Page page;

    // ✅ Просто передаём page и всё! Локаторы не храним!
    public RegistrationPage(Page page) {
        this.page = page;
    }

    // ✅ ВСЕ ДЕЙСТВИЯ ПРЯМО В МЕТОДАХ
    public RegistrationPage listOfItems() {
        // Локатор прямо здесь!
        List<Locator> login = page.locator("label").all();

        int count = login.size();
        log.info("Всего элементов у нас вот столько: {}", count);

        // ✅ Получаем список текстов всех элементов
        List<String> texts = login.stream()
                .map(m -> m.textContent().trim())
                .collect(Collectors.toList());

        log.info("Тексты элементов: {}", texts);
        return this;
    }


}
