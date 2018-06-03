package src;

import java.util.Calendar;

public class Trip {
	private int TripId;
	private int FlightId;
	private Calendar Date;
	private double Price;
	private int Status;

	public Trip(int TripId, int FlightId, Calendar Date, double Price, int Status)
	{
		this.TripId=TripId;
		this.FlightId=FlightId;
		this.Date=Date;
		this.Price=Price;
		this.Status=Status;
	}

	public Calendar getDate() { return Date; }

	public int getTripId() { return TripId; }
	public int getFlightId() { return FlightId; }
	public double getPrice() { return Price; }
}
