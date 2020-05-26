package adri.suys.un_mutescan.presenter;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Date;

import adri.suys.un_mutescan.model.User;
import adri.suys.un_mutescan.security.BCrypt;
import adri.suys.un_mutescan.utils.UnMuteDataHolder;
import adri.suys.un_mutescan.viewinterfaces.LoginViewInterface;

public class LoginPresenter extends Presenter {

    private String username;
    private String password;
    private static final int NOT_FOUND = 404;
    private static final int CANT_ACCESS = 403;
    private static final int OK = 400;
    private static final int WRONG_CREDENTIAL = 405;
    private final LoginViewInterface view;
    private boolean hasCredentialsStocked;

    public LoginPresenter(LoginViewInterface view){
        this.view = view;
    }

    public void logUser(boolean isInternetConnected, String username, String password){
        if (isInternetConnected){
            this.username = username;
            this.password = password;
            view.loginUser(username);
        } else {
            User userInDB = view.retrieveUserFromDB();
            if (userInDB != null && userInDB.getUsername().equals(username) && userInDB.isTokenStillActive()){
                handleNewConnection(userInDB);
                view.showNoAccessToInternetToast();
                view.changeScreen();
            } else {
                view.hideProgressBar();
                view.showConnectionProblemToast();
            }
        }
    }

    public void logUserSmartlock(String password, String id) {
        this.password = password;
        this.username = id;
        view.loginUser(username);
    }

    public void handleJSONObject(JSONObject response, Gson gson) {
        User user = gson.fromJson(response.toString(), User.class);
        switch (validateUser(user, password)) {
            case NOT_FOUND:
                view.showUnvalidUsernameToast();
                break;
            case CANT_ACCESS:
                view.showCantUseAppToast();
                break;
            case OK:
                if (!hasCredentialsStocked) view.saveCredentials(user.getUsername(), password);
                handleNewConnection(user);
                view.showHelloToast(user.getName());
                view.changeScreen();
                break;
            default:
                view.showBadCredentialsToast();
                break;
        }
        view.enableButton();
        view.hideProgressBar();
    }

    public void handleVolleyError(VolleyError error) {
        if (error instanceof NoConnectionError || error instanceof TimeoutError){
            view.showNoConnectionRetryToast();
            System.out.println("wifi 0");
            if (error instanceof  NoConnectionError){
                System.out.println("no connection error");
            } else {
                System.out.println("time out error");
            }
        } else if (error instanceof AuthFailureError){
            view.showServerConnectionProblemToast();
            System.out.println("wifi 1");
        } else if (error instanceof ServerError){
            view.showServerConnectionProblemToast();
            System.out.println("wifi 2");
        } else if (error instanceof NetworkError) {
            view.showServerConnectionProblemToast();
            System.out.println("wifi 3");
        } else if (error instanceof ParseError){
            view.showServerConnectionProblemToast();
            System.out.println("wifi 4");
        }
        view.enableButton();
        view.hideProgressBar();
    }

    public LoginViewInterface getView() {
        return view;
    }

    public void retrieveCredentials(){
        view.retrieveCredentials();
    }

    public void changeCreds(){
        hasCredentialsStocked = false;
        view.deleteCredential();
    }

    private int validateUser(User user, String input){
        if (user.getError().equals("Cet utilisateur n'existe pas.")){
            return NOT_FOUND;
        }
        if (user.getError().equals("Vous ne pouvez pas utiliser l'application. Vous devez être gestionnaire de campagnes.")){
            return CANT_ACCESS;
        }
        if (checkPwd(user.getPassword(), input)){
            return OK;
        } else {
            return WRONG_CREDENTIAL;
        }
    }

    private boolean checkPwd(String password, String input) {
        String userHash = password.substring(0, 2) + "a" + password.substring(3);
        String computedHash = hashPassword(input);
        boolean verifyPassword = checkPassword(input, userHash);
        boolean verifyComputed = checkPassword(input, computedHash);
        return verifyPassword && verifyComputed;
    }

    private boolean checkPassword(String input, String userHash) {
        if(null == userHash || !userHash.startsWith("$2a$")) {
            throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");
        }
        return BCrypt.checkpw(input, userHash);
    }

    private String hashPassword(String input) {
        int workload = 12; // arbitrary choice
        String salt = BCrypt.gensalt(workload);
        return BCrypt.hashpw(input, salt);
    }

    public void handleNewConnection(User user){
        user.setLastConnection(new Date().getTime());
        UnMuteDataHolder.setUser(user);
        view.backUpUser();
    }

    public void setHasCredentialsStocked(boolean hasCredentialsStocked) {
        this.hasCredentialsStocked = hasCredentialsStocked;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
