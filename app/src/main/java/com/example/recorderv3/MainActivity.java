package com.example.recorderv3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView nextActivity;
    private GifImageView utyiGif;
    private View startButton;
    private MediaRecorder mRecorder;
    private CountDownTimer countDownTimer;
    private static final String LOG_TAG = "AudioRecording";
    private static String mFileName = null;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = (View) findViewById(R.id.btnStart);
        nextActivity = (ImageView) findViewById(R.id.btnNextActivity);
        nextActivity = (ImageView) findViewById(R.id.btnNextActivity);
        utyiGif = (GifImageView) findViewById(R.id.utyiGif);
        utyiGif.setVisibility(View.INVISIBLE);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAudioRecording()) {
                    if (CheckPermissions()) {
                        mRecorder = new MediaRecorder();
                        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
                        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                        String date = LocalDate.now().toString();
                        String time = LocalTime.now().toString().replace(':', '-').substring(0, 8);

                        mFileName = getDataDir().getAbsolutePath();

                        mFileName += "/" + date + "-" + time + "AudioRecording.3gp";
                        mRecorder.setOutputFile(mFileName);
                        try {
                            mRecorder.prepare();
                        } catch (IOException e) {
                            Log.e(LOG_TAG, "prepare() failed");
                        }
                        @SuppressLint("UseCompatLoadingForDrawables") Drawable img = getResources().getDrawable(R.drawable.squarebutton);
                        startButton.setBackground(img);
                        utyiGif.setVisibility(View.VISIBLE);
                        mRecorder.start();
                        Toast.makeText(getApplicationContext(), "Recording Started", Toast.LENGTH_LONG).show();
                    } else {
                        RequestPermissions();
                    }
                } else {
                    @SuppressLint("UseCompatLoadingForDrawables") Drawable img = getResources().getDrawable(R.drawable.roundedbutton2);
                    startButton.setBackground(img);
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                    utyiGif.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Recording Stopped", Toast.LENGTH_LONG).show();
                }

            }
        });

        nextActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlayersActivity.class);
                startActivity(intent);
            }
        });
    }

    private Boolean isAudioRecording() {
        return mRecorder == null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_AUDIO_PERMISSION_CODE) {
            if (grantResults.length > 0) {
                boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (permissionToRecord && permissionToStore) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

}