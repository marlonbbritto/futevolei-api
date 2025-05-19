package com.futevolei.championship.futevolei_api.dto.championship;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ChampionshipInsertDto(
        @NotBlank
        String name,
        @NotNull
        @FutureOrPresent
        LocalDate startDate,
        @NotBlank
        String city
) {
}
