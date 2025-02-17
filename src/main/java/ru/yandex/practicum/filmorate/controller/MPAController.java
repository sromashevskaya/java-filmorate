package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@AllArgsConstructor
public class MPAController {
    private final MPAService mpaService;

    @GetMapping
    public Collection<MPA> findAll() {
          return mpaService.findAll();
    }

    @GetMapping("/{id}")
    public MPA getMPA(@PathVariable("id") Long mpaId) {
        return mpaService.getMPA(mpaId);
    }
}
