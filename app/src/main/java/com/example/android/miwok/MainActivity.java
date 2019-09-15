
package com.example.android.miwok;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);

        /**
         * NAUKA: są 2 sposoby tworzenia obiektu:
         * <klasa> <nazwa>  = new <klasa>() np. String imieKota = new String("Mruczek");
         * lub
         * <klasa> <nazwa>  = <klasa>.<static_"factory"_method>() np. String imieKota = String.stworzKota("Mruczek");
         * PS nie ma metody stworzKota w klasie String, to tylko abstrakcyjny przykład ;)
         *
         * tworzę obiekt za pomocą "factory method" <nazwa_klasy>.create()
         * create() to static funkcja klasy MediaPlayer - przypisana do klasy, nie do obiektu, powołuję ją j/w
         * start() to nie static funkcja, jest przypisana do instancji klasy, powołuję ją <moja_nazwa_obiektu>.start(), patrz niżej
         */

        //poniżej tworzę 4 OnClickListenery, które otwierają nowe 4 activities po kliknięciu odpowiedniego TextView

        TextView numbersTextView = findViewById(R.id.numbers);
        numbersTextView.setOnClickListener(new View.OnClickListener() {
            //ten kod wykonywany po kliknięciu tego TextView (Asynchronous callback)
            @Override
            public void onClick(View view) {
                Intent numbersIntent = new Intent(MainActivity.this, NumbersActivity.class);
                startActivity(numbersIntent);
                /**
                 * WAŻNE: context Intenta jest 'MainActivity.this', a nie samo 'this', bo:
                 * 'this' refers to your current object. In your case you must have implemented the intent
                 * in an inner class ClickEvent, and thats what it points to.
                 * 'Activity.this' points to the instance of the Activity you are currently in.
                 *
                 * onClickListener to anonymous class, bo nie podaję jego nazwy, tylko zagnieżdżam w kodzie
                 * z tego powodu nie mam dostępu do obiektów spoza tego kodu, chyba że są final
                 *
                 * To oznacza, że poza obiektem OnClickListener wystarczyłby sam 'this'
                 */
            }
        });

        TextView colorsTextView = findViewById(R.id.colors);
        colorsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //krótsza wersja tego co wyżej (nie tworzę referencji obiektu)
                startActivity(new Intent(MainActivity.this, ColorsActivity.class));
            }
        });

        // jeszcze krótsza wersja, nie tworzę referencji do istniejącego textview, tylko na nim od razu
        // odpalam setOnClickListener() (po tym jak go znajdę)
        findViewById(R.id.family).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FamilyActivity.class));
            }
        });

        findViewById(R.id.phrases).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PhrasesActivity.class));
            }
        });
    }

    /** wcześniej to samo uzyskiwałem za pomocą android:onClick = "odpowiedniaFunkcja" w XML i
     * definiowaniu 4 funkcji poniżej
     *

     // 4 funcje, które otwierają odpowiednie activities (używam w tym celu explicit intent)
     public void openNumbersList(View v) {
     Intent numbersIntent = new Intent(this, NumbersActivity.class);
     startActivity(numbersIntent);
     }

     public void openColorsList(View v) {
     Intent colorsIntent = new Intent(this, ColorsActivity.class);
     startActivity(colorsIntent);
     }

     public void openFamilyList(View v) {
     Intent familyIntent = new Intent(this, FamilyActivity.class);
     startActivity(familyIntent);
     }

     public void openPhrasesList(View v) {
     Intent phrasesIntent = new Intent(this, PhrasesActivity.class);
     startActivity(phrasesIntent);
     } */
}
