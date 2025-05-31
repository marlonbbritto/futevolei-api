package com.futevolei.championship.futevolei_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.futevolei.championship.futevolei_api.model.enums.Registrations;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "tb_player")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "id")
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Registrations registrations = Registrations.TO_PAY ;
    private String name;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

}
