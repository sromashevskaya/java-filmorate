package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Repository
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String INSERT_QUERY = """
            INSERT INTO users (email, login, name, birthday)
            VALUES (?,?,?,?)
            """;
    private static final String UPDATE_QUERY = """
            UPDATE users 
            SET login = ?, name = ?, email = ?, birthday = ?
            WHERE id = ?
            """;

    private static final String INSERT_FRIEND_QUERY = """
            INSERT INTO user_friends (user_id, friend_id)
            VALUES (?, ?)
            """;

    private static final String DELETE_FRIEND_QUERY = """
            DELETE FROM user_friends
            WHERE user_id = ? AND friend_id = ?
            """;

    private static final String FIND_FRIENDS_BY_ID = """
            SELECT u.*
            FROM user_friends uf
            JOIN users u ON uf.friend_id = u.id
            WHERE uf.user_id = ?
            ORDER BY u.id
            """;

    private static final String FIND_COMMON_FRIENDS_BY_ID = """
            SELECT u.*
            FROM user_friends uf1
            JOIN user_friends uf2 ON uf1.friend_id = uf2.friend_id
            JOIN users u ON uf1.friend_id = u.id
            WHERE uf1.user_id = ? AND uf2.user_id = ?
            """;

    private static final String FIND_LIKE_BY_ID_QUERY = """
            SELECT u.*
            FROM likes l
            JOIN users u ON l.user_id = u.id
            WHERE l.film_id = ?
            """;


    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper, User.class);
    }

    @Override
    public Collection<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<User> getUser(Long userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    @Override
    public User createUser(User newUser) {
        Long id = insert(INSERT_QUERY,
                newUser.getEmail(),
                newUser.getLogin(),  // Теперь login идет перед name
                newUser.getName(),
                newUser.getBirthday()
        );
        newUser.setId(id);
        return newUser;
    }

    @Override
    public User updateUser(User newUser) {
        update(
                UPDATE_QUERY,
                newUser.getLogin(),
                newUser.getName(),
                newUser.getEmail(),
                newUser.getBirthday(),
                newUser.getId()
        );
        return newUser;
    }

    @Override
    public void createFriend(Long userId, Long friendId) {
        update(
                INSERT_FRIEND_QUERY,
                userId,
                friendId
        );
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        delete(DELETE_FRIEND_QUERY, userId, friendId);
    }

    @Override
    public List<User> getFriendsById(Long userId) {
        return findMany(FIND_FRIENDS_BY_ID, userId);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
        return findMany(FIND_COMMON_FRIENDS_BY_ID, userId, friendId);
    }

    @Override
    public List<User> getLikesById(Long filmId) {
        return findMany(FIND_LIKE_BY_ID_QUERY, filmId);
    }
}
