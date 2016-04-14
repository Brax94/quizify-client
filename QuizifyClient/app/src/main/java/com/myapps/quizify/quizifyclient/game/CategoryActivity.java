package com.myapps.quizify.quizifyclient.game;

import android.app.Activity;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.myapps.quizify.quizifyclient.R;
import com.myapps.quizify.quizifyclient.util.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class CategoryActivity extends Activity{

    //Todo: implement via server
    private List categories = Arrays.asList(new String[]{"Send request1", "Send request2", "TO ROUND", "Something else", "Anything but Justin Bieber", "Hello world"});

    private RequestQueue mQueue;
    private static String url = "http://localhost:8000/categories/?format=json";
    private static String dummy = "http://echo.jsontest.com/title/ipsum/content/blah";
    private static String dummy2 = "http://echo.jsontest.com/key/value/one/two";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQueue = RequestHandler.getInstance(this.getApplicationContext())
                .getRequestQueue();

        setContentView(R.layout.activity_category);

        final Button btn =(Button) findViewById(R.id.btn1);
        btn.setText((CharSequence) categories.get(0));

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, dummy,
                        new JSONObject(), new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        String text = "";
                        try {
                            text = response.getString("content");
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


        //Changes button text to value of field "one" on response
        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url,
                        new JSONObject(), new Response.Listener(){
                    @Override
                    public void onResponse(Object response) {
                        String text = "";
                        try {
                            text = ((JSONObject) response).getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        btn1.setText(text);
                    }
                }, RequestHandler.getInstance(CategoryActivity.this.getApplicationContext()));
                mQueue.add(jsonRequest);
            }
        });


        Button btn2 =(Button) findViewById(R.id.btn3);
        btn2.setText((CharSequence) categories.get(2));

        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CategoryActivity.this, RoundActivity.class);
                startActivity(i);
            }
        });


        Button btn3 =(Button) findViewById(R.id.btn4);
        btn3.setText((CharSequence) categories.get(3));

        Button btn4 =(Button) findViewById(R.id.btn5);
        btn4.setText((CharSequence) categories.get(4));

        Button btn5 =(Button) findViewById(R.id.btn6);
        btn5.setText((CharSequence) categories.get(5));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
