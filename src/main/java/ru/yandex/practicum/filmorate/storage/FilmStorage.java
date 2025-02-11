package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> findAll();

    Film createFilm(Film newFilm);

    Film updateFilm(Film newFilm);

    void deleteFilm(Long filmId);

    Film getFilm(Long filmId);
}