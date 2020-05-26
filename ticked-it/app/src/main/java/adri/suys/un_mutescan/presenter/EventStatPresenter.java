package adri.suys.un_mutescan.presenter;

import java.util.Map;

import adri.suys.un_mutescan.model.Counterpart;
import adri.suys.un_mutescan.model.Event;
import adri.suys.un_mutescan.utils.UnMuteDataHolder;
import adri.suys.un_mutescan.viewinterfaces.EventStatViewInterface;

public class EventStatPresenter extends Presenter {

    private final Event event;
    private EventStatViewInterface view;

    public EventStatPresenter(EventStatViewInterface view){
        this.view = view;
        this.event = UnMuteDataHolder.getEvent();
        double presalePercentageOnOne = ((double) event.getNbSoldTicket()) / event.getNbTotalTicket();
        float presalePercentageOnHundred = (float) (presalePercentageOnOne * 100);
        view.displayEvent(event.getName(), event.getDate(), event.getNbTotalTicket(), event.getNbSoldTicket(), presalePercentageOnHundred);
    }

    public Event getEvent(){return this.event;}

    public int getTotalScanned(){
        return event.getNbScannedTicket();
    }

    public int getTotalSoldOnSite(){
        return event.getNbTicketSoldOnSite();
    }

    public int getTotalToBeScanned(){
        return event.getNbSoldTicket() - event.getNbScannedTicket();
    }

    public int getTotalSold(){
        return event.getNbSoldTicket() + event.getNbTicketSoldOnSite();
    }

    public int getTotalRemaining(){
        return event.getRemainingTicketToBeSold();
    }

    public int getTotalPaidInCash(){
        return event.getNbTicketBoughtInCash();
    }

    public Map<Counterpart, Integer> getDetailsOfSellCp(){
        return event.getStatsPerCp();
    }

}
