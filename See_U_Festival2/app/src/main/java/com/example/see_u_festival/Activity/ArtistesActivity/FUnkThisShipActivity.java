package com.example.see_u_festival.Activity.ArtistesActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.see_u_festival.Activity.Activity;
import com.example.see_u_festival.R;

public class FUnkThisShipActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_unk_this_ship);
        configActionBar("Funk This Ship");
    }
    public void goToFunkSpotify(View v){
        goToLink("https://open.spotify.com/artist/1bDpAxytfQIEN1hhZXpAE5");
    }

    public void goToFunkFB(View v){
        goToLink("https://www.facebook.com/funkthisship/");
    }

    public void goToFUnkInsta(View v){
        goToLink("https://www.instagram.com/funkthisship.official/");
    }

    public void goToFunkBand(View v){
        goToLink("https://funkthisship.bandcamp.com/releases");
    }
}
