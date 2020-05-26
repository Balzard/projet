package com.example.see_u_festival.Activity.ArtistesActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.TextView;

import com.example.see_u_festival.Activity.Activity;
import com.example.see_u_festival.R;

import java.util.Objects;

public class AbActivity extends Activity {

    TextView textView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ab);
        configActionBar(getString(R.string.ab));
        textView = findViewById(R.id.description_ab);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textView.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }
    }

    public void goToAbTwitter(View v){
        goToLink("https://twitter.com/ABmusiccc");
    }

    public void goToAbFb(View v){
        goToLink("https://m.facebook.com/abmusicc");
    }

    public void goToAbInsta(View v){
        goToLink("https://m.instagram.com/abbmusicc/");
    }

    public void goToAbsoundcloud(View v){
        goToLink("https://m.soundcloud.com/abmusicc");
    }
}
