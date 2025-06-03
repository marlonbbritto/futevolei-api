package com.futevolei.championship.futevolei_api.dto.match;

import com.futevolei.championship.futevolei_api.model.enums.MatchStatus;

public record MatchUpdateDto(
        MatchStatus matchStatus,
        Integer scoreTeam1,
        Integer scoreTeam2

) {
}
