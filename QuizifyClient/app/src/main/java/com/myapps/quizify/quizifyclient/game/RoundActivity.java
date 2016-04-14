package com.myapps.quizify.quizifyclient.game;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.myapps.quizify.quizifyclient.util.RequestHandler;
import com.myapps.quizify.quizifyclient.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.myapps.quizify.quizifyclient.R;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class RoundActivity extends Activity {



    private List alternatives = Arrays.asList(new String[]{"Alternative 1", "Alternative 2", "Alternative 3", "Alternative 4"});
    String url = "";

    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQueue = RequestHandler.getInstance(this.getApplicationContext()).getRequestQueue();

        setContentView(R.layout.activity_round);

        Button btn = (Button) findViewById(R.id.alternative1);
        btn.setText((CharSequence) alternatives.get(0));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCorrectAlternative(0)){

                }
            }
        });

        Button btn1 = (Button) findViewById(R.id.alternative2);
        btn1.setText((CharSequence) alternatives.get(1));

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCorrectAlternative(1)){

                }
            }
        });

        Button btn2 = (Button) findViewById(R.id.alternative3);
        btn2.setText((CharSequence) alternatives.get(2));

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCorrectAlternative(2)){

                }
            }
        });

        Button btn3 = (Button) findViewById(R.id.alternative4);
        btn3.setText((CharSequence) alternatives.get(3));

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCorrectAlternative(3)){

                }
            }
        });

    }

    private void initAlternatives(){
        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url,
                new JSONObject(), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                //Todo retrieve actual alternatives from Json File
            }
        }, RequestHandler.getInstance(this.getApplicationContext()));
        mQueue.add(jsonRequest);
    }

    private boolean isCorrectAlternative(int alternative){
        return false;
    }
}