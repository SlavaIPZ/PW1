package ua.edu.ukma.frankiv;
import java.sql.*;

public class WarehouseService {
    private Connection connection;

    public WarehouseService(Connection connection) {
        this.connection = connection;
    }

    public Good createGood(int id, String name, int quantity) throws SQLException {
        String sql = "INSERT INTO goods (id, name, quantity) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.setString(2, name);
        statement.setInt(3, quantity);
        statement.executeUpdate();
        return new Good(id, name, quantity);
    }

    public Good readGood(int id) throws SQLException {
        String sql = "SELECT * FROM goods WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            String name = resultSet.getString("name");
            int quantity = resultSet.getInt("quantity");
            return new Good(id, name, quantity);
        }
        return null;
    }

    public Good updateGood(int id, String name, int quantity) throws SQLException {
        String sql = "UPDATE goods SET name = ?, quantity = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.setInt(2, quantity);
        statement.setInt(3, id);
        statement.executeUpdate();
        return new Good(id, name, quantity);
    }

    public void deleteGood(int id) throws SQLException {
        String sql = "DELETE FROM goods WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.executeUpdate();
    }

    public int listGoodsByCriteria(String name) throws SQLException {
        String sql = "SELECT * FROM goods WHERE name = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        ResultSet resultSet = statement.executeQuery();

        int rowCount = 0;
        while (resultSet.next()) {
            rowCount++;
        }
        return rowCount;
    }
}