public class PlayerDto {
    private Long id;
    private String nickname;
    private String team;
    private double overallKd;

    public PlayerDto(Long id, String nickname, String team, double overallKd) {
        this.id = id;
        this.nickname = nickname;
        this.team = team;
        this.overallKd = overallKd;
    }

    public String getNickname() { return nickname; }
    public String getTeam() { return team; }
    public double getOverallKd() { return overallKd; }
}