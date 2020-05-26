package adri.suys.un_mutescan.presenter;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.List;

import adri.suys.un_mutescan.activities.TicketInfosActivity;
import adri.suys.un_mutescan.apirest.RestService;
import adri.suys.un_mutescan.model.Event;
import adri.suys.un_mutescan.model.Ticket;
import adri.suys.un_mutescan.utils.UnMuteDataHolder;
import adri.suys.un_mutescan.viewinterfaces.TicketInfosViewInterface;

public class TicketInfosPresenter extends Presenter {

    private final Event currentEvent;
    private final TicketInfosViewInterface view;
    private String barcode;
    private Ticket ticket;

    public TicketInfosPresenter(TicketInfosViewInterface view){
        this.view = view;
        this.currentEvent = UnMuteDataHolder.getEvent();
    }

    public void updateDB(){
        ticket.setIs_validated(true);
        currentEvent.scanTicket();
        view.scanTicket(UnMuteDataHolder.getUser().getId(), currentEvent.getId(), barcode, false);
    }

    public boolean validateBarcode(String barcodeValue){
        barcode = barcodeValue;
        boolean isValid = false;
        int i = UnMuteDataHolder.getTicketPosition(barcodeValue);
        if (i == -2){
            view.hideProgressBar();
            view.displayEventName(currentEvent.getName());
            view.displayTicketUnknwn(barcodeValue);
            view.displayAlert();
        } else {
            ticket = UnMuteDataHolder.getAudience().get(i);
            boolean isAlreadyScanned;
            if (ticket.isScanned()){
                isAlreadyScanned = true;
            } else {
                isAlreadyScanned = false;
                isValid = true;
                ticket.setIs_validated(true);
            }
            view.hideProgressBar();
            view.displayEventName(currentEvent.getName());
            view.displayTicket(isValid, isAlreadyScanned, ticket.getBarcodeText(), ticket.getName(), ticket.getTicketType(), ticket.getSeatValue());
            view.displayAlertMsg(isAlreadyScanned);
        }
        return isValid;
    }

    public void handleJSONObject(JSONObject response, Gson gson, boolean directRest) {
        if (directRest){
            Ticket ticketScanned = gson.fromJson(response.toString(), new TypeToken<Ticket>(){}.getType());
            if (ticketScanned.getError().equals("Ce ticket n'existe pas.")){
                view.hideProgressBar();
                view.displayEventName(currentEvent.getName());
                view.displayTicketUnknwn(ticketScanned.getBarcodeText());
                view.displayAlert();
            } else {
                ticket = UnMuteDataHolder.getAudience().get(UnMuteDataHolder.getTicketPosition(ticketScanned.getBarcodeText()));
                if (ticketScanned.getError().equals("")) {
                    ticket.setIs_validated(true);
                }
                view.hideProgressBar();
                view.displayEventName(currentEvent.getName());
                view.displayScanResult(ticketScanned);
                //view.displayTicket(isValid, isAlreadyScanned, ticket.getBarcodeText(), ticket.getName(), ticket.getTicketType(), ticket.getSeatValue(), true);
                //view.displayAlertMsg(isAlreadyScanned);
            }
        } else {
            System.out.println("J'ai fini ma requête.");
        }
    }

    public void handleVolleyError(VolleyError error){
        if (error instanceof NoConnectionError || error instanceof TimeoutError){
            view.showNoConnectionRetryToast();
        } else if (error instanceof AuthFailureError){
            view.showServerConnectionProblemToast();
        } else if (error instanceof ServerError){
            view.showServerConnectionProblemToast();
        } else if (error instanceof NetworkError) {
            view.showServerConnectionProblemToast();
        } else if (error instanceof ParseError){
            view.showServerConnectionProblemToast();
        }
        view.hideProgressBar();
    }
}
