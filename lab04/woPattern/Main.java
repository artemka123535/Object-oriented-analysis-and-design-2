public class Main {
    public static void main(String[] args) {
        PlayerRepository repository = new PlayerRepository();
        
        System.out.println("Запуск GUI без использования паттерна Converter...");
        TournamentGui.start(repository);
    }
}