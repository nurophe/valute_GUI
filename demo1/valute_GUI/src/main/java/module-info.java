module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires com.fasterxml.jackson.dataformat.xml;
    requires com.opencsv;
    requires com.fasterxml.jackson.databind;


    opens com.example.demo1 to javafx.fxml;
    exports com.example.demo1;
}