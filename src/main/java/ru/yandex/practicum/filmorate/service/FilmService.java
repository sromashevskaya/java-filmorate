package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MPAStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private int id = 1;
    private final int maxDescriptionLength = 200;
    private final LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    private final GenreStorage genreStorage;
    private final MPAStorage mpaStorage;

    public Film createFilm(Film film) {
        log.info("Входящий JSON: {}", film);
        try {
            validate(film);

            MPA mpa = null;
            if (Objects.nonNull(film.getMpa()) && Objects.nonNull(film.getMpa().getId())) {
                mpa = mpaStorage.getMPA(film.getMpa().getId())
                        .orElseThrow(() -> new NotFoundException("Mpa с id " + film.getMpa().getId() + " не найден"));
            }

            Set<Genre> uniqueGenres = new HashSet<>();
            if (Objects.nonNull(film.getGenres())) {
                for (Genre genre : film.getGenres()) {
                    Genre validGenre = genreStorage.getGenre(genre.getId())
                            .orElseThrow(() -> new NotFoundException("Жанр с id " + genre.getId() + " не найден"));
                    uniqueGenres.add(validGenre);
                }
            }

            film.setId((long) id++);
            filmStorage.createFilm(film);
            film.setMpa(mpa);
            film.setGenres(uniqueGenres.stream()
                    .sorted(Comparator.comparing(Genre::getId))
                    .collect(Collectors.toList()));

            for (Genre genre : uniqueGenres) {
                filmStorage.createGenre(genre.getId(), film.getId());
            }

            log.info("Фильм успешно создан: {}", film);
            return film;

        } catch (Exception e) {
            log.error("Ошибка при создании фильма: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Film updateFilm(Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Укажите id фильма");
        }

        Optional.ofNullable(filmStorage.getFilm(film.getId()))
                .orElseThrow(() -> new NotFoundException("Фильм не найден " + film.getId()));

        validate(film);
        return filmStorage.updateFilm(film);
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

    public Optional<Film> getFilm(Long filmId) {
        Film film = filmStorage.getFilm(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с указанным id не найден " + filmId));

        MPA mpa = mpaStorage.getMPAByFilmId(filmId)
                .orElseThrow(() -> new NotFoundException("MPA для фильма с id " + filmId + " не найден"));

        film.setMpa(mpa);

        List<Genre> genres = genreStorage.getGenreByFilmId(filmId);

        if (genres.isEmpty()) {
            genres = new ArrayList<>();
        }

        film.setGenres(genres);

        return Optional.of(film);
    }

    public void likeFilm(Long id, Long userId) {
        filmStorage.getFilm(id)
                .orElseThrow(() -> new NotFoundException("Фильм с указанным id не найден " + id));
        userStorage.getUser(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным id не найден " + userId));

        filmStorage.likeFilm(id, userId);
    }

    public void deleteLikeFilm(Long id, Long userId) {
        filmStorage.getFilm(id)
                .orElseThrow(() -> new NotFoundException("Фильм с указанным id не найден " + id));
        userStorage.getUser(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным id не найден " + userId));

        filmStorage.deleteLikeFilm(id, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        Collection<Film> list = new ArrayList<>();
        filmStorage.getPopularFilms(count).forEach(film -> {
            list.add(film);
        });
        return (List<Film>) list;
    }
}
