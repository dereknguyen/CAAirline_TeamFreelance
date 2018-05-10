package Code;

import java.util.Date;

public class Trip {
	private Date date;
	private int destination;
	
	public Trip(Date d, int f) {
		date = d;
		destination = f;
	}
   public Date getDate(){
      return date;
   }
   public int getDest(){
      return destination;
   }
}
