package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class FilmService {
    private int id = 1;
    private Map<Integer, Film> films = new HashMap<>();
    private final int maxDescriptionLength = 200;
    private final LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);

    public Film createFilm(Film film) {
        validate(film);
        film.setId(id++);
        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film) {
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
            return oldFilm;
        }
        throw new ValidationException("Фильм не найден " + film.getId());
    }


    public Collection<Film> findAll() {
        return films.values();
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
}
