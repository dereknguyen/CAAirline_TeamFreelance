package Code;

import javax.xml.soap.Text;
import java.sql.Date;

public class Trip {
	private Date date;
	private int destination;
	private int FlightId;
	
	public Trip(Date d, int f) {
	    Database db = Text_Database.getInstance();
		date = d;
		destination = f;
		FlightId = db.getFlightId(f, d);
	}
   public Date getDate(){
      return date;
   }
   public int getDest(){
      return destination;
   }
   public int getFlightId() { return FlightId; }
}
