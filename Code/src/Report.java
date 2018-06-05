package src;

public class Report {
    private int FlightId;
    private double Revenue;

    public Report(int FlightId, double Revenue) {
        this.FlightId = FlightId;
        this.Revenue = Revenue;
    }

    public String getToString() {
        SQL_Database db = SQL_Database.getInstance();
        return db.getFlightDest(FlightId);
    }
    public double getRevenue() { return Revenue; }
}
