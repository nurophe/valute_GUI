package com.example.demo1;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Valute {
    @JacksonXmlProperty(isAttribute = true, localName = "ID")
    public String ID;
    @JacksonXmlProperty(localName = "NumCode")
    public int NumCode;
    @JacksonXmlProperty(localName = "CharCode")
    public String CharCode;
    @JacksonXmlProperty(localName = "Nominal")
    public int Nominal;
    @JacksonXmlProperty(localName = "Name")
    public String Name;
    @JacksonXmlProperty(localName = "Value")
    public String value;

    public Valute(){

    }

    public Valute(String ID, int numCode, String charCode, int nominal, String name, String value) {
        this.ID = ID;
        this.NumCode = numCode;
        this.CharCode = charCode;
        this.Nominal = nominal;
        this.Name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Valute{" +
                "id='" + ID + '\'' +
                ", numCode=" + NumCode +
                ", charCode='" + CharCode + '\'' +
                ", nominal=" + Nominal +
                ", name='" + Name + '\'' +
                ", value=" + value +
                '}';
    }

    public String getID() {
        return ID;
    }

    public int getNumCode() {
        return NumCode;
    }

    public String getCharCode() {
        return CharCode;
    }

    public int getNominal() {
        return Nominal;
    }

    public String getName() {
        return Name;
    }

    public String getValue() {
        return value;
    }
}