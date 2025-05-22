package com.futevolei.championship.futevolei_api.repository;

import com.futevolei.championship.futevolei_api.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match,Long> {
}
