package com.example.demo1;

import org.sqlite.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    protected static Database instance = null;
    protected Connection connection;

    public void initDb() throws SQLException {
        Statement stmt = connection.createStatement();
        String sql = "CREATE TABLE if not exists CurrencyExchange " +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " value REAL    NOT NULL, " +
                " nominal INT NOT NULL, " +
                " currency_name VARCHAR(50), " +
                " currency_code VARCHAR(3)," +
                " date Date)";
        stmt.executeUpdate(sql);
        stmt.close();
    }

    protected Database() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        this.connection = DriverManager.getConnection("jdbc:sqlite:currencies.db");
        initDb();
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

}
