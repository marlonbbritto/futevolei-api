package com.futevolei.championship.futevolei_api.repository;

import com.futevolei.championship.futevolei_api.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player,Long> {
}
