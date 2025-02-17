package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.util.Collection;
import java.util.Optional;

@Repository
public class MPADbStorage extends BaseDbStorage<MPA> implements MPAStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa ORDER BY id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa WHERE id = ?";

    private static final String FIND_BY_FILM_ID_QUERY = """
            SELECT m.id, m.name
            FROM films f
            JOIN mpa m ON f.mpa_id = m.id
            WHERE f.id = ?
            """;

    public MPADbStorage(JdbcTemplate jdbc, RowMapper<MPA> mapper) {
        super(jdbc, mapper, MPA.class);
    }

    @Override
    public Collection<MPA> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<MPA> getMPA(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Optional<MPA> getMPAByFilmId(Long filmId) {
        return findOne(FIND_BY_FILM_ID_QUERY, filmId);
    }
}

/*
    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa WHERE id = ?";

    private static final String FIND_BY_FILM_ID_QUERY = """
            SELECT m.id, m.name
            FROM films as f  JOIN mpa m ON f.mpa_id = m.id WHERE f.id = ?
            """;

    public MpaDbStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper, Mpa.class);
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Mpa> getMpaById(int id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public Optional<Mpa> getMpaByFilmId(int filmId) {
        return findOne(FIND_BY_FILM_ID_QUERY, filmId);
    }
}

 */