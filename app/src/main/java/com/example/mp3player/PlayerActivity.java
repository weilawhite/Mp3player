package com.example.mp3player;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView titleText;
    private ImageView titleImg;
    private ImageButton playBtn, stopBtn;
    private String mp3name, mp3index;
    MediaPlayer mediaPlayer = null;
    boolean pause;
    private boolean readFromSD;

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
                        playMusic(mp3index);
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

    public void playMusic(String index) {

        stopMusic();


        if (readFromSD) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(mp3index);
                mediaPlayer.prepare();
                mediaPlayer.setLooping(false);
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            mediaPlayer = MediaPlayer.create(this, Integer.valueOf(mp3index));
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        }

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

    private Bitmap getBitmapFromSDCard(String file) {
        String sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        Bitmap bitmap = BitmapFactory.decodeFile(sd + "/" + file + ".jpg");
        return bitmap;
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
        readFromSD = bundle.getBoolean("readFromSD");
        int mp3no = bundle.getInt("mp3no");
        titleText.setText(mp3no + " - " + mp3name);


        //在這修改顯示圖片
        Bitmap image = getBitmapFromSDCard(mp3name);
        if(image!=null){
            titleImg.setImageBitmap(image);
        }else {
            titleImg.setImageResource(R.drawable.title);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.play_btn) {
            if (mediaPlayer == null) {
                playMusic(mp3index);
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