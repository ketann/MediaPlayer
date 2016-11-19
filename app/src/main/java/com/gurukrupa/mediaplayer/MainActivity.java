package com.gurukrupa.mediaplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private Button forward, pause, back, rewind;
    private ImageView imageView;
    private MediaPlayer mediaPlayer;

    private double startTime = 0;
    private double finalTime = 0;

    private Handler myHandler = new Handler();
    ;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView tx1, tx2, tx3;
    public static int oneTimeOnly = 0;
    private Intent playbackServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        forward = (Button) findViewById(R.id.btn_forward);
        pause = (Button) findViewById(R.id.btn_pause);
        back = (Button) findViewById(R.id.btn_back);
        rewind = (Button) findViewById(R.id.btn_rewind);
        imageView = (ImageView) findViewById(R.id.imageView);

        forward.setOnClickListener(this);
        pause.setOnClickListener(this);
        back.setOnClickListener(this);
        rewind.setOnClickListener(this);


        tx1 = (TextView) findViewById(R.id.textView2);
        tx2 = (TextView) findViewById(R.id.textView3);
        tx3 = (TextView) findViewById(R.id.textView4);

        tx3.setText("Hanuman Chalisa.mp3");

        playbackServiceIntent = new Intent(this, AudioService.class);

        mediaPlayer = MediaPlayer.create(this, R.raw.hanuman);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setClickable(false);
        pause.setEnabled(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_forward:
                int temp = (int) startTime;

                if ((temp + forwardTime) <= finalTime) {
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(), "You have Jumped forward 5 seconds", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
                }

                Log.d(TAG, " ---> Forward");
                break;

            case R.id.btn_pause:
                Toast.makeText(getApplicationContext(), "Pausing sound", Toast.LENGTH_SHORT).show();
                mediaPlayer.pause();
                //stopService(playbackServiceIntent);
                pause.setEnabled(false);
                back.setEnabled(true);

                Log.d(TAG, " ---> Pause");

                break;


            case R.id.btn_back:
                Toast.makeText(getApplicationContext(), "Playing sound", Toast.LENGTH_SHORT).show();
                mediaPlayer.start();
                //startService(playbackServiceIntent);

                finalTime = mediaPlayer.getDuration();
                startTime = mediaPlayer.getCurrentPosition();

                if (oneTimeOnly == 0) {
                    seekbar.setMax((int) finalTime);
                    oneTimeOnly = 1;
                }

                tx2.setText(String.format("%d : %d ",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        finalTime)))
                );

                tx1.setText(String.format("%d : %d ",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        startTime)))
                );

                seekbar.setProgress((int) startTime);
                myHandler.postDelayed(UpdateSongTime, 100);
                pause.setEnabled(true);
                back.setEnabled(false);
                Log.d(TAG, " ---> Back");
                break;

            case R.id.btn_rewind:

                int temp1 = (int) startTime;

                if ((temp1 - backwardTime) > 0) {
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(), "You have Jumped backward 5 seconds", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, " ---> Rewind");
                    break;
                }
        }
    }
        private Runnable UpdateSongTime = new Runnable() {
            public void run() {
                startTime = mediaPlayer.getCurrentPosition();
                tx1.setText(String.format("%d : %d ",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) startTime)))
                );
                seekbar.setProgress((int)startTime);
                myHandler.postDelayed(this, 100);
            }
        };
}
