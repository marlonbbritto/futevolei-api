package com.futevolei.championship.futevolei_api.config;

import com.futevolei.championship.futevolei_api.model.Championship;
import com.futevolei.championship.futevolei_api.model.Match;
import com.futevolei.championship.futevolei_api.model.Player;
import com.futevolei.championship.futevolei_api.model.Team;
import com.futevolei.championship.futevolei_api.model.enums.KeyType;
import com.futevolei.championship.futevolei_api.model.enums.MatchStatus;
import com.futevolei.championship.futevolei_api.model.enums.Registrations;
import com.futevolei.championship.futevolei_api.repository.ChampionshipRepository;
import com.futevolei.championship.futevolei_api.repository.MatchRepository;
import com.futevolei.championship.futevolei_api.repository.PlayerRepository;
import com.futevolei.championship.futevolei_api.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@Profile("integration-test") // SÓ VAI RODAR QUANDO O PERFIL "integration-test" ESTIVER ATIVO
public class TestConfig implements CommandLineRunner {

    @Autowired
    private ChampionshipRepository championshipRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private MatchRepository matchRepository;

    @Override
    @Transactional // Garante que todas as operações rodem dentro da mesma transação
    public void run(String... args) throws Exception {
        System.out.println(">>>> EXECUTANDO TestConfig (perfil integration-test ATIVO) - Populando banco H2... <<<<");

        // O ddl-auto=create-drop já limpa o banco, mas se precisar, a ordem de deleteAll seria:
        // matchRepository.deleteAllInBatch(); // deleteAllInBatch pode ser mais eficiente
        // playerRepository.deleteAllInBatch(); // Se Player tem FK para Team, precisa de cuidado ou cascade
        // teamRepository.deleteAllInBatch();
        // championshipRepository.deleteAllInBatch();


        // 1. Criar Campeonatos (instâncias transientes inicialmente)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Championship champMaringaTransient = Championship.builder()
                .name("Copa Verão de Futevôlei - Maringá")
                .city("Maringá")
                .startDate(LocalDate.parse("15/01/2026", formatter))
                .numberOfTeams(8)
                .build();

        Championship champCuritibaTransient = Championship.builder()
                .name("Circuito Nacional - Etapa Curitiba")
                .city("Curitiba")
                .startDate(LocalDate.parse("20/03/2026", formatter))
                .numberOfTeams(16)
                .build();

        List<Championship> championshipsToSave = new ArrayList<>(Arrays.asList(champMaringaTransient, champCuritibaTransient));

        // Salva os campeonatos E OBTER AS INSTÂNCIAS GERENCIADAS
        // Neste ponto, esperamos que o championshipRepository seja a implementação real do Spring Data JPA,
        // pois este TestConfig só roda no perfil "integration-test", onde não devemos mockar os repositórios.
        List<Championship> savedChampionships = championshipRepository.saveAll(championshipsToSave);

        Championship c1Maringa = null;
        Championship c2Curitiba = null;

        if (savedChampionships.isEmpty() && !championshipsToSave.isEmpty()) {
            // Se a lista de salvos está vazia, mas tentamos salvar algo, é um problema GRANDE.
            // Isso não deveria acontecer se os repositórios são reais e o banco está funcionando.
            String errorMsg = "ERRO CRÍTICO no TestConfig: championshipRepository.saveAll() retornou uma lista vazia ao tentar salvar campeonatos. Verifique a configuração do banco e do JPA.";
            System.err.println(errorMsg);
            throw new IllegalStateException(errorMsg);
        }

        // Atribui as instâncias gerenciadas (com ID)
        // Filtrar pelo nome é uma forma mais robusta de pegar as instâncias corretas
        // caso a ordem de retorno do saveAll não seja estritamente garantida (embora geralmente seja).
        c1Maringa = savedChampionships.stream()
                .filter(c -> "Copa Verão de Futevôlei - Maringá".equals(c.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Campeonato de Maringá não encontrado na lista de salvos."));

        c2Curitiba = savedChampionships.stream()
                .filter(c -> "Circuito Nacional - Etapa Curitiba".equals(c.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Campeonato de Curitiba não encontrado na lista de salvos."));

        System.out.println("Campeonatos populados:");
        System.out.println(" - ID: " + c1Maringa.getId() + ", Nome: " + c1Maringa.getName());
        System.out.println(" - ID: " + c2Curitiba.getId() + ", Nome: " + c2Curitiba.getName());


        // 2. Criar Jogadores
        Player p1 = Player.builder().name("Marlon Britto").registrations(Registrations.PAID).build();
        Player p2 = Player.builder().name("Gustavo Guanabara").registrations(Registrations.PAID).build();
        Player p3 = Player.builder().name("Maria Silva").registrations(Registrations.TO_PAY).build();
        Player p4 = Player.builder().name("João Souza").registrations(Registrations.PAID).build();
        Player p5 = Player.builder().name("Ana Costa").registrations(Registrations.PAID).build();
        Player p6 = Player.builder().name("Carlos Lima").registrations(Registrations.TO_PAY).build();
        Player p7 = Player.builder().name("Beatriz Ferreira").registrations(Registrations.PAID).build();
        Player p8 = Player.builder().name("Pedro Santos").registrations(Registrations.PAID).build();

        // Salva os jogadores para obter IDs (eles ainda não estão em times)
        // É importante salvar antes de associar a times se Team não tiver cascade para Player.
        playerRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8));


        // 3. Criar Times para o Campeonato C1 (Maringá)
        Team t1_c1 = Team.builder().name("Os Incríveis").championship(c1Maringa).build(); // Associa com o c1Maringa gerenciado
        t1_c1.getPlayers().addAll(Arrays.asList(p1, p2));
        p1.setTeam(t1_c1); // Associa o time ao jogador
        p2.setTeam(t1_c1);

        Team t2_c1 = Team.builder().name("Dupla Dinâmica").championship(c1Maringa).build();
        t2_c1.getPlayers().addAll(Arrays.asList(p3, p4));
        p3.setTeam(t2_c1);
        p4.setTeam(t2_c1);

        // Adiciona os times à lista do campeonato (para o relacionamento bidirecional)
        // e para que o CascadeType.ALL do Championship funcione se salvarmos o Championship novamente.
        c1Maringa.getTeams().addAll(Arrays.asList(t1_c1, t2_c1));

        // Salva os times (já estão associados ao campeonato gerenciado c1Maringa)
        teamRepository.saveAll(Arrays.asList(t1_c1, t2_c1));
        // Re-salva os jogadores para persistir a FK team_id
        playerRepository.saveAll(Arrays.asList(p1, p2, p3, p4));


        // 4. Criar Times para o Campeonato C2 (Curitiba)
        Team t1_c2 = Team.builder().name("Feras do Futevôlei").championship(c2Curitiba).build();
        t1_c2.getPlayers().addAll(Arrays.asList(p5, p6));
        p5.setTeam(t1_c2);
        p6.setTeam(t1_c2);

        Team t2_c2 = Team.builder().name("Pés de Ouro").championship(c2Curitiba).build();
        t2_c2.getPlayers().addAll(Arrays.asList(p7, p8));
        p7.setTeam(t2_c2);
        p8.setTeam(t2_c2);

        c2Curitiba.getTeams().addAll(Arrays.asList(t1_c2, t2_c2));
        teamRepository.saveAll(Arrays.asList(t1_c2, t2_c2));
        playerRepository.saveAll(Arrays.asList(p5, p6, p7, p8));


        // 5. Criar Partidas para o Campeonato C1 (Maringá)
        // Lembre-se que Match ainda não tem team1 e team2. Quando tiver, associe os times aqui.
        Match m1_c1 = Match.builder()
                .round(1)
                .keyType(KeyType.WINNERS)
                .matchStatus(MatchStatus.SCHEDULED)
                .championship(c1Maringa) // c1Maringa já é gerenciado
                // .team1(t1_c1) // Exemplo se Match tivesse referência a Team (t1_c1 já foi salvo)
                // .team2(t2_c1) // Exemplo (t2_c1 já foi salvo)
                .scoreTeam1(0)
                .scoreTeam2(0)
                .build();

        Match m2_c1 = Match.builder()
                .round(1)
                .keyType(KeyType.WINNERS)
                .matchStatus(MatchStatus.COMPLETED)
                .scoreTeam1(21)
                .scoreTeam2(18)
                .championship(c1Maringa)
                // .team1(algumOutroTimeDeC1)
                // .team2(algumOutroTimeDeC1)
                .build();

        c1Maringa.getMatches().addAll(Arrays.asList(m1_c1, m2_c1));
        // Salvar as partidas explicitamente. Como c1Maringa é gerenciado, não deve haver TransientObjectException.
        matchRepository.saveAll(Arrays.asList(m1_c1, m2_c1));

        // Adicionar partidas para c2Curitiba se necessário
        // Exemplo:
        // Match m1_c2 = Match.builder().round(1).championship(c2Curitiba)...build();
        // c2Curitiba.getMatches().add(m1_c2);
        // matchRepository.save(m1_c2);


        // 6. Opcional: Salvar os campeonatos novamente se você quiser que o CascadeType.ALL
        // do Championship (para as listas de teams e matches) seja o responsável final
        // por persistir essas associações, caso não tenha feito saves explícitos de teams e matches.
        // Com @Transactional, as modificações nas listas de c1Maringa e c2Curitiba (que são gerenciados)
        // devem ser persistidas ao final da transação.
        // championshipRepository.saveAll(Arrays.asList(c1Maringa, c2Curitiba)); // Geralmente não é necessário se já salvou os filhos explicitamente
        // ou se as alterações nas listas são em objetos já gerenciados
        // e o @Transactional está ativo.

        System.out.println(">>>> TestConfig: Finalizada a população de dados para o perfil 'integration-test'. <<<<");
    }
}