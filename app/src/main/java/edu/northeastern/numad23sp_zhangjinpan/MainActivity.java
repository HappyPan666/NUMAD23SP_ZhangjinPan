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

        //Assignment4: Link Collector
        Button button_link_collector = (Button) findViewById(R.id.buttonLinkCollector);
        button_link_collector.setOnClickListener(this);

        //Assignment5: Prime Directive
        Button button_prime_directive = (Button) findViewById(R.id.buttonPrimeDirective);
        button_prime_directive.setOnClickListener(this);

        //Assignment7: Location
        Button button_location = (Button) findViewById(R.id.buttonLocation);
        button_location.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_aboutMe) {
            Intent intent = new Intent(MainActivity.this, about_me.class);
            startActivity(intent);
        } else if(view.getId() == R.id.buttonClicky) {
            Intent intent = new Intent(MainActivity.this, ClickyActivity.class);
            startActivity(intent);
        } else if(view.getId() == R.id.buttonLinkCollector) {
            Intent intent = new Intent(MainActivity.this, link_collector.class);
            startActivity(intent);
        } else if(view.getId() == R.id.buttonPrimeDirective) {
            Intent intent = new Intent(MainActivity.this, prime_directive.class);
            startActivity(intent);
        } else if(view.getId() == R.id.buttonLocation) {
            Intent intent = new Intent(MainActivity.this, location.class);
            startActivity(intent);
        }

    }
}
