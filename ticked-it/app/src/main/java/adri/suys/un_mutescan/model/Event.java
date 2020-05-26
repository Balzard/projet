package adri.suys.un_mutescan.model;

import android.text.format.DateUtils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A event is a event organized by an user for which he/she wants to scan ticket at the gates
 * It has an id, a name and a date
 */
public class Event implements Serializable {

    private final int id;
    private final String name;
    private int nbTotalTicket;
    private final int nbSoldTicket;
    private int nbScannedTicket;
    private int nbTicketSoldOnSite;
    private final Date date;
    private int nbTicketBoughtInCash;
    private List<Ticket> audience;
    private List<Counterpart> counterparts;
    private Map<Counterpart, Integer> statsPerCp;
    private boolean isUnMute;
    private String photoPath;

    /**
     *
     * @param id the id of the event
     * @param name the name of the event
     * @param nbTotalTicket the number of people the room can fit (the global soldout)
     * @param nbScannedTicket the number of tickets that are already scanned
     * @param nbSoldTicket the number of tickets sold prior the event (pre-sale)
     * @param nbTicketSoldOnSite the number of tickets sold on site at the gates
     * @param date the date of the event
     */
    public Event(int id, String name, int nbTotalTicket, int nbScannedTicket, int nbSoldTicket, int nbTicketSoldOnSite, Date date, int nbTicketBoughtInCash){
        this.id = id;
        if (name.contains("[Ticked-it]")){
            this.name = name.substring(12);
            this.isUnMute = false;
        } else {
            this.name = name;
            this.isUnMute = true;
        }
        this.nbTotalTicket = nbTotalTicket;
        this.nbScannedTicket = nbScannedTicket;
        this.nbSoldTicket = nbSoldTicket;
        this.nbTicketSoldOnSite = nbTicketSoldOnSite;
        this.date = date;
        this.nbTicketBoughtInCash = nbTicketBoughtInCash;
        this.statsPerCp = new HashMap<>();
    }

    public Counterpart findCpById(String id){
        for (Counterpart cp : counterparts){
            if (cp.getId() == Integer.parseInt(id)){
                return cp;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNbTotalTicket() {
        return nbTotalTicket;
    }

    public int getNbScannedTicket() {
        return nbScannedTicket;
    }

    public Date getDate() {
        return date;
    }

    public int getNbSoldTicket() {
        return nbSoldTicket;
    }

    public int getNbTicketSoldOnSite() {
        return nbTicketSoldOnSite;
    }

    public void scanTicket() {
        nbScannedTicket++;
    }

    public int getRemainingTicketToBeSold(){
        return nbTotalTicket - nbSoldTicket - nbTicketSoldOnSite;
    }

    public void addTicketSoldOnSite(int totalTicketSold) {
        this.nbTicketSoldOnSite += totalTicketSold;
    }

    public int getNbTicketBoughtInCash() {
        return nbTicketBoughtInCash;
    }

    public void addTicketPaidInCash(int ticketPaidInCash){
        this.nbTicketBoughtInCash += ticketPaidInCash;
    }

    public boolean isToday() {
        if (date == null){
            return false;
        }
        Calendar calToday = Calendar.getInstance();
        calToday.setTime(new Date());
        int todayDay = calToday.get(Calendar.DAY_OF_MONTH);
        int todayMonth = calToday.get(Calendar.MONTH);
        int todayYear = calToday.get(Calendar.YEAR);
        Calendar calEvent = Calendar.getInstance();
        calEvent.setTime(date);
        int eventDay = calEvent.get(Calendar.DAY_OF_MONTH);
        int eventMonth = calEvent.get(Calendar.MONTH);
        int eventYear = calEvent.get(Calendar.YEAR);
        if (todayDay != eventDay) return false;
        if (todayMonth != eventMonth) return false;
        if (todayYear != eventYear) return false;
        return true;
    }

    public boolean isPassed(){
        if (date == null){
            return true;
        } else {
            Calendar calToday = Calendar.getInstance();
            calToday.setTime(new Date());
            int todayDay = calToday.get(Calendar.DAY_OF_MONTH);
            int todayMonth = calToday.get(Calendar.MONTH) + 1;
            int todayYear = calToday.get(Calendar.YEAR);
            Calendar calEvent = Calendar.getInstance();
            calEvent.setTime(date);
            int eventDay = calEvent.get(Calendar.DAY_OF_MONTH);
            int eventMonth = calEvent.get(Calendar.MONTH) + 1;
            int eventYear = calEvent.get(Calendar.YEAR);
            if (eventYear < todayYear){
                return true;
            } else if (eventYear > todayYear){
                return false;
            } else {
                if (eventMonth < todayMonth){
                    return true;
                } else if (eventMonth > todayMonth){
                    return false;
                } else {
                    if (eventDay < todayDay){
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
    }

    public List<Ticket> getAudience() {
        return audience;
    }

    public void setAudience(List<Ticket> audience) {
        this.audience = audience;
        Collections.sort(this.audience, new Comparator<Ticket>() {
            @Override
            public int compare(Ticket ticket, Ticket t1) {
                return ticket.getName().toLowerCase().compareTo(t1.getName().toLowerCase());
            }
        });
    }

    public List<Counterpart> getCounterparts() {
        return counterparts;
    }

    public void setCounterparts(List<Counterpart> counterparts) {
        this.counterparts = counterparts;
    }

    public void setNbTotalTicket(int nbTotalTicket) {
        this.nbTotalTicket = nbTotalTicket;
    }

    public void setMap(Map<String, String> stats){
        statsPerCp.clear();
        for (Map.Entry<String, String> stat : stats.entrySet()){
            Counterpart cp = findCpById(stat.getKey());
            int nb = Integer.parseInt(stat.getValue());
            statsPerCp.put(cp, nb);
        }
    }

    public void updateMap(Counterpart cp, int nbTixToAdd){
        Integer nbTixAlreadySold = statsPerCp.get(cp);
        if (nbTixAlreadySold != null) {
            int updatedNb = nbTixAlreadySold + nbTixToAdd;
            statsPerCp.put(cp, updatedNb);
        }
    }

    public Map<Counterpart, Integer> getStatsPerCp() {
        return statsPerCp;
    }

    public boolean isUnMute() {
        return isUnMute;
    }

    public String getPhotoPath() {
        if (photoPath.equals("")){
            return "http://192.168.1.33:8888/GroopyMusic/web/images/artist-card-default.jpg";
        }
        if (isUnMute){
            return "http://192.168.1.33:8888/GroopyMusic/web/images/festivals/" + photoPath;
        } else {
            return "http://192.168.1.33:8888/GroopyMusic/web/yb/images/campaigns/" + photoPath;
        }
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
