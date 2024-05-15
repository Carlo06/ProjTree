package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class database {
    private String url = "jdbc:sqlserver://DESKTOP-Q81T6O9\\SQLEXPRESS;databaseName=projTree;integratedSecurity=true;encrypt=true;trustServerCertificate=true";

    public void connectDB() {
        try {
            Connection connect = DriverManager.getConnection(url);
            System.out.println("Connection successful");
        } catch (SQLException e) {
            System.out.println("Oops, there's an error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        database db = new database();
        db.connectDB();
    }
}
