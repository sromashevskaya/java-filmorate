package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;

    private Set<Long> likes;

    public void addLike(Long id) {
        if (likes == null) {
            likes = new HashSet<>();
        }
        likes.add(id);
    }

    public void removeLike(Long id) {
        if (likes != null) {
            likes.remove(id);
        }
        likes.remove(id);
    }

    public Set<Long> getLikes() {
        if (likes == null) {
            return new HashSet<>();
        }
        return likes;
    }
}
