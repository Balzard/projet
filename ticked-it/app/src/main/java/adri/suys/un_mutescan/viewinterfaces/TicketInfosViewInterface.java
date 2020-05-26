package adri.suys.un_mutescan.viewinterfaces;

import adri.suys.un_mutescan.model.Ticket;

public interface TicketInfosViewInterface {

    void hideProgressBar();
    void displayEventName(String eventName);
    void displayTicketUnknwn(String barcodeText);
    void displayAlert();
    void displayTicket(boolean isValid, boolean isScanned, String barcodeText, String name, String ticketType, String seatType);
    void showNoConnectionRetryToast();
    void showServerConnectionProblemToast();
    void displayAlertMsg(boolean isScanned);
    void scanTicket(int id, int id1, String barcode, boolean directRest);
    void displayScanResult(Ticket ticketScanned);
}
