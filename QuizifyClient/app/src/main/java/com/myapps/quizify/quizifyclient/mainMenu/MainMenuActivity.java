package com.myapps.quizify.quizifyclient.mainMenu;


import com.myapps.quizify.quizifyclient.logIn.QuizifyLogin;
import com.myapps.quizify.quizifyclient.net.quizifyapp.net.APIObjectResponseListener;
import com.myapps.quizify.quizifyclient.net.quizifyapp.net.NetworkManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.myapps.quizify.quizifyclient.R;
import com.myapps.quizify.quizifyclient.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 */
public class MainMenuActivity extends Activity {

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checks if logged in - TODO: Create some sort of session for login + autologin?

        if(!NetworkManager.getInstance(getApplicationContext()).existsValidToken()){
            Intent logInIntent = new Intent(this, QuizifyLogin.class);
            startActivity(logInIntent);
            NetworkManager.getInstance().destroy();
            finish();
        }

        setContentView(R.layout.activity_main_menu);
        prefs = PreferenceManager.getDefaultSharedPreferences(MainMenuActivity.this);

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
                NetworkManager.getInstance().destroy();
                prefs.edit().clear();
                finish();
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getGames();
        Log.d("ELIAS", "Trying Request");
    }

    //ArrayList<String> yourTurnGames = new ArrayList<>();
    //String[] urturn = {"Sindrefl", "morten", "andreas","Sindrefl", "morten"};

    public void renderLists(){
        CustomRelativeAdapter yourTurnAdapter = new CustomRelativeAdapter(MainMenuActivity.this, yourTurn, "Play", true);
        ListView yourTurnListView = (ListView) findViewById(R.id.yourTurnList);
        yourTurnListView.setAdapter(yourTurnAdapter);
        Utility.setDynamicHeight(yourTurnListView);
        yourTurnListView.setEnabled(false);

        CustomRelativeAdapter invitesAdapter = new CustomRelativeAdapter(MainMenuActivity.this, invites, "Accept", true);        ListView invitesList = (ListView) findViewById(R.id.yourInvites);
        invitesList.setAdapter(invitesAdapter);
        Utility.setDynamicHeight(invitesList);
        invitesList.setEnabled(false);

        CustomRelativeAdapter theirTurnAdapter = new CustomRelativeAdapter(MainMenuActivity.this, theirTurn, "Their Turn", false);        ListView theirTurnList = (ListView) findViewById(R.id.theirTurnList);
        theirTurnList.setAdapter(theirTurnAdapter);
        Utility.setDynamicHeight(theirTurnList);
        theirTurnList.setEnabled(false);

        CustomRelativeAdapter pendingAdapter = new CustomRelativeAdapter(MainMenuActivity.this, pending, "Pending", false);        ListView pendingList = (ListView) findViewById(R.id.pendingList);
        pendingList.setAdapter(pendingAdapter);
        Utility.setDynamicHeight(pendingList);
        pendingList.setEnabled(false);


    }

    //Arrays for sorting games to be run through custom adaptor
    private ArrayList<JSONObject> invites = new ArrayList<>();
    private ArrayList<JSONObject> yourTurn = new ArrayList<>();
    private ArrayList<JSONObject> theirTurn = new ArrayList<>();
    private ArrayList<JSONObject> pending = new ArrayList<>();

    //TODO:Make sure this method always corresponds with json file from server!

    public void sortJsonRequest(JSONArray games) throws JSONException {
        for(int i = 0; i < games.length(); i++){
            JSONObject game = games.getJSONObject(i);
            if(game.getString("invitation_status").equals("accepted")){
                JSONArray array = game.getJSONArray("rounds");
                if(array.length() != 0){
                if(array.getJSONObject(array.length()-1).getJSONObject("whos_turn").getString("username").equals(prefs.getString("username", "#noValidUsername"))){
                    yourTurn.add(game);
                }
                else {
                    theirTurn.add(game);
                }}
                pending.add(game);
            }
            else{
                if(game.getJSONObject("player1").getString("username").equals(prefs.getString("username", "#noValidUsername"))){
                    pending.add(game);
                }
                else{
                    invites.add(game);
                }
            }
        }
        System.out.println("Their Turn: "+theirTurn.toString());
        renderLists();
    }
    //Get games method

    public void getGames(){
        NetworkManager.getInstance(getApplicationContext()).getGames(new APIObjectResponseListener<String, JSONArray>() {
            @Override
            public void getResult(String error, JSONArray result) {
                if(error != null) {
                    Log.d("ELIAS_GETGAME_ERROR", error);
                    return;
                }
                try {
                    Log.d("ELIAS", "RESULT FROM SERVER: " + result.toString());
                    sortJsonRequest(result);
                } catch (JSONException e) {
                    //TODO: Show user some error or do something drastic!
                    e.printStackTrace();
                }
                }
        });
    }
}
