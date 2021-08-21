package ru.netology;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CardDeliveryTest {

    DataGenerator.UserInfo user;
    String firstMeetingDate;
    String secondMeetingDate;
    SelenideElement element;

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void setUpEach() {
        user = DataGenerator.Registration.generateUser();
        firstMeetingDate = DataGenerator.generateDate(3, 5);
        secondMeetingDate = DataGenerator.generateDate(5, 10);
        open("http://localhost:9999");
    }

    @AfterEach
    void tearDownEach() {
        closeWebDriver();
    }

    @AfterAll
    static void tearDownAll() { SelenideLogger.removeListener("allure"); }

    @Test
    void shouldSuccessfulPlanAndReplanMeeting() {
        $("[data-test-id=city] input").setValue(user.getCity());
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(user.getName());
        $("[data-test-id=phone] input").setValue(user.getPhone());
        $("[data-test-id=agreement] .checkbox__box").click();
        $("[role=button] .button__content").click();

        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(text("Встреча успешно запланирована на " + firstMeetingDate));

        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $("[role=button] .button__content").click();

        $("[data-test-id=replan-notification] .notification__content")
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));

        $(byText("Перепланировать")).click();

        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(text("Встреча успешно запланирована на " + secondMeetingDate));
    }

    @Test
    void shouldDontSuccessfulIfInvalidCity() {
        $("[data-test-id=city] input").setValue(DataGenerator.invalidCity);
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.DELETE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);

        $("[data-test-id=name] input").setValue(user.getName());
        $("[data-test-id=phone] input").setValue(user.getPhone());
        $("[data-test-id=agreement] .checkbox__box").click();
        $("[role=button] .button__content").click();

        $(byText("Доставка в выбранный город недоступна")).isDisplayed();
    }

}
