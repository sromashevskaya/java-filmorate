package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private int id;
    private Map<Integer, Film> films = new HashMap<>();
    private final int maxDescriptionLength = 200;
    private final LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        log.info("Добавление нового фильма: {}", film.getName());
        validate(film);
        film.setId((long) id++);
        films.put(Math.toIntExact((film.getId())), film);
        log.trace("Фильм {} добавлен, его id {}",film, film.getId());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Обновление фильма {}", film.getId());
        if (film.getId() == null) {
            throw new ValidationException("Укажите id фильма");
        }
        if (films.containsKey(film.getId())) {
            Film oldFilm = films.get(film.getId());
            validate(film);
            oldFilm.setName(film.getName());
            oldFilm.setDescription(film.getDescription());
            oldFilm.setReleaseDate(film.getReleaseDate());
            oldFilm.setDuration(film.getDuration());
            log.trace("Фильм id {} успешно обновлен", oldFilm.getId());
            return oldFilm;
        }
        throw new ValidationException("Фильм не найден " + film.getId());
    }

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    private void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.debug("Название пустое");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > maxDescriptionLength) {
            log.debug("Превышена максимальная длина описания");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(minReleaseDate)) {
            log.debug("Некорректная дата релиза");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.debug("Некорректная продолжительность фильма");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }

}