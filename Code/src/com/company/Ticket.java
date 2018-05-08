package com.company;
import java.util.Date;


public class Ticket {
	private Date date;
	private String trip;
	private int ticketNumber;
	private int planeNumber;
	
	public Ticket(Date d, String t, int ticketNum, int planeNum) {
		date = d;
		trip = t;
		ticketNumber = ticketNum;
		planeNumber = planeNum;
	}
	
	public Date getDate() {
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
	}
	
	
	}
}
