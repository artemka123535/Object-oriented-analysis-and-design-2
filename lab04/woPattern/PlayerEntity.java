public class PlayerEntity {
    private Long id;
    private String nickname;
    private int totalKills;
    private int totalDeaths;

    public PlayerEntity(Long id, String nickname, int totalKills, int totalDeaths) {
        this.id = id;
        this.nickname = nickname;
        this.totalKills = totalKills;
        this.totalDeaths = totalDeaths;
    }

    public String getNickname() { return nickname; }
    public int getTotalKills() { return totalKills; }
    public int getTotalDeaths() { return totalDeaths; }
    public Long getId() { return id; }

    public double getKdForDisplay() {
        double kd = totalDeaths == 0 ? totalKills : (double) totalKills / totalDeaths;
        return Math.round(kd * 100.0) / 100.0;
    }
}