package com.example.mp3player;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ListView mp3List;
    private String[] mp3name = {"我們不一樣", "是我太傻", "飛鳥旋空", "散了就散了"};
    private int[] resId = {R.raw.mp3_0, R.raw.mp3_1, R.raw.mp3_2, R.raw.mp3_3};
    MediaPlayer mediaPlayer = null;
    private Button pauseBtn, stopBtn;
    private boolean pause = false, isComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        setData();

        mp3List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = view.findViewById(view.getId());
                Log.d("MainActivity", textView.getText().toString());
                Toast.makeText(MainActivity.this, mp3name[position], Toast.LENGTH_SHORT).show();
                playMusic(position);
            }
        });

    }

    public void playMusic(int index) {

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = MediaPlayer.create(this, resId[index]);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
        pause = false;

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isComplete = true;
                pause = false;
                pauseBtn.setText(R.string.play);
                Toast.makeText(MainActivity.this,"Finish",Toast.LENGTH_LONG);
            }
        });

    }

    private void setData() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mp3name);
        mp3List.setAdapter(arrayAdapter);
    }

    private void findView() {
        mp3List = findViewById(R.id.mp3list);
        pauseBtn = findViewById(R.id.pause_btn);
        stopBtn = findViewById(R.id.stop_btn);

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer==null){
                    return;
                }
                pause = false;
                isComplete = false;
                pauseBtn.setText(R.string.play);
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer == null) {
                    return;
                }

                if (isComplete) {
                    pause = false;
                    isComplete = false;
                    pauseBtn.setText(R.string.pause);
                    mediaPlayer.start();
                    return;
                }

                pause = !pause;

                if (!pause) {
                    pauseBtn.setText(R.string.pause);
                } else {
                    pauseBtn.setText(R.string.resume);
                }
                if (pause && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    return;
                }
                mediaPlayer.start();
            }
        });

    }
}