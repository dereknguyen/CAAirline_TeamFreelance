package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Would you like to log in as an employee or customer?");
        System.out.println("Type 1 for employee or 2 for customer");
        int resp = scan.nextInt();

        if(resp == 1) {
            System.out.println("Type 1 for purchase ticket, 2 for checkin, 3 for view flight status");
            System.out.println("Type 4 for Add employee, 5 for add flight, 6 for edit flight status, 7 for get price recommendation");
            resp = scan.nextInt();
            //call functions here
        }
        else if (resp == 2) {
            System.out.println("Type 1 for purchase ticket, 2 for checkin, 3 for view flight status");
            resp = scan.nextInt();
            //call functions here
        }

    }
}
