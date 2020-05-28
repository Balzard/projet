package adri.suys.un_mutescan.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;

import java.util.Objects;

import adri.suys.un_mutescan.R;
import adri.suys.un_mutescan.festival.activites.AccueilFestivalActivity;
import adri.suys.un_mutescan.fragments.AudienceFragment;
import adri.suys.un_mutescan.fragments.BarcodeManualInputFragment;
import adri.suys.un_mutescan.fragments.BarcodeScannerFragment;
import adri.suys.un_mutescan.fragments.BuyTicketOnSiteFragment;
import adri.suys.un_mutescan.fragments.EventStatFragment;
import adri.suys.un_mutescan.model.Event;
import adri.suys.un_mutescan.presenter.EventPresenter;
import adri.suys.un_mutescan.presenter.EventStatPresenter;
import adri.suys.un_mutescan.utils.UnMuteDataHolder;
import adri.suys.un_mutescan.viewinterfaces.EventListViewInterface;
import adri.suys.un_mutescan.viewinterfaces.EventStatViewInterface;

public class OneEventActivity extends Activity {

    private BottomNavigationView bottomNavigationView;
    private static final int STAT_FRAGMENT = 0;
    private static final int SCAN_FRAGMENT = 1;
    private static final int INPUT_FRAGMENT = 2;
    private static final int ADD_FRAGMENT = 3;
    private static final int PUBLIC_FRAGMENT = 4;

    public OneEventActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_event);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        hideMenuItem(false);
        setNavigationActions();
        loadFragment(new EventStatFragment());
        configActionBar(getString(R.string.my_event));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id==android.R.id.home){
            this.finish();}

        return super.onOptionsItemSelected(item);
    }

    public void goToManualBarcodeActivity(View view){
        Intent i = new Intent(OneEventActivity.this,BarcodeManualActivity.class);
        startActivity(i);
    }


    @Override
    public void onResume() {
        super.onResume();
        switch (UnMuteDataHolder.getCurrentFragment()){
            case STAT_FRAGMENT :
                bottomNavigationView.setSelectedItemId(R.id.action_stat);
                loadFragment(new EventStatFragment());
                break;
            case SCAN_FRAGMENT :
                bottomNavigationView.setSelectedItemId(R.id.action_qrcode);
                loadFragment(new BarcodeScannerFragment());
                break;
            //case INPUT_FRAGMENT :
                //bottomNavigationView.setSelectedItemId(R.id.action_type_code);
                //loadFragment(new BarcodeManualInputFragment());
                //break;
            case ADD_FRAGMENT :
                bottomNavigationView.setSelectedItemId(R.id.action_add_ticket);
                loadFragment(new BuyTicketOnSiteFragment());
                break;
            case PUBLIC_FRAGMENT :
                bottomNavigationView.setSelectedItemId(R.id.action_get_guest);
                loadFragment(new AudienceFragment());
                break;
        }
    }

    public void goToCreateTicketActivity(View view){
        Intent i = new Intent(OneEventActivity.this,CreateTicketActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(this, EventListActivity.class);
        i.putExtra("refresh", false);
        startActivity(i);
    }


    private void setNavigationActions(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_stat :
                        makePendingRequest();
                        loadFragment(new EventStatFragment());
                        UnMuteDataHolder.setCurrentFragment(STAT_FRAGMENT);
                        return true;
                    case R.id.action_qrcode :
                        //if (UnMuteDataHolder.getEvent().isToday()){
                            loadFragment(new BarcodeScannerFragment());
                            UnMuteDataHolder.setCurrentFragment(SCAN_FRAGMENT);
                        //} else {
                            //showAlertDialog(getResources().getString(R.string.nav_error_scan));
                        //}
                        makePendingRequest();
                        return true;
                    //case R.id.action_type_code :
                        //startActivity(new Intent(OneEventActivity.this, AccueilFestivalActivity.class));
                        //if (UnMuteDataHolder.getEvent().isToday()) {
                            //loadFragment(new BarcodeManualInputFragment());
                            //UnMuteDataHolder.setCurrentFragment(INPUT_FRAGMENT);
                        //} else {
                            //showAlertDialog(getResources().getString(R.string.nav_error_keyboard));
                        //}
                        //makePendingRequest();
                        //return true;
                    case R.id.action_add_ticket :
                        //if (UnMuteDataHolder.getEvent().isToday()) {
                            loadFragment(new BuyTicketOnSiteFragment());
                            UnMuteDataHolder.setCurrentFragment(ADD_FRAGMENT);
                        //} else {
                            //showAlertDialog(getResources().getString(R.string.nav_error_sell));
                        //}
                        makePendingRequest();
                        return true;
                    case R.id.action_get_guest:
                        //if (UnMuteDataHolder.getEvent().isToday()) {
                            loadFragment(new AudienceFragment());
                            UnMuteDataHolder.setCurrentFragment(PUBLIC_FRAGMENT);
                        //} else {
                            //showAlertDialog(getResources().getString(R.string.nav_error_audience));
                        //}
                        makePendingRequest();
                        return true;
                    default :
                        // do noting
                        return false;
                }
            }
        });
    }

    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

   private void showAlertDialog(String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogNotTodayStyle);
        alertDialogBuilder.setMessage(message).setTitle("");
        alertDialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.setTitle(R.string.error);
        alert.show();
    }

}
