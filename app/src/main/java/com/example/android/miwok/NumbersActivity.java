package com.example.android.miwok;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class NumbersActivity extends AppCompatActivity {

    /**
     * We’re going to use this simplified activity that sets the activity_category.xml as the content view.
     * Then a new NumbersFragment is created and inserted it into the container view, using a FragmentTransaction
     * (no need to understand the details of this now). fragmnent_container has “match_parent”, so NumbersFragment will take up the whole screen.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getSupportFragmentManager().beginTransaction()  // to dochodzi, zeby odpalic Fragment właściwy (FragmentTransaction)
                .replace(R.id.fragments_container, new NumbersFragment())
                .commit();
    }

    /*
   To clarify, the NumbersActivity.java used to display the word_list.xml layout.
   Now, the NumbersActivity displays the activity_category.xml layout,
   and the NumbersFragment displays the word_list.xml layout.
     */
    /**
     * to było przed Fragments
     *
     * @Override protected void onCreate(Bundle savedInstanceState) {
     * super.onCreate(savedInstanceState);
     * setContentView(R.layout.word_list);
     * (...)
     */
}