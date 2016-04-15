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
import com.myapps.quizify.quizifyclient.R;
import com.myapps.quizify.quizifyclient.mainMenu.MainMenuActivity;
import com.myapps.quizify.quizifyclient.util.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryActivity extends Activity implements Response.ErrorListener{

    //Todo: implement via server
    private List<String> categories = Arrays.asList(new String[]{"Test for server: reply wonderwaste", "Cat2", "TO ROUND", "Something else", "Anything but Justin Bieber", "Hello world"});

    private RequestQueue mQueue;
    private static String url = "http://kane.royrvik.org:8000/categories/";
    private static String dummy = "http://echo.jsontest.com/title/ipsum/content/blah";
    private static String dummy2 = "http://echo.jsontest.com/key/value/one/two";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQueue = RequestHandler.getInstance(this.getApplicationContext())
                .getRequestQueue();

        initCategories();

        setContentView(R.layout.activity_category);



        final Button btn =(Button) findViewById(R.id.btn1);
        btn.setText((CharSequence) categories.get(0));

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, url,
                        new JSONArray(), new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        String text = "";
                        try {
                            text = response.getJSONObject(0).getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        btn.setText(text);
                    }
                }, RequestHandler.getInstance(CategoryActivity.this.getApplicationContext()));
                mQueue.add(jsonRequest);
            }
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

    @Override
    protected void onStart() {
        super.onStart();
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
            i.putExtra("score", 0);
            i.putExtra("round", 0);
        }
        finish();
        startActivity(i);

        /*
        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url,
                new JSONObject(), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                Intent i = new Intent(CategoryActivity.this, RoundActivity.class);
                i.putExtra("JSON", response.toString());
                i.putExtra("scores", new ArrayList<Integer>());
                startActivity(i);
            }
        }, CategoryActivity.this);
        mQueue.add(jsonRequest);
*/
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
    }
}
