-- Наполнение таблицы пользователей
INSERT INTO users (email, login, name, birthday) VALUES
('john@example.com', 'john_doe', 'John Doe', '1990-05-15'),
('alice@example.com', 'alice_wonder', 'Alice Wonderland', '1985-10-23'),
('bob@example.com', 'bob_smith', 'Bob Smith', '1993-07-08'),
('eva@example.com', 'eva_green', 'Eva Green', '2000-01-12');

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

-------
insert into films (name, description, release_date, duration, mpa_id) values ('Гладиатор', 'Исторический', '1922-08-20',
 180, 3);
insert into films (name, description, release_date, duration, mpa_id) values ('Гладиатор1', 'Исторический1', '1922-08-20',
 170, 2);
insert into films (name, description, release_date, duration, mpa_id) values ('Гладиатор2', 'Исторический2', '1922-08-20',
 160, 4);
insert into films (name, description, release_date, duration, mpa_id) values ('Гладиатор3', 'Исторический3', '1922-08-20',
  150, 5);
insert into films (name, description, release_date, duration, mpa_id) values ('Гладиатор4', 'Исторический5', '1922-08-20',
   140, 1);