package src;

import javafx.beans.property.SimpleStringProperty;

public class Report {
    Database db;

    //properties
    SimpleStringProperty FromString;
    SimpleStringProperty ToString;
    SimpleStringProperty DataString;

    public Report(String from, String to, String data) {
        db = SQL_Database.getInstance();
        this.FromString = new SimpleStringProperty(from);
        this.ToString = new SimpleStringProperty(to);
        this.DataString = new SimpleStringProperty(data);

    }
}
