package db;

import model.GameRoundResult;
import model.TrafficNetwork;

import java.sql.*;

public class DbManager {

    private static final String URL = "jdbc:mysql://localhost:3306/traffic_game";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Ab2#*De#";

    public void init() {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {

            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS players (" +
                            "player_id INT AUTO_INCREMENT PRIMARY KEY," +
                            "name VARCHAR(100) NOT NULL" +
                            ")"
            );

            // IMPORTANT: player_id is now NULLABLE
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS game_rounds (" +
                            "round_id INT AUTO_INCREMENT PRIMARY KEY," +
                            "player_id INT NULL," +
                            "played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +

                            "cap_ab INT, cap_ac INT, cap_ad INT," +
                            "cap_be INT, cap_bf INT," +
                            "cap_ce INT, cap_cf INT," +
                            "cap_df INT," +
                            "cap_eg INT, cap_eh INT," +
                            "cap_fh INT," +
                            "cap_gt INT, cap_ht INT," +

                            "correct_max_flow INT NOT NULL," +
                            "player_answer INT NOT NULL," +
                            "result VARCHAR(10) NOT NULL," +

                            "FOREIGN KEY (player_id) REFERENCES players(player_id) " +
                            "ON DELETE SET NULL" +
                            ")"
            );

            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS algorithm_runs (" +
                            "run_id INT AUTO_INCREMENT PRIMARY KEY," +
                            "round_id INT NOT NULL," +
                            "algorithm VARCHAR(50) NOT NULL," +
                            "time_ms DOUBLE NOT NULL," +
                            "FOREIGN KEY (round_id) REFERENCES game_rounds(round_id) " +
                            "ON DELETE CASCADE" +
                            ")"
            );

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    // Create player only if a correct answer is given (call this ONLY on WIN)
    public int getOrCreatePlayerId(String name) {
        if (name == null || name.isBlank()) {
            return -1; // don't create anonymous players
        }

        try (Connection conn = getConnection()) {

            try (PreparedStatement ps =
                         conn.prepareStatement("SELECT player_id FROM players WHERE name = ?")) {
                ps.setString(1, name.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO players(name) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, name.trim());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public int saveGameRound(GameRoundResult result) {
        int roundId = -1;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO game_rounds(" +
                             "player_id," +
                             "cap_ab, cap_ac, cap_ad," +
                             "cap_be, cap_bf," +
                             "cap_ce, cap_cf," +
                             "cap_df," +
                             "cap_eg, cap_eh," +
                             "cap_fh," +
                             "cap_gt, cap_ht," +
                             "correct_max_flow, player_answer, result" +
                             ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            TrafficNetwork net = result.getNetwork();

            // If playerId not valid, store NULL so players table stays "correct-only"
            if (result.getPlayerId() > 0) {
                ps.setInt(1, result.getPlayerId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            ps.setInt(2, net.getCapAB());
            ps.setInt(3, net.getCapAC());
            ps.setInt(4, net.getCapAD());
            ps.setInt(5, net.getCapBE());
            ps.setInt(6, net.getCapBF());
            ps.setInt(7, net.getCapCE());
            ps.setInt(8, net.getCapCF());
            ps.setInt(9, net.getCapDF());
            ps.setInt(10, net.getCapEG());
            ps.setInt(11, net.getCapEH());
            ps.setInt(12, net.getCapFH());
            ps.setInt(13, net.getCapGT());
            ps.setInt(14, net.getCapHT());
            ps.setInt(15, result.getCorrectMaxFlow());
            ps.setInt(16, result.getPlayerAnswer());
            ps.setString(17, result.getResult());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) roundId = rs.getInt(1);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return roundId;
    }

    public void saveAlgorithmRun(int roundId, String algorithm, double timeMs) {
        if (roundId <= 0) return;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO algorithm_runs(round_id, algorithm, time_ms) VALUES (?,?,?)")) {
            ps.setInt(1, roundId);
            ps.setString(2, algorithm);
            ps.setDouble(3, timeMs);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
