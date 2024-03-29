package com.s0qva.application.controller;

import com.s0qva.application.controller.scene.SceneSwitcher;
import com.s0qva.application.dto.UserDto;
import com.s0qva.application.fxml.FxmlPageLoader;
import com.s0qva.application.service.RegistrationService;
import com.s0qva.application.util.AlertUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@FxmlView("registration-page.fxml")
public class RegistrationController {
    private final RegistrationService registrationService;
    private final FxmlPageLoader fxmlPageLoader;
    private final Class<LoginController> loginControllerClass;
    @FXML
    private TextField email;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;

    @Autowired
    public RegistrationController(RegistrationService registrationService, FxmlPageLoader fxmlPageLoader) {
        this.registrationService = registrationService;
        this.fxmlPageLoader = fxmlPageLoader;
        this.loginControllerClass = LoginController.class;
    }

    public void onSignUp(ActionEvent event) {
        var userDto = buildUserDto();
        var created = registrationService.signUp(userDto);

        if (!created) {
            AlertUtil.generateErrorAlert(
                    DefaultAlertValue.ERROR_ALERT_TITLE,
                    DefaultAlertValue.ERROR_ALERT_HEADER,
                    DefaultAlertValue.ERROR_ALERT_CONTENT
            );
            return;
        }
        var root = fxmlPageLoader.loadFxmlFile(loginControllerClass);

        SceneSwitcher.switchScene(event, root);
        AlertUtil.generateInformationAlert(
                DefaultAlertValue.INFO_ALERT_TITLE,
                DefaultAlertValue.INFO_ALERT_HEADER,
                DefaultAlertValue.INFO_ALERT_CONTENT
        );
    }

    public void onBackToLogin(ActionEvent event) {
        var root = fxmlPageLoader.loadFxmlFile(loginControllerClass);

        SceneSwitcher.switchScene(event, root);
    }

    private UserDto buildUserDto() {
        return UserDto.builder()
                .email(email.getText())
                .username(username.getText())
                .password(password.getText())
                .firstName(firstName.getText())
                .lastName(lastName.getText())
                .blocked(false)
                .build();
    }

    private static class DefaultAlertValue {
        private static final String ERROR_ALERT_TITLE = "Ошибка регистрации";
        private static final String ERROR_ALERT_HEADER = "Логин уже существует";
        private static final String ERROR_ALERT_CONTENT = "Пользователь с таким логином уже существует";
        private static final String INFO_ALERT_TITLE = "Успешная регистрация";
        private static final String INFO_ALERT_HEADER = "Регистрация прошла успешно, теперь войдите в свой аккаунт";
        private static final String INFO_ALERT_CONTENT = "";
    }
}
