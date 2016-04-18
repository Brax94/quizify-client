package com.myapps.quizify.quizifyclient.mainMenu;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myapps.quizify.quizifyclient.game.CategoryActivity;
import com.myapps.quizify.quizifyclient.logIn.QuizifyLogin;
import com.myapps.quizify.quizifyclient.util.RequestHandler;
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

        mQueue = RequestHandler.getInstance(this.getApplicationContext())
                .getRequestQueue();

        //Checks if logged in - TODO: Create some sort of session for login + autologin?
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.prefs = prefs;
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

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        try {
            jsonRequest();
            System.out.println("Trying Request");
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Request Failed 1.");
        }
        renderLists();
    }

    //ArrayList<String> yourTurnGames = new ArrayList<>();
    //String[] urturn = {"Sindrefl", "morten", "andreas","Sindrefl", "morten"};

    public void renderLists(){
        CustomRelativeAdapter yourTurnAdapter = new CustomRelativeAdapter(MainMenuActivity.this, yourTurn, "Play");
        ListView yourTurnListView = (ListView) findViewById(R.id.yourTurnList);
        yourTurnListView.setAdapter(yourTurnAdapter);
        Utility.setDynamicHeight(yourTurnListView);
        yourTurnListView.setEnabled(true);

        CustomRelativeAdapter invitesAdapter = new CustomRelativeAdapter(MainMenuActivity.this, invites, "Play");        ListView invitesList = (ListView) findViewById(R.id.yourInvites);
        invitesList.setAdapter(invitesAdapter);
        Utility.setDynamicHeight(invitesList);
        invitesList.setEnabled(true);

        CustomRelativeAdapter theirTurnAdapter = new CustomRelativeAdapter(MainMenuActivity.this, theirTurn, "Their Turn");        ListView theirTurnList = (ListView) findViewById(R.id.theirTurnList);
        theirTurnList.setAdapter(theirTurnAdapter);
        Utility.setDynamicHeight(theirTurnList);
        theirTurnList.setEnabled(true);

        CustomRelativeAdapter pendingAdapter = new CustomRelativeAdapter(MainMenuActivity.this, pending, "Pending");        ListView pendingList = (ListView) findViewById(R.id.pendingList);
        pendingList.setAdapter(pendingAdapter);
        Utility.setDynamicHeight(pendingList);
        pendingList.setEnabled(true);


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
                if(array.getJSONObject(array.length()-1).getJSONObject("whos_turn").getString("username").equals(prefs.getString("username", "#noValidUsername"))){
                    yourTurn.add(game);
                }
                else {
                    theirTurn.add(game);
                }
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
    }
    //TODO: This method is for testing, can be improved - use best practise!
    private static String url = "http://kane.royrvik.org:8000/games/";
    private RequestQueue mQueue;
    public void jsonRequest() throws JSONException{
        System.out.println("RequestMethod is running");
        final JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, url,
                new JSONArray(), new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("Got Response: " + response.toString());
                try {
                    System.out.println("sorting");
                    sortJsonRequest(response);
                    renderLists();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, RequestHandler.getInstance(MainMenuActivity.this.getApplicationContext()));
        System.out.println(jsonRequest.toString());
        this.mQueue.add(jsonRequest);
    }
}
