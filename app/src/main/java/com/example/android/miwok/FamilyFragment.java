package com.example.android.miwok;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class FamilyFragment extends Fragment {


    public FamilyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater someInflater, ViewGroup fragmentsContainer,
                             Bundle savedInstanceState) {
        View rootView = someInflater.inflate(R.layout.word_list, fragmentsContainer, false);
        //attachToRoot:false oznacza chyba ze nie tworzymy jeszcze tego View, tylko dopiero w return rootView


        final ArrayList<Words> familyWords = new ArrayList<>();
        familyWords.add(new Words("әpә", getString(R.string.father), R.drawable.family_father, R.raw.family_father));
        familyWords.add(new Words("әṭa", getString(R.string.mother), R.drawable.family_mother, R.raw.family_mother));
        familyWords.add(new Words("angsi", getString(R.string.son), R.drawable.family_son, R.raw.family_son));
        familyWords.add(new Words("tune", getString(R.string.daughter), R.drawable.family_daughter, R.raw.family_daughter));
        familyWords.add(new Words("taachi", getString(R.string.older_brother), R.drawable.family_older_brother, R.raw.family_older_brother));
        familyWords.add(new Words("teṭe", getString(R.string.older_sister), R.drawable.family_older_sister, R.raw.family_older_sister));
        familyWords.add(new Words("chalitti", getString(R.string.younger_brother), R.drawable.family_younger_brother, R.raw.family_younger_brother));
        familyWords.add(new Words("kolliti", getString(R.string.younger_sister), R.drawable.family_younger_sister, R.raw.family_younger_sister));
        familyWords.add(new Words("paapa", getString(R.string.grandfather), R.drawable.family_grandfather, R.raw.family_grandfather));
        familyWords.add(new Words("ama", getString(R.string.grandmother), R.drawable.family_grandmother, R.raw.family_grandmother));


        WordsAdapter numbersAdapter = new WordsAdapter(getActivity(), familyWords, R.color.category_family);
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
