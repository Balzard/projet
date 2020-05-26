package adri.suys.un_mutescan.apirest;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adri.suys.un_mutescan.BuildConfig;
import adri.suys.un_mutescan.activities.Activity;
import adri.suys.un_mutescan.model.Counterpart;
import adri.suys.un_mutescan.model.Event;
import adri.suys.un_mutescan.model.User;
import adri.suys.un_mutescan.presenter.EventPresenter;
import adri.suys.un_mutescan.presenter.LoginPresenter;
import adri.suys.un_mutescan.presenter.PayPresenter;
import adri.suys.un_mutescan.presenter.TicketInfosPresenter;
import adri.suys.un_mutescan.utils.UnMuteDataHolder;

public class RestService {

    private static final int SCAN_TICKET = 0;
    private static final int USER = 2;
    private static final String IP_ADDRESS = "192.168.1.7"; // changer en fonction des besoins ou utiliser "localhost"
    private static final boolean USE_REAL_URL_IN_DEBUG = true;

    private static String BASE_URL_USER;
    private static String BASE_URL_SCAN_TICKET;
    private static String BASE_URL_EVENTS;
    private static String BASE_URL_ADD_TICKET;

    private TicketInfosPresenter ticketPresenter;
    private LoginPresenter userPresenter;
    private EventPresenter eventPresenter;
    private PayPresenter payPresenter;

    private final RequestQueue requestQueue;
    private final Gson gson;
    private int cpt;
    private List<String> urls = new ArrayList<>();
    private Activity activity;

    public RestService(AppCompatActivity activity){
        this.activity = (Activity) activity;
        requestQueue = Volley.newRequestQueue(activity);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        handleUrls();
    }

    private void handleUrls(){
        if (BuildConfig.IS_DEBUG_MODE){
            if (!USE_REAL_URL_IN_DEBUG) {
                BASE_URL_USER = "http://" + IP_ADDRESS + ":8888/GroopyMusic/web/app_dev.php/yb/rest/loginuser?";
                BASE_URL_SCAN_TICKET = "http://" + IP_ADDRESS + ":8888/GroopyMusic/web/app_dev.php/yb/rest/scanticket?";
                BASE_URL_EVENTS = "http://" + IP_ADDRESS + ":8888/GroopyMusic/web/app_dev.php/yb/rest/getevents?";
                BASE_URL_ADD_TICKET = "http://" + IP_ADDRESS + ":8888/GroopyMusic/web/app_dev.php/yb/rest/addticket?";
            } else {
                BASE_URL_USER = "https://ticked-it.be/rest/loginuser?";
                BASE_URL_SCAN_TICKET = "https://ticked-it.be/rest/scanticket?";
                BASE_URL_EVENTS = "https://ticked-it.be/rest/getevents?";
                BASE_URL_ADD_TICKET = "https://ticked-it.be/rest/addticket?";
            }
        } else {
            BASE_URL_USER = "https://ticked-it.be/rest/loginuser?";
            BASE_URL_SCAN_TICKET = "https://ticked-it.be/rest/scanticket?";
            BASE_URL_EVENTS = "https://ticked-it.be/rest/getevents?";
            BASE_URL_ADD_TICKET = "https://ticked-it.be/rest/addticket?";
        }
    }

    /**
     * Creates the url we need to reach the api
     * Create a request with that url
     * According to the username, get back the user with all its infos.
     * @param username the username of the user that tries to log in
     */
    public void loginUser(String username){
        String url = BASE_URL_USER + "username=" + username;
        createJsonObjectRequest(url, USER, false, false);
    }

    /**
     * Creates the url we need to reach the api
     * Create a request with that url
     * According the userID, the eventID and the barcode, validates a ticket
     * @param userID the id of the current user
     * @param eventID the id of the current event
     * @param barcodeValue the barcode value scanned
     */
    public void scanTicket(int userID, int eventID, String barcodeValue, boolean directRest){
        String url = BASE_URL_SCAN_TICKET + "user_id=" + userID + "&event_id=" + eventID + "&barcode=" + barcodeValue;
        if (activity.isInternetConnected()) {
            createJsonObjectRequest(url, SCAN_TICKET, false, directRest);
        } else {
            UnMuteDataHolder.addRequest(url);
            activity.backUpUrls();
        }
    }

    /**
     * Creates the url we need to reach the api
     * Create a request with that url
     * According to the id of the current user, fetches all the event he organises.
     * @param id the id of the current user
     */
    public void collectEvents(int id){
        String url = BASE_URL_EVENTS + "id=" + id;
        createJsonArrayRequest(url);
    }

