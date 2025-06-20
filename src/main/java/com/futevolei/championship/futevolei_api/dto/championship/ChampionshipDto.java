package com.futevolei.championship.futevolei_api.dto.championship;

import com.futevolei.championship.futevolei_api.dto.team.TeamDto;
import com.futevolei.championship.futevolei_api.dto.team.TeamSummaryDto;

import java.time.LocalDate;
import java.util.List;

public record ChampionshipDto(
        Long id,
        String name,
        LocalDate startDate,
        String city,
        Integer numberOfTeams,
        List<TeamSummaryDto> teamSummaries
) {

}
