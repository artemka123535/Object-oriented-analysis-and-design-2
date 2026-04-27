import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerRepository {
    private static final String URL = "jdbc:sqlite:tournament.db";

    public PlayerRepository() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS players (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "nickname TEXT NOT NULL," +
                         "kills INTEGER," +
                         "deaths INTEGER" +
                         ");";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Ошибка БД: " + e.getMessage());
        }
    }

    public void delete() {
        String sqlDelete = "DELETE FROM players";
        String sqlReset = "DELETE FROM sqlite_sequence WHERE name='players'";
        try (Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sqlDelete);
            stmt.executeUpdate(sqlReset);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void save(PlayerEntity player) {
        String sql = "INSERT INTO players(nickname, kills, deaths) VALUES(?,?,?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, player.getNickname());
            pstmt.setInt(2, player.getTotalKills());
            pstmt.setInt(3, player.getTotalDeaths());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<PlayerEntity> getAllPlayers() {
        List<PlayerEntity> players = new ArrayList<>();
        String sql = "SELECT * FROM players";
        
        try (Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                players.add(new PlayerEntity(
                    (long) rs.getInt("id"),
                    rs.getString("nickname"),
                    rs.getInt("kills"),
                    rs.getInt("deaths")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return players;
    }   
}