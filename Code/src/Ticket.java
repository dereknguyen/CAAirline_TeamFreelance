package src;

import java.util.Calendar;

public class Ticket {
	/*private Calendar date;
	private String trip;
	private int ticketNumber;
	private int planeNumber;
	
	public Ticket(Calendar d, String t, int ticketNum, int planeNum) {
		date = d;
		trip = t;
		ticketNumber = ticketNum;
		planeNumber = planeNum;
	}
	
	public Calendar getDate() {
		return date;
	}
	
	public String getTrip() {
		return trip;
	}
	
	public int getTicketNumber() {
		return ticketNumber;
	}
	
	public int getPlaneNumber() {
		return planeNumber;
	}*/
	private int TripId;
	private int SeatNumber;
	private int NumBags;
	private boolean CheckedIn;

	public Ticket(int TripId, int SeatNumber, int NumBags, boolean CheckedIn)
	{
		this.TripId = TripId;
		this.SeatNumber = SeatNumber;
		this.NumBags = NumBags;
		this.CheckedIn = CheckedIn;
	}

	public int getTripId() { return TripId; }
	public int getSeatNumber() { return SeatNumber; }
	public int getNumBags() { return NumBags; }
	public boolean getCheckedIn() { return CheckedIn; }
}
