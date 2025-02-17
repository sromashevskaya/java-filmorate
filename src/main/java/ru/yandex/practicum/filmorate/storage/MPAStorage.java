package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;
import java.util.Optional;

public interface MPAStorage {
    Collection<MPA> findAll();

    Optional<MPA> getMPA(Long id);

    Optional<MPA> getMPAByFilmId(Long filmId);
}
