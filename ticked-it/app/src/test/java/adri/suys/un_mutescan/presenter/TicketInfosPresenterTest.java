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

import adri.suys.un_mutescan.model.Event;
import adri.suys.un_mutescan.model.Ticket;
import adri.suys.un_mutescan.utils.UnMuteDataHolder;
import adri.suys.un_mutescan.viewinterfaces.TicketInfosViewInterface;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TicketInfosPresenterTest {

    @Mock TicketInfosViewInterface infosViewInterface;
    TicketInfosPresenter presenter;

    @Before
    public void setUp() throws Exception {
        Event event = new Event(1, "Mon événement", 50, 10, 40, 3, new Date(), 1);
        Ticket t5 = new Ticket("Personne 11", "Ticket adulte", "N/A", "barcode1", "", "vrai", "");
        Ticket t6 = new Ticket("Personne 12", "Ticket adulte", "N/A", "barcode2", "", "faux", "");
        Ticket t7 = new Ticket("Personne 13", "Ticket adulte", "N/A", "barcode3", "", "faux"," ");
        List<Ticket> audience = new ArrayList<>(Arrays.asList(t5, t6, t7));
        event.setAudience(audience);
        UnMuteDataHolder.setEvent(event);
        presenter = new TicketInfosPresenter(infosViewInterface);
    }

    @Test
    public void checkNonValidtyNotExist(){
        presenter.validateBarcode("jnjdnsjdn");
        verify(infosViewInterface).displayTicketUnknwn("jnjdnsjdn");
    }

    @Test
    public void checkNonValidtyScanned() {
        assertFalse(presenter.validateBarcode("barcode1"));
    }

    @Test
    public void checkValidty() {
        assertTrue(presenter.validateBarcode("barcode2"));
        assertTrue(UnMuteDataHolder.getAudience().get(1).isScanned());
    }
}