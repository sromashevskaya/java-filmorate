package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User getUser(Long userId) {
        return users.get(userId);
    }

    @Override
    public User createUser(User newUser) {
        newUser.setId((long) id++);
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User updateUser(User newUser) {
        User oldUser = users.get(newUser.getId());
        oldUser.setName(newUser.getName());
        oldUser.setBirthday(newUser.getBirthday());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        return oldUser;
    }

    @Override
    public void deleteUser(Long userId) {
        users.remove(userId);
    }
}
