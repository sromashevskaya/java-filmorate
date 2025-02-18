package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private int id = 1;

    private final UserStorage userStorage;

    public User createUser(User user) {
        validate(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new ValidationException("Укажите id пользователя");
        }
        if (userStorage.getUser(user.getId()) != null) {
            validate(user);
            return userStorage.updateUser(user);
        }
        throw new NotFoundException("Пользователь не найден " + user.getId());
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
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

    public User getUser(Long userId) {
        return userStorage.getUser(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным id не найден " + userId));
    }

    public void createFriend(Long userId, Long friendId) {
      //  Optional<User> user = getUserByIdOrThrow(userId);
      //  Optional<User> friend = getUserByIdOrThrow(friendId);
      //  userStorage.createFriend(userId, friendId);

        if (userStorage.getUser(userId).isEmpty() || userStorage.getUser(friendId).isEmpty()) {
            throw new NotFoundException("Один из пользователей не найден");
        }
        userStorage.createFriend(userId, friendId);
    }

    private Optional<User> getUserByIdOrThrow(Long userId) {
        Optional<User> user = userStorage.getUser(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с указанным id не найден " + userId);
        }
        return user;
    }

    public void deleteFriend(Long userId, Long friendId) {
        if (userStorage.getUser(userId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (userStorage.getUser(friendId).isEmpty()) {
            throw new NotFoundException("Друг не найден");
        }

        /*   List<User> friends = userStorage.getFriendsById(userId);
        if (!friends.stream().anyMatch(f -> f.getId().equals(friendId))) {
            throw new NotFoundException("Пользователь не является другом");
        } */

        userStorage.deleteFriend(userId, friendId);

    }

    public Collection<User> findAllUserFriends(Long userId) {
        if (userStorage.getUser(userId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        return userStorage.getFriendsById(userId);
    }

    public Collection<User> getCommonFriendsWithAnotherUser(Long userId, Long commonId) {
        Optional<User> user = getUserByIdOrThrow(userId);
        Optional<User> otherUser = getUserByIdOrThrow(commonId);

        return userStorage.getCommonFriends(userId, commonId);
    }
}