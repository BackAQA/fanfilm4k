package ui.tests;

import Betuganov_Admir.ui.pages.MainPage;
import ui.base.BasePageTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
// ========== ВЛОЖЕННЫЙ КЛАСС: ПРОФИЛЬ ПОЛЬЗОВАТЕЛЯ ==========
@Slf4j
public class GeneralClassForTests extends BasePageTest {

    @Test
    @DisplayName("Открытие сайта по ссылке")
    public void test(){
        log.info("Сайт открылся");
    }

    @Test
    @DisplayName("Клик по кнопке регистрации")
    public void test2() {
        // ✅ Вот так тест будет выглядеть завершённым
        MainPage mainPage = new MainPage(page);

        mainPage
                .clickRegistrationButton()   // ← переход на RegistrationPage
                .listOfItems();              // ← работа со списком элементов
    }


}


