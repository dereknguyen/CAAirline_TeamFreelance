package src;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Report {
    private int FlightId;
    private double Revenue;

    /* Properties */
    private SimpleStringProperty ToString;
    private SimpleDoubleProperty RevenueString;

    public Report(int FlightId, double Revenue) {
        this.FlightId = FlightId;
        this.Revenue = Revenue;
    }

    public void completeInfo()
    {
        SQL_Database db = SQL_Database.getInstance();
        this.ToString = new SimpleStringProperty(db.getFlightDest(FlightId));
        this.RevenueString = new SimpleDoubleProperty(Revenue);
    }
}
