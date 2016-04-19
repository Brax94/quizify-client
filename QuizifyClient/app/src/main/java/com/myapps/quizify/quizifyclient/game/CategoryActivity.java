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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myapps.quizify.quizifyclient.R;
import com.myapps.quizify.quizifyclient.mainMenu.MainMenuActivity;
import com.myapps.quizify.quizifyclient.net.quizifyapp.net.APIObjectResponseListener;
import com.myapps.quizify.quizifyclient.net.quizifyapp.net.NetworkManager;
import com.myapps.quizify.quizifyclient.util.RequestHandler;
import com.myapps.quizify.quizifyclient.util.SystemUiHider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CategoryActivity extends Activity implements Response.ErrorListener{

    //Todo: implement via server
    private List<String> categories = Arrays.asList(new String[]{"Test for server: reply wonderwaste", "Cat2", "TO ROUND", "Something else", "Anything but Justin Bieber", "Hello world"});

    List correctAlternatives;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initCategories();

        setContentView(R.layout.activity_category);



        final Button btn =(Button) findViewById(R.id.btn1);
        btn.setText((CharSequence) categories.get(0));

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                NetworkManager manager = NetworkManager.getInstance(getApplicationContext());
                manager.getCateogries(new APIObjectResponseListener<String, JSONArray>() {
                    @Override
                    public void getResult(String error, JSONArray result) {
                        if(error != null){
                            System.err.print(error);
                            return;
                        }
                        /*
                        Random rand = new Random();
                        for(int i =0; i < 6; i++){
                            int n = rand.nextInt(result.length() - i);
                            try {
                                categories.set(i, ((JSONObject) result.get(n)).getString("name"));
                            }catch (JSONException e){e.printStackTrace();};
                        }
                        */
                        categories.set(5, result.get())
                    }
                });
            };
    });


        final Button btn1 =(Button) findViewById(R.id.btn2);
        btn1.setText((CharSequence) categories.get(1));


        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                chooseCategory(1);
            }
        });


        Button btn2 =(Button) findViewById(R.id.btn3);
        btn2.setText((CharSequence) categories.get(2));

        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                chooseCategory(2);
            }
        });


        Button btn3 =(Button) findViewById(R.id.btn4);
        btn3.setText((CharSequence) categories.get(3));

        btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                chooseCategory(3);
            }
        });

        Button btn4 =(Button) findViewById(R.id.btn5);
        btn4.setText((CharSequence) categories.get(4));

        btn4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                chooseCategory(4);
            }
        });

        Button btn5 =(Button) findViewById(R.id.btn6);
        btn5.setText((CharSequence) categories.get(5));

        btn5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                chooseCategory(5);
            }
        });

    }

    private void initCategories(){
        //TODO get categories from server
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

    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
    }
}
