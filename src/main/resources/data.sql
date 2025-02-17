-- Наполнение таблицы пользователей
INSERT INTO users (login, name, email, birthday) VALUES
('john_doe', 'John Doe', 'john@example.com', '1990-05-15'),
('alice_wonder', 'Alice Wonderland', 'alice@example.com', '1985-10-23'),
('bob_smith', 'Bob Smith', 'bob@example.com', '1993-07-08'),
('eva_green', 'Eva Green', 'eva@example.com', '2000-01-12');

-- Наполнение таблицы рейтингов MPA
INSERT INTO mpa (name) VALUES
('G'), ('PG'), ('PG-13'), ('R'), ('NC-17');

-- Наполнение таблицы жанров
INSERT INTO genres (name) VALUES
('Комедия'), ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик');

-- Наполнение таблицы фильмов
INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES
('Inception', 'A mind-bending thriller.', '2010-07-16', 148, 3),
('The Matrix', 'A hacker discovers reality is a simulation.', '1999-03-31', 136, 4),
('The Godfather', 'The rise of a mafia family.', '1972-03-24', 175, 4),
('Titanic', 'A love story on the ill-fated ship.', '1997-12-19', 195, 2),
('Interstellar', 'A journey beyond space and time.', '2014-11-07', 169, 3);

-- Наполнение таблицы лайков
INSERT INTO likes (user_id, film_id) VALUES
(1, 1), (1, 2), (2, 1), (2, 3), (3, 4), (4, 5);

-- Наполнение таблицы связей жанров с фильмами
INSERT INTO genre_films (genre_id, film_id) VALUES
(1, 1), (5, 1), -- Inception (Action, Sci-Fi)
(1, 2), (5, 2), -- The Matrix (Action, Sci-Fi)
(3, 3), -- The Godfather (Drama)
(3, 4), (2, 4), -- Titanic (Drama, Comedy)
(5, 5); -- Interstellar (Sci-Fi)

-- Наполнение таблицы друзей пользователей
INSERT INTO user_friends (user_id, friend_id) VALUES
(1, 2), (1, 3), (2, 3), (3, 4), (4, 1);
