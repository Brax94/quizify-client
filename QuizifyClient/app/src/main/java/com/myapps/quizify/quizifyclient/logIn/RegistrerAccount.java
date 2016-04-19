package com.myapps.quizify.quizifyclient.logIn;

import com.myapps.quizify.quizifyclient.mainMenu.MainMenuActivity;
import com.myapps.quizify.quizifyclient.net.quizifyapp.net.APIAuthenticationResponseListener;
import com.myapps.quizify.quizifyclient.net.quizifyapp.net.NetworkManager;
import com.myapps.quizify.quizifyclient.util.SystemUiHider;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.myapps.quizify.quizifyclient.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class RegistrerAccount extends Activity {

    private RegisterNewAccount mRegTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mRegFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registrer_account);

        mEmailView = (EditText) findViewById(R.id.email_register_field);
        mUsernameView = (EditText) findViewById(R.id.usernameField);
        mPasswordView = (EditText) findViewById(R.id.passwordField);

        mProgressView = findViewById(R.id.reg_process);
        mRegFormView = findViewById(R.id.reg_form);


        Button register = (Button) findViewById(R.id.registerButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Send info from textfields to server get token in return
                attemptRegister();

            }
        });
    }

    public void attemptRegister(){

        if(mRegTask != null){
            return;
        }

        mEmailView.setError(null);
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String user = mUsernameView.getText().toString();
        String pass = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(email)){
            mEmailView.setError("Field Cannot Be Empty");
            focusView = mEmailView;
            cancel =  true;
        }
        if(TextUtils.isEmpty(user)){
            mUsernameView.setError("Field Cannot Be Empty");
            focusView = mEmailView;
            cancel = true;
        }
        if(TextUtils.isEmpty(pass) || pass.length() < 8){
            mPasswordView.setError("Password too short");
            focusView = mPasswordView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mRegTask = new RegisterNewAccount(email,user, pass);
            mRegTask.execute((Void) null);
        }


    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mRegFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private String globaleError;
    private boolean isRegAuth;
    private boolean serverAuth = true;

    public class RegisterNewAccount extends AsyncTask<Void, Void, Boolean>{

        private final String mEmail;
        private final String mPassword;
        private final String mUsername;

        public RegisterNewAccount(String email, String user, String pass){
            mEmail = email;
            mPassword = pass;
            mUsername = user;

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            NetworkManager.getInstance(getApplicationContext()).register(mEmail, mUsername, mPassword, new APIAuthenticationResponseListener<String>() {
                @Override
                public void getResult(String object) {
                    if(object != null){
                        isRegAuth = false;
                        globaleError = object;
                        serverAuth = false;
                        return;
                    }
                    isRegAuth = true;
                    serverAuth = false;
                }
            });
            while(serverAuth){}

            return isRegAuth;
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            mRegTask = null;
            showProgress(false);

            if (success) {
                finish();

                //TODO: Fix welcome view and confirmation

                Intent intent = new Intent(RegistrerAccount.this, QuizifyLogin.class);
                startActivity(intent);
            } else {
                mPasswordView.setError(globaleError);
                mPasswordView.requestFocus();
            }
        }
        @Override
        protected void onCancelled() {
            mRegTask = null;
            showProgress(false);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
