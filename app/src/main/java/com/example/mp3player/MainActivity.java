package com.example.mp3player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

public class MainActivity extends AppCompatActivity {

    private ListView mp3List;
    private String[] mp3name;
    private String[] resId;
    private Button readBtn;
    private boolean pause = false, isComplete;
    private boolean readFromSD;

    private void readRawFile() {
        mp3name = new String[]{"我們不一樣", "是我太傻", "飛鳥旋空", "散了就散了"};
        resId = new String[]{String.valueOf(R.raw.mp3_0), String.valueOf(R.raw.mp3_1), String.valueOf(R.raw.mp3_2), String.valueOf(R.raw.mp3_3)};

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        readRawFile();
        setData();

    }

    private void setData() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mp3name);
        mp3List.setAdapter(arrayAdapter);

        mp3List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = view.findViewById(view.getId());
                Log.d("MainActivity", textView.getText().toString());

                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("mp3name", mp3name[position]);
                bundle.putString("mp3index", resId[position]);
                bundle.putBoolean("readFromSD", readFromSD);
                bundle.putInt("mp3no", position);
                intent.putExtras(bundle);
                startActivity(intent);
                Toast.makeText(MainActivity.this, mp3name[position], Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPermission() {
        int permissions = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissions != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        } else {
            Toast.makeText(this, R.string.author_done, Toast.LENGTH_LONG).show();
            readSDFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.author_done, Toast.LENGTH_LONG).show();
                    readSDFile();
                } else {
                    Toast.makeText(this, R.string.author_fail, Toast.LENGTH_LONG).show();
                    readFromSD = false;
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void findView() {
        mp3List = findViewById(R.id.mp3list);
        readBtn = findViewById(R.id.read_btn);

        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readFromSD = !readFromSD;
                if (readFromSD) {
                    getPermission();
                } else {
                    readRawFile();
                }
                setData();
            }
        });
    }

    private void readSDFile() {

        FilenameFilter fileFilter = new FilenameFilter() {
            private String[] filter = {"mp3", "ogg", "mp4", "wav"};

            @Override
            public boolean accept(File pathname, String filename) {
                for (int i = 0; i < filter.length; i++) {
                    if (filename.toLowerCase().endsWith(filter[i])) {
                        return true;
                    }
                    //if (filename.indexOf(filter[i]) != 1) return true;
                }
                return false;
            }
        };

        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        /*String path = root.getPath();
        File folder = new File(path);*/


        File[] files = root.listFiles(fileFilter);
        if (files == null) {
            return;
        }
        mp3name = new String[files.length];
        resId = new String[files.length];
        int count = 0;
        for (File file : files) {
            String filename = file.getName();
            String absolutePath = file.getAbsolutePath();
            String name = filename.substring(0, filename.indexOf("."));
            //String name = file.getName();
            mp3name[count] = name;
            resId[count] = absolutePath;
            count++;

        }

    }
}