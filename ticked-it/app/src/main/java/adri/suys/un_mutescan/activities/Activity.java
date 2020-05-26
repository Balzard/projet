package adri.suys.un_mutescan.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Objects;

import adri.suys.un_mutescan.R;
import adri.suys.un_mutescan.apirest.RestService;
import adri.suys.un_mutescan.festival.activites.AccueilFestivalActivity;
import adri.suys.un_mutescan.fragments.PreferenceFragment;
import adri.suys.un_mutescan.model.Event;
import adri.suys.un_mutescan.model.User;
import adri.suys.un_mutescan.utils.LocaleHelper;
import adri.suys.un_mutescan.utils.UnMuteDataHolder;

import static adri.suys.un_mutescan.activities.Notifications.CHANNEL_1_ID;

public abstract class Activity extends AppCompatActivity {

    private static final String USER = "user-backup";
    private static final String EVENTS = "events-backup";
    private static final String URLS = "urls-backup";
    private String initialLocale;
    private NotificationManagerCompat notificationManager;
    private boolean isDarkTheme;

    @Override
    protected void onCreate(Bundle state){
        super.onCreate(state);
        applySettings();
        initialLocale = LocaleHelper.getPersistedLocale(this);
        notificationManager = NotificationManagerCompat.from(this);

    }


    /**Apply the theme chosen by user in the preferences activity **/
    public void applySettings() {
        if (isDarkTheme = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(PreferenceFragment.KEY_DARK_MODE, true)) {
            setTheme(R.style.darkTheme);
        }
        else{
            setTheme(R.style.AppTheme);
        }
    }

    /** finish current activity by clicking on the back arrow in the toolbar **/
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    /**
     * Remove the title and the elevation (in order to remove the shadow underneath it) from the Action Bar
     * @param title is the title displayed in the action bar of the activity
     */
    void configActionBar(String title){
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_orange_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setIcon(R.drawable.ic_3_dots_24dp);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**To start a new activity or refresh the current by clicking on an item
     * home represents the back button in the action bar
     * **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.disconnect){
            makePendingRequest();
            UnMuteDataHolder.reinit();
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        } else if (id == R.id.faq){
            makePendingRequest();
            startActivity(new Intent(this, FAQActivity.class));
            return true;
        } else if (id == R.id.refresh){
            makePendingRequest();
            Intent i = new Intent(this, EventListActivity.class);
            i.putExtra("refresh", true);
            startActivity(i);
        } else if(id == R.id.settings){
            startActivity(new Intent(this, SettingsActivity.class));
        } else if(id==R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        menu.findItem(R.id.refresh).setVisible(!UnMuteDataHolder.isHideRefreshButton());
        return super.onPrepareOptionsMenu(menu);
    }

    public void hideMenuItem(boolean mustHide){
        UnMuteDataHolder.setHideRefreshButton(mustHide);
        invalidateOptionsMenu();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    /**
     * Display a Toast on the screen
     * @param msg the message that will be displayed in the toast
     */
    public void showToast(String msg){
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Checks if the device has an internet connexion
     * @return true if the device is connected to the internet, false otherwise
     */
    public boolean isInternetConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * Write all the data on the user in a internal storage file
     */
    public void backUpUser() {
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos = openFileOutput(USER, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(UnMuteDataHolder.getUser());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null){
                try {
                    oos.flush();
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Write all the data on the events in a internal storage file
     */
    public void backUpEvents() {
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos = openFileOutput(EVENTS, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(UnMuteDataHolder.getEvents());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null){
                try {
                    oos.flush();
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Write all the pending requests' url in a internal storage file.
     */
    public void backUpUrls() {
        try {
            FileOutputStream fos = openFileOutput(URLS, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(UnMuteDataHolder.getRequestURLs());
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the user's data stored in a internal storage file
     * @return the user
     */
    public User retrieveUserFromDB(){
        User user = null;
        try {
            FileInputStream fis = openFileInput(USER);
            ObjectInputStream ois = new ObjectInputStream(fis);
            user = (User) ois.readObject();
            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }

    @SuppressWarnings("unchecked")
    /**
     * Get the events' data stored in a internal storage file
     * @return the list of events
     */
    public List<Event> retrieveEvents(){
        List<Event> events = null;
        try {
            FileInputStream fis = openFileInput(EVENTS);
            ObjectInputStream ois = new ObjectInputStream(fis);
            events = (List<Event>) ois.readObject();
            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return events;
    }

    @SuppressWarnings("unchecked")
    /**
     * Get the pending requests' url stored in a internal storage file
     * @return the list of requests' url
     */
    private List<String> retrievePendingRequest(){
        List<String> requests = null;
        try {
            FileInputStream fis = openFileInput(URLS);
            ObjectInputStream ois = new ObjectInputStream(fis);
            requests = (List<String>) ois.readObject();
            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return requests;
    }

    /**
     * If the device is connected to the internet, tries to connect with the server
     * and make all the pending requests.
     */
    public void makePendingRequest(){
        if (isInternetConnected()) {
            List<String> pendingRequests = retrievePendingRequest();
            UnMuteDataHolder.setRequestURLs(pendingRequests);
            if (UnMuteDataHolder.getRequestURLs() != null && UnMuteDataHolder.getRequestURLs().size() > 0) {
                RestService restService = new RestService(this);
                restService.makePendingRequest();
            }
        }
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (initialLocale != null && !initialLocale.equals(LocaleHelper.getPersistedLocale(this))) {
            recreate();
        }
    }

    public void sendOnChannel1(View v) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean pref = prefs.getBoolean("notif_checkbox", true);

        if(pref){

            Intent activityIntent = new Intent(this, LoginActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this,
                    0, activityIntent, 0);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.app_logo)
                    .setContentTitle("Ticked-it!")
                    .setContentText("Nouvelle notification")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true)
                    .build();

            notificationManager.notify(1, notification);}
    }
}
