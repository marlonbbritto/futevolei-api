package com.futevolei.championship.futevolei_api.repository;

import com.futevolei.championship.futevolei_api.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Long> {
}
