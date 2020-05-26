package com.example.see_u_festival.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.see_u_festival.R;
import com.example.see_u_festival.Utils.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class SundayFragment extends Fragment {


    public SundayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sunday, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager viewPager = view.findViewById(R.id.viewpagersunday);
        TabLayout tabLayout = view.findViewById(R.id.tablayoutsunday);

        /** to attach tablayout with viewpager **/
        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        /** to add the fragments **/
        adapter.addFrag(new Stage1SundayFragment(), getString(R.string.scene_1));
        adapter.addFrag(new Stage2SundayFragment(), getString(R.string.scene_2));
        adapter.addFrag(new Stage3SundayFragment(), getString(R.string.scene_3));

        /** set adapter on viewpager **/
        viewPager.setAdapter(adapter);
    }
}
