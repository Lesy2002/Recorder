package com.example.recorderv3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayersActivity extends AppCompatActivity {
    private ImageButton playbtn, stopplay;
    private ImageView prevActivity;
    private MediaPlayer mPlayer;
    private ListView filesList;
    private List<AudioFiles> listFiles;
    private AudioFiles selectedFileFromListView;
    private static final String LOG_TAG = "AudioRecording";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        playbtn = (ImageButton) findViewById(R.id.btnPlay);
        stopplay = (ImageButton) findViewById(R.id.btnStopPlay);
        prevActivity = (ImageView) findViewById(R.id.btnPrevActivity);
        stopplay.setEnabled(false);
        setInitialData();
        // получаем элемент ListView
        filesList = findViewById(R.id.countriesList);
        // создаем адаптер
        AudioFilesAdapter audioFilesAdapter = new AudioFilesAdapter(this, R.layout.list_item, listFiles);
        // устанавливаем адаптер
        filesList.setAdapter(audioFilesAdapter);

        // слушатель выбора в списке
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // получаем выбранный файл
                AudioFiles selectedFile = (AudioFiles)parent.getItemAtPosition(position);
                setSelectedFileFromListView(null);
                setSelectedFileFromListView(selectedFile);
            }
        };
        filesList.setOnItemClickListener(itemListener);

        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSelectedFileFromListView() != null) {
                    playbtn.setEnabled(false);
                    stopplay.setEnabled(true);
                    mPlayer = new MediaPlayer();
                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            stopplay.callOnClick();
                        }
                    });
                    try {
                        mPlayer.setDataSource(getSelectedFileFromListView().getPathToFile());
                        mPlayer.prepare();
                        mPlayer.start();
                        Toast.makeText(getApplicationContext(), "Recording Started Playing", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "prepare() failed");
                    }
                }

            }
        });
        stopplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.release();
                mPlayer = null;
                playbtn.setEnabled(true);
                stopplay.setEnabled(false);
                Toast.makeText(getApplicationContext(), "Playing Audio Stopped", Toast.LENGTH_SHORT).show();
            }
        });
        prevActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayersActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void setInitialData(){
        String path = getDataDir().getAbsolutePath();
        File directory = new File(path);
        listFiles = new ArrayList<>();
        File[] files = directory.listFiles();
        if(files != null){
            for(File f : files){
                String name = f.getName();
                if(name.contains(".3gp")){
                    listFiles.add(new AudioFiles(f.getName(),f.getAbsolutePath(), getDuration(f)));
                }
            }
        }
    }
    private static String getDuration(File file) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(file.getAbsolutePath());
        String durationStr = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return AudioFiles.formateMilliSeccond(Long.parseLong(durationStr));
    }
    public AudioFiles getSelectedFileFromListView() {
        return selectedFileFromListView;
    }

    public void setSelectedFileFromListView(AudioFiles selectedFileFromListView) {
        this.selectedFileFromListView = selectedFileFromListView;
    }
}