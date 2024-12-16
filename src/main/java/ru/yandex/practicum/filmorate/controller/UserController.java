package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    private int id = 1;

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Добавление нового пользователя: {}", user.getLogin());
        validate(user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.trace("Пользователь добавлен {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Обновление пользователя {} ", user.getId());
        if (user.getId() == null) {
            throw new ValidationException("Укажите id пользователя");
        }
        if (users.containsKey(user.getId())) {
            User oldUser = users.get(user.getId());
            validate(user);
            oldUser.setName(user.getName());
            oldUser.setBirthday(user.getBirthday());
            oldUser.setEmail(user.getEmail());
            oldUser.setLogin(user.getLogin());
            log.trace("Пользователь id {} успешно обновлен", oldUser.getId());
            return oldUser;
        }
        throw new ValidationException("Пользователь не найден " + user.getId());
    }

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    private void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() ||
                user.getEmail().split("@").length != 2) {
            log.debug("электронная почта не заполнена");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() ||
                user.getLogin().split(" ").length > 1) {
            log.debug("логин пустой или содержит пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя для отображения пустое, был использован логин", user.getLogin());
        }
        if (!user.getBirthday().isBefore(LocalDate.now())) {
            log.debug("дата рождения некорректная");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}