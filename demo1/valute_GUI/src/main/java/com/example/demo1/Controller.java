package com.example.demo1;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.opencsv.CSVWriter;
import java.io.FileWriter;

public class Controller {

    Post post = new Post();
    CurrencyExchangeRepositorySqliteImpl currExchange = new CurrencyExchangeRepositorySqliteImpl();
    ObservableList<CurrencyExchange> currencyExchangesToTable = FXCollections.observableArrayList();

    @FXML
    private Button updateButton;

    @FXML
    private TableView<CurrencyExchange> currencyExchangeTableView;

    @FXML
    private TableColumn<CurrencyExchange, Integer> id;

    @FXML
    private TableColumn<CurrencyExchange, String> code;

    @FXML
    private TableColumn<CurrencyExchange, String> name;

    @FXML
    private TableColumn<CurrencyExchange, Integer> nominal;

    @FXML
    private TableColumn<CurrencyExchange, Double> value;

    @FXML
    private TableColumn<CurrencyExchange, Date> date;

    @FXML
    private ResourceBundle resources;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu file;

    @FXML
    private MenuItem toCSV;

    @FXML
    private MenuItem toJSON;

    @FXML
    private MenuItem exit;

    @FXML
    private URL location;

    public Controller() throws SQLException {
    }

    @FXML
    void initialize() {
        updateButton.setOnAction(actionEvent -> {
            try {
                String result = post.getResponse("https://www.cbr.ru/scripts/XML_daily.asp"); // запрос по ссылке
                ValCurs valCurs = deserializeFromXML(result); // десереализация данных
                List<CurrencyExchange> currencyExchange = converterToCurrencyExchange(valCurs);

                currExchange.deleteAll();
                for (CurrencyExchange currencyExchange2: currencyExchange){
                    currExchange.insert(currencyExchange2);
                }

                currencyExchangesToTable.clear();
                List<CurrencyExchange> currencyExchangesFromDB = currExchange.findAll();
                currencyExchangesToTable.addAll(currencyExchangesFromDB);

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        });

        toCSV.setOnAction(actionEvent -> {
            List<String[]> csvData = null;
            try {
                csvData = createCsvDataSimple();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try (CSVWriter writer = new CSVWriter(new FileWriter("currencies.csv"))) {
                writer.writeAll(csvData);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        toJSON.setOnAction(actionEvent -> {
            File f = new File("currencies.json");
            ObjectMapper mapper = new ObjectMapper();
            List<CurrencyExchange> curr = currExchange.findAll();
            try {
                mapper.writeValue(f, curr);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        value.setCellValueFactory(new PropertyValueFactory<>("value"));
        nominal.setCellValueFactory(new PropertyValueFactory<>("nominal"));
        name.setCellValueFactory(new PropertyValueFactory<>("currencyName"));
        code.setCellValueFactory(new PropertyValueFactory<>("currencyCode"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));

        currencyExchangeTableView.setItems(currencyExchangesToTable);
    }

    public List<String[]> createCsvDataSimple() throws SQLException {
        String[] header = {"id", "value", "nominal", "currencyName", "currencyCode", "date"};
        List<String[]> list = new ArrayList<>();
        list.add(header);

        List<CurrencyExchange> currencyExchangeList = currExchange.findAll();

        for (CurrencyExchange curr: currencyExchangeList){
            String[] info = {String.valueOf(curr.getId()), String.valueOf(curr.getValue()),
                    String.valueOf(curr.getNominal()), curr.getCurrencyName(), curr.getCurrencyCode(),
                    String.valueOf(curr.getDate())};
            list.add(info);
        }
        return list;
    }

    public ValCurs deserializeFromXML(String data) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            System.out.println("Проверка");
            ValCurs deserializedData = xmlMapper.readValue(data, ValCurs.class);
            return deserializedData;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("check");
            return null;
        }
    }

    public List<CurrencyExchange> converterToCurrencyExchange(ValCurs valCurs) throws ParseException {
        List<CurrencyExchange> currencyExchangeList = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Date date = format.parse(valCurs.getDate());
        String nameValCurs = valCurs.getName();

        for(Valute valute: valCurs.getValuteList()){
            String id = valute.getID();
            int numCode = valute.getNumCode();
            String charCode = valute.getCharCode();
            int nominal = valute.getNominal();
            String name = valute.getName();
            double value = Double.parseDouble(valute.getValue().replace(",", ".")); // "123.543252"

            CurrencyExchange currencyExchange = new CurrencyExchange(-1, value, nominal, name, charCode, date);
            currencyExchangeList.add(currencyExchange);
        }

        return currencyExchangeList;

    }

}
