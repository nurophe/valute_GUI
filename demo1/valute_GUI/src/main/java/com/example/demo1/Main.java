package com.example.demo1;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, ParseException, SQLException {
        Post post = new Post();
        String result = post.getResponse("https://www.cbr.ru/scripts/XML_daily.asp"); // запрос по ссылке
        ValCurs valCurs = deserializeFromXML(result); // десереализация данных
        System.out.println(valCurs);
        List<CurrencyExchange> currencyExchange = converterToCurrencyExchange(valCurs); // конвертация
//        System.out.println(currencyExchange);
        CurrencyExchangeRepositorySqliteImpl currExchange = new CurrencyExchangeRepositorySqliteImpl();
        for (CurrencyExchange currencyExchange2: currencyExchange){
            currExchange.insert(currencyExchange2);
        }

//        System.out.println(currExchange.findById(5));
//        System.out.println(currExchange.findAllByCode("AUD"));
//        System.out.println(currExchange.delete(1));
//        currExchange.deleteAll();

//        CurrencyExchange currencyExchangeNew = new CurrencyExchange(45.8756, 999,
//                "Австралийский доллар ПРОВЕРКА", "AUD",
//                new SimpleDateFormat("dd.MM.yyyy").parse("24.12.2022"));
//        currencyExchangeNew.setId(1);
//
//        System.out.println(currExchange.update(currencyExchangeNew));
//
//        System.out.println(currExchange.findAll());
    }

    public static List<CurrencyExchange> converterToCurrencyExchange(ValCurs valCurs) throws ParseException {
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

    public static ValCurs deserializeFromXML(String data) {
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
}