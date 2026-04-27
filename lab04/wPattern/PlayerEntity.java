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
    
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setTeam(String team) { this.team = team; }
}