package com.futevolei.championship.futevolei_api.dto.championship;

import java.time.LocalDate;

public record ChampionshipDto(
        Long id,
        String name,
        LocalDate startDate,
        String city,
        Integer numberOfTeams
) {

}
