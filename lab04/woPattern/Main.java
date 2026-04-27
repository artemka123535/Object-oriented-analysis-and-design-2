public class Main {
    public static void main(String[] args) {
        PlayerRepository repository = new PlayerRepository();
        
        TournamentGui.start(repository);
    }
}