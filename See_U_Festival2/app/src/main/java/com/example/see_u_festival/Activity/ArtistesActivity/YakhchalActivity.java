package com.example.see_u_festival.Activity.ArtistesActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.see_u_festival.Activity.Activity;
import com.example.see_u_festival.R;

public class YakhchalActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yakhchal);
        configActionBar("Yakhchal");
    }

    public void goToYakchalSpotify(View v){
        goToLink("https://open.spotify.com/artist/0ZYrDurLvnluaI3yVGaIP2?si=5twxCHk1Tdexbo1834I16g");
    }

    public void goToYakhchalFb(View v){
        goToLink("https://www.facebook.com/yakhchalmusic/");
    }

    public void goToYakhchalInsta(View v){
        goToLink("https://www.instagram.com/yakhchal_music/");
    }

    public void goToYakhchalBand(View v){
        goToLink("https://yakhchal.bandcamp.com/");
    }
}
