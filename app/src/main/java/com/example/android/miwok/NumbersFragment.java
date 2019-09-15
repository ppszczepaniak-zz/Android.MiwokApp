package com.example.android.miwok;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class NumbersFragment extends Fragment {


    public NumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater someInflater, ViewGroup fragmentsContainer,
                             Bundle savedInstanceState) {
        View rootView = someInflater.inflate(R.layout.word_list, fragmentsContainer, false);
        //attachToRoot:false oznacza chyba ze nie tworzymy jeszcze tego View, tylko dopiero w return rootView


        final ArrayList<Words> numberWords = new ArrayList<>(Arrays.asList(
                new Words("lutti", getString(R.string.one), R.drawable.number_one, R.raw.number_one),
                new Words("otiiko", getString(R.string.two), R.drawable.number_two, R.raw.number_two),
                new Words("tolookosu", getString(R.string.three), R.drawable.number_three, R.raw.number_three),
                new Words("oyyisa", getString(R.string.four), R.drawable.number_four, R.raw.number_four),
                new Words("massokka", getString(R.string.five), R.drawable.number_five, R.raw.number_five),
                new Words("temmokka", getString(R.string.six), R.drawable.number_six, R.raw.number_six),
                new Words("kenekaku", getString(R.string.seven), R.drawable.number_seven, R.raw.number_seven),
                new Words("kawinta", getString(R.string.eight), R.drawable.number_eight, R.raw.number_eight),
                new Words("wo’e", getString(R.string.nine), R.drawable.number_nine, R.raw.number_nine),
                new Words("na’aacha", getString(R.string.ten), R.drawable.number_ten, R.raw.number_ten)));

        WordsAdapter numbersAdapter = new WordsAdapter(getActivity(), numberWords, R.color.category_numbers);
        /** zanim zrobiłem fragments, w  NumbersActivity.java było "WordsAdapter numbersAdapter = new WordsAdapter(<this>, numberWords, R.color.category_numbers);"
         *  tutaj <this> nie działa, bo Fragment to nie jest prawidłowy Context (prawidłowym była klasa NumbersActivity.java
         *  wrzucam getActivity(), ktre przekazuje odniesienie do Activty, ktora obejmuje ten Fragment, jako Context
         *  ||The method getActivity() is normally used in fragments to get the context of the activity in which they are inserted or inflated.
         */

        ListView numbersListView = rootView.findViewById(R.id.list); //w Activity było po prostu findViewById
        // tu musi być rootView.findViewById; rootView object ma id list, bo powstał z xml word_list
        // poza tym zawiera on children views rodzaju ListView
        numbersListView.setAdapter(numbersAdapter);


        return rootView;
    }


    @Override  //czyszcze pamiec podczas przechodzenia do stop przez usera
    public void onStop() {
        super.onStop();
        WordsAdapter.releaseMediaPlayer();
    }

}
