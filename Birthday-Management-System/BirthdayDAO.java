import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BirthdayDAO {

    public void addBirthday(Birthday b) {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO birthdays (name, birthdate) VALUES (?, ?)");
            ps.setString(1, b.getName());
            ps.setDate(2, Date.valueOf(b.getBirthdate()));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBirthday(int id) {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM birthdays WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Birthday> getAllBirthdays() {
        List<Birthday> list = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM birthdays ORDER BY MONTH(birthdate), DAY(birthdate)");
            while (rs.next()) {
                list.add(new Birthday(rs.getInt("id"), rs.getString("name"), rs.getDate("birthdate").toLocalDate()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Birthday> searchByNameOrMonth(String query) {
        List<Birthday> list = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM birthdays WHERE name LIKE ? OR MONTHNAME(birthdate) LIKE ?");
            ps.setString(1, "%" + query + "%");
            ps.setString(2, "%" + query + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Birthday(rs.getInt("id"), rs.getString("name"), rs.getDate("birthdate").toLocalDate()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Birthday> getTodaysBirthdays() {
        List<Birthday> list = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM birthdays WHERE MONTH(birthdate) = MONTH(CURDATE()) AND DAY(birthdate) = DAY(CURDATE())");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Birthday(rs.getInt("id"), rs.getString("name"), rs.getDate("birthdate").toLocalDate()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
