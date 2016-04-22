package com.myapps.quizify.quizifyclient.game;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.google.android.gms.games.Game;
import com.google.gson.JsonObject;
import com.myapps.quizify.quizifyclient.R;
import com.myapps.quizify.quizifyclient.mainMenu.MainMenuActivity;
import com.myapps.quizify.quizifyclient.net.quizifyapp.net.APIAuthenticationResponseListener;
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

    private List<Button> buttons;
    private String[] categories;
    private int[] categoryIds;

    private static int[] ids = new int[]{R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4,R.id.btn5,R.id.btn6};

    private View mProgressView;
    private View mCategoryView;
    private String gameType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getIntent().hasExtra("game_type")){
            gameType = getIntent().getStringExtra("game_type");
            if(gameType.equals("Play")){
                Intent i = new Intent(CategoryActivity.this, RoundActivity.class);
                i.putExtra("id", getIntent().getIntExtra("id",-1));
                startActivity(i);
            }
        }


        buttons = new ArrayList<>();
        setContentView(R.layout.activity_category);
        mProgressView = findViewById(R.id.category_process);
        mCategoryView = findViewById(R.id.category_form);

        System.out.println("HEI IGJEN HER ER GAME ID FRA CATEGORY" + getIntent().getIntExtra("game_id", -1));

        initCategories();
    }

    private void initCategories(){
        showProgress(true);
        RenderPageTask mRenderTask = new RenderPageTask();
        mRenderTask.execute((Void) null);
    }

    private void chooseCategory(int category){
        Intent i = new Intent(CategoryActivity.this, RoundActivity.class);
        i.putExtra("category_id", categoryIds[category]);
        i.putExtra("game_id", getIntent().getIntExtra("game_id", -1));
        if(getIntent().hasExtra("game_type")) i.putExtra("game_type", gameType);
        System.out.println("HELLOTHERE " + gameType);
        finish();
        startActivity(i);
    }

    public class RenderPageTask extends AsyncTask<Void, Void, Boolean> {

        private boolean serverAuth = true;

        public RenderPageTask() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            NetworkManager.getInstance(getApplicationContext()).getCategories(new APIObjectResponseListener<String, JSONArray>() {
                @Override
                public void getResult(String error, JSONArray result) {
                    if (error != null){
                        System.err.print(error);
                        serverAuth = false;
                        return;
                    }
                    try{
                        categoryIds = new int[6];
                        categories = new String[6];
                        for (int i = 0; i < categories.length; i++){
                            categories[i] = ((JSONObject) result.get(i)).getString("name");
                            categoryIds[i] = (((JSONObject) result.get(i)).getInt("id"));
                        }
                        serverAuth = false;
                    }catch(JSONException e){
                        e.printStackTrace();
                    }

                }
            });
            while(serverAuth){}
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);

            if (success) {
                for (int j = 0; j < ids.length; j++){
                    Button button = (Button) findViewById(ids[j]);
                    button.setText((CharSequence) categories[j]);
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
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mCategoryView.setVisibility(show ? View.GONE : View.VISIBLE);
            mCategoryView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCategoryView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mCategoryView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
