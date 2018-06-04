package src;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Trip {
	private int TripId;
	private int FlightId;
	private Calendar Date;
	private Calendar RT_Date;
	private SimpleStringProperty DateString;
	private SimpleStringProperty RT_DateString;
	private SimpleStringProperty FromString;
	private SimpleStringProperty ToString;
	private SimpleDoubleProperty Price;
	private SimpleIntegerProperty Status;

	public Trip(int TripId, int FlightId, Calendar Date, SimpleDoubleProperty Price, SimpleIntegerProperty Status)
	{
		Format sdf = new SimpleDateFormat("E, dd MMM HH:mm:ss");
		SQL_Database db = SQL_Database.getInstance();
		this.TripId=TripId;
		this.FlightId=FlightId;
		this.Date=Date;
		this.RT_Date = null;
		this.DateString = new SimpleStringProperty(sdf.format(Date.getTime()));
		this.RT_DateString = null;
		this.FromString = new SimpleStringProperty(db.getFlightSrc(FlightId));
		this.ToString = new SimpleStringProperty(db.getFlightDest(FlightId));
		this.Price=Price;
		this.Status=Status;
	}

	public Trip(int TripId, int FlightId, Calendar Date, Calendar RT_Date,SimpleDoubleProperty Price, SimpleIntegerProperty Status)
	{
		Format sdf = new SimpleDateFormat("E, dd MMM HH:mm:ss");
		SQL_Database db = SQL_Database.getInstance();
		this.TripId=TripId;
		this.FlightId=FlightId;
		this.Date=Date;
		this.RT_Date = RT_Date;
		this.DateString = new SimpleStringProperty(sdf.format(Date.getTime()));
		this.RT_DateString = new SimpleStringProperty(((SimpleDateFormat) sdf).format(RT_Date.getTime()));
		this.FromString = new SimpleStringProperty(db.getFlightSrc(FlightId));
		this.ToString = new SimpleStringProperty(db.getFlightDest(FlightId));
		this.Price=Price;
		this.Status=Status;
	}

	public Calendar getDate() { return Date; }
	public String getDateString() { return DateString.get();}
	public String getFromString() { return FromString.get();}
	public String getToString() { return ToString.get();}
	public int getTripId() { return TripId; }
	public int getFlightId() { return FlightId; }
	public double getPrice() { return Price.get(); }
	public int getStatus() { return Status.get(); }
}
