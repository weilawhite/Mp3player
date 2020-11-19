package com.example.mp3player;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView titleText;
    private ImageView titleImg;
    private ImageButton playBtn, stopBtn;
    private String mp3name, mp3index;
    MediaPlayer mediaPlayer = null;
    boolean pause;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            stopMusic();
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        findView();
        swapPrepare();
        //playMusic(Integer.valueOf(mp3index));
    }

    public void swapPrepare() {
        playBtn.setEnabled(false);
        stopBtn.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playBtn.setEnabled(true);
                        stopBtn.setEnabled(true);
                        playMusic(Integer.valueOf(mp3index));


                    }
                });
            }
        }).start();


    }

    public void stopMusic() {

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            pause = false;
        }
    }

    public void playMusic(int index) {

        stopMusic();
        mediaPlayer = MediaPlayer.create(this, Integer.valueOf(mp3index));
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
        playBtn.setImageResource(R.drawable.music_pause);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopMusic();
                //pause = false;
                playBtn.setImageResource(R.drawable.music_play);

            }
        });
    }

    private void findView() {
        titleText = findViewById(R.id.title_text);
        titleImg = findViewById(R.id.title_img);
        playBtn = findViewById(R.id.play_btn);
        stopBtn = findViewById(R.id.stop_btn);

        playBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        mp3name = bundle.getString("mp3name");
        mp3index = bundle.getString("mp3index");
        int mp3no = bundle.getInt("mp3no");
        titleText.setText(mp3no + " - " + mp3name);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.play_btn) {
            if (mediaPlayer == null) {
                playMusic(Integer.valueOf(mp3index));
                return;
            }

            if (pause) {
                pause = false;
                mediaPlayer.start();
                playBtn.setImageResource(R.drawable.music_pause);
                return;
            }

            if (mediaPlayer.isPlaying()) {
                pause = true;
                mediaPlayer.pause();
                playBtn.setImageResource(R.drawable.music_play);
                return;
            }


        } else if (v.getId() == R.id.stop_btn) {
            if (mediaPlayer != null) {
                //pause = false;
                //isComplete = false;
                stopMusic();
                /*
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                */
                playBtn.setImageResource(R.drawable.music_play);
            }


        }
    }
}