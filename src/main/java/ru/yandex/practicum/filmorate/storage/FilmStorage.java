package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> findAll();

    Film createFilm(Film newFilm);

    Film updateFilm(Film newFilm);

  //  void deleteFilm(Long filmId);

    Optional<Film> getFilm(Long filmId);

    void createGenre(Long genreId, Long filmId);

    void likeFilm(Long filmId, Long userId);

    void deleteLikeFilm(Long filmId, Long userId);

    Collection<Film> getPopularFilms(Integer count);
}