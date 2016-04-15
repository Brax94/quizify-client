package com.myapps.quizify.quizifyclient.game;


import java.util.List;

/**
 * Created by Sindre on 15.04.2016.
 */
public class GameObserver {

    public static int NUMBER_OF_ROUNDS = 5;
    private static GameObserver mObserver;

    private String category;
    private int score;
    private List<String> songUrls;
    private List<List<String>> alternatives;
    private List<String> correctAlternatives;

    private int currentRound;

    public static synchronized GameObserver getInstance(String category){
        //TODO: initialize with variables from server call with category
        //Do we need another user as well?
        if (mObserver == null) return new GameObserver();
        return mObserver;
    }

    public String getSongURL(){return songUrls.get(currentRound);}
    public String getCorrectAlternative(){return correctAlternatives.get(currentRound);}
    public List<String> getAlternatives(){return alternatives.get(currentRound);}
    public boolean nextRound(){return ++currentRound == NUMBER_OF_ROUNDS;}
}
