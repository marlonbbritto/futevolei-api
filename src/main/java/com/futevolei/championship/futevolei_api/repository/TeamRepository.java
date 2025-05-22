package com.futevolei.championship.futevolei_api.repository;

import com.futevolei.championship.futevolei_api.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team,Long> {
}
