package adri.suys.un_mutescan.utils;

import android.content.Context;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import adri.suys.un_mutescan.R;
import adri.suys.un_mutescan.model.Counterpart;
import adri.suys.un_mutescan.model.Event;
import adri.suys.un_mutescan.model.Ticket;
import adri.suys.un_mutescan.model.User;

/**
 * Holds the datas that are shared through the whole app
 */
public class UnMuteDataHolder {

    private static User user;
    private static Event event;
    private static int currentFragment = 0;
    private static List<Event> events;
    private static List<String> requestURLs;
    private static boolean hideRefreshButton = false;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        UnMuteDataHolder.user = user;
    }

    public static Event getEvent() {
        return event;
    }

    public static void setEvent(Event event) {
        UnMuteDataHolder.event = event;
    }

    public static boolean isHideRefreshButton(){
        return hideRefreshButton;
    }

    public static void setHideRefreshButton(boolean hideRefreshButton) {
        UnMuteDataHolder.hideRefreshButton = hideRefreshButton;
    }

    public static int getCurrentFragment() {
        return currentFragment;
    }

    public static void setCurrentFragment(int currentFragment) {
        UnMuteDataHolder.currentFragment = currentFragment;
    }

    public static List<Counterpart> getCounterparts() {
        return event.getCounterparts();
    }

    public static List<Event> getEvents() {
        return events;
    }

    public static void setEvents(List<Event> events) {
        List<Event> sorted = sortEvents(events);
        UnMuteDataHolder.events = sorted;
    }

    public static List<Ticket> getAudience(){
        return event.getAudience();
    }

    public static void reinit(){
        user = null;
        event = null;
        currentFragment = 0;
        events = null;
    }

    public static void addTickets(){
        for (Counterpart cp : event.getCounterparts()){
            for (int i = 0; i < cp.getQuantity(); i++) {
                Ticket t = new Ticket("anonyme", cp.getName(), "", "000", "", "vrai", "");
                event.getAudience().add(t);
            }
            event.updateMap(cp, cp.getQuantity());
        }
        Collections.sort(event.getAudience(), new Comparator<Ticket>() {
            @Override
            public int compare(Ticket ticket, Ticket t1) {
                return ticket.getName().compareTo(t1.getName());
            }
        });
    }

    public static List<String> getRequestURLs(){
        return requestURLs;
    }

    public static void addRequest(String url){
        requestURLs.add(url);
    }

    public static void setRequestURLs(List<String> requestURLs){
        UnMuteDataHolder.requestURLs = requestURLs;
    }

    public static List<Event> sortEvents(List<Event> events){
        List<Event> sorted = new ArrayList<>();
        List<Event> past = new ArrayList<>();
        List<Event> future = new ArrayList<>();
        List<Event> undetermined = new ArrayList<>();
        List<Event> today = new ArrayList<>();
        for (Event e : events){
            if (e.getDate() == null){
                undetermined.add(e);
            } else if (e.isToday()){
                today.add(e);
            } else if (e.getDate().compareTo(new Date()) >= 0){
                future.add(e);
            } else {
                past.add(e);
            }
        }
        Collections.sort(future, new Comparator<Event>() {
            @Override
            public int compare(Event event, Event t1) {
                return event.getDate().compareTo(t1.getDate());
            }
        });
        Collections.sort(past, new Comparator<Event>() {
            @Override
            public int compare(Event event, Event t1) {
                return t1.getDate().compareTo(event.getDate());
            }
        });
        sorted.addAll(today);
        sorted.addAll(future);
        sorted.addAll(past);
        return sorted;
    }

    public static int getTicketPosition(String barcode){
        for (int i = 0; i < getAudience().size(); i++){
            if (getAudience().get(i).getBarcodeText().equals(barcode)){
                return i;
            }
        }
        return -2;
    }

    public static int[] getColorsArrayForStatPieChart(Context ctx){
        int auroraGreen = ContextCompat.getColor(ctx, R.color.aurora_green);
        int azraqBlue = ContextCompat.getColor(ctx, R.color.azraq_blue);
        int jalapenoRed = ContextCompat.getColor(ctx, R.color.jalapeno_red);
        int spray = ContextCompat.getColor(ctx, R.color.spray);
        return new int[]{
            auroraGreen, azraqBlue, jalapenoRed, spray
        };
    }

    public static int[] getColorsArrayForCounterpartStat(Context ctx){
        int mandarinRed = ContextCompat.getColor(ctx, R.color.mandarin_red);
        int reefEncounter = ContextCompat.getColor(ctx, R.color.reef_encounter);
        int carrotOrange = ContextCompat.getColor(ctx, R.color.carrot_orange);
        int paradiseGreen = ContextCompat.getColor(ctx, R.color.paradise_green);
        int livid = ContextCompat.getColor(ctx, R.color.livid);
        return new int[]{
            mandarinRed, reefEncounter, carrotOrange, paradiseGreen, livid
        };
    }

    public static int[] getColorsArrayForHorizontalBarchart(Context ctx){
        int yueGuangLanBlue = ContextCompat.getColor(ctx, R.color.yue_guang_lan_blue);
        int tomatoRed = ContextCompat.getColor(ctx, R.color.tomato_red);
        int waterfall = ContextCompat.getColor(ctx, R.color.waterfall);
        return new int[]{
            tomatoRed, yueGuangLanBlue, waterfall
        };
    }

    public static Event GetEventFromID(int eventID){
        for (Event e : events){
            if (e.getId() == eventID){
                return e;
            }
        }
        return null;
    }
}
