package com.example.see_u_festival.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.see_u_festival.Activity.ArtistesActivity.AbActivity;
import com.example.see_u_festival.Activity.SettingsActivity;
import com.example.see_u_festival.R;
import com.example.see_u_festival.Utils.CustomListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class Stage3Fragment extends Fragment {

    private ListView listView;
    private String[] schedule = {"16h","17h","18h","19h","20h"};
    private Integer[] imgID = {R.drawable.liquid_zenith,R.drawable.marylene_corro,R.drawable.b_astre,R.drawable.tuktuk_thailand,R.drawable.caberu};
    private String[] onAir = {"","","","",""};
    private String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    private String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
    private String[] artistesName = {"Liquid Zenith","Marylène Corro","B-Astre","TUK TUK Thailand","Cabéru"};

    public Stage3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stage3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = getView().findViewById(R.id.listviewstage3);

        CustomListView customListView = new CustomListView(getActivity(),imgID,schedule,artistesName,onAir);
        listView.setAdapter(customListView);

        /** Detect which element is selected in the listview and set onCLickListener for each element
         * @param position is the index of the element in the listview
         * **/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /** add intent here **/
            }});

        /* day if the current date **/
        String day = currentDate.substring(0,2);
        int jour = Integer.parseInt(day);

        /* month of the current date **/
        String month = currentDate.substring(3,5);
        int mois = Integer.parseInt(month);

        /* year of the current date **/
        String year = currentDate.substring(6,10);
        int annee = Integer.parseInt(year);

        /* hour of the current time **/
        String hour = currentTime.substring(0,2);
        int heure = Integer.parseInt(hour);

        /* minute of the current time **/
        String minutes = currentTime.substring(3,5);
        int min = Integer.parseInt(minutes);

        /* secondes of the current time **/
        String secondes = currentTime.substring(6,8);
        int sec = Integer.parseInt(secondes);


        /* You can here configure the different schedules to display an ON AIR textview
           for the good artist if it's its time to be on the scene
           Example here **/
        if (annee == 2020 && mois == 5 && jour == 16){
            if(heure == 16){
                onAir[0] = getString(R.string.on_air);
                artistesName[0] = "";
                schedule[0]= "";}
            else if (heure == 17){
                onAir[1] = getString(R.string.on_air);
                artistesName[1] = "";
                schedule[1] = "";

                onAir[0] = getString(R.string.past);
                artistesName[0] = "";
                schedule[0]= "";
            }
            else if (heure == 18){
                onAir[2] = getString(R.string.on_air);
                artistesName[2] = "";
                schedule[2]= "";

                onAir[1] = getString(R.string.past);
                artistesName[1] = "";
                schedule[1] = "";

                onAir[0] = getString(R.string.past);
                artistesName[0] = "";
                schedule[0]= "";
            }
            else if (heure == 19){
                onAir[3] = getString(R.string.on_air);
                artistesName[3] = "";
                schedule[3]= "";

                onAir[2] = getString(R.string.past);
                artistesName[2] = "";
                schedule[2]= "";

                onAir[1] = getString(R.string.past);
                artistesName[1] = "";
                schedule[1] = "";

                onAir[0] = getString(R.string.past);
                artistesName[0] = "";
                schedule[0]= "";
            }
            else if (heure == 20){
                onAir[4] = getString(R.string.on_air);
                artistesName[4] = "";
                schedule[4]= "";

                onAir[3] = getString(R.string.past);
                artistesName[3] = "";
                schedule[3]= "";

                onAir[2] = getString(R.string.past);
                artistesName[2] = "";
                schedule[2]= "";

                onAir[1] = getString(R.string.past);
                artistesName[1] = "";
                schedule[1] = "";

                onAir[0] = getString(R.string.past);
                artistesName[0] = "";
                schedule[0]= "";
            }
            else if (heure > 20){
                onAir[4] = getString(R.string.past);
                artistesName[4] = "";
                schedule[4]= "";

                onAir[3] = getString(R.string.past);
                artistesName[3] = "";
                schedule[3]= "";

                onAir[2] = getString(R.string.past);
                artistesName[2] = "";
                schedule[2]= "";

                onAir[1] = getString(R.string.past);
                artistesName[1] = "";
                schedule[1] = "";

                onAir[0] = getString(R.string.past);
                artistesName[0] = "";
                schedule[0]= "";
            }


        }
    }
}
