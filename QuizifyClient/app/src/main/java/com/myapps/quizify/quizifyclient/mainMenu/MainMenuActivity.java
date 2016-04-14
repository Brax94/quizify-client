package com.myapps.quizify.quizifyclient.mainMenu;

import com.myapps.quizify.quizifyclient.game.CategoryActivity;
import com.myapps.quizify.quizifyclient.logIn.QuizifyLogin;
import com.myapps.quizify.quizifyclient.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.myapps.quizify.quizifyclient.R;

import java.util.ArrayList;

/**
 */
public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checks if logged in - TODO: Create some sort of session for login + autologin?

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isLogin = prefs.getBoolean("isLogin", false); // get value of last login status

        if(!isLogin){
            Intent logInIntent = new Intent(this, QuizifyLogin.class);
            startActivity(logInIntent);
        }
        //Makes you log in every time - TODO: REMOVE WHEN DONE TESTING
        //prefs.edit().putBoolean("isLogin", false).commit();

        setContentView(R.layout.activity_main_menu);

        Button new_game = (Button) findViewById(R.id.new_game);
        new_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newGame = new Intent(MainMenuActivity.this, NewGame.class);
                startActivity(newGame);
            }
        });

        Button sign_out = (Button) findViewById(R.id.signOut_button);
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signOut = new Intent(MainMenuActivity.this, QuizifyLogin.class);
                startActivity(signOut);
                prefs.edit().putBoolean("isLogin", false).commit();
            }
        });
        renderYourTurns(urturn);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    ArrayList<String> yourTurnGames = new ArrayList<>();
    String[] urturn = {"Sindrefl", "morten", "andreas","Sindrefl", "morten"};

    public void renderYourTurns(String[] games){
        for(String players : games){
            System.out.println(players);
            yourTurnGames.add(players);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.list_cosmetics ,R.id.list_text_cosmetics, yourTurnGames);
        ListView yourTurnListView = (ListView) findViewById(R.id.yourTurnList);
        yourTurnListView.setAdapter(arrayAdapter);
        yourTurnListView.setEnabled(true);
    }
}
