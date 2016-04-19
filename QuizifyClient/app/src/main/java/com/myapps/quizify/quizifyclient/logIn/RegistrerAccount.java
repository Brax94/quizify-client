package com.myapps.quizify.quizifyclient.logIn;

import com.myapps.quizify.quizifyclient.mainMenu.MainMenuActivity;
import com.myapps.quizify.quizifyclient.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registrer_account);


        Button register = (Button) findViewById(R.id.registerButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Send info from textfields to server get token in return
                String user = ((EditText) findViewById(R.id.usernameField)).getText().toString();
                String pass = ((EditText) findViewById(R.id.passwordField)).getText().toString();
                //Todo: Handler.registerUser(user, pass);
                Log.d("Username: ", user);
                Log.d("Password: ", pass);

                Intent i = new Intent(RegistrerAccount.this, MainMenuActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
