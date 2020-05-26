package com.example.see_u_festival.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public abstract class Activity extends AppCompatActivity {

    /**Personnalize the action bar of an activity
     * @param title is the title displayed in the action bar of the activity
     * **/
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void configActionBar(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**Close the current activity when back button pressed**/
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id==android.R.id.home){
            this.finish();}

        return super.onOptionsItemSelected(item);
    }

    public void goToUrl(View v){
        goToLink("https://un-mute.be/benevoles");
    }

    public void goToTickets(View v){
        goToLink("https://un-mute.be/tickets");
    }

    /**Allow to redirect to an url **/
    public void goToLink(String link){
        Uri uriUrl = Uri.parse(link);
        Intent LaunchBrowser = new Intent(Intent.ACTION_VIEW,uriUrl);
        startActivity(LaunchBrowser);
    }
}
