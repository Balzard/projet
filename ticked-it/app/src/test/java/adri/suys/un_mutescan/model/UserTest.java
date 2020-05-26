package adri.suys.un_mutescan.model;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserTest {

    private User user;

    @Before
    public void setUp() throws Exception {
        user = new User(1, "test@test.be", "Prénom Nom", "xxx", "");
    }

    @Test
    public void isTokenStillActiveTestFalse(){
        try {
            String s = "31-Dec-1998 23:37:50";
            Date d = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(s);
            long time = d.getTime();
            user.setLastConnection(time);
            assertFalse(user.isTokenStillActive());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void isTokenStillActiveTestTrue(){
        Date d = new Date();
        long time = d.getTime();
        user.setLastConnection(time);
        assertTrue(user.isTokenStillActive());
    }
}