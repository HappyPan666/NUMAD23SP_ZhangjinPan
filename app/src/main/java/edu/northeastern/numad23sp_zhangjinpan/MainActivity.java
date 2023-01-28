package edu.northeastern.numad23sp_zhangjinpan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assignment1: about Me
        Button button_AboutMe = (Button) findViewById(R.id.button_aboutMe);
        button_AboutMe.setOnClickListener(this);


        //Assignment3: ClickyClicky
        Button button_clicky = (Button) findViewById(R.id.buttonClicky);
        button_clicky.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_aboutMe) {
            Toast.makeText(MainActivity.this, "ZhangjinPan(pan.zhan@northeastern.edu)", Toast.LENGTH_LONG).show();
        } else if(view.getId() == R.id.buttonClicky) {
            Intent intent = new Intent(MainActivity.this, ClickyActivity.class);
            startActivity(intent);
        }

    }
}
