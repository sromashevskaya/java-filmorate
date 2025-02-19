package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@Import({FilmDbStorage.class, FilmRowMapper.class, GenreRowMapper.class,
        UserDbStorage.class, UserRowMapper.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    @Autowired
    private FilmDbStorage filmDbStorage;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final UserDbStorage userDbStorage;

    private Film film;

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;


    @BeforeEach
    void setUp() {
        film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller.");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);
        film.setMpa(new MPA(1L, "G"));
        film.setGenres(new ArrayList<>());
    }

    @Test
    void createFilm_ShouldSaveAndReturnFilm() {
        Film savedFilm = filmDbStorage.createFilm(film);

        assertNotNull(savedFilm.getId(), "ID фильма не должен быть null после сохранения");
        assertEquals(film.getName(), savedFilm.getName());
        assertEquals(film.getDescription(), savedFilm.getDescription());
        assertEquals(film.getReleaseDate(), savedFilm.getReleaseDate());
        assertEquals(film.getDuration(), savedFilm.getDuration());
        assertNotNull(savedFilm.getMpa(), "MPA не должен быть null");
    }

    @Test
    void getFilm_ShouldReturnSavedFilm() {
        Film savedFilm = filmDbStorage.createFilm(film);

        Optional<Film> retrievedFilm = filmDbStorage.getFilm(savedFilm.getId());

        assertTrue(retrievedFilm.isPresent(), "Фильм должен быть найден в базе данных");
        assertEquals(savedFilm.getId(), retrievedFilm.get().getId());
        assertEquals(savedFilm.getName(), retrievedFilm.get().getName());
        assertEquals(savedFilm.getDescription(), retrievedFilm.get().getDescription());
    }

    @Test
    void testCreateLikeNew() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        user = userDbStorage.createUser(user);

        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        film.setMpa(new MPA(1L, "G"));
        film = filmDbStorage.createFilm(film);

        filmDbStorage.likeFilm(film.getId(), user.getId());
        int likes = userStorage.getLikesById(2L).size();
        assertEquals(1, likes, "Количество лайков не соответствует");
    }

    @Test
    void testGetPopularFilms() {
        Film film1 = new Film();
        film1.setName("Popular Film");
        film1.setDescription("Most liked film");
        film1.setReleaseDate(LocalDate.of(2010, 1, 1));
        film1.setDuration(150);
        film1.setMpa(new MPA(1L, "G"));
        film1 = filmDbStorage.createFilm(film1);

        Film film2 = new Film();
        film2.setName("Less Popular Film");
        film2.setDescription("Fewer likes");
        film2.setReleaseDate(LocalDate.of(2012, 5, 5));
        film2.setDuration(100);
        film2.setMpa(new MPA(2L, "PG"));
        film2 = filmDbStorage.createFilm(film2);

        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1");
        user1.setName("User One");
        user1.setBirthday(LocalDate.of(1995, 1, 1));
        user1 = userDbStorage.createUser(user1);

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setLogin("user2");
        user2.setName("User Two");
        user2.setBirthday(LocalDate.of(1996, 2, 2));
        user2 = userDbStorage.createUser(user2);

        filmDbStorage.likeFilm(film1.getId(), user1.getId());
        filmDbStorage.likeFilm(film1.getId(), user2.getId());
        filmDbStorage.likeFilm(film2.getId(), user1.getId());

        List<Film> popularFilms = (List<Film>) filmDbStorage.getPopularFilms(2);

        assertNotNull(popularFilms);
        assertEquals(2, popularFilms.size());
    }

}
