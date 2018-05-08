package com.company;
import java.util.Date;


public class Ticket {
	private Date date;
	private Trip trip;
	private int ticketNumber;
	private int planeNumber;
	
	public Ticket(Date d, Trip t, int ticketNum, int planeNum) {
		date = d;
		trip = t;
		ticketNumber = ticketNum;
		planeNumber = planeNum;

	}
}
