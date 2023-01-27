package org.report.repository;

import org.report.domain.Holiday;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataReader {

    private static final String URL = "jdbc:postgresql://localhost:5432/jasper";

    private static final String USERNAME = "postgres";

    private static final String PASSWORD = "postgres";

    private static final String DRIVER = "org.postgresql.Driver";

    private final Connection connection;

    public DataReader() {
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public ResultSet findAllToResultSet() throws SQLException {
        Statement statement = connection.createStatement();
        return statement
                .executeQuery("select country, name, date from tab_holiday where extract(year from date) = 2017 order by date;");
    }

    public List<Holiday> findAllToList() throws SQLException {
        ResultSet resultSet = findAllToResultSet();
        List<Holiday> list = new ArrayList<>();
        while (resultSet.next()) {
            Holiday holiday = new Holiday();
            holiday.setCountry(resultSet.getString("country"));
            holiday.setName(resultSet.getString("name"));
            holiday.setDate(resultSet.getDate("date"));
            list.add(holiday);
        }
        return list;
    }

    public List<Map<String, ?>> findAllToListOfMaps() throws SQLException {
        ResultSet resultSet = findAllToResultSet();
        List<Map<String, ?>> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(Map.of("country", resultSet.getString("country"),
                    "name", resultSet.getString("name"),
                    "date", resultSet.getDate("date")));
        }
        return list;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
