package adri.suys.un_mutescan.model;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EventTest {

    private Event event;
    private Counterpart cp1;

    @Before
    public void setUp() throws Exception {
        event = new Event(1, "Mon événement", 50, 10, 40, 3, new Date(), 1);
        cp1 = new Counterpart(1, "Ticket adulte", 15);
        Counterpart cp2 = new Counterpart(2, "Ticket enfant", 10);
        List<Counterpart> cps = new ArrayList<>(Arrays.asList(cp1, cp2));
        event.setCounterparts(cps);
    }

    @Test
    public void findCpByIdTestSuccess(){
        String i = "1";
        Counterpart cp = event.findCpById(i);
        assertEquals(cp1, cp);
    }

    @Test
    public void findCpByIdTestFail(){
        String i = "0";
        Counterpart cp = event.findCpById(i);
        assertEquals(null, cp);
    }

    @Test
    public void isTodayTestFail(){
        Date d = null;
        Event event = new Event(1, "Mon événement", 50, 10, 40, 3, d, 1);
        boolean isToday = event.isToday();
        assertFalse(isToday);
    }

    @Test
    public void isPassedTestFail1(){
        Date d = null;
        Event e = new Event(1, "Mon événement", 50, 10, 40, 3, d, 1);
        assertTrue(e.isPassed());
    }

    @Test
    public void isPassedTestFail2(){
        assertFalse(event.isPassed());
    }

    @Test
    public void isPassedTestSuccess(){
        try {
            String s = "31-Dec-1998 23:37:50";
            Date d = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(s);
            Event e = new Event(1, "Mon événement", 50, 10, 40, 3, d, 1);
            assertTrue(e.isPassed());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}