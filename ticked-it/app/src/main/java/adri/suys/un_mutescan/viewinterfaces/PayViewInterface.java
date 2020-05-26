package adri.suys.un_mutescan.viewinterfaces;

public interface PayViewInterface {

    void showTicketAddedToast();
    void hideProgressBar();
    void onBackPressed();
    void showToast(String message);
    void showNoConnectionRetryToast();
    void showServerConnectionProblemToast();
    void showErrorMessage(String message);
    void addTicket(boolean paidInCash, String email, String firstname, String lastname);
    void addAnotherTicket();
}
