package Tests;

import org.junit.Test;
import src.Database;
import src.SQL_Database;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class TestCustomerAccount
{
    @Test
    public void testCustomerLogin()
    {
        String username = "user";
        String password = "password";
        password = src.MD5Password.encodePassword(password);
        Database db = SQL_Database.getInstance();
        List<String> output =  db.getCustomerInfo(username);
        assertEquals(output.get(1), password);
        assertEquals(output.get(0), username);
    }

    @Test
    public void testCreateAccountDuplicate()
    {
        SQL_Database db = SQL_Database.getInstance();
        assertEquals(db.addCustomerAccount("user",
                "password", "Nick", "Parra"), -1);
    }

    @Test
    public void testCreateAccountNew()
    {
        SQL_Database db = SQL_Database.getInstance();
        assertEquals(db.addCustomerAccount("usertest",
                "password", "n", "p"), 0);
        db.removeCustomer("usertest");
    }
}
