# 🎬 fanfilm4k — автоматизация тестирования сайта

Автотесты для сайта fanfilm4k на **Playwright + Java + JUnit 5**

## 🛠 Стек технологий
- Java 17
- Playwright (автоматизация браузера)
- JUnit 5 (тестовый фреймворк)
- Maven (сборка)
- Lombok (логирование)
- Allure (отчёты)

## 🚀 Как запустить тесты локально

```bash
# 1. Клонировать репозиторий
git clone https://github.com/BackAQA/fanfilm4k.git
cd fanfilm4k

# 2. Установить браузеры Playwright
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install chromium"

# 3. Запустить тесты
mvn clean test
