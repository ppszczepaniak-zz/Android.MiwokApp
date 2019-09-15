package com.example.android.miwok;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class PhrasesFragment extends Fragment {


    public PhrasesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater someInflater, ViewGroup fragmentsContainer,
                             Bundle savedInstanceState) {
        View rootView = someInflater.inflate(R.layout.word_list, fragmentsContainer, false);

        final ArrayList<Words> phrasesWords = new ArrayList<>();
        phrasesWords.add(new Words("Tinnә oyaase'nә?", getString(R.string.what_is_your_name), R.raw.phrase_what_is_your_name));
        phrasesWords.add(new Words("Oyaaset...", getString(R.string.my_name_is), R.raw.phrase_my_name_is));
        phrasesWords.add(new Words("Michәksәs?", getString(R.string.how_are_you_feeling), R.raw.phrase_how_are_you_feeling));
        phrasesWords.add(new Words("Kuchi achit", getString(R.string.Im_feeling_good), R.raw.phrase_im_feeling_good));
        phrasesWords.add(new Words("Әәnәs'aa?", getString(R.string.are_you_coming), R.raw.phrase_are_you_coming));
        phrasesWords.add(new Words("Hәә’ әәnәm.", getString(R.string.yes_Im_coming), R.raw.phrase_yes_im_coming));
        phrasesWords.add(new Words("Yoowutis!", getString(R.string.lets_go), R.raw.phrase_lets_go));
        phrasesWords.add(new Words("Әәnәm.", getString(R.string.Im_coming), R.raw.phrase_im_coming));
        phrasesWords.add(new Words("Minto wuksus?", getString(R.string.where_are_you_going), R.raw.phrase_where_are_you_going));
        phrasesWords.add(new Words("Әnni'nem.", getString(R.string.come_here), R.raw.phrase_come_here));

        WordsAdapter numbersAdapter = new WordsAdapter(getActivity(), phrasesWords, R.color.category_phrases);
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
