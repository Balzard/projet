package adri.suys.un_mutescan.festival.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.util.Map;
import java.util.Objects;

import adri.suys.un_mutescan.R;

public class AccueilFestivalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil_festival);
        configActionBar();
    }

    void configActionBar(){
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.festival_action_bar));
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id==android.R.id.home){
            this.finish();}

        return super.onOptionsItemSelected(item);
    }

    public void goToMap(View view){
        Intent i = new Intent(AccueilFestivalActivity.this, MapActivity.class);
        startActivity(i);
    }

}
