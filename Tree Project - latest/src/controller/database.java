package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class database {
    String url = "jdbc:sqlserver://DESKTOP-Q81T6O9\\SQLEXPRESS;databaseName=projTree;encrypt=true;trustServerCertificate=true";
    String user = "sa";
    String pass = "12345";

    public void connectDB() {
        try {
            Connection connect = DriverManager.getConnection(url,user,pass);
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
