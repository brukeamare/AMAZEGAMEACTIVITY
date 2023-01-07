package edu.wm.cs.cs301.brukeamare.gui;

import static edu.wm.cs.cs301.brukeamare.generation.MazeHolder.getMaze;
import static edu.wm.cs.cs301.brukeamare.gui.Apptesting.tag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import edu.wm.cs.cs301.brukeamare.R;
import edu.wm.cs.cs301.brukeamare.extra.Constants;
import edu.wm.cs.cs301.brukeamare.extra.State;
import edu.wm.cs.cs301.brukeamare.extra.StatePlaying;

/**
 * Class Name: PlayManuallyActivity.java
 * OnCreate
 * zoomin
 * zoomout
 * showwall
 * showmaze
 * showsolution
 * Shortcut
 * jumpbutton
 * forwardbutton
 * leftbutton
 * rightbutton
 * evenshorter
 * onBackPressed
 *
 *
 * Collaborators:
 *  AMazeActivity.java
 *  LosingActivity.java
 *  WinningActivity.java
 *  GeneratingActivity.java
 *  PlayManuallyActivity.java
 *
 * @author BRUKE AMARE
 *
 */
public class PlayManuallyActivity extends AppCompatActivity {
    public Snackbar snackbar;
    public View viewing;
    int count=0;

    public int steps=0;
    public boolean showwallbool=false;
    public boolean showsolutionbool=false;
    public boolean showmazebool=false;
    public StatePlaying currentState;
    public int shortest=0;
    public PlayManuallyActivity these=this;

    public MediaPlayer backgroundMusic;
    public ConstraintLayout relativeLayout;
    public OnSwipeTouchListener swipeListener;

    public ImageButton imagebutton;
    public SpeechRecognizer speechRecognizer;
    public Intent intentrecognizer;

    /*
        creates playing state on layout
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stateplayingactivity);
        viewing= findViewById(R.id.stateplayingactivity);

        relativeLayout= findViewById(R.id.stateplayingactivity);
        swipeListener= new OnSwipeTouchListener(relativeLayout);

        backgroundMusic = MediaPlayer.create(this, R.raw.meh);
        backgroundMusic.start();

        currentState= new StatePlaying();
        currentState.playManuallyActivity=this;
        currentState.setMaze(getMaze());
        MazePanel1 panel= findViewById(R.id.damazepanelmanual);
        currentState.speed=4;

        int[] post=getMaze().getStartingPosition();
        int x=post[0];
        int y=post[1];
        shortest=getMaze().getDistanceToExit(x,y);
        currentState.start(panel);


       imagebutton= findViewById(R.id.microphone);


       ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);


       intentrecognizer= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
       intentrecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches= results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String string= "";
                if (matches!=null){
                    string=matches.get(0);
                    detectspeech(string);
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

    }
    public void detectspeech(String string){
        String [] bruh= string.split("\\s+");
        for(int i=(bruh.length-1); i>=0; i--){
            switch(bruh[i]){
                case "forward":
                    Log.v(tag,"voice command move forward");
                    forwardbuttoned();
                    return;
                case "right":
                    Log.v(tag,"voice command turn right");
                    rightbuttoned();
                    return;
                case "left":
                    Log.v(tag,"voice command turn left");
                    leftbuttoned();
                    return;
                case "jump":
                    Log.v(tag,"voice command jump");
                    jumpbuttoned();
                    return;
                default:
                    Log.v(tag,"invalid voice command");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MediaPlayer backgroundMusic = MediaPlayer.create(these, R.raw.what);
                            backgroundMusic.start();
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            backgroundMusic.stop();
                        }
                    }).start();
                    return;

            }

        }

    }
    public void micon(View view){
        speechRecognizer.startListening(intentrecognizer);
        Toast.makeText(this,"Voice control turned on", Toast.LENGTH_LONG ).show();
    }





    public class OnSwipeTouchListener implements View.OnTouchListener {

        GestureDetector gestureDetector;

        OnSwipeTouchListener(View view) {
            int threshold = 100;
            int velocity_threshold = 100;
            GestureDetector.SimpleOnGestureListener listener =
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onDown(MotionEvent e) {
                            return true;
                        }

                        @Override
                        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                            boolean result = false;
                            float diffY = e2.getY() - e1.getY();
                            float diffX = e2.getX() - e1.getX();
                            try {

                                if (Math.abs(diffX) > Math.abs(diffY)) {
                                    if (Math.abs(diffX) > threshold && Math.abs(velocityX) > velocity_threshold) {
                                        if (diffX > 0) {
                                            rightbuttoned();
                                        } else {
                                            leftbuttoned();
                                        }
                                        return true;
                                    }
                                } else {
                                    if (Math.abs(diffY) > threshold && Math.abs(velocityY) > velocity_threshold) {
                                        if (diffY > 0) {
                                            jumpbuttoned();
                                        } else {
                                            forwardbuttoned();
                                        }
                                        return true;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return false;
                        }
                    };
            gestureDetector = new GestureDetector(listener);
            view.setOnTouchListener(this);
        }

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return gestureDetector.onTouchEvent(event);
                        }
    }




    /*
    funsction for when the exit is reached in maze, leads to winning
     */
    public void reachedexit(int g){
        evenshorter(g);
    }


