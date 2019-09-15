package com.example.android.miwok;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class WordsAdapterNoCommentsCopy extends ArrayAdapter<Words> {

    private int mColorResourceId;
    static private MediaPlayer mediaPlayer;
    static AudioManager mAudioManager;

    static AudioManager.OnAudioFocusChangeListener mojListenerZmianyAudioFocusa = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                releaseMediaPlayer();
                mAudioManager.abandonAudioFocus(mojListenerZmianyAudioFocusa);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                mediaPlayer.start();
            }
        }
    };

    private MediaPlayer.OnCompletionListener mojListenerKonca = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    static void releaseMediaPlayer() {

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            mAudioManager.abandonAudioFocus(mojListenerZmianyAudioFocusa);
        }
    }

    public WordsAdapterNoCommentsCopy(Activity context, ArrayList<Words> words, int colorResourceId) {
        super(context, 0, words);
        mColorResourceId = colorResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Words nextWordsObject = getItem(position);
        View listItemView = convertView;
        if (listItemView == null) {
            LayoutInflater this_is_my_Inflater = LayoutInflater.from(getContext());
            listItemView = this_is_my_Inflater.inflate(R.layout.list_item, parent, false);
        }

        TextView miwokTextView = listItemView.findViewById(R.id.miwok_text_view);
        miwokTextView.setText(nextWordsObject.getMiwokWord());
        TextView translationTextView = listItemView.findViewById(R.id.default_text_view);
        translationTextView.setText(nextWordsObject.getDefualtTranslation());

        ImageView pictureImageView = listItemView.findViewById(R.id.image_of_word);
        if (nextWordsObject.getImageResourceId() > 0) {
            pictureImageView.setVisibility(View.VISIBLE);
            pictureImageView.setImageResource(nextWordsObject.getImageResourceId());
        } else {
            pictureImageView.setVisibility(View.GONE);
        }

        View textContainer = listItemView.findViewById(R.id.text_container);
        textContainer.setBackgroundResource(mColorResourceId);

        textContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releaseMediaPlayer();
                mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
                int result = mAudioManager.requestAudioFocus(mojListenerZmianyAudioFocusa, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer = MediaPlayer.create(getContext(), nextWordsObject.getSoundResourceId());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mojListenerKonca);
                }
            }
        });
        return listItemView;
    }
}







