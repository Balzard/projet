package adri.suys.un_mutescan.viewinterfaces;

import com.google.android.gms.common.api.Status;

import adri.suys.un_mutescan.model.User;

public interface LoginViewInterface {

    User retrieveUserFromDB();
    void showNoAccessToInternetToast();
    void showConnectionProblemToast();
    void showUnvalidUsernameToast();
    void showCantUseAppToast();
    void showHelloToast(String username);
    void showBadCredentialsToast();
    void showNoConnectionRetryToast();
    void showServerConnectionProblemToast();
    void showCredentialsSavedToast();
    void changeScreen();
    void hideProgressBar();
    void showSmartLockPopUp(String username);
    void backUpUser();
    void resolveResult(Status status, int rcSave, boolean isResolving);
    void loginUser(String username);
    void retrieveCredentials();
    void deleteCredential();
    void saveCredentials(String username, String password);
    void enableButton();
}
