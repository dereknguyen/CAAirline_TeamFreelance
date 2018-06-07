package Tests;

import org.junit.Assert;
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
        assertEquals(password, output.get(1));
        assertEquals(username, output.get(0));
    }

    @Test
    public void testCreateAccountDuplicate()
    {
        SQL_Database db = SQL_Database.getInstance();
        assertEquals(-1, db.addCustomerAccount("user",
                "password", "Nick", "Parra"));
    }

    @Test
    public void testCreateAccountNew()
    {
        SQL_Database db = SQL_Database.getInstance();
        assertEquals(0, db.addCustomerAccount("usertest",
                "password", "n", "p"));
        db.removeCustomer("usertest");
    }

    @Test
    public void testGetCustInfo() {
        SQL_Database db = SQL_Database.getInstance();
        int i = 0;
        db.addCustomerAccount("nparra2", "abc", "Nicolas", "Parra");
        List<String> customer = db.getCustomerInfo("nparra2");
        Assert.assertEquals(customer.get(i++), "nparra2");
        Assert.assertEquals(customer.get(i++), "abc");
        Assert.assertEquals(customer.get(i++), "Nicolas");
        Assert.assertEquals(customer.get(i), "Parra");
        db.removeCustomer("nparra2");
    }
}
