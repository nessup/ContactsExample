package com.aaln.contactsexample.app;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class EventActivity extends ActionBarActivity {
    ArrayList<String> events  = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        initEventArray();
        String contact = getIntent().getStringExtra("contact");
        System.out.println("Key 1" + contact);
        RelativeLayout rl=(RelativeLayout)findViewById(R.id.r2);
        ScrollView sv = new ScrollView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        ll.setOrientation(1);
        sv.addView(ll);

        Display display = getWindowManager().getDefaultDisplay();

        for(String event : events) {
            Button b = new Button(this);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setStroke(5, Color.BLUE);
            drawable.setColor(Color.RED);
            b.setBackgroundDrawable(drawable);
            ViewGroup.LayoutParams buttonLayoutParams = new ViewGroup.LayoutParams(display.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
            b.setLayoutParams(buttonLayoutParams);
            b.setText(event);
            b.setTextSize(40);
            b.setTextColor(Color.WHITE);
            ll.addView(b);
        }
        rl.addView(sv);
    }

    public void initEventArray() {
        events.add("Breakfast");
        events.add("Lunch");
        events.add("Dinner");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
