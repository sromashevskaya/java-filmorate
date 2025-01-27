package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> findAll();

    User getUser(Long userId);
    
    User createUser(User newUser);

    User updateUser(User newUser);

    void deleteUser(Long userId);
}