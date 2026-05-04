package bdd.steps;

import Betuganov_Admir.bdd.BaseCucumberPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;

import static Betuganov_Admir.bdd.BaseCucumberPage.closeLight;
import static Betuganov_Admir.bdd.BaseCucumberPage.createLight;

public class Hooks {

    @Before
    public void setUp() {
        BaseCucumberPage.initHeavy();  // тяжёлые (1 раз)
        createLight();            // лёгкие (перед каждым)
    }

    @After
    public void tearDown() {
        closeLight();        // закрываем страницу
    }
}
