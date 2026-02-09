package ru.netology.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldLoginSuccessfullyIfRegisteredActiveUser() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("h2").shouldHave(Condition.exactText("Личный кабинет"), Duration.ofSeconds(10));

        $("h2").shouldHave(Condition.exactText("Личный кабинет"), Duration.ofSeconds(10))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = DataGenerator.Registration.getRegisteredUser("blocked");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("button.button").click();
        // Проверяем наличие сообщения о блокировке
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"), Duration.ofSeconds(10));

        $("[data-test-id='error-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        var wrongLogin = DataGenerator.getRandomLogin();
        $("[data-test-id='login'] input").setValue(wrongLogin);
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        // Проверяем сообщение о неверном логине/пароле
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));

        $("[data-test-id='error-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        var wrongPassword = DataGenerator.getRandomPassword();
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $("button.button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"), Duration.ofSeconds(10));

        $("[data-test-id='error-notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(10))
                .shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }
}
