package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {

    UserService userService = new UserService();
    UserController userController = new UserController(userService);

    @Test
    public void createUserWithNoData() {
        User user = new User();
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    public void createUserWithIncorrectLogin() {
        User user = new User();
        user.setLogin("dolore ullamco");
        user.setEmail("yandex@mail.ru");
        user.setBirthday(LocalDate.of(2000, 3, 15));
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }
}
