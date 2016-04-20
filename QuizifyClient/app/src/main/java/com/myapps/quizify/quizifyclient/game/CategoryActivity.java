package com.myapps.quizify.quizifyclient.game;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.games.Game;
import com.myapps.quizify.quizifyclient.R;
import com.myapps.quizify.quizifyclient.mainMenu.MainMenuActivity;
import com.myapps.quizify.quizifyclient.net.quizifyapp.net.APIObjectResponseListener;
import com.myapps.quizify.quizifyclient.net.quizifyapp.net.NetworkManager;
import com.myapps.quizify.quizifyclient.util.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryActivity extends Activity {

    private List<String> categories = Arrays.asList(new String[]{"Test for server: reply wonderwaste", "Cat2", "TO ROUND", "Something else", "Anything but Justin Bieber", "Hello world"});

    private List<Button> buttons;

    private static int[] ids = new int[]{R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4,R.id.btn5,R.id.btn6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buttons = new ArrayList<>();
        initCategories();

        setContentView(R.layout.activity_category);
    }

    private void initCategories(){
        NetworkManager.getInstance(getApplicationContext()).getCategories(new APIObjectResponseListener<String, JSONArray>() {
            @Override
            public void getResult(String error, JSONArray result) {
                if (error != null){
                    System.err.print(error);
                    return;
                }
                try{
                    String[] s = new String[6];
                    for (int i = 0; i < s.length; i++){
                        s[i] = ((JSONObject) result.get(0)).getString("name");
                    }

                    for (int j = 0; j < ids.length; j++){
                        Button button = (Button) findViewById(ids[j]);
                        button.setText((CharSequence) s[j]);
                        buttons.add(button);
                    }
                    buttons.get(0).setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {chooseCategory(0);}
                    });
                    buttons.get(1).setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {chooseCategory(1);}
                    });
                    buttons.get(2).setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {chooseCategory(2);}
                    });
                    buttons.get(3).setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {chooseCategory(3);}
                    });
                    buttons.get(4).setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {chooseCategory(4);}
                    });
                    buttons.get(5).setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {chooseCategory(5);}
                    });

                }catch(JSONException e){
                    e.printStackTrace();
                }

            }
        });
    }

    private void chooseCategory(int category){

        String chosen = categories.get(category);



        Intent i = null;
        //TODO: find better implementation of flow?
        if(getIntent().hasExtra("previous")){
            if(getIntent().getStringExtra("previous").equals("RoundActivity")){
               //TODO: post result to server
               i = new Intent(CategoryActivity.this, MainMenuActivity.class);
            }
        }else{
            //TODO: post category choice to server
            //TODO: get resulting song urls and alternatives
            i = new Intent(CategoryActivity.this, RoundActivity.class);
            i.putExtra("Category", categories.get(category));
        }
        finish();
        startActivity(i);

    }
}
