package src;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.text.Format;
import java.text.SimpleDateFormat;

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

	private SimpleIntegerProperty Id;
	private SimpleStringProperty FromString;
	private SimpleStringProperty ToString;
	private SimpleStringProperty DepartDateString;
	private SimpleIntegerProperty FlightStatus;
	private SimpleIntegerProperty NumberOfBags;
	private SimpleBooleanProperty CheckedInStatus;

	public Ticket(int TripId, int SeatNumber, int NumBags, boolean CheckedIn)
	{
		this.TripId = TripId;
		this.SeatNumber = SeatNumber;
		this.NumBags = NumBags;
		this.CheckedIn = CheckedIn;
	}

	public void completeInfo(int TripID) {
        Format sdf = new SimpleDateFormat("E, dd MMM HH:mm:ss z yyyy");
	    SQL_Database db = SQL_Database.getInstance();
	    Trip temp = db.getTripInfo(TripID);

	    this.Id = new SimpleIntegerProperty(this.TripId);
	    this.FromString = new SimpleStringProperty(temp.getFromString());
	    this.ToString = new SimpleStringProperty(temp.getToString());
	    this.DepartDateString = new SimpleStringProperty(sdf.format(temp.getDate().getTime()));
        this.FlightStatus = new SimpleIntegerProperty(temp.getStatus());
        this.NumberOfBags = new SimpleIntegerProperty(this.NumBags);
        this.CheckedInStatus = new SimpleBooleanProperty(this.CheckedIn);
    }

	public int getTripId() { return TripId; }
	public int getSeatNumber() { return SeatNumber; }
	public int getNumBags() { return NumBags; }
	public boolean getCheckedIn() { return CheckedIn; }

	public int getId() { return Id.get(); }
	public String getFromString() { return FromString.get(); }
	public String getToString() { return ToString.get(); }
	public String getDepartDateString() { return DepartDateString.get(); }
	public int getFlightStatus() { return FlightStatus.get(); }
	public int getNumberOfBags() { return NumberOfBags.get(); }
	public boolean getCheckedInStatus() { return CheckedInStatus.get(); }
}
