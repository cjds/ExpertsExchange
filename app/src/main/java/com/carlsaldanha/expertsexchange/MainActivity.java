package com.carlsaldanha.expertsexchange;

import android.content.Intent;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import android.net.Uri;

public class MainActivity extends AppCompatActivity {

    int step=0;

    int no_of_steps=10;
    int procedure_id=0;
    Step[] steps;

    VideoView video;
    ImageView image;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Title > Step 1");
        Intent i=getIntent();
        String user_id = i.getStringExtra("user_id");
        String task_id = i.getStringExtra("task_id");

        text=(TextView)findViewById(R.id.text);
        video=(VideoView)findViewById(R.id.video);

        image=(ImageView)findViewById(R.id.image);
        Transfer t=new Transfer();
        t.transferDB(this,task_id,user_id);
        beginStep();
    }

    public void nextStep(){
        if(step<steps.length) {
            step++;
            getSupportActionBar().setTitle("Title > Step "+step);
        }
        else{
            endStep();
        }
    }

    public void prevStep(){
        if(step>0) {
            step--;
            getSupportActionBar().setTitle("Title > Step "+step);
        }
        else{
            beginStep();
        }

    }

    public void beginStep(){

    }

    public void endStep(){

    }

    public void loadStep(Step step){
        if(step.type=="Photo"){
            image.setVisibility(View.VISIBLE);
            video.setVisibility(View.GONE);
            loadImage(step.link);

        }
        else if(step.type=="Video"){
            video.setVisibility(View.VISIBLE);
            image.setVisibility(View.GONE);
            loadVideo(step.link);

        }
        else {
            video.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
        }
        text.setText(step.text);
    }

    public void loadImage(String url){
            image.setImageURI(Uri.fromFile(new File(url)));
    }

    public void loadVideo(String url){
        video.setVideoPath(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
