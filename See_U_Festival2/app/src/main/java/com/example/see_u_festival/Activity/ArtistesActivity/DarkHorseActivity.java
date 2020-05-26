package com.example.see_u_festival.Activity.ArtistesActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.see_u_festival.Activity.Activity;
import com.example.see_u_festival.R;

public class DarkHorseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dark_horse);
        configActionBar("Dark Horse");
    }
    public void goToDarkHorseSite(View v){
        goToLink("https://vimeo.com/328351544");
    }
}
