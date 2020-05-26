package adri.suys.un_mutescan.presenter;

import java.util.ArrayList;
import java.util.List;

import adri.suys.un_mutescan.activities.Activity;
import adri.suys.un_mutescan.apirest.RestService;
import adri.suys.un_mutescan.fragments.BuyTicketOnSiteFragment;
import adri.suys.un_mutescan.model.Counterpart;
import adri.suys.un_mutescan.model.Event;
import adri.suys.un_mutescan.utils.UnMuteDataHolder;
import adri.suys.un_mutescan.viewinterfaces.BuyTicketOnSiteViewInterface;
import adri.suys.un_mutescan.viewinterfaces.PurchaseRowViewInterface;

public class BuyTicketOnSitePresenter extends Presenter {

    private final Event event;
    private List<Counterpart> counterparts = new ArrayList<>();
    private double cartAmount = 0;
    private int totalTicketSold = 0;
    private final BuyTicketOnSiteViewInterface view;

    public BuyTicketOnSitePresenter(BuyTicketOnSiteViewInterface view){
        this.view = view;
        this.event = UnMuteDataHolder.getEvent();
    }

    // FETCHES ALL THE CP IN THE DB

    /**
     * Fetches in the DB all the counterparts that are linked to the currentEvent
     */
    public void collectCounterparts(){
        counterparts = UnMuteDataHolder.getCounterparts();
        view.hideProgressBar();
        view.updateTicketTypesList();
    }

    // SELL TICKET AT THE GATES

    /**
     * Add tickets to the database
     */
    public void completeCart() {
        double amountToPay = cartAmount;
        int numberTicketsSold = totalTicketSold;
        reinit();
        view.changeScreen(amountToPay, numberTicketsSold);
    }

    // OTHERS

    public double getCartAmount(){
        return this.cartAmount;
    }

    public int getItemCount() {
        return counterparts.size();
    }

    /**
     * Displays the informations for each counterpart (name, price, and +/- button)
     * @param i the position
     * @param view the viewHolder
     */
    public void onViewCounterpartAtPosition(int i, PurchaseRowViewInterface view) {
        Counterpart currentCounterpart = counterparts.get(i);
        view.displayInfos(currentCounterpart.getName(), Double.toString(currentCounterpart.getPrice()));
    }

    /**
     * Checks if the event is soldout (if we still can sell tickets)
     * @return a boolean indicating if the event is soldout
     */
    public boolean isEventSoldout(int toBeSold){
        return event.getRemainingTicketToBeSold() <= toBeSold;
    }

    public int getTotalTicketSold(){
        return totalTicketSold;
    }

    private void reinit(){
        totalTicketSold = 0;
        cartAmount = 0;
    }

    public void setQuantityToCp(String name, double price, int quantity) {
        for (Counterpart cp : UnMuteDataHolder.getCounterparts()){
            if (cp.getName().equals(name) && cp.getPrice() == price){
                cp.setQuantity(quantity);
                cartAmount += price * quantity;
                totalTicketSold += quantity;
            }
        }
    }
}
