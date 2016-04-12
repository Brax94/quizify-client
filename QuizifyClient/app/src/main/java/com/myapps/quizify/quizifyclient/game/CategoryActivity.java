package com.myapps.quizify.quizifyclient.game;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.myapps.quizify.quizifyclient.R;

import java.util.Arrays;
import java.util.List;

public class CategoryActivity extends Activity {

    private List categories = Arrays.asList(new String[]{"Rock", "Salsa", "Ipsum", "Something else", "Anything but Justin Bieber", "Hello world"});

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);


        Button btn =(Button) findViewById(R.id.btn1);
        btn.setText((CharSequence) categories.get(0));

        Button btn1 =(Button) findViewById(R.id.btn1);
        btn1.setText((CharSequence) categories.get(1));

        Button btn2 =(Button) findViewById(R.id.btn1);
        btn2.setText((CharSequence) categories.get(2));

        Button btn3 =(Button) findViewById(R.id.btn1);
        btn3.setText((CharSequence) categories.get(3));

        Button btn4 =(Button) findViewById(R.id.btn1);
        btn4.setText((CharSequence) categories.get(4));

        Button btn5 =(Button) findViewById(R.id.btn1);
        btn5.setText((CharSequence) categories.get(5));



    }

}
