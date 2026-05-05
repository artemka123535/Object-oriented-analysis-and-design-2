import java.util.List;

public class Main {
    public static void main(String[] args) {
        PlayerRepository repository = new PlayerRepository();
        PlayerConverter converter = new PlayerConverter();

        List<PlayerEntity> existingPlayers = repository.getAllPlayers();
        
        if (existingPlayers.isEmpty()) {
            System.out.println("Main: База пуста. Генерируем тестовые данные...");
            
            repository.savePlayer("ArtM_Malyutin", "TSU_Cyber");
            repository.savePlayer("Radmir_228", "Pro_Gamer_Team");

            List<PlayerEntity> newPlayers = repository.getAllPlayers();
            
            for (PlayerEntity p : newPlayers) {
                repository.saveMatch(p.getId(), 25, 10, "2026-04-27");
            }
            System.out.println("Main: Тестовые игроки и матчи добавлены.");
        }

        System.out.println("Main: Запуск графического интерфейса...");

        TournamentGui.start(repository, converter);
    }
}