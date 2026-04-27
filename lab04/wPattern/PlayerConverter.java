import java.util.List;

public class PlayerConverter {
    public PlayerDto convertToDto(PlayerEntity player, List<MatchEntity> matches) {
        int totalKills = matches.stream().mapToInt(MatchEntity::getKills).sum();
        int totalDeaths = matches.stream().mapToInt(MatchEntity::getDeaths).sum();
        
        double overallKd = totalDeaths == 0 ? totalKills : (double) totalKills / totalDeaths;
        overallKd = Math.round(overallKd * 100.0) / 100.0;

        return new PlayerDto(player.getId(), player.getNickname(), player.getTeam(), overallKd);
    }

    public MatchDto convertToMatchDto(MatchEntity match) {
        double matchKd = match.getDeaths() == 0 ? match.getKills() : (double) match.getKills() / match.getDeaths();
        return new MatchDto(match.getDate(), match.getKills(), match.getDeaths(), Math.round(matchKd * 100.0) / 100.0);
    }
}