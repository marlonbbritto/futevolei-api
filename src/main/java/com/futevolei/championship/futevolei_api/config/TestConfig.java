package com.futevolei.championship.futevolei_api.config;

import com.futevolei.championship.futevolei_api.model.Championship;
import com.futevolei.championship.futevolei_api.repository.ChampionshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {
    @Autowired
    private ChampionshipRepository championshipRepository;

    @Override
    public void run(String... args) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Championship championship1 = Championship.builder()
                .name("Copa Maringá de Futevolei")
                .city("Maringá")
                .startDate(LocalDate.parse("10/06/2025",formatter))
                .build();

        Championship championship2 = Championship.builder()
                .name("Campeonato Nacional de Futevolei 2025")
                .city("Cuiabá")
                .startDate(LocalDate.parse("20/10/2025", formatter))
                .build();

        Championship championship3 = Championship.builder()
                .name("Liga municipal de Futevolei 2025")
                .city("Londrina")
                .startDate(LocalDate.parse("18/12/2025",formatter))
                .build();

    }
}


