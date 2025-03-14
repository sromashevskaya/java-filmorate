package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MPAService {
    private final MPAStorage mpaStorage;

    public Collection<MPA> findAll() {
        return mpaStorage.findAll();
    }

    public MPA getMPA(Long mpaId) {
        Optional<MPA> mpa = mpaStorage.getMPA(mpaId);
        if (mpa.isEmpty()) {
            throw new NotFoundException("Рейтинг не найден");
        }

        return mpaStorage.getMPA(mpaId)
                .orElseThrow(() -> new NotFoundException("Рейтинг не найден"));
    }
}