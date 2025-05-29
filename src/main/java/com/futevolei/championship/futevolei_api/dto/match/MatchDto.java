package com.futevolei.championship.futevolei_api.dto;

import com.futevolei.championship.futevolei_api.dto.championship.ChampionshipSummaryDto;
import com.futevolei.championship.futevolei_api.dto.team.TeamSummaryDto;
import com.futevolei.championship.futevolei_api.model.Team;
import com.futevolei.championship.futevolei_api.model.enums.KeyType;
import com.futevolei.championship.futevolei_api.model.enums.MatchStatus;

public record MatchDto(
        Long id,
        Integer round,
        KeyType keyType,
        MatchStatus matchStatus,
        Integer scoreTeam1,
        Integer scoreTeam2,
        ChampionshipSummaryDto championship,
        TeamSummaryDto team1,
        TeamSummaryDto team2,
        TeamSummaryDto winner,
        TeamSummaryDto loser
) {
}
