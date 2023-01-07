package edu.wm.cs.cs301.brukeamare.gui;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;

import edu.wm.cs.cs301.brukeamare.R;
/**
 * Class Name: LosingActivity.java
 * OnCreate
 * onBackPressed
 * playagainlosing
 *
 * Collaborators:
 * AMazeActivity.java
 * WinningActivity.java
 * GeneratingActivity.java
 * PlayingAnimationActivity.java
 * PlayingManuallyActivity.java
 *
 *
 * @author BRUKE AMARE
 *
 */

public class LosingActivity extends AppCompatActivity {
    public Snackbar snackbar;
    public View viewing;
    public Handler Handy=new Handler();


    /*
    creates state losing as layout, also shows a visual for either depleted battery or broken robot
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.losingactivity);
        viewing= findViewById(R.id.losingactivity);
        final MediaPlayer backgroundMusic = MediaPlayer.create(this, R.raw.rooftop);
        backgroundMusic.start();

        if(getIntent().getExtras().getBoolean("isrobottired")){
            ImageView g= findViewById(R.id.nobatterypic);
            g.setVisibility(View.VISIBLE);
        }
        else{
            ImageView g= findViewById(R.id.robotbrokenpic);
            g.setVisibility(View.VISIBLE);
        }


        int batt= getIntent().getExtras().getInt("battery");
        int steps=getIntent().getExtras().getInt("pathlength");
        int shortest=getIntent().getExtras().getInt("shortest");

        Handy.post(new Runnable() {
            @Override
            public void run() {
                TextView also= findViewById(R.id.totalpathtext);
                also.setText(" Total Path Length taken: "+steps);
                also= findViewById(R.id.totalenergytext);
                also.setText("  Total Energy left: "+batt+"/3500");
                also= findViewById(R.id.totalpathtext2);
                also.setText("length of the shortest possible path: "+shortest);
            }
        });

    }
    /*
    makes the back button go to state title
     */
    @Override
    public void onBackPressed(){
        Log.v(Apptesting.tag, "going back to state title");
        Toast.makeText(this,"going back to state title", Toast.LENGTH_LONG ).show();
        Intent intent= new Intent(this, AMazeActivity.class);
        vibrator();
        intent.putExtra("builder", getIntent().getExtras().getString("builder"));
        intent.putExtra("room", getIntent().getExtras().getBoolean("room"));
        intent.putExtra("skill", getIntent().getExtras().getInt("skill"));
        intent.putExtra("seed", getIntent().getExtras().getInt("seed"));
        startActivity(intent);
        this.finish();

    }
    /*
        takes you back to state title
      */
    public void playagainlosing(View view){
        Intent intent= new Intent(this, AMazeActivity.class);
        intent.putExtra("builder", getIntent().getExtras().getString("builder"));
        intent.putExtra("room", getIntent().getExtras().getBoolean("room"));
        intent.putExtra("skill", getIntent().getExtras().getInt("skill"));
        intent.putExtra("seed", getIntent().getExtras().getInt("seed"));
        vibrator();
        Log.v(Apptesting.tag, "restarting game going to main title");
        Toast.makeText(this,"restarting game going to main title", Toast.LENGTH_LONG ).show();
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
