package com.company;

public class Database_Interface {

    public Database_Interface(){

    }

    public void getCustomerById(){}
    public void getEmployeeById(){}
    public void getFlightById(){}
    public void getEmptySeats(){}
    public void getNumFlights(){}

    // Attempts to add customer account to database. Returns 0 on success, 1 on error.
    // Will replace/update existing accounts with the same name/id
    public int addCustomerAccount(){
        return 0;
    }

    // Attempts to remove customer account to database. Returns 0 on success, 1 on error
    public int removeCustomerAccount(){
        return 0;
    }

    // Attempts to add employee account to database. Returns 0 on success, 1 on error
    // Will replace/update existing accounts with the same name/id
    public int addEmployeeAccount(){
        return 0;
    }

    // Attempts to remove employee account to database. Returns 0 on success, 1 on error
    public int removeEmployeeAccount(){
        return 0;
    }

    // Attempts to add flight to database. Returns 0 on success, 1 on error
    public int addFlight(){
        return 0;
    }

    // Attempts to remove flight from database. Returns 0 on success, 1 on erro
    public int removeFlight(){ return 0; }

    // Calculates recommended ticket price using previous two weeks of flight information. Returns recommended price
    // TODO Move this to a control class?
    public double calculatePrice(double base){
        getEmptySeats();
        return 0.0;
    }

    // Attempts to reserve flight seat for user. Returns 0 on success, 1 on error
    public int reserveSeat(){
        return 0;
    }

    // Attempts to check user in for flight. Returns 0 on success, 1 on error
    public int flightCheckIn(){
        return 0;
    }

    //Database branch

}
