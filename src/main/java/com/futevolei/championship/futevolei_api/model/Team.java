package com.futevolei.championship.futevolei_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_team")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @JsonIgnore
    @OneToMany (mappedBy = "team")
    @Builder.Default
    private List<Player> players = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "championship_id")
    @JsonIgnore
    private Championship championship;


}
