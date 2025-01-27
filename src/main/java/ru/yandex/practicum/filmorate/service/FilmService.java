package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private int id = 1;
    private final int maxDescriptionLength = 200;
    private final LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    public Film createFilm(Film film) {
        validate(film);
        film.setId((long) id++);
        filmStorage.createFilm(film);
        return film;
    }

    public Film updateFilm(Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Укажите id фильма");
        }
        if (filmStorage.getFilm(film.getId()) != null) {
            validate(film);
            return filmStorage.updateFilm(film);
        }
        throw new ValidationException("Фильм не найден " + film.getId());
    }

    public void deleteFilm(Long id) {
        filmStorage.deleteFilm(id);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    private void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > maxDescriptionLength) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(minReleaseDate)) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }

    public Film getFilm(Long filmId) {
        Film film = filmStorage.getFilm(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с указанным id не найден " + filmId);
        }
        return film;
    }

    public void likeFilm(Long id, Long userId) {
        Film film = filmStorage.getFilm(id);
        User user = userStorage.getUser(userId);
        if (film == null) {
            throw new NotFoundException("Фильм с указанным id не найден " + id);
        }
        if (user == null) {
            throw new NotFoundException("Пользователь с указанным id не найден " + userId);
        }
        film.getLikes().add(userId);
    }

    public void deleteLikeFilm(Long id, Long userId) {
        Film film = filmStorage.getFilm(id);
        User user = userStorage.getUser(userId);
        if (film == null) {
            throw new NotFoundException("Фильм с указанным id не найден " + id);
        }
        if (user == null) {
            throw new NotFoundException("Пользователь с указанным id не найден " + userId);
        }
        film.getLikes().remove(userId);
    }


    public Collection<Film> getPopularFilms(Integer count) {
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .toList();
    }

}
