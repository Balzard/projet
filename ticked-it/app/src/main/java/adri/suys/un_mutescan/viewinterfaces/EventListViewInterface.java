package adri.suys.un_mutescan.viewinterfaces;

import java.util.List;

import adri.suys.un_mutescan.model.Event;

public interface EventListViewInterface {

    void hideProgressBar();
    void updateEventsList(boolean isFiltered);
    void showNoConnectionRetryToast();
    void showServerConnectionProblemToast();
    void backUpEvents();
    void showToast(String message);
    List<Event> retrieveEvents();
    void collectEventsInDB(int id);
}
