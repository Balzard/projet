package adri.suys.un_mutescan.utils;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adri.suys.un_mutescan.model.Counterpart;
import adri.suys.un_mutescan.model.Event;
import adri.suys.un_mutescan.model.Ticket;

import static org.junit.Assert.*;

public class UnMuteDataHolderTest {

    private Counterpart cp1;

    @Before
    public void setUp() throws Exception {
        Event event = new Event(1, "Mon événement", 50, 10, 40, 3, new Date(), 1);
        cp1 = new Counterpart(1, "Ticket Adulte", 15);
        cp1.setQuantity(3);
        Counterpart cp2 = new Counterpart(2, "Ticket Enfant", 10);
        cp2.setQuantity(1);
        Counterpart cp3 = new Counterpart(3, "Ticket VIP", 30);
        List<Counterpart> cps = new ArrayList<>(Arrays.asList(cp1, cp2, cp3));
        List<Ticket> ts = new ArrayList<>();
        event.setCounterparts(cps);
        event.setAudience(ts);
        Map<String, String> map = new HashMap<>();
        map.put("1", "0");
        map.put("2", "0");
        map.put("3", "0");
        event.setMap(map);
        UnMuteDataHolder.setEvent(event);
    }

    @Test
    public void testAddingTickets(){
        UnMuteDataHolder.addTickets();
        assertEquals(UnMuteDataHolder.getAudience().size(), 4);
        int nb = UnMuteDataHolder.getEvent().getStatsPerCp().get(cp1);
        assertEquals(nb, 3);
    }

    @Test
    public void testSortingEvents(){
        try {
            Date d1 = new Date();
            String s2 = "31-Dec-1998 23:37:50";
            Date d2 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(s2);
            String s3 = "31-Dec-2019 23:37:50";
            Date d3 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(s3);
            String s4 = "31-Dec-2020 23:37:50";
            Date d4 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(s4);
            Event event1 = new Event(1, "Mon événement", 50, 10, 40, 3, d3, 1);
            Event event2 = new Event(2, "Mon événement", 50, 10, 40, 3, d2, 1);
            Event event3 = new Event(3, "Mon événement", 50, 10, 40, 3, d1, 1);
            Event event4 = new Event(4, "Mon événement", 50, 10, 40, 3, d4, 1);
            List<Event> events = new ArrayList<>(Arrays.asList(event1, event2, event3, event4));
            List<Event> sorted = UnMuteDataHolder.sortEvents(events);
            assertEquals(sorted.get(0).getId(), 3);
            assertEquals(sorted.get(1).getId(), 1);
            assertEquals(sorted.get(2).getId(), 4);
            assertEquals(sorted.get(3).getId(), 2);
        } catch (ParseException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testValidatingTicketSuccess(){
        Ticket t1 = new Ticket("Personne 1", "Ticket adulte", "N/A", "barcode1", "", "faux","");
        Ticket t2 = new Ticket("Personne 2", "Ticket adulte", "N/A", "barcode2", "", "faux","");
        UnMuteDataHolder.getAudience().add(t1);
        UnMuteDataHolder.getAudience().add(t2);
        int i = UnMuteDataHolder.isValidatedTicket("barcode1");
        assertTrue(i != -2);
    }

    @Test
    public void testValidatingTicketFail(){
        Ticket t1 = new Ticket("Personne 1", "Ticket adulte", "N/A", "barcode1", "", "faux","");
        Ticket t2 = new Ticket("Personne 2", "Ticket adulte", "N/A", "barcode2", "", "faux","");
        UnMuteDataHolder.getAudience().add(t1);
        UnMuteDataHolder.getAudience().add(t2);
        int i = UnMuteDataHolder.isValidatedTicket("nejuendenudne");
        assertTrue(i == -2);
    }
}