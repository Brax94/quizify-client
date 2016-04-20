package com.myapps.quizify.quizifyclient.mainMenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.myapps.quizify.quizifyclient.R;
import com.myapps.quizify.quizifyclient.game.CategoryActivity;
import com.myapps.quizify.quizifyclient.net.quizifyapp.net.APIObjectResponseListener;
import com.myapps.quizify.quizifyclient.net.quizifyapp.net.NetworkManager;

import java.util.Map;

public class NewGame extends AppCompatActivity {
    EditText mInviteView;
    View mInvFormView;
    View mProgressView;
    Intent sucessIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        mInviteView = (EditText) findViewById(R.id.inviteUsername);
        mInvFormView = findViewById(R.id.inv_form);
        mProgressView = findViewById(R.id.inv_process);

        sucessIntent = new Intent(this, MainMenuActivity.class);

        Button inviteButton = (Button) findViewById(R.id.inviteUsernameButton);
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cancel = false;
                Log.d("ELIAS_STRINGCHECK", "USERNAME: " + mInviteView.getText().toString());
                if(TextUtils.isEmpty(mInviteView.getText().toString())){
                    mInviteView.setError("Please enter a username to invite!");
                    mInviteView.requestFocus();
                    cancel = true;
                }
                if(!cancel) {
                    showProgress(true);
                    NetworkManager.getInstance(NewGame.this).sendInvite(mInviteView.getText().toString(), new APIObjectResponseListener<String, Map<String, Object>>() {
                        @Override
                        public void getResult(String error, Map<String, Object> result) {
                            if (error != null) {
                                showProgress(false);
                                mInviteView.setError("Can't find any user with that name");
                                mInviteView.requestFocus();
                                return;
                            } else {
                                Log.d("ELIAS_NMCHECK", "Result: " + result.toString());
                                startActivity(sucessIntent);
                                finish();
                            }

                        }
                    });
                }
            }
        });

        Button b1 =(Button) findViewById(R.id.Categories);

        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewGame.this, CategoryActivity.class);
                startActivity(i);
            }
        });

    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mInvFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mInvFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mInvFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mInvFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
