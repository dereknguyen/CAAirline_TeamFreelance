package src;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Trip {
	/* Fields */
	private int TripId;
	private int FlightId;
	private Calendar Date;
	private Calendar RTDate;
	private double Price;
	private int Status;

	/* Properties (used to populate tables) */
	private SimpleStringProperty DateString;
	private SimpleStringProperty RTDateString;
	private SimpleStringProperty FromString;
	private SimpleStringProperty ToString;
	private SimpleDoubleProperty PriceString;
	private SimpleStringProperty StatusString;

	public Trip(int TripId, int FlightId, Calendar Date, double Price, int Status)
	{
		Format sdf = new SimpleDateFormat("E, dd MMM HH:mm:ss z yyyy");
		SQL_Database db = SQL_Database.getInstance();
		this.TripId=TripId;
		this.FlightId=FlightId;
		this.Date=Date;
		this.RTDate = null;
		this.Price = Price;
		this.Status = Status;

		this.DateString = new SimpleStringProperty(sdf.format(Date.getTime()));
		this.RTDateString = null;
		this.FromString = new SimpleStringProperty(db.getFlightSrc(FlightId));
		this.ToString = new SimpleStringProperty(db.getFlightDest(FlightId));
		this.PriceString= new SimpleDoubleProperty(Price);
		if (Status == 0)
		{
			this.StatusString = new ReadOnlyStringWrapper("On-time");
		}
		else if (Status == 1)
		{
			this.StatusString = new ReadOnlyStringWrapper("Delayed");
		}
		else if (Status == 2)
		{
			this.StatusString = new ReadOnlyStringWrapper("Cancelled");
		}
		else
		{
			this.StatusString = new ReadOnlyStringWrapper("ERR");
		}

	}

	public Trip(int TripId, int FlightId, Calendar Date, Calendar RTDate, double Price, int Status)
	{
		Format sdf = new SimpleDateFormat("E, dd MMM HH:mm:ss z yyyy");
		SQL_Database db = SQL_Database.getInstance();
		this.TripId=TripId;
		this.FlightId=FlightId;
		this.Date=Date;
		this.RTDate = RTDate;
		this.Price = Price;
		this.Status = Status;

		this.DateString = new SimpleStringProperty(sdf.format(Date.getTime()));
		this.RTDateString = new SimpleStringProperty(((SimpleDateFormat) sdf).format(RTDate.getTime()));
		this.FromString = new SimpleStringProperty(db.getFlightSrc(FlightId));
		this.ToString = new SimpleStringProperty(db.getFlightDest(FlightId));
		this.PriceString= new SimpleDoubleProperty(Price);
		if (Status == 0)
		{
			this.StatusString = new ReadOnlyStringWrapper("On-time");
		}
		else if (Status == 1)
		{
			this.StatusString = new ReadOnlyStringWrapper("Delayed");
		}
		else if (Status == 2)
		{
			this.StatusString = new ReadOnlyStringWrapper("Cancelled");
		}
		else
		{
			this.StatusString = new ReadOnlyStringWrapper("ERR");
		}

	}

	public int getTripId() { return TripId; }
	public int getFlightId() { return FlightId; }
	public Calendar getDate() { return Date; }
	public Calendar getRTDate() { return RTDate; }
	public double getPrice() { return Price; }
	public int getStatus() { return Status; }

	public String getDateString() { return DateString.get();}
	public String getRTDateString() { return RTDateString.get();}
	public String getFromString() { return FromString.get();}
	public String getToString() { return ToString.get();}
	public double getPriceString() { return PriceString.get();}
	public String getStatusString() { return StatusString.get(); }
}
