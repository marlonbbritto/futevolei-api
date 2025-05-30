package com.futevolei.championship.futevolei_api.dto.championship;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.Native;
import java.time.LocalDate;

public record ChampionshipUpdateDto(
        String name,
        LocalDate startDate,
        String city
) {
}
