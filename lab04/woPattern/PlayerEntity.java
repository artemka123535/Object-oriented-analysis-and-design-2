import java.util.List;

public class PlayerEntity {
    private Long id;
    private String nickname;
    private String team;

    public PlayerEntity(Long id, String nickname, String team) {
        this.id = id;
        this.nickname = nickname;
        this.team = team;
    }

    public Long getId() { return id; }
    public String getNickname() { return nickname; }
    public String getTeam() { return team; }

    public double calculateOverallKd(List<MatchEntity> matches) {
        int totalKills = matches.stream().mapToInt(MatchEntity::getKills).sum();
        int totalDeaths = matches.stream().mapToInt(MatchEntity::getDeaths).sum();
        double kd = totalDeaths == 0 ? totalKills : (double) totalKills / totalDeaths;
        return Math.round(kd * 100.0) / 100.0;
    }
}