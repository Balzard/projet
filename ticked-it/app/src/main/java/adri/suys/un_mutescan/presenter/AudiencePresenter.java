package adri.suys.un_mutescan.presenter;

import java.util.ArrayList;
import java.util.List;

import adri.suys.un_mutescan.activities.Activity;
import adri.suys.un_mutescan.apirest.RestService;
import adri.suys.un_mutescan.fragments.AudienceFragment;
import adri.suys.un_mutescan.model.Ticket;
import adri.suys.un_mutescan.utils.UnMuteDataHolder;
import adri.suys.un_mutescan.viewinterfaces.AudienceRowViewInterface;
import adri.suys.un_mutescan.viewinterfaces.AudienceViewInterface;

public class AudiencePresenter extends Presenter {

    private final AudienceViewInterface view;
    private List<Ticket> audienceToBeDisplayed = new ArrayList<>();
    private static final int ALL = 1;
    private static final int IN = 2;
    private static final int OUT = 3;

    public AudiencePresenter(AudienceViewInterface view){
        this.view = view;
    }

    public void getAudience(int options) {
        if (options == ALL){
            audienceToBeDisplayed = UnMuteDataHolder.getAudience();
        } else {
            if (options == IN){
                audienceToBeDisplayed = sortAudienceIn();
            } else if (options == OUT) {
                audienceToBeDisplayed = sortAudienceOut();
            }
        }
        view.hideProgressBar();
        view.updateAudienceList();
    }

    public void onViewCounterpartAtPosition(int i, AudienceRowViewInterface audienceHolder) {
        Ticket currentCustomer = audienceToBeDisplayed.get(i);
        String name = currentCustomer.getName() + " (" + currentCustomer.getBarcodeText() + ")";
        String cartText = "Commande: " + currentCustomer.getCartNumber();
        audienceHolder.displayInfos(name, currentCustomer.getTicketType(), currentCustomer.getSeatValue(), cartText);
    }

    public int getItemCount() {
        return audienceToBeDisplayed.size();
    }

    public int getNbAll(){
        return UnMuteDataHolder.getEvent().getNbTotalTicket() - UnMuteDataHolder.getEvent().getRemainingTicketToBeSold();
    }

    public int getNbIn(){
        return UnMuteDataHolder.getEvent().getNbScannedTicket() + UnMuteDataHolder.getEvent().getNbTicketSoldOnSite();
    }

    public int getNbOut(){
        return getNbAll() - getNbIn();
    }

    private List<Ticket> sortAudienceIn() {
        List<Ticket> audienceSorted = new ArrayList<>();
        for (Ticket t : UnMuteDataHolder.getAudience()){
            if (t.isScanned()){
                audienceSorted.add(t);
            }
        }
        return audienceSorted;
    }

    private List<Ticket> sortAudienceOut() {
        List<Ticket> audienceSorted = new ArrayList<>();
        for (Ticket t : UnMuteDataHolder.getAudience()){
            if (!t.isScanned()){
                audienceSorted.add(t);
            }
        }
        return audienceSorted;
    }


    public String getBarcodeValue(int currentPosition) {
        return audienceToBeDisplayed.get(currentPosition).getBarcodeText();
    }

    public List<Ticket> getFilteredResult(String pattern, int currentState) {
        List<Ticket> filteredList = new ArrayList<>();
        if (pattern.isEmpty() || pattern == null){
            switch (currentState){
                case ALL : audienceToBeDisplayed = UnMuteDataHolder.getAudience(); break;
                case IN : audienceToBeDisplayed = sortAudienceIn(); break;
                default: audienceToBeDisplayed = sortAudienceOut(); break;
            }
            filteredList = audienceToBeDisplayed;
        } else {
            List<Ticket> unfilteredList;
            switch (currentState){
                case ALL : unfilteredList = UnMuteDataHolder.getAudience(); break;
                case IN : unfilteredList = sortAudienceIn(); break;
                default: unfilteredList = sortAudienceOut(); break;
            }
            for (Ticket t : unfilteredList){
                if (t.getName().toLowerCase().contains(pattern) || t.getCartNumber().toLowerCase().contains(pattern)){
                    filteredList.add(t);
                }
            }
        }
        return filteredList;
    }

    public void notifyChanged(List<Ticket> filteredList) {
        audienceToBeDisplayed = filteredList;
        view.updateAudienceList();
    }
}
