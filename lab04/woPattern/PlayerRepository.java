import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerRepository {
    private static final String URL = "jdbc:sqlite:tournament.db";

    public PlayerRepository() {
        try (Connection conn = DriverManager.getConnection(URL); Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS players (id INTEGER PRIMARY KEY AUTOINCREMENT, nickname TEXT, team TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS matches (id INTEGER PRIMARY KEY AUTOINCREMENT, player_id INTEGER, kills INTEGER, deaths INTEGER, date TEXT)");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<PlayerEntity> getAllPlayers() {
        return searchPlayers("");
    }

    public List<PlayerEntity> searchPlayers(String nick) {
        List<PlayerEntity> list = new ArrayList<>();
        String sql = "SELECT * FROM players WHERE nickname LIKE ?";
        try (Connection conn = DriverManager.getConnection(URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + nick + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new PlayerEntity(rs.getLong("id"), rs.getString("nickname"), rs.getString("team")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public void savePlayer(String nickname, String team) {
        String sql = "INSERT INTO players(nickname, team) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nickname);
            pstmt.setString(2, team);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void updatePlayer(long id, String nickname, String team) {
        String sql = "UPDATE players SET nickname = ?, team = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nickname);
            pstmt.setString(2, team);
            pstmt.setLong(3, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void deletePlayer(long id) {
        try (Connection conn = DriverManager.getConnection(URL); PreparedStatement pstmt = conn.prepareStatement("DELETE FROM players WHERE id = ?")) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<MatchEntity> getMatchesByPlayer(long playerId) {
        List<MatchEntity> list = new ArrayList<>();
        String sql = "SELECT * FROM matches WHERE player_id = ?";
        try (Connection conn = DriverManager.getConnection(URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, playerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new MatchEntity(rs.getLong("id"), rs.getLong("player_id"), rs.getInt("kills"), rs.getInt("deaths"), rs.getString("date")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public void saveMatch(long playerId, int k, int d, String date) {
        String sql = "INSERT INTO matches(player_id, kills, deaths, date) VALUES(?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, playerId);
            pstmt.setInt(2, k);
            pstmt.setInt(3, d);
            pstmt.setString(4, date);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void delete() {
        try (Connection conn = DriverManager.getConnection(URL); Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM players");
            stmt.execute("DELETE FROM matches");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='players' OR name='matches'");
        } catch (SQLException e) { e.printStackTrace(); }
    }
}