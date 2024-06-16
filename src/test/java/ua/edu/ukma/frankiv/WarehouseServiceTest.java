package ua.edu.ukma.frankiv;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.*;

public class WarehouseServiceTest {
    private Connection connection;
    private WarehouseService service;

    @Before
    public void setUp() throws SQLException {
        connection = DatabaseConnection.getConnection();
        service = new WarehouseService(connection);

        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tables = metaData.getTables(null, null, "goods", null);
        if (!tables.next()) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE goods (id SERIAL PRIMARY KEY, name TEXT, quantity INTEGER)");
        } else {
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM goods");
        }
    }

    @After
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testWarehouseService() throws SQLException {
        // Test createGood
        Good good = service.createGood(1, "Test Good", 10);
        assertEquals(1, good.getId());
        assertEquals("Test Good", good.getName());
        assertEquals(10, good.getQuantity());

        // Test readGood
        Good readGood = service.readGood(1);
        assertEquals(good, readGood);

        // Test updateGood
        Good updatedGood = service.updateGood(1, "Updated Good", 20);
        assertEquals(1, updatedGood.getId());
        assertEquals("Updated Good", updatedGood.getName());
        assertEquals(20, updatedGood.getQuantity());

        // Test deleteGood
        service.deleteGood(1);
        assertNull(service.readGood(1));

        // Test listGoodsByCriteria
        service.createGood(2, "Test Good", 10);
        service.createGood(3, "Test Good", 20);
        assertEquals(2, service.listGoodsByCriteria("Test Good"));
    }
}