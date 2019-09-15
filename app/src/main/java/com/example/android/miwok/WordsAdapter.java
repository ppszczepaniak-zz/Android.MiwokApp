package com.example.android.miwok;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

//tworzę Adapter, który będzie wrzucał tablicę do ListView w Activity Numbers
//to jest mój własny Adapter (WordsAdapter), który extenduje ArrayAdapter do moich potrzeb
//zwykły Adapter domaga się layout z jednym TextView i taki wyświetla, a ja potrzebuję dwóch słów i obrazka
public class WordsAdapter extends ArrayAdapter<Words> {
    //MEDIA PLAYER
    static private MediaPlayer mediaPlayer;  //static bo releaseMediaPlayer() jest static
    //AUDIO MANAGER
    static AudioManager mAudioManager; //handles audio focus when playing sound file, static bo powołam go poprzez static  releaseMediaPlayer()
    /** AudioManager roadmap:
     * A1) create it (in getView() onclick below)
     * A2) ask for audio focus (needs OnAudioFocusChangeListener inteface to do it) //when wants to play music (in getView() onclick below)
     * A3) use OnAudioFocusChangeListener callback method onAudioFocuschange to adapt playback behaviour of your app for different situations
     * A4) release audio focus when not needed */
    //AUDIOMANAGER: kod wziety z developer.android.com/../audio-focus
    //AudioManager is a system service, which allows us to:
    //1. Request audio focus
    //2. Abandon audio focus
    //3. Register a listener to get notified when audio focus changes
    //|| jak rozumiem: to jest service (app systemowa uzywana przez inne app zeby cos zrobic dla nich, np. dostarczyc dzwiek)
    //|| jest to klasa w Java, ale niedostepna normalnie, tylko mozna tworzyc obiekty z niej za pomoca konkretnej metody patrz nizej

