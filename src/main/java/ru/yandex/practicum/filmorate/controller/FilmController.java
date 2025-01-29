package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable("id") Long id) {
        filmService.deleteFilm(id);
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable Long filmId) {
        return filmService.getFilm(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable Long id, @PathVariable Long userId) throws NotFoundException {
        filmService.likeFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable Long id, @PathVariable Long userId) throws NotFoundException {
        filmService.deleteLikeFilm(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false) Integer count) {
        return filmService.getPopularFilms(count);
    }
}