package com.myapps.quizify.quizifyclient.game;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.myapps.quizify.quizifyclient.R;
import com.myapps.quizify.quizifyclient.util.RequestHandler;
import com.myapps.quizify.quizifyclient.util.SystemUiHider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class RoundActivity extends Activity implements MediaPlayer.OnPreparedListener{


    private GameObserver mObserver;
    private MediaPlayer mMediaPlayer;

    private ProgressBar bar;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int gameId = 1;

        mObserver = GameObserver.getInstance(getApplicationContext(), 1);

        List<String> alternatives = mObserver.getAlternatives();

        setContentView(R.layout.activity_round);

        Button disp = (Button) findViewById(R.id.displayButton);
        disp.setText((CharSequence) "Score: " + Integer.toString(mObserver.getScore()));


        Button disp2 = (Button) findViewById(R.id.displayButton2);
        disp2.setText((CharSequence) "Round number: " + Integer.toString(mObserver.getCurrentRound() + 1));


        Button btn = (Button) findViewById(R.id.alternative);
        btn.setText((CharSequence) alternatives.get(0));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseAlternative(0);
            }
        });

        Button btn1 = (Button) findViewById(R.id.alternative1);
        btn1.setText((CharSequence) alternatives.get(1));
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseAlternative(1);
            }
        });

        Button btn2 = (Button) findViewById(R.id.alternative2);
        btn2.setText((CharSequence) alternatives.get(2));
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseAlternative(2);
            }
        });

        Button btn3 = (Button) findViewById(R.id.alternative3);
        btn3.setText((CharSequence) alternatives.get(3));
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseAlternative(3);
            }
        });


        //play song from data resource
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try{
            mMediaPlayer.setDataSource(mObserver.getSongURL());
        }catch (IllegalArgumentException e){
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }
        mMediaPlayer.prepareAsync();
    }

    private void chooseAlternative(int i) {
        timer.cancel();
        if(mObserver.checkAlternative(i)) moveOn(bar.getProgress());
        else moveOn(0);
    }
    private void moveOn(int i){
        //TODO Better flow implementation
        mMediaPlayer.stop();
        if(mObserver.nextRound(i)){
            Intent intent = new Intent(RoundActivity.this, CategoryActivity.class);
            intent.putExtra("previous", "RoundActivity");
            startActivity(intent);
        }else{
            recreate();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer.start();
        bar = (ProgressBar) findViewById(R.id.timer);

        //Timer
        timer = new CountDownTimer(15000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                bar.setProgress((int) millisUntilFinished );
            }

            public void onFinish() {
                bar.setProgress(0);
                mMediaPlayer.stop();
                moveOn(0);
            }
        }.start();

    }

    @Override
    public void onBackPressed() {
        //Stops the application from returning while in game
    }

    @Override
    public void finish() {
        mMediaPlayer.stop();
        timer.cancel();
        super.finish();
    }
}