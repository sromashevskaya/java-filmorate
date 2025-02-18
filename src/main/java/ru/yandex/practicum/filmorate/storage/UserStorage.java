package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Collection<User> findAll();

    Optional<User> getUser(Long userId);

    User createUser(User newUser);

    User updateUser(User newUser);

    void createFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    List<User> getFriendsById(Long userId);

    List<User> getCommonFriends(Long userId, Long friendId);

    List<User> getLikesById(Long filmId);
}