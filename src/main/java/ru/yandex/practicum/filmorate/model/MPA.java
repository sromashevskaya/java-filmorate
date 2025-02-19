package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class MPA {
    private Long id;
    private String name;

    public MPA() {} // Пустой конструктор

    public MPA(Long id, String name) {  // Добавляем нужный конструктор
        this.id = id;
        this.name = name;
    }
}
