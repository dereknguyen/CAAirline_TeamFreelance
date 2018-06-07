package src;

import javafx.beans.property.SimpleStringProperty;

public class Employee
{
    private String Username;
    private String FirstName;
    private String LastName;

    /* Properties */
    private SimpleStringProperty UsernameString;
    private SimpleStringProperty FirstNameString;
    private SimpleStringProperty LastNameString;

    public Employee(String Username, String FirstName, String LastName)
    {
        this.Username = Username;
        this.FirstName = FirstName;
        this.LastName = LastName;

        this.UsernameString = new SimpleStringProperty(Username);
        this.FirstNameString = new SimpleStringProperty(FirstName);
        this.LastNameString = new SimpleStringProperty(LastName);
    }

    public String getUsername() { return Username; }
    public String getFirstName() { return FirstName; }
    public String getLastName() { return LastName; }

    public String getUsernameString() { return UsernameString.get(); }
    public String getFirstNameString() { return FirstNameString.get(); }
    public String getLastNameString() { return LastNameString.get(); }
}
