package com.example.android.miwok;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class NumbersActivityBeforeFragmentsCopy extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        //tworzę ArrayList słów angielskich
        //używam ArrayList a nie zwykłej Array, bo:
        //1) może zmieniać rozmiar dynamicznie, 2)jest klasą, można na niej używać metod
        //3) minus jest taki, że przechowuje tylko obiekty (nie prmitive types)
        //Arrays.asList() to metoda Arrays (klasa nadrzędna ArrayList)

        /**
         * NAUKA: ważne: dodaję tu niżej final, żebym mógł robić odniesienia do ArrayList numberWords w OnItemClick (na dole)
         * bez tego, kiedy byłbym w klasie onItemClicklistenera (poza klasą NumbersActivity) nie mógłbym odpalać metody numberWords.get()
         *
         * OnItemClickListener jest Anonymous Class w tym wypadku, a
         * anonymous class can't access local variables in its ecnlosing scope that aren't declared
         * as final (or effectively final)
         *
         */
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

        //to samo uzyskałbym robiąc to:
        //  ArrayList<Words> words2 = new ArrayList<>();
        //  words2.add(new Words("abc","one"));
        //  words2.add(new Words("ghi","two"));
        //  words2.add("three");
        //  ...

        /**
         //poniżej logi sprawdzające, TODO można usunąć te logi
         for (int i = 0; i < numberWords.size(); i++) {
         Log.v("Numbers activity", "numberWords at index " + i + ": " + numberWords.get(i));
         }*/

/** PODEJŚCIE A)
         * Zanim użyłem ListView, tworzyłem po prostu TextViews w LinearLayout (był zamiast ListView dla word_list.xml  licy 'numberWords' w poniżsy sposób
         * ale to mało efektywne, bo nie recyklinguje views (zapycham pamięć itp)

         //tworzę TextViews z mojej tablicy
         LinearLayout rootView = findViewById(R.id.list); //znajduję rootView (nadrzędny)

         //pętla tworząca TextViews
         for (int i = 0; i < numberWords.size(); i++) {
         TextView wordView = new TextView(this); //tworzę textView (context: ta activity) - na razie jest "w próżni"
         wordView.setText(numberWords.get(i)); //wypełniam go treścią
         rootView.addView(wordView); //dodaję nowy view
         } */


/** PODEJŚCIE B) (oszczędza pamięć, używa ListView z ArrayAdapter (który podaję żądane Views do ListView).

         What is an Adapter anyway? An adapter is an object that provides views for a List View.
         Whenever List View needs to draw a view at a particular list position, it gets it from the adapter.

        //tworzęArrayAdapter typu String, który będzie zapełniał elementy listy w ListView
        // simple_list_item_1 to layout przykładowy dla tego adaptera (z Android Framework) z jednym TextView
        // dzięki temu ArrayAdapter wyświetli pojedyncze słowo

        ArrayAdapter<String> numbersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, words);*/

/**PODEJŚCIE C) <String> zmieniam na mój obiekt <Words>

         *  Potem stworzyłem klasę Words, która wrzuca dwa słowa.
         *  ArrayList to generic, więc mogę pod <T> wrucić dowolny obiekt.
         *  W tym wypadku jest to obiekt klasy Words, którą stworzyłem.
         *
         * Ponadto zmieniam ResourceID layoutu simple_list_item_1 na mój własny, który wrzuca dwa TextViews
         *
         ArrayAdapter<Words> numbersAdapter = new ArrayAdapter<>(this, R.layout.list_item, words2);


         /** ZAKOŃCZENIE DZIAŁANIA - TUTAJ SIĘ WYKRZACZA APP więc zrobiłem podejście D
         (1) tworzę ListView w word_list.xml (R.id.list)

         ListView numbersListView = findViewById(R.id.list);

         (2) zapełniam go słowami z mojej listy za pomocą Adaptera
         // Make the {@link ListView} use the {@link ArrayAdapter} we created above, so that the
         // {@link ListView} will display list items for each word in the list of words.
         // Do this by calling the setAdapter method on the {@link ListView} object and pass in
         // 1 argument, which is the {@link ArrayAdapter} with the variable name numbersAdapter.
         numbersListView.setAdapter(numbersAdapter);
         */

/** PODEJŚCIE D) własny ArrayAdapter (WordsAdapter)
         *
        //ZANIM STWORZYŁEM KLASĘ wordsAdapter.java, to kod wyglądał tak jak powyżej:
        //ALE: wykrzaczał się, bo ArrayAdapter wymaga w konstruktorze TextView, a ja wrzuciłem
        //swój layout: R.layout.list_item, zamiast default: android.R.layout.simple_list_item_1
        //mój layout nie ma 1 TV tylko LL z 2 TV
        //TO JEST POWÓD DLA KTÓREGO STWORZYŁEM KLASĘ WordsAdapter extends ArrayAdapter i w której
         //zrobiłem override metody getView(), żeby to działało
        */

        //tworzę Adapter, który będzie wrzucał tablicę do ListView w Activity Numbers
        //to jest mój własny Adapter (WordsAdapter), który extenduje ArrayAdapter do moich potrzeb

        WordsAdapter numbersAdapter = new WordsAdapter(this, numberWords, R.color.category_numbers);  //wrzucam 3 parametry, ale inne, patrz moja klasa WordsAdapter.java
        ListView numbersListView = findViewById(R.id.list);  //ID word_list.xml, w którym tworzę ListView z wieloma TextViews za pomocą Adaptera
        numbersListView.setAdapter(numbersAdapter);



/** TAK ROBIŁ NANODEGREE, JA ZNALAZŁEM SWOJE ROZWIĄZANIE W WordsAdapter.java - listener kliku tylko na textContainer, a nie tez na obrazku
        //ustawiam onItemClikListener - taka metoda AdapterView (listView)
        numbersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //id to wiersz w View (w tym wypadku = position)
                Words specificItemInArrayList = numberWords.get(position); //tutaj biore konkretny obiekt z mojej ArrayList<Words> za pomoca ArrayList.get() z pozycji position
                final MediaPlayer mediaPlayer = MediaPlayer.create(NumbersActivity.this, specificItemInArrayList.getSoundResourceId());
                mediaPlayer.start();


                                                  //TEGO HANDLERA UŻYWAŁEM ZANIM POZNAŁEM OnCompletitionListener MediaPlayer. O
                                                  //Od tego momentu jest zbędny
                Handler handler = new Handler();  //to dodałem sam (znalazłem na stacku), postDelayed() czeka do końca audio pliku (mediaPlayer.getDuration()) i wtedy odpala
                                                  // run() ktory odpadla mediaPlayer.stop(); i mediaPlayer.release()
                                                  // to zrobiłem po to, zeby oczyscic pamiec z obiektu MediaPlayer jak skonczy grac
                                                  // inaczej po kilkudziesieciu kliknieciach przestawał działać (zapychał pamięć?)
                handler.postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        mediaPlayer.stop();
                        mediaPlayer.release(); }
                }, mediaPlayer.getDuration());
            }
        });*/
    }

    @Override  //czyszcze pamiec podczas przechodzenia do stop przez usera
    protected void onStop() {
        super.onStop();
        WordsAdapter.releaseMediaPlayer();
    }
}
