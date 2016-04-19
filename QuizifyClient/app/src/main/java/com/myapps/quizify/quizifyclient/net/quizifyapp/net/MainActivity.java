/*package com.myapps.quizify.quizifyclient.net.quizifyapp.net;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button registerButton;
    EditText usernameField;
//    EditText emailField;
    EditText passwordField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkManager.getInstance(this);
        setContentView(R.layout.activity_main);

        registerButton = (Button) findViewById(R.id.register_button);
        usernameField = (EditText) findViewById(R.id.username);
//        emailField = (EditText) findViewById(R.id.email);
        passwordField = (EditText) findViewById(R.id.password);

    }

    public void setLogin(View v) {
        String usernameText = usernameField.getText().toString();
//        String emailText = emailField.getText().toString();
        String passwordText = passwordField.getText().toString();


        NetworkManager.getInstance().login(usernameText, passwordText, new APIAuthenticationResponseListener<String>() {
            @Override
            public void getResult(String error) {
                if(error != null) {
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(getApplicationContext(), "Successfully logged in", Toast.LENGTH_LONG).show();

                NetworkManager.getInstance().getGames(new APIObjectResponseListener<String, Map<String,Object>>() {
                    @Override
                    public void getResult(String error, Map<String,Object> result) {
                        if(error != null) {
                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                            return;
                        }

                        Log.d("ANDREAS", result.toString());
                    }
                });

            }
        });

    }
}
*/
