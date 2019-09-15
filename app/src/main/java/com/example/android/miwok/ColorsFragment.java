package com.example.android.miwok;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;


public class ColorsFragment extends Fragment {


    public ColorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater someInflater, ViewGroup fragmentsContainer,
                             Bundle savedInstanceState) {
        View rootView = someInflater.inflate(R.layout.word_list, fragmentsContainer, false);
        //attachToRoot:false oznacza chyba ze nie tworzymy jeszcze tego View, tylko dopiero w return rootView


        final ArrayList<Words> colorWords = new ArrayList<>();
        colorWords.add(new Words("weṭeṭṭi", getString(R.string.red), R.drawable.color_red, R.raw.color_red));
        colorWords.add(new Words("chiwiiṭә", getString(R.string.mustard_yellow), R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));
        colorWords.add(new Words("ṭopiisә", getString(R.string.dusty_yellow), R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        colorWords.add(new Words("chokokki", getString(R.string.green), R.drawable.color_green, R.raw.color_green));
        colorWords.add(new Words("ṭakaakki", getString(R.string.brown), R.drawable.color_brown, R.raw.color_brown));
        colorWords.add(new Words("ṭopoppi", getString(R.string.gray), R.drawable.color_gray, R.raw.color_gray));
        colorWords.add(new Words("kululli", getString(R.string.black), R.drawable.color_black, R.raw.color_black));
        colorWords.add(new Words("kelelli", getString(R.string.white), R.drawable.color_white, R.raw.color_white));

        WordsAdapter numbersAdapter = new WordsAdapter(getActivity(), colorWords, R.color.category_colors);
        ListView numbersListView = rootView.findViewById(R.id.list);
        numbersListView.setAdapter(numbersAdapter);

        return rootView;
    }

    @Override  //czyszcze pamiec podczas przechodzenia do stop przez usera
    public void onStop() {
        super.onStop();
        WordsAdapter.releaseMediaPlayer();
    }

}
