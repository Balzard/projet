package adri.suys.un_mutescan.presenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import adri.suys.un_mutescan.fragments.AudienceFragment;
import adri.suys.un_mutescan.model.Event;
import adri.suys.un_mutescan.model.Ticket;
import adri.suys.un_mutescan.utils.UnMuteDataHolder;
import adri.suys.un_mutescan.viewinterfaces.AudienceViewInterface;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class AudiencePresenterTest {

    @Mock AudienceViewInterface viewInterface;
    AudiencePresenter presenter;
    private static final int ALL = 1;
    private static final int IN = 2;
    private static final int OUT = 3;

    @Before
    public void setUp() throws Exception {
        Ticket t1 = new Ticket("Personne 1", "Ticket adulte", "N/A", "barcode1", "", "vrai","bcd");
        Ticket t2 = new Ticket("Personne 2", "Ticket adulte", "N/A", "barcode1", "", "vrai","aaa");
        Ticket t3 = new Ticket("Personne 3", "Ticket adulte", "N/A", "barcode1", "", "vrai","bca");
        Ticket t4 = new Ticket("Personne 4", "Ticket adulte", "N/A", "barcode1", "", "vrai","abc");

        Ticket t5 = new Ticket("Personne 11", "Ticket adulte", "N/A", "barcode1", "", "faux","cde");
        Ticket t6 = new Ticket("Personne 12", "Ticket adulte", "N/A", "barcode1", "", "faux","eee");
        Ticket t7 = new Ticket("Personne 13", "Ticket adulte", "N/A", "barcode1", "", "faux","fee");

        List<Ticket> audience = new ArrayList<>(Arrays.asList(t1, t2, t3, t4, t5, t6, t7));
        Event event = new Event(1, "Mon événement", 50, 10, 40, 3, new Date(), 1);
        event.setAudience(audience);
        UnMuteDataHolder.setEvent(event);
        presenter = new AudiencePresenter(viewInterface);
    }

    @Test
    public void checkAudienceAll(){
        presenter.getAudience(ALL);
        assertEquals(7, presenter.getItemCount());
    }

    @Test
    public void checkAudienceIn(){
        presenter.getAudience(IN);
        assertEquals(4, presenter.getItemCount());
    }

    @Test
    public void checkAudienceOut(){
        presenter.getAudience(OUT);
        assertEquals(3, presenter.getItemCount());
    }

    @Test
    public void getNbAllTest(){
        int result = 43;
        assertEquals(presenter.getNbAll(), result);
    }

    @Test
    public void getNbInTest(){
        int result = 13;
        assertEquals(presenter.getNbIn(), result);
    }

    @Test
    public void getNbOutTest(){
        int result = 30;
        assertEquals(presenter.getNbOut(), result);
    }

    @Test
    public void filterAllFail(){
        String pattern = "Z";
        List<Ticket> filtered = presenter.getFilteredResult(pattern, ALL);
        assertEquals(0, filtered.size());
    }

    @Test
    public void filterAllSuccess(){
        String pattern = "1";
        List<Ticket> filtered = presenter.getFilteredResult(pattern, ALL);
        assertEquals(4, filtered.size());
    }

    @Test
    public void filterOutFail(){
        String pattern = "4";
        List<Ticket> filtered = presenter.getFilteredResult(pattern, OUT);
        assertEquals(0, filtered.size());
    }

    @Test
    public void filterOutSuccess(){
        String pattern = "2";
        List<Ticket> filtered = presenter.getFilteredResult(pattern, OUT);
        assertEquals(1, filtered.size());
    }

    @Test
    public void filterInFail(){
        String pattern = "11";
        List<Ticket> filtered = presenter.getFilteredResult(pattern, IN);
        assertEquals(0, filtered.size());
    }

    @Test
    public void filterInSuccess(){
        String pattern = "pers";
        List<Ticket> filtered = presenter.getFilteredResult(pattern, IN);
        assertEquals(4, filtered.size());
    }

    @Test
    public void filterCartSuccess(){
        String pattern = "bc";
        List<Ticket> filtered = presenter.getFilteredResult(pattern, ALL);
        assertEquals(3, filtered.size());
    }
}