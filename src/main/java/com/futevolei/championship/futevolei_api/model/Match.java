package com.futevolei.championship.futevolei_api.model;

import com.futevolei.championship.futevolei_api.model.enums.KeyType;
import com.futevolei.championship.futevolei_api.model.enums.MatchStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tb_match")
public class Match implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer round;
    @Enumerated(EnumType.STRING)
    private KeyType keyType;
    @Enumerated(EnumType.STRING)
    private MatchStatus matchStatus;
    Integer scoreTeam1;
    Integer scoreTeam2;
    @ManyToOne
    @JoinColumn(name = "championship_id")
    private Championship championship;
    @ManyToOne
    @JoinColumn(name = "team1_id")
    private Team team1;
    @ManyToOne
    @JoinColumn(name = "team2_id")
    private Team team2;
    @ManyToOne
    @JoinColumn(name = "winner_team_id", nullable = true)
    private Team winner;
    @ManyToOne
    @JoinColumn(name = "loser_team_id", nullable = true)
    private Team loser;

}
