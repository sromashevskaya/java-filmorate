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
        User user = userStorage.getUser(userId);
        if (user != null) {
            validate(user);
            return user;
        }
        throw new NotFoundException("Пользователь с указанным id не найден " + userId);
    }

    public void deleteUser(Long userId) {
        if (userStorage.getUser(userId) == null) {
            throw new NotFoundException("Пользователь с указанным id не найден " + userId);
        }
        userStorage.deleteUser(userId);
    }

    public void createFriend(Long userId, Long friendId) {
        User user = getUserByIdOrThrow(userId);
        User friend = getUserByIdOrThrow(friendId);
        if (user.getFriends().contains(friendId)) {
          //  throw new NotFoundException("Друг с указанным id уже существует");
            return;
        }
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    private User getUserByIdOrThrow(Long userId) {
        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с указанным id не найден " + userId);
        }
        return user;
    }

    public void deleteFriend(Long userId, Long friendId) {
        User user = getUserByIdOrThrow(userId);
        User friend = getUserByIdOrThrow(friendId);
        if (!user.getFriends().contains(friendId)) {
            throw new NotFoundException("Друг с id = " + friendId + " отсутствует в списке друзей");
        }
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public Collection<User> findAllUserFriends(Long userId) {
        User user = getUserByIdOrThrow(userId);

        //    User user = userStorage.getUser(userId);

        if (user.getFriends().isEmpty()) {
            throw new NotFoundException("Друзья не найдены");
        }

        return user.getFriends().stream()
                .map(userStorage::getUser)
                .toList();
    /*    return userStorage.getUserById(userId).getFriends().stream()
                .map(userStorage::getUserById)
                .toList();
        return getUser(userId).getFriends().stream()
                .map(this::getUser)
                .collect(Collectors.toList()); */
    }

    public Collection<User> getCommonFriendsWithAnotherUser(Long userId, Long commonId) {
        User user = getUserByIdOrThrow(userId);
        User otherUser = getUserByIdOrThrow(commonId);

        return user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .map(userStorage::getUser)
                .toList();
    }
}