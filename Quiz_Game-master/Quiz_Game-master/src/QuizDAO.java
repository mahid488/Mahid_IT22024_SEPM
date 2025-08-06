import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDAO {

    public boolean isIdAllowed(String playerId) {
        String sql = "SELECT COUNT(*) FROM allowed_ids WHERE player_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, playerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Question> getRandomQuestions(int count) {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM questions ORDER BY RAND() LIMIT ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                questions.add(new Question(
                        rs.getInt("id"),
                        rs.getString("question"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("option_d"),
                        rs.getString("correct_option")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public void savePlayer(String playerId, int score) {
        String sql = "INSERT INTO players (player_id, score) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE score = VALUES(score)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, playerId);
            ps.setInt(2, score);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
