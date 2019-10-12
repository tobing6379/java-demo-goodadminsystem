package top.tobing.test;

import java.sql.*;

public class ConnTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?serverTimezone=UTC", "root", "root");
        Statement statement = connection.createStatement();
        String sql = "select * from user";
        ResultSet resultSet = statement.executeQuery(sql);
    }
}