    /**
     * A3) use OnAudioFocusChangeListener callback method onAudioFocuschange to adapt playback behaviour of your app for different situations
     * when something changes our audiofocus during using our app
     */
    //uruchamiam listener zmiany AudioFocusa, potrzebny do odpalenia requestAudioFocus()w getView onClick() poniżej - prosby o focus
    //oraz uzycia pozniej do zrobienia if-else na rozne przypadki: focus lost, gain, transient loss etc
    //Chodzi o to zeby przerwac odtwarzanie np. podczas powiadomienia, telefonu etc.
    static AudioManager.OnAudioFocusChangeListener mojListenerZmianyAudioFocusa = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {  //ta funkcja przyjmuje rozne stany: ...GAIN, DUCK, LOSS, TRANSIENT_LOSS, it/
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || //temp loss of a.f.
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) { //temp loss of a.f., can "duck" (lower) volume if wants
                //we treat them both the same cause our app plays short audio files\
                Log.v("AudioManager", "Curent audio focus: AUDIOFOCUS_LOSS_TRANSIENT or LOSS_TRANSIENT_CAN_DUCK");//TODO LOG ew. do usunięcia
                mediaPlayer.pause(); //not stop cause stop() deletes what was loaded into it and it would have to prepare again
                mediaPlayer.seekTo(0); //back to beginning of sound file
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) { //permanent loss of a.f
                Log.v("AudioManager", "Curent audio focus: AUDIOFOCUS_LOSS");//TODO LOG ew. do usunięcia
                releaseMediaPlayer();
                /** A4) release audio focus when not needed*/
                mAudioManager.abandonAudioFocus(mojListenerZmianyAudioFocusa);  //abandon a.f.Cause previous a.f. owner (if any) to gain focus.
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {  //gain a.f. again (after losing it)
                mediaPlayer.start(); //mediaPlayer starts playing whatever was loaded into it.
                Log.v("AudioManager", "Curent audio focus: AUDIOFOCUS_GAIN");//TODO LOG ew. do usunięcia
            }
        }
    };

    /**
     * This listener gets triggered when the Media Player has completed playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mojListenerKonca = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) { //odpala relase jak skonczy grac
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();  //deklaracja funkcji na  górze
        }
    };

    //MEDIA PLAYER czyści pamięć
    static void releaseMediaPlayer() { //static, zebym mogl powołać w klasach NumbersActivity i pozostałych (w onStop())

        if (mediaPlayer != null) {// If the media player is not null, then it may be currently playing a sound.
            mediaPlayer.release(); // Regardless of the current state of the media player, release its resources cause we no longer need it.
            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;
            mAudioManager.abandonAudioFocus(mojListenerZmianyAudioFocusa); /** A4) release audio focus when not needed*/
            //it's safe to do it here cause we know we dont play audio now
        }
    }

    private int mColorResourceId; //posłuży do przechowania R.colors.<mój_kolor>, który wrzucam do konstruktora,
    //po to, żebym mógł tego użyć w getView() poniżej.

    /**
     * KONSTRUKTOR WORD ADAPTERA
     * This is our own custom constructor (NOTE:it doesn't mirror a superclass constructor(!)).
     * The "context" is used to inflate the layout file, and the "words" is the data to show on the screen
     *
     * @param context The current context. Used by inflator (in getView()) to inflate the layout file.
     * @param words   A List of Words objects to display in a ListView
     */


    public WordsAdapter(Activity context, ArrayList<Words> words, int colorResourceId) {
        // Kostruktor ArrayAdapter to: ArrayAdapter(Context context, int resource, List<T> objects)
        // nie podajemy resourceID layoutu, bo nie chcemy tworzyć jednego TV, tylko modyfikujemy getView() (główną metodę ArrayAdaptera)
        // która ręcznie inflatuje nasz wybrany Layout (taki co ma dwa TextView)
        // Adapter nie będzie używał tego resourcu, więc podajemy dowolny, np. 0.
        // To nie wykrzaczy Adaptera, bo Overrideujemy jego metodę (getView()), która go wykorzystuje
        super(context, 0, words); //to jest konstruktor ArrayAdaptera (3 parametry, ja wrzucam 2)
        //TODO EW. zrobić constructor wg https://medium.com/@Sudhagar/android-array-adapters-what-most-of-the-tutorials-don-t-tell-you-90f898fb54a2
        //tzn. nie pomijac resourceID i nie wpisywac recznie swojego list_item.xml do getView, tylko zrobić zmienna mResource,
        // którą powołam w getView./ bo to podobno "bad practice". wyjaśnienie w ostatnich kilku akapitach (w moim przypadku raczej bez znaczenia)
        mColorResourceId = colorResourceId; //tutaj przechowuję R.colors.<mój_kolor>, który przekażę do getView()
    }

    /**
     * teraz modyfikujemy getView()
     * getView() is the main part of your adapter.
     * It returns View that will be displayed as our ListView (this can be any AdapterView, e.g. GridView, Gallery)
     * It triggers when you scroll (or start) the list.
     * ||ArrayAdapter class has getView() method that is responsible for creating the views.
     * ||So behind the scenes, a ListView calls this method to get a view for a particular position.
     */
    //W SKRÓCIE: żeby odpalić poprawnie getView() musisz mieć 3 rzeczy:
    //1. poprzedni View, który recyklingujesz (ten co znika poza ekranem i pójdzie znowu do wyświetlenia)
    //2. pozycję (z tablicy twojej) kolejnego obiektu, który ma być wyświetlony na View który się pojawi na ekranie
    //3. parent (potrzebny tylko do nadmuchania) mówi nam jaki View będzie parentem tego View, który stworzymy (gdzie go umieścić)
    //   W naszym przypadku to będzie ListView

    /**
     * We override getView() method and make sure it returns our custom View there.
     * In our case it should be a Layout with two TextViews.
     *
     * @param position    The position in the list of data that should be displayed in the
     *                    list view.
     *                    ||czyli krótko mówiąc pozycja tego co jest na ekranie na ListView
     *                    ||ListView włącza getView() z danej pozycji na swojej liście (np. 1sza pozycja)
     *                    ||a wtedy Adapter bierze (getItem()) obiekt ze swojej tablicy z konkretnej pozycji
     *                    ||
     *                    |||the AdapterView position that is requesting a view
     *                    |||| getView going to be called for each position every time it is displayed.
     * @param convertView The recycled view to populate.
     *                    || view który recyklinguję (bierzemy ten co znika)
     *                    ||| As getView is called many times inflating a new view every time is expensive
     *                    |||so list view provides you one of the previously created view to re-use.
     * @param parent      The parent ViewGroup that is used for inflation. (this parameter is used in inflate() method)
     *                    ||    A reference to the parent view that this view will be a child of.
     * @return The View for the position in the AdapterView (ten zrecyklingowany, ale z nową zawartością,
     * albo po prostu z nową zawartością, gdy wyświetla się pierwszy raz)
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /** 1. najpierw bierzemy obiekt    */
        //getItem() bierze obiekt z mojej tablicy (ArrayList Words)
        //ten obiekt zawiera to co ma być wyświetlone w ListView na konkretnej pozycji
        // getItem() zwraca obiekt z listy z podanego indeksu (position)
        //obiekt, który zwraca to Words, bo podaliśmy na początku deklarację
        // public class WordsAdapter extends ArrayAdapter<Words>
        //tzn. że ArrayAdapter będzie oczekiwać obiektu Words

        //Adapter weźmie ten obiekt Words i przekaże go do wyświetlenia w ListView za pomocą getView()
        //czyli getItem() to takie szczypce, które wyjmują obiekt z mojej tablicy Words z podanej pozycji do Adaptera
        final Words nextWordsObject = getItem(position);  //tu final bo powołuję w  klasie anonymous onClickListenera na dole (wyjaśnienie w NumbersActivity)


        /** NAUKA dodałem log, ew. do usunięcia, powiązany z ovveridowanym toString() w Words.java (tam jest komentarz na samym dole)    */
        Log.v("WordsAdapter", "Curent word: " + nextWordsObject);
        //WAŻNE w tym miejscu nextWordsObject oznacza to samo co nextWordsObject.toString();
        //dzieje się tak dlatego że po Stringu "Abc" zrobiłem "+" i Java powołuje niejawnie toString() w tym miejscu

        /**
         * 2. I teraz wypełniamy go treścią...ALE, najpierw sprawdzamy czy mamy co wypełnić (coś jest recyklingowane?)
         * jak nie to nadmuchujemy sobie nowe, jak tak, to od razu do 3.
         */

        View listItemView = convertView;  //mozna bez tego (po prostu operowac convertView), ale tak jest czytelniej dla zrozumienia

        // 1st check if the existing view is being reused, if not then inflate the new view
        // || Sprawdzamy czy convertView jest null (nie ma go). Jak go nie ma, to znaczy, że lista
        // ||nie została przesunięta (np. dopiero się zaczyna wyświetlać na ekranie) i nie ma nic do recyklingu,
        // || więc nie recyklingujemy istniejącego View, tylko tworzymy nowy (pętla if wykonywana)
        //  ||wtedy getView tworzy nowy view (nadmuchuje mój layout) a potem wypełnia dwoma TextView wg list_item.xml
        // || JEŚLI lista pełna i przesunięta i kolejny raz to odpalamy
        // ||to convertView nie jest pusty bo mamy jakiś z recyklingu, który nam zniknął z ekranu)
        // ||to wtedy nie nadmuchujemy nowego, tylko używamy stary (pętla if pomijana)
        // ||i kod od razu przeskakuje do stworzenia nowych dwóch textView
        if (listItemView == null) {

            //LayoutLInflater używa mojego layoutu list_item.xml i właśnie dlatego nie podawałem
            //resource w konstruktorze ArrayAdaptera

            //getContext() bierze kontekst ten, który podaliśmy w konstruktorze (czyli gdzie ma to stworzyć)

            /**
             * tworzę obiekt LayoutInflater za pomocą "factory method" from(), wyjaśnienie w MainActivity.java
             */
            //TODO nie rozumiem jeszcze jak dokłdanie działa LayoutInflater, to chyba oznacza, że każda Activity ma swój, który biorę z kontekstu danej

            LayoutInflater this_is_my_Inflater = LayoutInflater.from(getContext());

            // View = someInflator.inflate(); oznacza stworzenie view z xmlu (nadmuchanie go)
            // ||LayoutInflater class is used to instantiate layout XML file into its corresponding View objects.
            // ||It takes as input an XML file and builds the View objects from it.


            /** TO PONIŻEJ Z DOKUMENTACJI inflate() LayoutInflater.java
             *
             * public View inflate(int resource, ViewGroup root, boolean attachToRoot)
             *
             * @param resource ID for an XML layout resource to load
             *                 || ten będziemy nadmuchiwać
             * @param root Optional view to be the parent of the generated hierarchy (if
             *        "attachToRoot" is true), or else simply an object that
             *        provides a set of LayoutParams values for root of the returned
             *        hierarchy (if <em>attachToRoot</em> is false.)
             *                  || w naszym przypadku tylko bierzemy parametry (padding, layout_height etc)
             * @param attachToRoot Whether the inflated hierarchy should be attached to
             *        the root parameter? If false, root is only used to create the
             *        correct subclass of LayoutParams for the root view in the XML.
             * @return The root View of the inflated hierarchy. If root was supplied and
             *         attachToRoot is true, this is root; otherwise it is the root of
             *         the inflated XML file.
             */

            listItemView = this_is_my_Inflater.inflate(R.layout.list_item, parent, false);
            //attachToRoot:false oznacza chyba ze nie tworzymy jeszcze tego View, tylko dopiero w return getView\
            // po wypełnieniu TextViews właściwymi wartościami
        }


        /**
         * 3. OK tutaj wypełniamy sobie treścią (2 TVs + 1 ImageView)
         */
        /**
         * A) pierwszy TV
         */
        TextView miwokTextView = listItemView.findViewById(R.id.miwok_text_view);
        // Bierze słowo Miwok z kolejnego obiektu (nextWordsObject, deklaracja w p. 1)
        // i wrzuca do TV (setText()) za pomocą gettera z klasy Words (getMiwokWord())
        miwokTextView.setText(nextWordsObject.getMiwokWord());
        /**
         * B) drugi TV
         */
        TextView translationTextView = listItemView.findViewById(R.id.default_text_view);
        // Bierze tłumaczeniez kolejnego obiektu (nextWordsObject)
        // i wrzuca do TV (setText()) za pomocą gettera z klasy Words (getDefualtTranslation())
        translationTextView.setText(nextWordsObject.getDefualtTranslation());
        /**
         * C) ImageView, ale dodałem IF, bo nie zawsze mam obrazek (nie ma w PhrasesActivity)
         */
        // Bierze ikonę słowa z kolejnego obiektu (nextWordsObject, deklaracja w p. 1)
        // i wrzuca do ImageView (setImageResource(int)) za pomocą gettera z klasy Words (getImageResourceId())
        ImageView pictureImageView = listItemView.findViewById(R.id.image_of_word);

        // w Phrases są obiekty Words bez obrazka (konstruktor tylko z 2 TV, bez IV)
        // dodaję IF, żeby usunąć widoczność ImageView z layouta, jeśli nie ma obrazka w obiekcie Words
        if (nextWordsObject.getImageResourceId() > 0) { //ResourceID jest >0,jeśli jest obrazek

            /**
             * ALTERNATYWNIE: ten kod można napisać inaczej (wg Google Nanodegree) robiąc dodatkową funkcję hasImage(); definicja hasImage() jest w Words.java
             *          if (nextWordsObject.hasImage()) {
             */
            pictureImageView.setVisibility(View.VISIBLE); //setVisibility to  metoda View, która ustawia widoczność)
            //dostęne opcje to int (VISIBLE = 0, INVISIBLE = 4, GONE = 8)
            //UWAGA: muszę tu ustawić View.VISIBLE, bo gdyby się trafił obiekt bez obrazka i poszedł do recyklingu,
            // to atrybut GONE by przeszedł na inny obiekt
            pictureImageView.setImageResource(nextWordsObject.getImageResourceId());
        } else { //ResourceID =0 ,gdy nie ma obrazka
            pictureImageView.setVisibility(View.GONE); //GONE nie tylko nie wyświetla obiektu, ale też nie zajmuje miejsca w layoucie (znika)
            //robię tak, żeby w PhrasesActivity tekst się wyświetlał po lewej, a nie zostawiał puste miejsce bez obrazka
        }

        /**
         * D) Ustawia kolor fragmentu View bez obrazka (text_container) taki jak chcę (taki jaki wrzuciłem w konstrukorze WordAdaptera)
         */
        View textContainer = listItemView.findViewById(R.id.text_container);
        textContainer.setBackgroundResource(mColorResourceId);

        /**
         * ALTERNATYWNIE: nanodegree podało inne podejście
         *         int color = ContextCompat.getColor(getContext(), mColorResourceId);
         *         textContainer.setBackgroundColor(color);
         * (jest to nr 3 na liście poniżej, z wyjaśnieniem)
         *
         * opcje zmiany koloru (setBackgroundColor() lub setBackgroundResource
         * (!) Using setBackgroundColor() you'll need to understand that is expects an object not a resource id.
         *
         * 1).setBackgroundColor(0x00FF00);
         * 2).setBackgroundColor(Color.GREEN);
         * 3).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.myGreen)); //ContextCompat.getColor(context, R.color.color_name)
         *
         * getColor(): returns a color associated with a particular resource ID.
         * The returned color will be styled for the specified Context's theme.
         * it returns: a single color value in the form 0xAARRGGBB.
         * ContextCompat to klasa która służy do zapewnienia kompatybilności na innych wersjach Androida,
         * automatyzuje pracę i dodaje parę metod obsługujących rzeczy z tym związane
         *
         * 4).setBackgroundColor(Color.parseColor("#e7eecc"));
         * 5).setBackgroundResource(R.color.myGreen);  //JA UŻYWAM TEGO, BO WG MNIE NAJLEPSZE
         */

        textContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releaseMediaPlayer(); // Release the media player if it currently exists because we are about to play a different sound file

                /** ZANIM UZYJE MEDIA PLAYER ODPALAM AUDIO MANAGER i prosze o zgode na granie uzywają listener zmiany audio focus
                 * A1) create AudioManager object (system service)
                 */
                mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE); //uruchamiam System Service AudioManager
                //from Documentations: AudioManager extends Objects; Instances of this class must be obtained using Context.getSystemService(Class)
                // where Class argument is AudioManager.class) or Context.getSystemService(String) with argument Context.AUDIO_SERVICE.
                //getsystemservice zwraca Object, wiec castuję go do AudioManagera
                //getContext() bierze kontekst z obiektu ktory jest w getView() (czyli z odp. klasy np. NumberActivity.java)

                /** A2) ask for audio focus (needs OnAudioFocusChangeListener inteface to do it) */
                //Tworzę audiofocusrequest i zapisuje rezultat. zaleznie od tego co otrzymam, bede odtwarzac dzwieki lub nie (bo po co mam odtwarzac
                //jak ich nie bedzie slychac)
                /**
                 * @param streamType  - typ dzwieku ktory chcemy odtwarzac (muzyka, dzwonek, powiadomienie, alarm itp)
                 * uzywam AudioManager.STREAM_MUSIC
                 * @param durationHint - wksazuje na jak dlugo chcemy focus (normalnie do grania, na krotki dzwiek np. jak w nawigacji, z wyciszeniem innego itp)
                 * uzywam AudioManager.AUDIOFOCUS_GAIN_TRANSIENT bo mam krotkie, kilkusekundowe dźwięki, a nie piosenkę
                 */
                int result = mAudioManager.requestAudioFocus(mojListenerZmianyAudioFocusa, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) { //gdy mamy audio focus to odpalamy dzwiek
                    Log.v("AudioManager", "Curent audio focus: AUDIOFOCUS_REQUEST_GRANTED");//TODO LOG ew. do usunięcia

                    mediaPlayer = MediaPlayer.create(getContext(), nextWordsObject.getSoundResourceId()); //tworze nowy (prepare jest w funkcji create)
                    mediaPlayer.start();  //uruchamia dzwięk
                    mediaPlayer.setOnCompletionListener(mojListenerKonca); //teraz nakładam Listener, żeby odpalił release(), jak skonczy sie dzwiek
                }
            }
        });

        return listItemView; //zwraca ten zmieniony convertView z nową zawartością (ew. nowy jak nic nie recyklingowaliśmy)
        // ||returns this view as a child to ListView
    }
}