    /*
    makes the back button go to state title
     */
    @Override
    public void onBackPressed(){
        Log.v(tag, "going back to state title");
        Toast.makeText(this,"going back to state title", Toast.LENGTH_LONG ).show();

        currentState.handleUserInput(Constants.UserInput.RETURNTOTITLE, 0);
       // repaint();
        vibrator();

        Intent intent= new Intent(this, AMazeActivity.class);
        intent.putExtra("builder", getIntent().getExtras().getString("builder"));
        intent.putExtra("room", getIntent().getExtras().getBoolean("room"));
        intent.putExtra("skill", getIntent().getExtras().getInt("skill"));
        intent.putExtra("seed", getIntent().getExtras().getInt("seed"));
        backgroundMusic.stop();
        startActivity(intent);

        this.finish();


    }

    /*
     function zooms into maze
    */
    public void zoomin(View view){
        snackbar= Snackbar.make(viewing,"Zoomin",Snackbar.LENGTH_SHORT);
        snackbar.show();
        Log.v(tag,"Zoomin" );
        currentState.handleUserInput(Constants.UserInput.ZOOMIN, 0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaPlayer backgroundMusic = MediaPlayer.create(these, R.raw.mouse);
                backgroundMusic.start();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                backgroundMusic.stop();
            }
        }).start();
       // repaint();
    }
    /*
    function zooms out of maze
     */
    public void zoomout(View view){
        snackbar= Snackbar.make(viewing,"Zoomout",Snackbar.LENGTH_SHORT);
        snackbar.show();
        Log.v(tag,"Zoomout" );
        currentState.handleUserInput(Constants.UserInput.ZOOMOUT, 0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaPlayer backgroundMusic = MediaPlayer.create(these, R.raw.mouse);
                backgroundMusic.start();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                backgroundMusic.stop();
            }
        }).start();
        //repaint();
    }
    /*
    function shows walls of maze
     */
    public void showwall(View view){

        if(!showwallbool){
            showwallbool=true;
            snackbar= Snackbar.make(viewing,"Showing wall",Snackbar.LENGTH_SHORT);
            snackbar.show();
            Log.v(tag,"Showing wall" );
            currentState.handleUserInput(Constants.UserInput.TOGGLELOCALMAP, 0);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MediaPlayer backgroundMusic = MediaPlayer.create(these, R.raw.mouse);
                    backgroundMusic.start();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    backgroundMusic.stop();
                }
            }).start();
           // repaint();
        }
        else{
            showwallbool=false;
            snackbar= Snackbar.make(viewing,"stopped Showing wall",Snackbar.LENGTH_SHORT);
            snackbar.show();
            Log.v(tag,"stopped Showing wall" );
            currentState.handleUserInput(Constants.UserInput.TOGGLELOCALMAP, 0);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MediaPlayer backgroundMusic = MediaPlayer.create(these, R.raw.mouse);
                    backgroundMusic.start();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    backgroundMusic.stop();
                }
            }).start();
           // repaint();
        }
    }
    /*
    function shows all of maze
     */
    public void showmaze(View view){
        if(!showmazebool) {
            showmazebool=true;
            snackbar= Snackbar.make(viewing,"Showing maze",Snackbar.LENGTH_SHORT);
            snackbar.show();
            Log.v(tag, "Showing maze");
            currentState.handleUserInput(Constants.UserInput.TOGGLEFULLMAP, 0);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MediaPlayer backgroundMusic = MediaPlayer.create(these, R.raw.mouse);
                    backgroundMusic.start();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    backgroundMusic.stop();
                }
            }).start();
            //repaint();
        }
        else{
            showmazebool=false;
            snackbar= Snackbar.make(viewing,"stopped Showing maze",Snackbar.LENGTH_SHORT);
            snackbar.show();
            Log.v(tag, "stopped Showing maze");
            currentState.handleUserInput(Constants.UserInput.TOGGLEFULLMAP, 0);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MediaPlayer backgroundMusic = MediaPlayer.create(these, R.raw.mouse);
                    backgroundMusic.start();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    backgroundMusic.stop();
                }
            }).start();
          //  repaint();

        }
    }
    /*
    function shows solution for maze
     */
    public void showsolution(View view){
        if(!showsolutionbool) {
            showsolutionbool=true;
            snackbar= Snackbar.make(viewing,"Showing solution",Snackbar.LENGTH_SHORT);
            snackbar.show();
            Log.v(tag, "Showing solution");
            currentState.handleUserInput(Constants.UserInput.TOGGLESOLUTION, 0);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MediaPlayer backgroundMusic = MediaPlayer.create(these, R.raw.mouse);
                    backgroundMusic.start();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    backgroundMusic.stop();
                }
            }).start();
          //  repaint();
        }
        else{
            showsolutionbool=false;
            snackbar= Snackbar.make(viewing,"stopped Showing solution",Snackbar.LENGTH_SHORT);
            snackbar.show();
            Log.v(tag, "stopped Showing solution");
            currentState.handleUserInput(Constants.UserInput.TOGGLESOLUTION, 0);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MediaPlayer backgroundMusic = MediaPlayer.create(these, R.raw.mouse);
                    backgroundMusic.start();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    backgroundMusic.stop();
                }
            }).start();
           // repaint();
        }
    }


    /*
        this function calls the even shorter function, used by UI buttons
     */

    public void Shortcut(View view){
        evenshorter(12);
    }
    /*
        this function jumps straight to winning page
     */
    public void evenshorter(int g){
        Intent intent= new Intent("android.intent.action.WINNING");
        boolean bean=false;
        intent.putExtra("anime",bean);
        intent.putExtra("steps",steps);
        intent.putExtra("builder", getIntent().getExtras().getString("builder"));
        intent.putExtra("room", getIntent().getExtras().getBoolean("room"));
        intent.putExtra("skill", getIntent().getExtras().getInt("skill"));
        intent.putExtra("seed", getIntent().getExtras().getInt("seed"));
        backgroundMusic.stop();
        Toast.makeText(getApplicationContext(), "going to winning" , Toast.LENGTH_LONG).show();
        Log.v(tag,"going to winning" );
        intent.putExtra("pathlength", g);
        intent.putExtra("shortest", shortest);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaPlayer backgroundMusic = MediaPlayer.create(these, R.raw.kids);
                backgroundMusic.start();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                backgroundMusic.stop();
            }
        }).start();
        vibrator();
        startActivity(intent);
        this.finish();
    }

    /*
        jump button increments path taken by 1
     */
    public void jumpbutton(View view){
        jumpbuttoned();
    }
    public void jumpbuttoned(){
        steps=steps+1;
        Log.v(tag,"jumps forward" );
        snackbar= Snackbar.make(viewing,"jumps forward",Snackbar.LENGTH_SHORT);
        snackbar.show();
        currentState.handleUserInput(Constants.UserInput.JUMP, 0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaPlayer backgroundMusic = MediaPlayer.create(these, R.raw.mario);
                backgroundMusic.start();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                backgroundMusic.stop();
            }
        }).start();
       // repaint();
    }
    /*
        forward button increments path taken by 1
     */
    public void forwardbutton(View view){
        forwardbuttoned();
    }
    public void forwardbuttoned(){
        steps=steps+1;
        Log.v(tag,"moves forward" );
        snackbar= Snackbar.make(viewing,"moves forward",Snackbar.LENGTH_SHORT);
        snackbar.show();
        currentState.handleUserInput(Constants.UserInput.UP, 0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaPlayer backgroundMusic = MediaPlayer.create(these, R.raw.swipe);
                backgroundMusic.start();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                backgroundMusic.stop();
            }
        }).start();
       // repaint();


    }
    /*
    left button rotates screen and player -90 degrees
    */
    public void leftbutton(View view){
        leftbuttoned();
    }
    public void leftbuttoned(){
        Log.v(tag,"turns left" );
        snackbar= Snackbar.make(viewing,"turns left",Snackbar.LENGTH_SHORT);
        snackbar.show();
        currentState.handleUserInput(Constants.UserInput.LEFT, 0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaPlayer backgroundMusic = MediaPlayer.create(these, R.raw.swipe);
                backgroundMusic.start();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                backgroundMusic.stop();
            }
        }).start();
       // repaint();
    }
    /*
    right button rotates screen and player 90 degrees
    */
    public void rightbutton(View view){
        rightbuttoned();
    }
    public void rightbuttoned(){
        Log.v(tag,"turns right" );
        snackbar= Snackbar.make(viewing,"turns right",Snackbar.LENGTH_SHORT);
        snackbar.show();
        currentState.handleUserInput(Constants.UserInput.RIGHT, 0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaPlayer backgroundMusic = MediaPlayer.create(these, R.raw.swipe);
                backgroundMusic.start();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                backgroundMusic.stop();
            }
        }).start();
       // repaint();

    }
    public void vibrator(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.cancel();
        if(!v.hasVibrator()){
            return;
        }
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            v.vibrate(
                    VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE)
            );
        }
        else {
            long[] pattern = {0, 400};
            v.vibrate(pattern, -1);
        }
    }


}
