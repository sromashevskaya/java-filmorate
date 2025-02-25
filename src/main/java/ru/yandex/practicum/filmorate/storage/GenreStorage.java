package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreStorage {
    Optional<Genre> getGenre(Long id);

    Collection<Genre> findAll();

    List<Genre> getGenreByFilmId(Long filmId);

    void addGenresToFilm(Long filmId, Set<Genre> genres);
}
