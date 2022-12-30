package com.example.demo1;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CurrencyExchangeRepositorySqliteImpl extends Database implements CurrencyExchangeRepository{


    protected CurrencyExchangeRepositorySqliteImpl() throws SQLException {
        super();
    }

    @Override
    public CurrencyExchange findById(int id) {
        try (Statement statement = this.connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM CurrencyExchange WHERE id = " + id);
            CurrencyExchange currencyExchange = new CurrencyExchange(
                        resultSet.getDouble("value"),
                        resultSet.getInt("nominal"),
                        resultSet.getString("currency_name"),
                        resultSet.getString("currency_code"),
                        resultSet.getDate("date"));

            return currencyExchange;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<CurrencyExchange> findAll() {
        try (Statement statement = this.connection.createStatement()) {
            List<CurrencyExchange> products = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM CurrencyExchange");
            while (resultSet.next()) {
                products.add(new CurrencyExchange(resultSet.getInt("id"),
                        resultSet.getDouble("value"),
                        resultSet.getInt("nominal"),
                        resultSet.getString("currency_name"),
                        resultSet.getString("currency_code"),
                        resultSet.getDate("date")));
            }
            return products;

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<CurrencyExchange> findAllByCode(String currencyCode) {
        try (Statement statement = this.connection.createStatement()) {
            List<CurrencyExchange> products = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM CurrencyExchange WHERE currency_code = '" + currencyCode + "'");
            while (resultSet.next()) {
                products.add(new CurrencyExchange(
                        resultSet.getInt("id"),
                        resultSet.getDouble("value"),
                        resultSet.getInt("nominal"),
                        resultSet.getString("currency_name"),
                        resultSet.getString("currency_code"),
                        resultSet.getDate("date")));
            }
            return products;

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public int delete(int id) {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "DELETE FROM CurrencyExchange WHERE id = ?")) {
            statement.setObject(1, id);
            statement.execute();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void deleteAll() {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "DELETE FROM CurrencyExchange")) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int insert(CurrencyExchange currency) {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "INSERT INTO CurrencyExchange(value, nominal, currency_name, currency_code, date) " +
                        "VALUES(?, ?, ?, ?, ?)")) {
            statement.setObject(1, currency.getValue());
            statement.setObject(2, currency.getNominal());
            statement.setObject(3, currency.getCurrencyName());
            statement.setObject(4, currency.getCurrencyCode());
            statement.setObject(5, currency.getDate());

            statement.execute();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int update(CurrencyExchange currency) {
        try (PreparedStatement statement = this.connection.prepareStatement("UPDATE CurrencyExchange " +
                "SET value=?, nominal=?, currency_name=?, currency_code=?, date=? WHERE id=?")) {
            statement.setObject(1, currency.getValue());
            statement.setObject(2, currency.getNominal());
            statement.setObject(3, currency.getCurrencyName());
            statement.setObject(4, currency.getCurrencyCode());
            statement.setObject(5, currency.getDate());
            statement.setObject(6, currency.getId());
            statement.executeUpdate();
            return 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}