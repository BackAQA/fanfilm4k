package ui.base;

import Betuganov_Admir.ui.base.BasePage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static ui.base.Config.URL;

public class BasePageTest extends BasePage {  // ← наследовать BasePage!

    @BeforeAll
    static void setUpHeavy() {
        setUpHeavyResources();  // ← static метод
    }

    @BeforeEach
    void setUpLight() {
        createLightResources();  // ← создаём context и page
        openPage(URL);           // ← теперь page существует!
    }

    @AfterEach
    void tearDownLight() {
        closeLightResources();
    }

    @AfterAll
    static void tearDownHeavy() {
        closeHeavyResources();
    }
}
