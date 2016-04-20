package com.myapps.quizify.quizifyclient.game;

import android.app.Activity;
import android.content.Context;
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
import com.myapps.quizify.quizifyclient.net.quizifyapp.net.APIObjectResponseListener;
import com.myapps.quizify.quizifyclient.net.quizifyapp.net.NetworkManager;
import com.myapps.quizify.quizifyclient.util.RequestHandler;
import com.myapps.quizify.quizifyclient.util.SystemUiHider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RoundActivity extends Activity implements MediaPlayer.OnPreparedListener{

    private String opponent;
    private String player;

    private String category;

    public static int NUMBER_OF_QUESTIONS = 5;
    private int score;
    private List<String> songUrls;
    private List<List<String>> alternatives;
    private List<String> correctAlternatives;

    private int currentQuestion;

    private String getSongURL(){return songUrls.get(currentQuestion);}
    private List<String> getAlternatives(){return alternatives.get(currentQuestion);}

    private boolean checkAlternative(int alternative){return alternatives.get(currentQuestion).get(alternative).equals(correctAlternatives.get(currentQuestion));}

    private MediaPlayer mMediaPlayer;

    private ProgressBar bar;
    private CountDownTimer timer;

    Button btn;
    Button btn1;
    Button btn2;
    Button btn3;
    Button disp;
    Button disp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRound(1);

        setContentView(R.layout.activity_round);

        disp = (Button) findViewById(R.id.displayButton);

        disp2 = (Button) findViewById(R.id.displayButton2);

        btn = (Button) findViewById(R.id.alternative);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseAlternative(0);
            }
        });

        btn1 = (Button) findViewById(R.id.alternative1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseAlternative(1);
            }
        });

        btn2 = (Button) findViewById(R.id.alternative2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseAlternative(2);
            }
        });

        btn3 = (Button) findViewById(R.id.alternative3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseAlternative(3);
            }
        });
    }

    private void chooseAlternative(int i) {
        timer.cancel();
        if(checkAlternative(i)) nextRound(bar.getProgress());
        else nextRound(0);
    }

    private void nextRound(int score){
        mMediaPlayer.stop();
        this.score += score;

        if(++currentQuestion == NUMBER_OF_QUESTIONS){
            sendScore();
            Intent intent = new Intent(RoundActivity.this, CategoryActivity.class);
            intent.putExtra("previous", "RoundActivity");
            startActivity(intent);
        }
        else{askQuestion();}

    }

    private void updateButtonText(){
        List<String> alternatives = getAlternatives();
        btn.setText((CharSequence) alternatives.get(0));
        btn1.setText((CharSequence) alternatives.get(1));
        btn2.setText((CharSequence) alternatives.get(2));
        btn3.setText((CharSequence) alternatives.get(3));
        disp.setText((CharSequence) "Score: " + Integer.toString(score));
        disp2.setText((CharSequence) "Round: " + Integer.toString(currentQuestion));

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
                nextRound(0);
            }
        }.start();

    }

    public void askQuestion(){
        updateButtonText();
        //play song from data resource
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try{
            mMediaPlayer.setDataSource(getSongURL());
        }catch (IllegalArgumentException e){
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }
        mMediaPlayer.prepareAsync();
    }


    @Override
    public void finish() {
        mMediaPlayer.stop();
        timer.cancel();
        super.finish();
    }



    //IO:
    private void initRound(int id){
        correctAlternatives = new ArrayList<>();
        songUrls = new ArrayList<>();
        alternatives = new ArrayList<>();
        this.score = 0;
        this.currentQuestion = 0;

        NetworkManager.getInstance(getApplicationContext()).getSingleGame(id, new APIObjectResponseListener<String, JSONObject>() {
            @Override
            public void getResult(String error, JSONObject result) {
                if(error != null){
                    System.err.print(error);
                    return;
                }
                try {
                    parseJson(result);
                    askQuestion();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void parseJson(JSONObject array) throws JSONException{
        player = array.getJSONObject("player1").getString("username");
        opponent = array.getJSONObject("player2").getString("username");
        JSONArray rounds = array.getJSONArray("rounds");
        JSONObject roundsObject = rounds.getJSONObject(rounds.length() - 1);
        JSONArray questions = roundsObject.getJSONArray("questions");
        //TODO More questions on server questions.length()
        for(int i = 0; i < questions.length(); i++){
            correctAlternatives.add(((JSONObject)questions.get(i)).getJSONObject("correct_answer").getString("name"));
            songUrls.add(((JSONObject)questions.get(i)).getJSONObject("correct_answer").getString("url"));
            JSONArray alternatives = ((JSONObject)questions.get(i)).getJSONArray("alternatives");
            List<String> innerList = new ArrayList<>();
            for (int j = 0; j < 3; j++){
                //TODO RANDOMLY GET ALTERNATIVES
                innerList.add(((JSONObject)alternatives.get(j)).getString("name"));
            }
            innerList.add(((JSONObject)questions.get(i)).getJSONObject("correct_answer").getString("name"));
            this.alternatives.add(innerList);
            System.out.println("Length of json:" + alternatives.length());
        }

        System.out.println("SINDRE: sanger: " + songUrls);
        System.out.println("Sindre: correctalt: " + correctAlternatives);
        System.out.println("Sindre: alternativ1: " + alternatives.get(0));
        System.out.println("Lengde: " + alternatives.get(0).size());
    }

    private void sendScore(){

    }
}