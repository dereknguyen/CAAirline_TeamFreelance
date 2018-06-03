package src;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
       Parent root = FXMLLoader.load(getClass().getResource("../Views/pricerecommendview.fxml"));
       primaryStage.setTitle("California System");
       //primaryStage.setScene(new Scene(root, 1600, 900));
        primaryStage.setScene( new Scene(root, 1000, 600));
        primaryStage.show();
//        while(true) {
//            mainLoop();
//        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    private static void mainLoop() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Would you like to log in as an employee or customer?");
        System.out.println("Type 1 for employee or 2 for customer");
        int resp = scan.nextInt();
        String username;

        if(resp == 1) {
            System.out.println("Type 1 for purchase ticket, 2 for checkin, 3 for view flight status");
            System.out.println("Type 4 for Edit employee, 5 for add flight, " +
                    "6 for edit flight status, 7 for get price recommendation");
            resp = scan.nextInt();
            CustomerControl cc = CustomerControl.getInstance();
            EmployeeControl ec = EmployeeControl.getInstance();
            //call functions here
            if (resp == 1)
            {
                System.out.println("Enter your username: ");
                scan.nextLine();
                username = scan.nextLine();
                cc.reserve(username);
            }
            else if (resp == 2)
            {
                System.out.println("Enter your username: ");
                scan.nextLine();
                username = scan.nextLine();
                cc.checkin(username);
            }
            else if (resp == 3)
            {
                cc.status();
            }
            else if (resp == 4) {
                //edit employee
                ec.editEmployee();
            }
            else if (resp == 5) {
                //add flight
                ec.scheduleFlight();
            }
            else if (resp == 6) {
                //edit flight status
                ec.editFlight();
            }
            else if (resp == 7) {
                //get price recommendation
                ec.viewPrice();
            }
        }
        else if (resp == 2) {
            System.out.println("Type 1 for purchase ticket, 2 for checkin, 3 for view flight status");
            resp = scan.nextInt();
            CustomerControl cc = CustomerControl.getInstance();
            //call functions here
            if (resp == 1)
            {
                System.out.println("Enter your username: ");
                scan.nextLine();
                username = scan.nextLine();
                cc.reserve(username);
            }
            else if (resp == 2)
            {
                System.out.println("Enter your username: ");
                scan.nextLine();
                username = scan.nextLine();
                cc.checkin(username);
            }
            else if (resp == 3)
            {
                cc.status();
            }
        }
    }
}
