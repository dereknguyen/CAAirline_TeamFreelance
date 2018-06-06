package src;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.text.Format;
import java.text.SimpleDateFormat;

public class Ticket {
	private int TripId;
	private int SeatNumber;
	private int NumBags;
	private boolean CheckedIn;

	private SimpleIntegerProperty Id;
	private SimpleStringProperty FromString;
	private SimpleStringProperty ToString;
	private SimpleStringProperty DepartDateString;
	private SimpleStringProperty FlightStatus;
	private SimpleIntegerProperty NumberOfBags;
	private SimpleBooleanProperty CheckedInStatus;
	private SimpleIntegerProperty SeatNum;

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
        this.FlightStatus = new ReadOnlyStringWrapper(temp.getStatusString());
        this.NumberOfBags = new SimpleIntegerProperty(this.NumBags);
        this.CheckedInStatus = new SimpleBooleanProperty(this.CheckedIn);
        this.SeatNum = new SimpleIntegerProperty(this.SeatNumber);
    }

	public int getTripId() { return TripId; }
	public int getSeatNumber() { return SeatNumber; }
	public int getNumBags() { return NumBags; }
	public boolean getCheckedIn() { return CheckedIn; }

	public int getId() { return Id.get(); }
	public String getFromString() { return FromString.get(); }
	public String getToString() { return ToString.get(); }
	public String getDepartDateString() { return DepartDateString.get(); }
	public String getFlightStatus() { return FlightStatus.get(); }
	public int getNumberOfBags() { return NumberOfBags.get(); }
	public boolean getCheckedInStatus() { return CheckedInStatus.get(); }
	public int getSeatNum() { return SeatNum.get(); }
}
