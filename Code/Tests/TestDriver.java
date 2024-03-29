package Tests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestDriver
{
    public static void main(String[] args)
    {
        Result r = JUnitCore.runClasses(TestSQLDB.class,
                TestFlightStatus.class, TestCustomerAccount.class, IntegrationTestA.class);

        for (Failure f : r.getFailures())
        {
            System.out.println(f.toString());
        }
        System.out.println("Success: " + r.wasSuccessful());
    }
}
