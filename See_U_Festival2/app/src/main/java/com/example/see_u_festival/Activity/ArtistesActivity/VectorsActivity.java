package com.example.see_u_festival.Activity.ArtistesActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.see_u_festival.Activity.Activity;
import com.example.see_u_festival.R;

public class VectorsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vectors);
        configActionBar(getString(R.string.vector));
    }

    public void goToVectorsSite(View v){
        goToLink("https://thevectors.net/");
    }

    public void goToVectorsFb(View v){
        goToLink("https://www.facebook.com/thevectorsmusic/");
    }

    public void goToVectorsSoundcloud(View v){
        goToLink("https://soundcloud.com/the-vectors");
    }
}
