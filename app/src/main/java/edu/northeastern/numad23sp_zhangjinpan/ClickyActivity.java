package edu.northeastern.numad23sp_zhangjinpan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuPresenter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ClickyActivity extends AppCompatActivity implements View.OnClickListener{
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicky);
        //Button A
        Button btnA = (Button) findViewById(R.id.button_a);
        btnA.setOnClickListener(this);
        //Button B
        Button btnB = (Button) findViewById(R.id.button_b);
        btnB.setOnClickListener(this);
        //Button C
        Button btnC = (Button) findViewById(R.id.button_c);
        btnC.setOnClickListener(this);
        //Button D
        Button btnD = (Button) findViewById(R.id.button_d);
        btnD.setOnClickListener(this);
        //Button E
        Button btnE = (Button) findViewById(R.id.button_e);
        btnE.setOnClickListener(this);
        //Button F
        Button btnF = (Button) findViewById(R.id.button_f);
        btnF.setOnClickListener(this);

        //TextView
        text = findViewById(R.id.txt_pressed);

    }

    @SuppressLint("SetTextI18n")
    public void onClick(View view) {
        if (view.getId() == R.id.button_a) {
            text.setText("Pressed: A");
        } else if (view.getId() == R.id.button_b) {
            text.setText("Pressed: B");
        } else if (view.getId() == R.id.button_c) {
            text.setText("Pressed: C");
        } else if (view.getId() == R.id.button_d) {
            text.setText("Pressed: D");
        } else if (view.getId() == R.id.button_e) {
            text.setText("Pressed: E");
        } else if (view.getId() == R.id.button_f) {
            text.setText("Pressed: F");
        }


    }
}