package edu.wm.cs.cs301.brukeamare.gui;

import static edu.wm.cs.cs301.brukeamare.gui.Apptesting.tag;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import edu.wm.cs.cs301.brukeamare.R;
/**
 * Class Name: WinningActivity.java
 * OnCreate
 * onBackPressed
 * playagainwinning
 *
 * Collaborators:
 * AMazeActivity.java
 * LosingActivity.java
 * GeneratingActivity.java
 * PlayingAnimationActivity.java
 * PlayingManuallyActivity.java
 *
 * @author BRUKE AMARE
 *
 */
public class WinningActivity extends AppCompatActivity {
public static boolean animationbat=false;
public Handler Handy=new Handler();
    public Snackbar snackbar;
    public View viewing;
    public MediaPlayer backgroundMusic;


    /*
    creates state winning as layout and if the previous screen was
    playing animation activity it shows the total energy consumed
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewing= findViewById(R.id.winningactivity);
        backgroundMusic = MediaPlayer.create(this, R.raw.meta);
        backgroundMusic.start();

        animationbat=getIntent().getExtras().getBoolean("anime");
        int batt= getIntent().getExtras().getInt("battery");
        Log.v(tag,"winning"+batt);

        if(animationbat){
            Handy.post(new Runnable() {
                @Override
                public void run() {
                    TextView also= findViewById(R.id.totalenergywinning);
                    also.setText(" Total Energy left: "+batt+"/3500");
                    also.setVisibility(View.VISIBLE);
                }
            });
        }
        int steps=getIntent().getExtras().getInt("pathlength");
        int shortest=getIntent().getExtras().getInt("shortest");
        Handy.post(new Runnable() {
            @Override
            public void run() {
                TextView also= findViewById(R.id.totalpathtextwining);
                also.setText(" Total Path Length taken: "+steps);
                also= findViewById(R.id.totalpathtext3);
                also.setText("length of the shortest possible path: "+shortest);
            }
        });
        setContentView(R.layout.winningactivity);


    }
    /*
    makes the back button go to state title
     */
    @Override
    public void onBackPressed(){
        Log.v(Apptesting.tag, "going back to state title");
        Toast.makeText(this,"going back to state title", Toast.LENGTH_LONG ).show();
        Intent intent= new Intent(this, AMazeActivity.class);
        intent.putExtra("builder", getIntent().getExtras().getString("builder"));
        intent.putExtra("room", getIntent().getExtras().getBoolean("room"));
        intent.putExtra("skill", getIntent().getExtras().getInt("skill"));
        intent.putExtra("seed", getIntent().getExtras().getInt("seed"));
        backgroundMusic.stop();
        vibrator();
        startActivity(intent);
        this.finish();

    }

    public void playagainwinning(View view){
        Intent intent= new Intent(this, AMazeActivity.class);
        intent.putExtra("builder", getIntent().getExtras().getString("builder"));
        intent.putExtra("room", getIntent().getExtras().getBoolean("room"));
        intent.putExtra("skill", getIntent().getExtras().getInt("skill"));
        intent.putExtra("seed", getIntent().getExtras().getInt("seed"));
        backgroundMusic.stop();
        Toast.makeText(this,"restarting game going to main title", Toast.LENGTH_LONG ).show();
        Log.v(Apptesting.tag, "restarting game going to main title");
        vibrator();
        startActivity(intent);

        this.finish();

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
