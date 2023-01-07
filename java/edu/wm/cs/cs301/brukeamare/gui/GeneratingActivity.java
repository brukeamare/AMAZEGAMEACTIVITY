package edu.wm.cs.cs301.brukeamare.gui;



import static edu.wm.cs.cs301.brukeamare.generation.MazeHolder.setDriver;
import static edu.wm.cs.cs301.brukeamare.generation.MazeHolder.setMaze;
import static edu.wm.cs.cs301.brukeamare.generation.MazeHolder.setSkill;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import edu.wm.cs.cs301.brukeamare.R;
import edu.wm.cs.cs301.brukeamare.extra.StatePlaying;
import edu.wm.cs.cs301.brukeamare.generation.DefaultOrder;
import edu.wm.cs.cs301.brukeamare.generation.Maze;
import edu.wm.cs.cs301.brukeamare.generation.MazeFactory;
import edu.wm.cs.cs301.brukeamare.generation.Order;


/**
 * Class Name: GeneratingActivity.java
 * OnCreate
 * openplaying
 * openanimation
 * onBackPressed
 *
 * Collaborators:
 * AMazeActivity.java
 * PlayingAnimationActivity.java
 * PlayingManuallyActivity.java
 *
 *
 * @author BRUKE AMARE
 *
 */
public class GeneratingActivity extends AppCompatActivity {

    public Handler Handy= new Handler();
    public Snackbar snackbar;
    public View viewing;

    public int currentprog=0;
    public boolean running=true;
    public TextView also;
    public MediaPlayer backgroundMusic;



    /*
    Creates state for genrating layout and runs thread to load progressbar
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generatingactivity);
        viewing= findViewById(R.id.generatingactivity);

        backgroundMusic = MediaPlayer.create(this, R.raw.elevator);
        backgroundMusic.start();







        // need to instantiate and configure the generating state

        // The generating state needs
        // 1) the builder algorithm to use
        // 2) whether the maze needs to be perfect (no rooms) or can have rooms
        // 3) the size of the maze or skill level
        // 4) which seed to use for the random number generation

        String b =getIntent().getExtras().getString("builder");
        Order.Builder f=Order.Builder.DFS;;

        if (b.equals("Boruvka")){
            f= Order.Builder.Boruvka;
        }
        if (b.equals("Prim")){
            f= Order.Builder.Prim;
        }

        MazeFactory damaze= new MazeFactory();
        DefaultOrder daorder= new DefaultOrder(getIntent().getExtras().getInt("skill"), f, getIntent().getExtras().getBoolean("room"), getIntent().getExtras().getInt("seed"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                damaze.order(daorder);
                damaze.waitTillDelivered();
                Maze yeahmaze= daorder.getMaze();
                setMaze(yeahmaze);
                setSkill(getIntent().getExtras().getInt("skill"));
            }}).start();

        new Thread(new Runnable() {
            @Override
            public void run(){

                while(currentprog<100 &&running){
                    currentprog=daorder.getProgress();
                    Handy.post(new Runnable() {
                            @Override
                            public void run() {
                                Spinner driver= findViewById(R.id.driveroptions);
                                String selecteddriver=driver.getSelectedItem().toString();
                                if(!selecteddriver.equals("Select")&& running) {
                                    TextView also = findViewById(R.id.textView4);
                                    also.setVisibility(View.VISIBLE);
                                }
                            }
                    });

                                ProgressBar progressBar = findViewById(R.id.progressbargeneration);
                                progressBar.setProgress(currentprog);
                                Handy.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        TextView also= findViewById(R.id.Mazetextrendring);
                                        also.setText("Rendering Maze: "+(currentprog)+"%");

                                    }
                                });


                }
                Spinner driver= findViewById(R.id.driveroptions);
                String selecteddriver=driver.getSelectedItem().toString();
                if(selecteddriver.equals("Select")&& running){
                    Handy.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView also= findViewById(R.id.textView2);
                            also.setVisibility(View.VISIBLE);
                        }
                    });
                }
                while(driver.getSelectedItem().toString().equals("Select") && running){

                }
                if(!driver.getSelectedItem().toString().equals("Manual")&& running){
                    Spinner sensor= findViewById(R.id.sensoroptions);

                    Handy.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView also= findViewById(R.id.alsosensortext);
                            also.setVisibility(View.VISIBLE);
                        }
                    });


                    while(sensor.getSelectedItem().toString().equals("Select")&& running){
                        if(driver.getSelectedItem().toString().equals("Manual")&& running){
                                openplaying();
                                return;
                        }
                    }
                    if(running){
                    openanimation();
                    return;
                    }

                }
                if(running){
                openplaying();
                }
            }
        }).start();




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
        running=false;
        backgroundMusic.stop();
        vibrator();
        startActivity(intent);

        this.finish();


    }



    /*
    Creates state for playing manually layout
     */
    public void openplaying(){
        running=false;
        Log.v(Apptesting.tag, "started manually driver without sensors");
        Handy.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GeneratingActivity.this,"started manually driver without sensors", Toast.LENGTH_LONG ).show();
            }
        });
        vibrator();

        Intent intent= new Intent("android.intent.action.MANUAL");
        intent.putExtra("builder", getIntent().getExtras().getString("builder"));
        intent.putExtra("room", getIntent().getExtras().getBoolean("room"));
        intent.putExtra("skill", getIntent().getExtras().getInt("skill"));
        intent.putExtra("seed", getIntent().getExtras().getInt("seed"));
        backgroundMusic.stop();
        startActivity(intent);
        this.finish();

    }
    /*
    Creates state for playing manually layout
     */
    public void openanimation(){
        running=false;
        Log.v(Apptesting.tag, "started animation driver with sensors");
        Handy.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GeneratingActivity.this,"started animation driver with sensors", Toast.LENGTH_LONG ).show();

            }
        });
        vibrator();



        Intent intent= new Intent("android.intent.action.ANIMATION");
        Spinner driver= findViewById(R.id.driveroptions);
        String selecteddriver=driver.getSelectedItem().toString();
        Spinner sensor= findViewById(R.id.sensoroptions);
        String sensortype=sensor.getSelectedItem().toString();
        intent.putExtra("builder", getIntent().getExtras().getString("builder"));
        intent.putExtra("room", getIntent().getExtras().getBoolean("room"));
        intent.putExtra("skill", getIntent().getExtras().getInt("skill"));
        intent.putExtra("seed", getIntent().getExtras().getInt("seed"));
        intent.putExtra("sensor", sensortype);
        intent.putExtra("driver", selecteddriver);
        startActivity(intent);
        backgroundMusic.stop();
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
