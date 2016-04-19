package com.myapps.quizify.quizifyclient.game;


import android.content.Context;
import android.widget.Toast;

import com.myapps.quizify.quizifyclient.net.quizifyapp.net.APIObjectResponseListener;
import com.myapps.quizify.quizifyclient.net.quizifyapp.net.NetworkManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Sindre on 15.04.2016.
 */
public class GameObserver {

    public static int NUMBER_OF_ROUNDS = 5;
    private static GameObserver mObserver;

    private String category;
    private int score;
    private List<String> songUrls = Arrays.asList(
            "https://p.scdn.co/mp3-preview/04fac4f932a798c9dc0eb03e1df2c78081becb6e",
            "https://p.scdn.co/mp3-preview/04fac4f932a798c9dc0eb03e1df2c78081becb6e",
            "https://p.scdn.co/mp3-preview/04fac4f932a798c9dc0eb03e1df2c78081becb6e",
            "https://p.scdn.co/mp3-preview/04fac4f932a798c9dc0eb03e1df2c78081becb6e",
            "https://p.scdn.co/mp3-preview/04fac4f932a798c9dc0eb03e1df2c78081becb6e");
    //Includes all alternatives, not only the wrong ones:
    private List<List<String>> alternatives;
    //For testing, will be removed:
    private void initList(){
        alternatives = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            alternatives.add(Arrays.asList(new String[]{"Alternative 1", "Alternative 2", "Alternative 3", "Alternative 4"}));
        }
    }

    private List<String> correctAlternatives;

    private int currentRound;

    private GameObserver(final Context context, int gameId){
        this.category = category;
        this.score = 0;
        this.currentRound = 0;
       //initList();

/*
        NetworkManager manager = NetworkManager.getInstance(context);
        manager.getSingleGame(gameId, new APIObjectResponseListener<String, Map<String, Object>>() {
            @Override
            public void getResult(String error, Map<String, Object> result) {
                if (error == null){
                    correctAlternatives = ((List) result.get("correct_answer"));
                    alternatives = ((List<List<String>>) result.get("alternatives"));
                }else Toast.makeText(context, error, Toast.LENGTH_LONG).show();
            }
        });
        */
    }

    public static synchronized GameObserver getInstance(Context context, int gameId){
        if (mObserver == null) mObserver = new GameObserver(context, gameId);
        return mObserver;
    }

    public String getSongURL(){return songUrls.get(currentRound);}
    public List<String> getAlternatives(){return alternatives.get(currentRound);}
    public boolean nextRound(int score){
        this.score+=score;
        boolean isFinished = ++currentRound == NUMBER_OF_ROUNDS;
        if (isFinished){
            //TODO POST RESULT
            destroy();
        }
        return isFinished;
    }
    public boolean checkAlternative(int alternative){return alternatives.get(currentRound).get(alternative).equals(correctAlternatives.get(currentRound));}

    public int getScore(){return score;}
    public int getCurrentRound(){return currentRound;}

    private void destroy(){
        mObserver = null;
    }
}
