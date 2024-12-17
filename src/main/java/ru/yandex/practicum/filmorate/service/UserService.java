package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private Map<Integer, User> users = new HashMap<>();

    private int id = 1;

    public User createUser(User user) {
        validate(user);
        user.setId(id++);
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {
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
            return oldUser;
        }
        throw new ValidationException("Пользователь не найден " + user.getId());
    }

    public Collection<User> findAll() {
        return users.values();
    }

    private void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() ||
                user.getEmail().split("@").length != 2) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() ||
                user.getLogin().split(" ").length > 1) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (!user.getBirthday().isBefore(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
