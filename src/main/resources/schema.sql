DROP TABLE IF EXISTS users, films, likes, genres, mpa, user_friends, genre_films CASCADE;

-- Таблица пользователей
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    login VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(100),
    birthday DATE
);

-- Таблица рейтингов MPA
CREATE TABLE IF NOT EXISTS mpa (
    id SERIAL PRIMARY KEY,
    name VARCHAR(10) NOT NULL
);

-- Таблица фильмов
CREATE TABLE IF NOT EXISTS films (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(200),
    release_date DATE,
    duration INT NOT NULL CHECK (duration > 0),
    mpa_id INT REFERENCES mpa(id) ON DELETE SET NULL
);

-- Таблица лайков фильмов
CREATE TABLE IF NOT EXISTS likes (
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    film_id INT REFERENCES films(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, film_id)
);

-- Таблица жанров
CREATE TABLE IF NOT EXISTS genres (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Таблица друзей пользователей
CREATE TABLE IF NOT EXISTS user_friends (
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    friend_id INT REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, friend_id)
);

-- Таблица связки жанров и фильмов
CREATE TABLE IF NOT EXISTS genre_films (
    genre_id INT REFERENCES genres(id) ON DELETE CASCADE,
    film_id INT REFERENCES films(id) ON DELETE CASCADE,
    PRIMARY KEY (genre_id, film_id)
);
