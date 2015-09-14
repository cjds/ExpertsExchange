package com.carlsaldanha.expertsexchange;

import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import android.net.Uri;

public class MainActivity extends AppCompatActivity {

    int step=-1;
    int procedure_id=0;
    int expert_id;
    int task_id;
    int user_id;

    Step[] steps;

    VideoView video;
    ImageView image;
    TextView text;

    MediaController mc;
    String PATH;

    Transfer t=new Transfer();

    ArrayList<StepRecorder> stepRecorders=new ArrayList<StepRecorder>();
    int index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Welcome");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2299aa")));

        Intent i=getIntent();

        ProcedureDB pd=new ProcedureDB(this);
        String user_id = i.getStringExtra("user_id");
        String task_id = i.getStringExtra("task_id");

        this.expert_id=Integer.parseInt(user_id);
        this.task_id=Integer.parseInt(task_id);
        this.user_id=t.getLastUserID(this);


        text=(TextView)findViewById(R.id.text);
        video=(VideoView)findViewById(R.id.video);

        setUpVideo();
        //mc.setAnchorView(video);
        image=(ImageView)findViewById(R.id.image);

        findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextStep();
            }
        });

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevStep();
            }
        });


        steps=t.transferDB(this, task_id, user_id);

        PATH=Environment.getExternalStorageDirectory().toString();

        beginStep();
    }


    private void setUpVideo(){
        mc=new MediaController(this);
        video.setMediaController(mc);
        mc.setAnchorView(video);
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        mc = new MediaController(MainActivity.this);
                        mc.setAnchorView(video);
                    }
                });
            }
        });
    }

    public void nextStep(){


        if(step<steps.length-1) {
            step++;
            loadStep(steps[step]);
             getSupportActionBar().setTitle("Step "+(step+1)+" / "+steps.length);

        }
        else{
            step++;
            endStep();
        }
    }

    public void prevStep(){
        if(step>0) {
            step--;
            getSupportActionBar().setTitle("Step "+(step+1)+" / "+steps.length);
            loadStep(steps[step]);
        }
        else{
            beginStep();
        }

    }

    public void beginStep(){
        getSupportActionBar().setTitle("Welcome");
        text.setText("Welcome. Press next or swipe right to begin");
        video.setVisibility(View.GONE);
        image.setVisibility(View.GONE);
    }

    public void endStep(){
        getSupportActionBar().setTitle("Thank You");
        text.setText("Congrats. Thank You");
        video.setVisibility(View.GONE);
        image.setVisibility(View.GONE);
    }

    public void loadStep(Step step){
        if(index>0) {
            StepRecorder st = stepRecorders.get(index - 1);
            st.record_end_time();
            stepRecorders.set(index - 1, st);
        }
        stepRecorders.add(new StepRecorder(user_id,task_id,expert_id,step.id));
        index++;

        if(step.type.equals("Photo")){
            image.setVisibility(View.VISIBLE);
            video.setVisibility(View.GONE);
            loadImage(step.link);

        }
        else if(step.type.equals("Video")){
            video.setVisibility(View.VISIBLE);
            image.setVisibility(View.GONE);
            loadVideo(step.link);
            Log.d("uuhiu","gyuyu");
        }
        else {
            video.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
        }
        text.setText(step.text);
    }

    public void loadImage(String url){
        Bitmap b=BitmapFactory.decodeFile(PATH+"/media/DCIM/Camera/" + url);
        image.setImageBitmap(b);
    }

    public void loadVideo(String url){
        video.setVideoPath(PATH+"/media/DCIM/Camera/" +url);

        video.requestFocus();
        video.start();
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
