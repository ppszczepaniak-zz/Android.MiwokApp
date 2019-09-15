package com.example.android.miwok;

/**
 * Stworzyłem taką klasę żeby wypełniać array dwoma słowami: Miwok i tłumaczeniem
 * tej klasy użyję tworząc tablicę z dwoma słowami
 * Robię tak dlatego, bo ArrayAdapter nie pozwala użyć w konstruktorze ArrayAdapter<String>(this, layout, jakiśObiekt<np. List>)
 * dwóch list, więc stworzę własny obiekt Words, który zawiera dwa słowa i to wrzucę do 1 tablicy (1 listy)
 */
public class Words {

    //state
    private String mMiwokWord;
    private String mDefualtTranslation;
    private int mImageResourceId;
    private int msoundResourceId;
    /**ALTERNATYWNIE: wg Google Nanodegree kod wygląda tak (z metodą hasImage(), użytą w WordsAdapter.java) ale ja wolę swoją wersję
     *
     *     private int mImageResourceId = NO_IMAGE_PROVIDED;  //Image resource ID for the word
     *     private static final int NO_IMAGE_PROVIDED = -1; //  dodatkowa stała = -1 oznaczająca brak poprawnego ResourceID obrazka, czyli brak obrazka
     *                                                      //  to się zmienia w konstruktorze #1, jeśli dodajemy obrazek jakiś
     *     public boolean hasImage() {
     *         return mImageResourceId != NO_IMAGE_PROVIDED;  //TRUE jeśli ResourceID nie jest -1 (a jest zawsze > 0, jeśli jest obrazek jakiś)
     *     }
     */

    //constructor #1
    public Words(String miwokWord, String defaultTranslation, int imageOfTheWord, int soundOfTheWord) {
        this.mMiwokWord = miwokWord;
        this.mDefualtTranslation = defaultTranslation;
        this.mImageResourceId = imageOfTheWord;
        this.msoundResourceId = soundOfTheWord;
    }

    //constructor #2 (no image)
    public Words(String miwokWord, String defaultTranslation, int soundOfThePhrase) {
        this.mMiwokWord = miwokWord;
        this.mDefualtTranslation = defaultTranslation;
        this.msoundResourceId = soundOfThePhrase;
        }
    //getters
    public String getMiwokWord() {
        return mMiwokWord;
    }

    public String getDefualtTranslation() {
        return mDefualtTranslation;
    }

    public int getImageResourceId() {
        return mImageResourceId;
    }

    public int getSoundResourceId() {
        return msoundResourceId;
    }



    //to poniżej do generate->toString() zrobiony po to, żeby tworzyć logi (patrz WordsAdapter.java)

    @Override
    public String toString() {
        return "Words{" +
                "mMiwokWord='" + mMiwokWord + '\'' +
                ", mDefualtTranslation='" + mDefualtTranslation + '\'' +
                ", mImageResourceId=" + mImageResourceId +
                ", msoundResourceId=" + msoundResourceId +
                '}';
    }
}
