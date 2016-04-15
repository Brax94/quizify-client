package com.myapps.quizify.quizifyclient.game;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myapps.quizify.quizifyclient.util.RequestHandler;
import com.myapps.quizify.quizifyclient.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.myapps.quizify.quizifyclient.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class RoundActivity extends Activity implements MediaPlayer.OnPreparedListener{


    private List scores;
    private List alternatives = Arrays.asList(new String[]{"Alternative 1", "Alternative 2", "Alternative 3", "Alternative 4"});
    private String songURL = "https://p.scdn.co/mp3-preview/04fac4f932a798c9dc0eb03e1df2c78081becb6e";

    private String correctAlternative = "Alternative 3";
    private int roundNumber;

    private MediaPlayer mMediaPlayer;

    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initAlternatives();
        initScores();
        roundNumber = scores.size();


        mQueue = RequestHandler.getInstance(this.getApplicationContext()).getRequestQueue();
        setContentView(R.layout.activity_round);

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
            mMediaPlayer.setDataSource(songURL);
        }catch (IllegalArgumentException e){
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }
        mMediaPlayer.prepareAsync();

    }

    private void chooseAlternative(int i) {
        if (correctAlternative.equals(alternatives.get(i))) scores.add(1);
        else scores.add(0);
        this.recreate();
    }

    private void initScores() {
        scores = getIntent().getIntegerArrayListExtra("scores");
    }

    private void initAlternatives(){

        try {
            JSONObject Json = new JSONObject(getIntent().getStringExtra("JSON"));

            //TODO
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer.start();
        //Timer
        CountDownTimer timer = new CountDownTimer(15000, 1000) {
            public void onTick(long millisUntilFinished) {
                ((TextView) findViewById(R.id.timerWindow)).setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                mMediaPlayer.stop();
            }
        }.start();
    }
}