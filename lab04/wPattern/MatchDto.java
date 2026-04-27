public class MatchDto {
    private String date;
    private int kills;
    private int deaths;
    private double matchKd;

    public MatchDto(String date, int kills, int deaths, double matchKd) {
        this.date = date;
        this.kills = kills;
        this.deaths = deaths;
        this.matchKd = matchKd;
    }

    public String getDate() { return date; }
    public int getKills() { return kills; }
    public int getDeaths() { return deaths; }
    public double getMatchKd() { return matchKd; }
}