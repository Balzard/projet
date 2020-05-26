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

import adri.suys.un_mutescan.model.Counterpart;
import adri.suys.un_mutescan.model.Event;
import adri.suys.un_mutescan.utils.UnMuteDataHolder;
import adri.suys.un_mutescan.viewinterfaces.BuyTicketOnSiteViewInterface;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class BuyTicketOnSitePresenterTest {

    @Mock
    BuyTicketOnSiteViewInterface viewInterface;
    BuyTicketOnSitePresenter presenter;
    Counterpart cp1;
    Counterpart cp2;
    Counterpart cp3;

    @Before
    public void setUp() throws Exception {
        Event event = new Event(1, "Mon événement", 50, 10, 40, 3, new Date(), 1);
        cp1 = new Counterpart(1, "Ticket Adulte", 15);
        cp2 = new Counterpart(2, "Ticket Enfant", 10);
        cp3 = new Counterpart(3, "Ticket VIP", 30);
        List<Counterpart> cps = new ArrayList<>(Arrays.asList(cp1, cp2, cp3));
        event.setCounterparts(cps);
        UnMuteDataHolder.setEvent(event);
        presenter = new BuyTicketOnSitePresenter(viewInterface);
    }

    @Test
    public void addToCartTestSuccess1(){
        presenter.setQuantityToCp("Ticket Adulte", 15, 3);
        assertEquals(3, presenter.getTotalTicketSold());
        assertEquals(45, presenter.getCartAmount(), 0);
        assertEquals(3, cp1.getQuantity());
        presenter.setQuantityToCp("Ticket Adulte", 15, 1);
        assertEquals(1, cp1.getQuantity());
    }

    @Test
    public void addToCartTestSuccess2(){
        presenter.setQuantityToCp("Ticket Adulte", 15, 3);
        presenter.setQuantityToCp("Ticket VIP", 30, 1);
        assertEquals(4, presenter.getTotalTicketSold());
        assertEquals(75, presenter.getCartAmount(), 0);
        assertEquals(3, cp1.getQuantity());
        assertEquals(1, cp3.getQuantity());
    }

    @Test
    public void addToCartTestFail1(){
        presenter.setQuantityToCp("Ticket Adultes", 15, 3);
        assertEquals(0, presenter.getTotalTicketSold());
        assertEquals(0, presenter.getCartAmount(), 0);
        assertEquals(0, cp1.getQuantity());
    }

    @Test
    public void addToCartTestFail2(){
        presenter.setQuantityToCp("Ticket Adulte", 14, 3);
        assertEquals(0, presenter.getTotalTicketSold());
        assertEquals(0, presenter.getCartAmount(), 0);
        assertEquals(0, cp1.getQuantity());
    }
}