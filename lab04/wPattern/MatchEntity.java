public class MatchEntity {
    private Long id;
    private Long playerId;
    private int kills;
    private int deaths;
    private String date;

    public MatchEntity(Long id, Long playerId, int kills, int deaths, String date) {
        this.id = id;
        this.playerId = playerId;
        this.kills = kills;
        this.deaths = deaths;
        this.date = date;
    }

    public int getKills() { return kills; }
    public int getDeaths() { return deaths; }
    public String getDate() { return date; }
}