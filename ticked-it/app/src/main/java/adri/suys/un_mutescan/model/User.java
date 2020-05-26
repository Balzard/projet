package adri.suys.un_mutescan.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * An user of the app, he must be in the UM database + he must organize event to access the app
 */
public class User implements Serializable {

    private String username;
    private String password;
    private final String id;
    private final String name;
    private final String error;
    private long lastConnection;

    /**
     *
     * @param id its id
     * @param username its username
     * @param password its crypted password
     * @param error the error message if the user has no access to the app, nothing if the user has access
     */
    public User(int id, String username, String name, String password, String error) {
        this.id = Integer.toString(id);
        this.username = username;
        this.name = name;
        this.password = password;
        this.error = error;
    }

    public int getId() {
        return Integer.parseInt(id);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getError(){
        return error;
    }

    public String getName(){
        return name;
    }

    public String toString(){
        return id + "-" + username + "-" + password + "-" + error;
    }

    public void setLastConnection(long lastConnection) {
        this.lastConnection = lastConnection;
    }

    public boolean isTokenStillActive(){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        String lastConnection = formatter.format(new Date(this.lastConnection));
        String now = formatter.format(new Date());
        return lastConnection.equals(now);
    }
}
