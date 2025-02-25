package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class GenreDbStorage extends BaseDbStorage<Genre> implements GenreStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM genres ORDER BY id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";

    private static final String FIND_BY_FILM_ID_QUERY = """
            SELECT g.id, g.name
            FROM genre_films gf
            JOIN genres g ON gf.genre_id = g.id
            WHERE gf.film_id = ?
            ORDER BY g.id
            """;

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper, Genre.class);
    }

    @Override
    public Collection<Genre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Genre> getGenre(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<Genre> getGenreByFilmId(Long filmId) {
        return findMany(FIND_BY_FILM_ID_QUERY, filmId);
    }

    @Override
    public void addGenresToFilm(Long filmId, Set<Genre> genres) {
        String sql = "INSERT INTO genre_films (film_id, genre_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        for (Genre genre : genres) {
            jdbc.update(sql, filmId, genre.getId());
        }
    }

}
