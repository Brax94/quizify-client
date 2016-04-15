package com.myapps.quizify.quizifyclient.game;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private List<String> correctAlternatives = Arrays.asList(new String[]{"Alternative 3","Alternative 3","Alternative 3","Alternative 3","Alternative 3"});

    private int currentRound;

    private GameObserver(String category){
        this.category = category;
        this.score = 0;
        this.currentRound = 0;
        initList();
    }

    public static synchronized GameObserver getInstance(String category){
        //TODO: initialize with variables from server call with category
        //Do we need another user as well?
        if (mObserver == null) mObserver = new GameObserver(category);
        return mObserver;
    }

    public String getSongURL(){return songUrls.get(currentRound);}
    public String getCorrectAlternative(){return correctAlternatives.get(currentRound);}
    public List<String> getAlternatives(){return alternatives.get(currentRound);}
    public boolean nextRound(int score){
        this.score+=score;
        System.out.println("HEY THERE! SCORE IS: " + score);
        return ++currentRound == NUMBER_OF_ROUNDS;
    }
    public boolean checkAlternative(int alternative){return alternatives.get(currentRound).get(alternative).equals(correctAlternatives.get(currentRound));}

    public int getScore(){return score;}
    public int getCurrentRound(){return currentRound;}
}
