package src;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Password
{
    /* encodes a password using MD5 and returns the encoded password, or null on error */
    public static String encodePassword(String input)
    {
        if (input.length() == 0) {return null;}
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes)
            {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