    /**
     * Creates the url we need to reach the api
     * Create a request with that url
     * Add a/some ticket(s) to the DB --> the tickets that are sold on site during the event
     */
    public void addTicket(boolean paidInCash, String email, String firstname, String lastname){
        urls = getUrlFromEvent(paidInCash, email, firstname, lastname);
        cpt = 0;
        if (activity.isInternetConnected()){
            createAddRequest(urls.get(cpt), false);
        } else {
            for (String url : urls){
                UnMuteDataHolder.addRequest(url);
            }
            activity.backUpUrls();
            payPresenter.notifyTicketsWellAdded(urls.size()-1, urls.size());
        }
    }

    /**
     * After having added one ticket, inserts another ticket to the db
     */
    public void addAnotherTicket(){
        cpt++;
        createAddRequest(urls.get(cpt), false);
    }

    /**
     * Make pending request
     * Loop through a url lists and try to launch a request per url.
     */
    public void makePendingRequest() {
        for (String url : UnMuteDataHolder.getRequestURLs()){
            if (url.contains("scanticket")){
                createJsonObjectRequest(url, SCAN_TICKET, true, false);
            } else if (url.contains("addticket")){
                createAddRequest(url, true);
            }
        }
    }

    //|||||||||||||||||||||//
    //||SETTERS & GETTERS||//
    //|||||||||||||||||||||//

    public void setTicketPresenter(TicketInfosPresenter ticketPresenter) {
        this.ticketPresenter = ticketPresenter;
    }

    public void setUserPresenter(LoginPresenter userPresenter) {
        this.userPresenter = userPresenter;
    }

    public void setEventPresenter(EventPresenter eventPresenter) {
        this.eventPresenter = eventPresenter;
    }

    public void setPayPresenter(PayPresenter payPresenter) {
        this.payPresenter = payPresenter;
    }

    //|||||||||||//
    //||PRIVATE||//
    //|||||||||||//

    private void createJsonObjectRequest(final String url, final int hint, final boolean isSilent, final boolean directRest){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (isSilent){
                            handleSilentResponse(response, url);
                        } else {
                            switch (hint) {
                                case SCAN_TICKET:
                                    ticketPresenter.handleJSONObject(response, gson, directRest);
                                    break;
                                case USER:
                                    userPresenter.handleJSONObject(response, gson);
                                    break;
                                default:
                                    // nothing
                                    break;
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (isSilent){
                            // do nothing
                        } else {
                            switch (hint) {
                                case SCAN_TICKET:
                                    ticketPresenter.handleVolleyError(error);
                                    break;
                                case USER:
                                    userPresenter.handleVolleyError(error);
                                    break;
                                default:
                                    // nothing
                                    break;
                            }
                        }
                    }
                });
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    private void handleSilentResponse(JSONObject response, String url) {
        UnMuteDataHolder.getRequestURLs().remove(url);
        activity.backUpUrls();
    }

    private void createJsonArrayRequest(String url){
        System.out.println(url);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        eventPresenter.handleJSONArray(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        eventPresenter.handleVolleyError(error);
                    }
                });
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    private void createAddRequest(final String url, final boolean isSilent){
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (isSilent){
                            handleSilentResponse(response, url);
                        } else {
                            try {
                                System.out.println("FEEDBACK");
                                String feedback = response.getString("error");
                                System.out.println("FEEDBACK" + feedback);
                                if (feedback.contains("Tout s'est bien passé !")) {
                                    payPresenter.notifyTicketsWellAdded(cpt, urls.size());
                                } else {
                                    System.out.println("MF-PROBLEM " + feedback);
                                    payPresenter.notifyTicketsNotAdded(cpt, feedback);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (isSilent){
                            System.out.println("PROBLEM IS SILENT ADDING TICKET " + error.getMessage());
                        } else {
                            payPresenter.handleVolleyError(error);
                        }
                    }
                }
        );
        jsonArrayRequest.setShouldCache(false);
        requestQueue.add(jsonArrayRequest);
    }

    private List<String> getUrlFromEvent(boolean paidInCash, String email, String firstname, String lastname){
        List<String> urls = new ArrayList<>();
        User user = UnMuteDataHolder.getUser();
        Event event = UnMuteDataHolder.getEvent();
        for (Counterpart cp : UnMuteDataHolder.getCounterparts()){
            if (cp.getQuantity() > 0){
                String url = getUrlFromCounterpart(user.getId(), cp, event.getId(), paidInCash, email, firstname, lastname);
                urls.add(url);
            }
        }
        return urls;
    }

    private String getUrlFromCounterpart(int userID, Counterpart cp, int eventID, boolean paidInCash, String email, String firstname, String lastname) {
        String mode = paidInCash ? "cash" : "bancontact";
        return BASE_URL_ADD_TICKET + "user_id=" + userID + "&event_id=" + eventID
                + "&counterpart_id=" + cp.getId() + "&quantity=" + cp.getQuantity()
                + "&mode=" + mode + "&email=" + email + "&firstname=" + firstname + "&lastname=" + lastname;
    }

    public void refreshData(int id) {
        String url = BASE_URL_EVENTS + "id=" + id;
        createJsonArrayRequest(url);
    }
}
