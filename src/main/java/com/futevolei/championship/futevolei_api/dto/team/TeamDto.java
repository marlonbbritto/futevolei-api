package com.futevolei.championship.futevolei_api.dto.team;

import com.futevolei.championship.futevolei_api.model.Championship;
import com.futevolei.championship.futevolei_api.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public record TeamDto(
        Long id,
        String name,
        List<Player> players,
        Championship championship

) {
}
