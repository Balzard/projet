package adri.suys.un_mutescan.viewinterfaces;

import java.util.Date;

public interface EventStatViewInterface {
    void displayEvent(String name, Date date, int totalTicket, int soldTicket, float presalePercentage);
}
