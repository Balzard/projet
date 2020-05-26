package com.example.see_u_festival.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.see_u_festival.Activity.ArtistesActivity.AbActivity;
import com.example.see_u_festival.Activity.MainActivity;
import com.example.see_u_festival.Activity.SettingsActivity;
import com.example.see_u_festival.R;
import com.example.see_u_festival.Utils.CustomListView;
import com.example.see_u_festival.Utils.ViewPagerAdapter;
import com.example.see_u_festival.ui.home.HomeViewModel;
import com.google.android.material.tabs.TabLayout;

import static android.os.Build.VERSION_CODES.O;

/**
 * A simple {@link Fragment} subclass.
 */
public class SaturdayFragment extends Fragment {

    private HomeViewModel homeViewModel;


    public SaturdayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saturday, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ViewPager viewPager = view.findViewById(R.id.viewpagersaturday);
        TabLayout tabLayout = view.findViewById(R.id.tablayoutsaturday);

        /** to attach tablayout with viewpager **/
        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        /** to add the fragments **/
        adapter.addFrag(new Stage1Fragment(), getString(R.string.scene_1));
        adapter.addFrag(new Stage2Fragment(), getString(R.string.scene_2));
        adapter.addFrag(new Stage3Fragment(), getString(R.string.scene_3));

        /** set adapter on viewpager **/
        viewPager.setAdapter(adapter);
    }
}


