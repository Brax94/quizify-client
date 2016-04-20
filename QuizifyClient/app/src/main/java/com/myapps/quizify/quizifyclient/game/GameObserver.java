package com.myapps.quizify.quizifyclient.game;


import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import com.myapps.quizify.quizifyclient.net.quizifyapp.net.APIObjectResponseListener;
import com.myapps.quizify.quizifyclient.net.quizifyapp.net.NetworkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sindre on 15.04.2016.
 */
public class GameObserver {

    private String opponent;
    private String player;


    public static int NUMBER_OF_ROUNDS = 5;
    private static GameObserver mObserver;

    private String category;
    private int score;
    private List<String> songUrls= Arrays.asList(
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

    private List<String> correctAlternatives = Arrays.asList(new String[]{"Alternative 3","Alternative 3","Alternative 3","Alternative 3","Alternative 3"});

    private int currentRound;

    private GameObserver(int id, Context context){

        //correctAlternatives = new ArrayList<>();
        //songUrls = new ArrayList<>();
        this.score = 0;
        this.currentRound = 0;
        initList();

        /*
        NetworkManager.getInstance(context).getSingleGame(id, new APIObjectResponseListener<String, JSONObject>() {
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
        */
    }

    public static synchronized GameObserver getInstance(int id, Context context){
        if (mObserver == null) mObserver = new GameObserver(id, context);
        return mObserver;
    }

    public String getSongURL(){return songUrls.get(currentRound);}
    public List<String> getAlternatives(){return alternatives.get(currentRound);}
    public boolean nextRound(int score){
        this.score+=score;
        boolean isFinished = (++currentRound == NUMBER_OF_ROUNDS);
        if(isFinished){
            sendScore();
            destroy();
        }
        return isFinished;
    }
    public boolean checkAlternative(int alternative){return alternatives.get(currentRound).get(alternative).equals(correctAlternatives.get(currentRound));}

    public int getScore(){return score;}
    public int getCurrentRound(){return currentRound;}

    private void sendScore(){

    }

    private void destroy(){
        mObserver = null;
    }

    private void parseJson(JSONObject array) throws JSONException{
        player = array.getJSONObject("player1").getString("username");
        opponent = array.getJSONObject("player2").getString("username");
        JSONArray rounds = array.getJSONArray("rounds");
        JSONObject roundsObject = rounds.getJSONObject(rounds.length() - 1);
        JSONArray questions = roundsObject.getJSONArray("questions");
        for(int i = 0; i < questions.length(); i++){
            correctAlternatives.add(((JSONObject)questions.get(i)).getJSONObject("correct_answer").getString("name"));
            songUrls.add(((JSONObject)questions.get(i)).getJSONObject("correct_answer").getString("url"));
        }


        System.out.println("SINDRE: sanger: " + songUrls);
        System.out.println("Sindre: correctalt: " + correctAlternatives);
    }
}