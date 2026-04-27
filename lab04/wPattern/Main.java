import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Инициализируем компоненты паттерна
        PlayerRepository repository = new PlayerRepository();
        PlayerConverter converter = new PlayerConverter();

        // 2. Наполняем базу начальными данными (только если она пуста)
        // Это поможет сразу увидеть работу конвертера и K/D
        List<PlayerEntity> existingPlayers = repository.getAllPlayers();
        
        if (existingPlayers.isEmpty()) {
            System.out.println("Main: База пуста. Генерируем тестовые данные...");
            
            // Добавляем игроков (ID генерируется базой, поэтому передаем null в конструктор не нужно, 
            // так как в репозитории метод savePlayer принимает только строки)
            repository.savePlayer("ArtM_Malyutin", "TSU_Cyber");
            repository.savePlayer("Radmir_228", "Pro_Gamer_Team");

            // Получаем их из базы, чтобы узнать ID для матчей
            List<PlayerEntity> newPlayers = repository.getAllPlayers();
            
            for (PlayerEntity p : newPlayers) {
                // Добавляем по одному матчу каждому игроку
                // saveMatch(playerId, kills, deaths, date)
                repository.saveMatch(p.getId(), 25, 10, "2026-04-27");
            }
            System.out.println("Main: Тестовые игроки и матчи добавлены.");
        }

        System.out.println("Main: Запуск графического интерфейса...");

        // 3. Запускаем GUI
        // Передаем зависимости (репозиторий и конвертер) в конструктор окна
        TournamentGui.start(repository, converter);
    }
}