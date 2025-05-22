package com.futevolei.championship.futevolei_api.config;

import com.futevolei.championship.futevolei_api.model.Championship;
import com.futevolei.championship.futevolei_api.model.Match;
import com.futevolei.championship.futevolei_api.model.Player;
import com.futevolei.championship.futevolei_api.model.Team;
import com.futevolei.championship.futevolei_api.model.enums.KeyType;
import com.futevolei.championship.futevolei_api.model.enums.MatchStatus;
import com.futevolei.championship.futevolei_api.repository.ChampionshipRepository;
import com.futevolei.championship.futevolei_api.repository.MatchRepository; // Novo
import com.futevolei.championship.futevolei_api.repository.PlayerRepository; // Novo
import com.futevolei.championship.futevolei_api.repository.TeamRepository; // Novo
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays; // Para Arrays.asList
import java.util.List;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private ChampionshipRepository championshipRepository;
    @Autowired
    private TeamRepository teamRepository; // Injetar TeamRepository
    @Autowired
    private PlayerRepository playerRepository; // Injetar PlayerRepository
    @Autowired
    private MatchRepository matchRepository; // Injetar MatchRepository

    @Override
    public void run(String... args) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // 1. Apagar tudo antes de iniciar (para garantir um estado limpo a cada execução)
        // Isso é importante se você testar várias vezes ou não quiser que dados se acumulem
        matchRepository.deleteAll();
        playerRepository.deleteAll();
        teamRepository.deleteAll();
        championshipRepository.deleteAll();


        // 2. Criar Campeonatos
        Championship champMaringa = Championship.builder()
                .name("Copa Maringá de Futevolei 2025")
                .city("Maringá")
                .startDate(LocalDate.parse("10/06/2025", formatter))
                .numberOfTeams(8) // Adicione um número de times para lógica de chaveamento futura
                .build();

        Championship champNacional = Championship.builder()
                .name("Campeonato Nacional de Futevolei 2025")
                .city("Cuiabá")
                .startDate(LocalDate.parse("20/10/2025", formatter))
                .numberOfTeams(16)
                .build();

        Championship champLondrina = Championship.builder()
                .name("Liga Municipal de Futevolei 2025")
                .city("Londrina")
                .startDate(LocalDate.parse("18/12/2025", formatter))
                .numberOfTeams(4)
                .build();

        // Salvar campeonatos (sem teams e matches ainda)
        championshipRepository.saveAll(List.of(champMaringa, champNacional, champLondrina));

        // 3. Criar Jogadores
        Player player1 = Player.builder().name("João da Silva").build();
        Player player2 = Player.builder().name("Maria Souza").build();
        Player player3 = Player.builder().name("Pedro Santos").build();
        Player player4 = Player.builder().name("Ana Costa").build();
        Player player5 = Player.builder().name("Carlos Lima").build();
        Player player6 = Player.builder().name("Beatriz Ferreira").build();

        playerRepository.saveAll(List.of(player1, player2, player3, player4, player5, player6));


        // 4. Criar Times e Associá-los a Campeonatos e Jogadores
        // Time 1 para Copa Maringá
        Team teamAlpha = Team.builder().name("Dupla Alpha").championship(champMaringa).build();
        // Adicionar jogadores ao time (e vice-versa, para consistência bidirecional)
        teamAlpha.getPlayers().add(player1);
        player1.setTeam(teamAlpha);
        teamAlpha.getPlayers().add(player2);
        player2.setTeam(teamAlpha);


        // Time 2 para Copa Maringá
        Team teamBeta = Team.builder().name("Dupla Beta").championship(champMaringa).build();
        teamBeta.getPlayers().add(player3);
        player3.setTeam(teamBeta);
        teamBeta.getPlayers().add(player4);
        player4.setTeam(teamBeta);

        // Time para Campeonato Nacional (sem jogadores por enquanto, para mostrar flexibilidade)
        Team teamOmega = Team.builder().name("Dupla Omega").championship(champNacional).build();


        // Adicionar times ao campeonato (lado Championship - opcional se cascade estiver no Championship)
        // Isso é crucial para o Championship gerenciar a lista de Teams
        champMaringa.getTeams().addAll(List.of(teamAlpha, teamBeta));
        champNacional.getTeams().add(teamOmega);

        // Salvar times (se você não usar cascade=ALL do Championship para Team)
        // Se usar CascadeType.ALL em Championship para Team, salvar o campeonato já salva os times.
        // Se não usar, você precisa salvar os times explicitamente:
        teamRepository.saveAll(List.of(teamAlpha, teamBeta, teamOmega));
        // E salve os jogadores novamente para garantir que a associação com o time seja persistida
        playerRepository.saveAll(List.of(player1, player2, player3, player4)); // Apenas os que foram associados a times


        // 5. Criar Partidas e Associá-las a Campeonatos e Times
        // Partida 1 para Copa Maringá
        Match match1 = Match.builder()
                .round(1)
                .keyType(KeyType.WINNERS)
                .matchStatus(MatchStatus.SCHEDULED)
                .championship(champMaringa) // Liga à instância do Championship
                .scoreTeam1(0) // Sem placar inicial
                .scoreTeam2(0)
                // Você precisará associar os times aqui quando tiver o relacionamento na Match
                // team1 e team2 na Match
                .build();

        // Partida 2 para Copa Maringá (exemplo de partida concluída)
        Match match2 = Match.builder()
                .round(1)
                .keyType(KeyType.WINNERS)
                .matchStatus(MatchStatus.COMPLETED)
                .championship(champMaringa)
                .scoreTeam1(21)
                .scoreTeam2(18)
                .build();


        // Adicionar partidas ao campeonato (lado Championship - opcional se cascade estiver no Championship)
        champMaringa.getMatches().addAll(List.of(match1, match2));
        // Salvar partidas (se você não usar cascade=ALL do Championship para Match)
        matchRepository.saveAll(List.of(match1, match2));


        // Atualizar/Salvar os Campeonatos novamente para persistir os relacionamentos (se os cascades não forem ALL)
        // Se cascade = CascadeType.ALL estiver no Championship para Match e Team,
        // um único saveAll no final para os championships já seria suficiente.
        // championshipRepository.saveAll(List.of(champMaringa, champNacional, champLondrina));
        // No seu caso, como você tem CascadeType.ALL, essa linha final é o que efetivamente persistiria tudo de forma cascata.
        // Mas como você já salvou os elementos individualmente, este passo final é mais para garantir que as listas
        // de Championship.matches e Championship.teams estejam preenchidas no contexto de persistência.
        // championshipRepository.save(champMaringa);
        // championshipRepository.save(champNacional);
        // championshipRepository.save(champLondrina);



    }
}