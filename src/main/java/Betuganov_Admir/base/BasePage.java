package Betuganov_Admir.base;

import com.microsoft.playwright.*;

public class BasePage {

  // ========== ТЯЖЁЛЫЕ РЕСУРСЫ - СТАТИК (1 раз на все тесты) ==========
    protected static Playwright playwright;
    protected static Browser browser;

    // ========== ЛЁГКИЕ РЕСУРСЫ - НЕ СТАТИК (новые перед каждым тестом) ==========
    protected BrowserContext context;
    protected Page page;

    public static void setUpHeavyResources(){
        if (playwright == null) {
            playwright = Playwright.create();
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setChannel("chrome")
                    .setHeadless(false)
                    .setSlowMo(2000)
            );
        }
    }

    // ✅ Новый метод для создания лёгких ресурсов (вызывать в @BeforeEach)
    public void createLightResources() {
        context = browser.newContext();  // ← новая изолированная сессия!
        page = context.newPage();         // ← новая вкладка!
    }

    // Открыть страницу https://v11.fanfilm4k.media/
    public void openPage(String url) {
        if (page == null) {
            createLightResources();
        }
        page.navigate(url);
    }

    // Закрыть лёгкие ресурсы (вызывать в @AfterEach)
    public void closeLightResources() {
        if (page != null) page.close();
        if (context != null) context.close();
    }

    // Закрыть тяжёлые ресурсы (вызывать в @AfterAll)
    public static void closeHeavyResources() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}