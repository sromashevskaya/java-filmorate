package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    private final FilmController filmController = new FilmController();

    @Test
    public void createFilmWithNoData() {
        Film film = new Film();
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void createFilmWithoutName() {
        Film film = new Film();
        film.setDescription("Описание 1");
        film.setReleaseDate(LocalDate.of(2000, 1, 15));
        film.setDuration(60);
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }

    @Test
    public void createFilmWithIncorrectReleaseDate() {
        Film film = new Film();
        film.setName("Название 1");
        film.setDescription("Описание 1");
        film.setReleaseDate(LocalDate.of(1890, 3, 25));
        film.setDuration(60);
        assertThrows(ValidationException.class, () -> filmController.createFilm(film));
    }
}