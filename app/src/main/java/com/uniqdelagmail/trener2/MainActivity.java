package com.uniqdelagmail.trener2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button clickActivity1Button;
    Button clickActivity2Button;
    Button clickActivity3Button;
    Button clickActivity4Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clickActivity1Button = (Button) findViewById(R.id.clickActivity1button);
        clickActivity2Button = (Button) findViewById(R.id.clickActivity2button);
        clickActivity3Button = (Button) findViewById(R.id.clickActivity3button);
        clickActivity4Button = (Button) findViewById(R.id.clickActivity4button);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.clickActivity1button:
                        clickActivity1(Exercise1.class);
                        break;
                    case R.id.clickActivity2button:
                        clickActivity1(Exercise2.class);
                        break;
                    case R.id.clickActivity3button:
                        clickActivity1(Exercise3.class);
                        break;
                    case R.id.clickActivity4button:
                        clickActivity1(Exercise4.class);
                        break;
                }
            }
        };

        clickActivity1Button.setOnClickListener(onClickListener);
        clickActivity2Button.setOnClickListener(onClickListener);
        clickActivity3Button.setOnClickListener(onClickListener);
        clickActivity4Button.setOnClickListener(onClickListener);

    }

    public void clickActivity1(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

}