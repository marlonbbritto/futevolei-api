package com.futevolei.championship.futevolei_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_championships")
@Builder
public class Championship implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate startDate;
    private String city;
    private Integer numberOfTeams;
    @OneToMany(mappedBy = "championship", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Match> matches = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "championship", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Team> teams = new ArrayList<>();


}
