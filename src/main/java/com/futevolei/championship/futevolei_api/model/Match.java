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
    @ManyToOne
    @JoinColumn(name = "championship_id")
    private Championship championship;




}
