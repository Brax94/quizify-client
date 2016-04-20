package com.myapps.quizify.quizifyclient.game;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class RoundActivity extends Activity implements MediaPlayer.OnPreparedListener{

    private static final int NUMBER_OF_QUESTIONS = 5;

    private String opponent;
    private String player;
    private String category;

    private int roundNumber;

    private int score;

    private List<String> songUrls;
    private List<List<String>> alternatives;
    private List<String> correctAlternatives;

    private int currentQuestion;
    private MediaPlayer mMediaPlayer;
    private ProgressBar bar;
    private CountDownTimer timer;
    private Button btn;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button disp;
    private Button disp2;
    private TextView p1;
    private TextView p2;


    private View mProgressView;
    private View mRoundView;


    private String currentSongURL(){return songUrls.get(currentQuestion);}
    private List<String> currentAlternatives(){return alternatives.get(currentQuestion);}
    private boolean checkAlternative(int alternative){return alternatives.get(currentQuestion).get(alternative).equals(correctAlternatives.get(currentQuestion));}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initRound(getIntent().getIntExtra("game_id", -1));

        setContentView(R.layout.activity_round);

        p1 = (TextView) findViewById(R.id.round_player1);
        p2 = (TextView) findViewById(R.id.round_player2);

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

        mProgressView = findViewById(R.id.round_process);
        mRoundView = findViewById(R.id.round_form);
        initRound(1);
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
        List<String> alternatives = currentAlternatives();
        btn.setText((CharSequence) alternatives.get(0));
        btn1.setText((CharSequence) alternatives.get(1));
        btn2.setText((CharSequence) alternatives.get(2));
        btn3.setText((CharSequence) alternatives.get(3));
        disp.setText((CharSequence) "Score: " + Integer.toString(score));
        disp2.setText((CharSequence) "Round: " + Integer.toString(currentQuestion + 1));

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
            mMediaPlayer.setDataSource(currentSongURL());
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
                    p1.setText(player);
                    p2.setText(opponent);
                    askQuestion();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        /*
        showProgress(true);
        RenderPageTask mRenderTask = new RenderPageTask(id);
        mRenderTask.execute((Void) null);
        */
    }

    private static final String[] buttonChoices = new String[]{"name", "artist"};
    private static final Random RANDOM = new Random();
    private String randomButtonChoice(){return buttonChoices[RANDOM.nextInt(buttonChoices.length)];}

    private void parseJson(JSONObject array) throws JSONException{
        player = array.getJSONObject("player1").getString("username");
        opponent = array.getJSONObject("player2").getString("username");
        JSONArray rounds = array.getJSONArray("rounds");
        JSONArray questions = rounds.getJSONObject(rounds.length() - 1).getJSONArray("questions");

        for(int i = 0; i < NUMBER_OF_QUESTIONS; i++){
            String choice = randomButtonChoice();
            JSONObject question = (JSONObject) questions.get(i);
            songUrls.add(question.getJSONObject("correct_answer").getString("url"));
            correctAlternatives.add(question.getJSONObject("correct_answer").getString(choice));

            JSONArray alternatives = question.getJSONArray("alternatives");
            List<String> innerList = new ArrayList<>();
            for (int j = 0; j < 3; j++){innerList.add(((JSONObject)alternatives.get(j)).getString(choice));}
            innerList.add(question.getJSONObject("correct_answer").getString(choice));
            Collections.shuffle(innerList);
            this.alternatives.add(innerList);
        }
    }

    private void sendScore(){

    }

    public class RenderPageTask extends AsyncTask<Void, Void, Boolean> {

        private boolean serverAuth = true;
        private int id;

        public RenderPageTask(int id) {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            NetworkManager.getInstance(getApplicationContext()).getSingleGame(id, new APIObjectResponseListener<String, JSONObject>() {
                @Override
                public void getResult(String error, JSONObject result) {
                    if(error != null){
                        System.err.print(error);
                        return;
                    }
                    try {
                        parseJson(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            while(serverAuth){}
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);

            if (success) {

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

                askQuestion();
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRoundView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRoundView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRoundView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRoundView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {}
}