package adri.suys.un_mutescan.presenter;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adri.suys.un_mutescan.activities.PayActivity;
import adri.suys.un_mutescan.apirest.RestService;
import adri.suys.un_mutescan.model.Counterpart;
import adri.suys.un_mutescan.model.Event;
import adri.suys.un_mutescan.utils.UnMuteDataHolder;
import adri.suys.un_mutescan.viewinterfaces.PayViewInterface;

public class PayPresenter extends Presenter {

    private final PayViewInterface view;
    private final Event event;
    private int totalTicketSold;
    private boolean cashPayment;

    public PayPresenter(PayViewInterface view){
        this.view = view;
        event = UnMuteDataHolder.getEvent();
    }

    public void addTickets(boolean paidInCash, String email, String firstname, String lastname){
        cashPayment = paidInCash;
        view.addTicket(paidInCash, email, firstname, lastname);
    }

    /**
     * Notifies that the tickets have been well added
     * Reinit all the value
     * Goes to the screen where the buyer can pay
     */
    public void notifyTicketsWellAdded(int cpt, int nbUrls) {
        if (cpt == nbUrls-1){
            // fini
            view.showTicketAddedToast();
            UnMuteDataHolder.addTickets();
            event.addTicketSoldOnSite(totalTicketSold);
            if (cashPayment){
                event.addTicketPaidInCash(totalTicketSold);
                view.hideProgressBar();
                view.onBackPressed();
            } else {
                // si bancontact / plus applicable
            }
        } else {
            view.addAnotherTicket();
        }
    }

    /**
     * Notifies that the tickets have not been well added (error)
     * Reinit all the value
     * Invites the user to try again
     */
    public void notifyTicketsNotAdded(int cpt, String message) {
        StringBuilder msg;
        if (cpt == 0){
            view.hideProgressBar();
            view.showErrorMessage(message);
        } else {
            List<Counterpart> cps = UnMuteDataHolder.getCounterparts();
            msg = new StringBuilder("Les tickets ");
            for (int i = 0; i < cpt; i++){
                msg.append(cps.get(cpt).getName());
                if (i != cpt - 1){
                    msg.append(", ");
                } else {
                    msg.append(" ");
                }
            }
            msg.append("ont été ajoutés. Pour les autres, il y a eu un problème...");
            view.hideProgressBar();
            view.showToast(msg.toString());
        }
    }

    /**
     * If the Volley connection encounters problem, it displays an error message to the user
     * @param error the error
     */
    public void handleVolleyError(VolleyError error){
        if (error instanceof NoConnectionError || error instanceof TimeoutError){
            view.showNoConnectionRetryToast();
            System.out.println("1");
        } else if (error instanceof AuthFailureError){
            view.showServerConnectionProblemToast();
            System.out.println("2");
        } else if (error instanceof ServerError){
            view.showServerConnectionProblemToast();
            System.out.println("3");
        } else if (error instanceof NetworkError) {
            view.showServerConnectionProblemToast();
            System.out.println("4");
        } else if (error instanceof ParseError){
            view.showServerConnectionProblemToast();
            System.out.println(error.getMessage());
            System.out.println("5");
        }
    }

    public void setTotalTicketSold(int totalTicketSold) {
        this.totalTicketSold = totalTicketSold;
    }

    public boolean validateEmail(String email){
        if (email == null) return false;
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }
}
