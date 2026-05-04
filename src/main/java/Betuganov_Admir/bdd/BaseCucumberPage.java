package Betuganov_Admir.bdd;

import com.microsoft.playwright.*;

public class BaseCucumberPage {

    // ========== ТЯЖЁЛЫЕ РЕСУРСЫ - СТАТИК ==========
    private static Playwright playwright;
    private static Browser browser;

    // ========== ЛЁГКИЕ РЕСУРСЫ - СТАТИК ==========
    private static BrowserContext context;
    private static Page page;

    // Инициализация тяжёлых ресурсов (1 раз)
    public static synchronized void initHeavy() {
        if (playwright == null) {
            playwright = Playwright.create();
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setChannel("chrome")
                    .setHeadless(false)
                    .setSlowMo(2000)
            );
        }
    }

    // Создание лёгких ресурсов (перед каждым сценарием)
    public static void createLight() {       // ← СДЕЛАЛ СТАТИК
        context = browser.newContext();
        page = context.newPage();
    }

    // Закрытие лёгких ресурсов (после каждого сценария)
    public static void closeLight() {        // ← СДЕЛАЛ СТАТИК
        if (page != null) page.close();
        if (context != null) context.close();
    }

    // Закрытие тяжёлых ресурсов (в конце всех тестов)
    public static void closeHeavy() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    public static Page getPage() {
        return page;
    }
}