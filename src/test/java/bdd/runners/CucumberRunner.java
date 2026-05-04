package bdd.runners;


import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "bdd.steps")
public class CucumberRunner {
}






























/**
 *
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │  🔵 PLAYWRIGHT (UI)                                                      │
 * │  ├── main/java/.../ui/          ← Page Object, BasePage                 │
 * │  ├── test/java/ui/              ← JUnit тесты                            │
 * │  └── НЕ ИСПОЛЬЗУЕТ .feature файлы                                        │
 * │                                                                          │
 * │  🟢 REST ASSURED (API)                                                   │
 * │  ├── main/java/.../api/         ← Модели, клиенты, спецификации         │
 * │  ├── test/java/api/             ← JUnit тесты                            │
 * │  └── НЕ ИСПОЛЬЗУЕТ .feature файлы                                        │
 * │                                                                          │
 * │  🟡 CUCUMBER (BDD)                                                       │
 * │  ├── main/java/.../bdd/steps/   ← Шаги (Given/When/Then)                │
 * │  ├── test/java/bdd/runners/     ← Запускатор                             │
 * │  ├── test/resources/features/   ← .feature файлы                         │
 * │  └── МОЖЕТ использовать UI ИЛИ API                                       │
 * └─────────────────────────────────────────────────────────────────────────┘*/

