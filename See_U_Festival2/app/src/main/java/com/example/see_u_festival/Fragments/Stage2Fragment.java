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
public class Stage2Fragment extends Fragment {

    private ListView listView;
    private String[] schedule = {"14h30","15h30","16h30","17h30","18h30"};
    private Integer[] imgID = {R.drawable.jimmy_marguerite,R.drawable.jizznjazz,R.drawable.labal_collectif,R.drawable.mac_lister,R.drawable.artiste_default};
    private String[] onAir = {"","","","",""};
    private String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    private String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
    private String[] artistesName = {"Jimmy Marguerite","Jizz'N Jazz","Labal Collectif","Mac Lister","ArtSchool"};

    public Stage2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stage2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = getView().findViewById(R.id.listviewstage2);

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
            if((heure == 14 && min >=30) || (heure==15 && min <30)){
                onAir[0] = getString(R.string.on_air);
                artistesName[0] = "";
                schedule[0]= "";}
            else if ((heure == 15 && min >=30) || (heure==16 && min <30)){
                onAir[1] = getString(R.string.on_air);
                artistesName[1] = "";
                schedule[1] = "";

                onAir[0] = getString(R.string.past);
                artistesName[0] = "";
                schedule[0]= "";
            }
            else if ((heure == 16 && min >=30) || (heure==17 && min <30)){
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
            else if((heure == 17 && min >=30) || (heure==18 && min <30)){
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
            else if((heure == 18 && min >=30) || (heure==19 && min <30)){
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
            else if (heure > 19){
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
