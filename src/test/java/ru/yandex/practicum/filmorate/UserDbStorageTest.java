package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@Import({UserDbStorage.class, UserRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final UserDbStorage userDbStorage;

    @Test
    void testGetAllUsers() {
        List<User> usersBefore = (List<User>) userDbStorage.findAll();

        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1");
        user1.setName("User One");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        userDbStorage.createUser(user1);

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setLogin("user2");
        user2.setName("User Two");
        user2.setBirthday(LocalDate.of(1995, 5, 5));
        userDbStorage.createUser(user2);

        List<User> usersAfter = (List<User>) userDbStorage.findAll();

        assertEquals(usersBefore.size() + 2, usersAfter.size());
    }

    @Test
    void testGetUserById() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1992, 3, 15));
        User savedUser = userDbStorage.createUser(user);

        Optional<User> foundUser = userDbStorage.getUser(savedUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail());
        assertEquals("testuser", foundUser.get().getLogin());
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setEmail("newuser@example.com");
        user.setLogin("newuser");
        user.setName("New User");
        user.setBirthday(LocalDate.of(2000, 7, 20));

        User savedUser = userDbStorage.createUser(user);

        assertNotNull(savedUser.getId());

        Optional<User> retrievedUser = userDbStorage.getUser(savedUser.getId());
        assertTrue(retrievedUser.isPresent());
        assertEquals("newuser@example.com", retrievedUser.get().getEmail());
        assertEquals("newuser", retrievedUser.get().getLogin());
    }

    @Test
    void testAddFriend() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1");
        user1.setName("User One");
        user1.setBirthday(LocalDate.of(1993, 4, 10));
        user1 = userDbStorage.createUser(user1);

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setLogin("user2");
        user2.setName("User Two");
        user2.setBirthday(LocalDate.of(1994, 5, 15));
        user2 = userDbStorage.createUser(user2);

        userDbStorage.createFriend(user1.getId(), user2.getId());

        List<User> friends = userDbStorage.getFriendsById(user1.getId());
        assertEquals(1, friends.size());
        assertEquals(user2.getId(), friends.get(0).getId());
    }

    @Test
    void testGetCommonFriends() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1");
        user1.setName("User One");
        user1.setBirthday(LocalDate.of(1991, 2, 20));
        user1 = userDbStorage.createUser(user1);

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setLogin("user2");
        user2.setName("User Two");
        user2.setBirthday(LocalDate.of(1992, 3, 25));
        user2 = userDbStorage.createUser(user2);

        User commonFriend = new User();
        commonFriend.setEmail("common@example.com");
        commonFriend.setLogin("common");
        commonFriend.setName("Common Friend");
        commonFriend.setBirthday(LocalDate.of(1990, 1, 10));
        commonFriend = userDbStorage.createUser(commonFriend);

        userDbStorage.createFriend(user1.getId(), commonFriend.getId());
        userDbStorage.createFriend(user2.getId(), commonFriend.getId());

        List<User> commonFriends = userDbStorage.getCommonFriends(user1.getId(), user2.getId());

        assertEquals(1, commonFriends.size());
        assertEquals(commonFriend.getId(), commonFriends.get(0).getId());
    }

}
