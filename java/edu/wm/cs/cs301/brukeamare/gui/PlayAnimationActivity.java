package edu.wm.cs.cs301.brukeamare.gui;

import static edu.wm.cs.cs301.brukeamare.generation.MazeHolder.getMaze;
import static edu.wm.cs.cs301.brukeamare.generation.MazeHolder.setDriver;
import static edu.wm.cs.cs301.brukeamare.gui.Apptesting.tag;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;

import edu.wm.cs.cs301.brukeamare.R;
import edu.wm.cs.cs301.brukeamare.extra.Constants;
import edu.wm.cs.cs301.brukeamare.extra.Robot;
import edu.wm.cs.cs301.brukeamare.extra.RobotDriver;
import edu.wm.cs.cs301.brukeamare.extra.State;
import edu.wm.cs.cs301.brukeamare.extra.StatePlaying;
import edu.wm.cs.cs301.brukeamare.generation.ReliableRobot;
import edu.wm.cs.cs301.brukeamare.generation.UnreliableRobot;

/**
 * Class Name: PlayingAnimationActivity.java
 * OnCreate
 * zoomin
 * zoomout
 * showwall
 * showmaze
 * showsolution
 * Go2Winning
 * Go2Losing
 * onBackPressed
 * pauseanimation
 * playanimation
 * Losing
 * crashed
 * Winning
 * reachedexit
 *
 * Collaborators:
 * AMazeActivity.java
 * WinningActivity.java
 * LosingActivity.java
 * GeneratingActivity.java
 * PlayingManuallyActivity.java
 *
 * @author BRUKE AMARE
 *
 */
public class PlayAnimationActivity extends AppCompatActivity {
    public boolean roboton=false;

    public ImageView lefty;
    public ImageView righty;
    public ImageView fronty;
    public ImageView backy;

    public MediaPlayer backgroundMusic;

    private Handler Handy= new Handler();
    public int batterylevel=3500;
    public boolean showwallbool=false;
    public boolean showsolutionbool=false;
    public boolean showmazebool=false;

    public Snackbar snackbar;
    public View viewing;
    public StatePlaying currentState;
    public PlayAnimationActivity these= this;

    public boolean f=false;
    public boolean paused=false;
    public int speed=2;
    public int shortest=0;
    public RelativeLayout relativeLayout;


    public boolean firstunreliablestarted=false;
   // public SeekBar seekbar;

    /*
        creates animation state on layout
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stateplayinganimationactivity);
        viewing= findViewById(R.id.stateplayinganimationactivity);
        backgroundMusic = MediaPlayer.create(this, R.raw.dopamine);
        backgroundMusic.start();

        roboton=true;

        lefty=findViewById(R.id.leftsensor);
        righty=findViewById(R.id.rightsensor);
        fronty=findViewById(R.id.frontsensor);
        backy=findViewById(R.id.backsensor);

        SeekBar seekbar = (SeekBar)findViewById(R.id.seekBar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentState.speed= progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        currentState= new StatePlaying();
        currentState.playAnimationActivity=this;
        currentState.setMaze(getMaze());
        MazePanel1 panel= findViewById(R.id.damazepanelanimation);
        int[] post=getMaze().getStartingPosition();
        int x=post[0];
        int y=post[1];
        shortest=getMaze().getDistanceToExit(x,y);
        currentState.speed=0;



        String arg[]= new String[1];
        String selecteddriver=getIntent().getExtras().getString("driver");
        String selectedsensor=getIntent().getExtras().getString("sensor");


        if(selectedsensor.equals("Mediocre")) {
            currentState.rightReliable=false;
            currentState.leftReliable=false;
        }
        if(selectedsensor.equals("Soso")) {
            currentState.frontReliable=false;
            currentState.backReliable=false;
        }
        if(selectedsensor.equals("Shaky")) {
            currentState.leftReliable=false;
            currentState.rightReliable=false;
            currentState.frontReliable=false;
            currentState.backReliable=false;
        }


        if(selecteddriver.equals("WallFollower")) {

            setDriver("WallFollower");
            currentState.WallFollowermode = true;
        }
        if(selecteddriver.equals("Wizard")) {

            setDriver("Wizard");
            currentState.Wizardmode = true;
        }
        if(selecteddriver.equals("SmartWallFollower")) {
            currentState.SmartWallFollowermode = true;
            setDriver("SmartWallFollower");
        }
        if(selecteddriver.equals("SmartWizard")) {
            currentState.SmartWizardmode = true;
            setDriver("SmartWizard");
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                currentState.start(panel);
            }
        }).start();



    }
    /*
    makes the back button go to state title
     */
    @Override
    public void onBackPressed(){
        f=false;
        currentState.stopped=true;
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
        Thread.interrupted();
        this.finish();


    }
    public void updatebatt( Robot robocop) {
        f = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (f) {
                    int batt = (int) (robocop.getBatteryLevel());
                    ProgressBar progressBar = findViewById(R.id.progressbarenergyanimation);
                    progressBar.setProgress(batt);
                    Handy.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView also = findViewById(R.id.remainingenergytext);
                            also.setText("Remaining Energy: " + (batt));

                        }
                    });
                }
            }}).start();

    }

    /*
 function zooms into maze
*/
    public void startsensor() {
        f =true;
        new Thread(new Runnable() {
            @Override
            public void run() {
        if(!currentState.leftReliable) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (f) {
                        try {
                            Thread.sleep(4000);

                            Handy.post(new Runnable() {
                                @Override
                                public void run() {
                                    ImageView also = findViewById(R.id.leftsensor);
                                    also.setBackgroundColor(Color.GREEN);
                                }

                            });
                            Thread.sleep(2000);
                            Handy.post(new Runnable() {
                                @Override
                                public void run() {
                                    ImageView also = findViewById(R.id.leftsensor);
                                    also.setBackgroundColor(Color.RED);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }).start();
            try {
                Thread.sleep(1300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        firstunreliablestarted=true;
        }

        if(!currentState.rightReliable) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (f) {
                        try {
                            Thread.sleep(4000);

                            Handy.post(new Runnable() {
                                @Override
                                public void run() {
                                    ImageView also = findViewById(R.id.rightsensor);
                                    also.setBackgroundColor(Color.GREEN);
                                }

                            });
                            Thread.sleep(2000);
                            Handy.post(new Runnable() {
                                @Override
                                public void run() {
                                    ImageView also = findViewById(R.id.rightsensor);
                                    also.setBackgroundColor(Color.RED);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }).start();
            if (firstunreliablestarted) {
                try {
                    Thread.sleep(1300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            firstunreliablestarted=true;
        }


        if(!currentState.frontReliable) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (f) {
                        try {
                            Thread.sleep(4000);

                            Handy.post(new Runnable() {
                                @Override
                                public void run() {
                                    ImageView also = findViewById(R.id.frontsensor);
                                    also.setBackgroundColor(Color.GREEN);
                                }

                            });
                            Thread.sleep(2000);
                            Handy.post(new Runnable() {
                                @Override
                                public void run() {
                                    ImageView also = findViewById(R.id.frontsensor);
                                    also.setBackgroundColor(Color.RED);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }).start();
            if (firstunreliablestarted) {
                try {
                    Thread.sleep(1300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            firstunreliablestarted=true;
        }
        if(!currentState.backReliable) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (f) {
                        try {
                            Thread.sleep(4000);

                            Handy.post(new Runnable() {
                                @Override
                                public void run() {
                                    ImageView also = findViewById(R.id.backsensor);
                                    also.setBackgroundColor(Color.GREEN);
                                }

                            });
                            Thread.sleep(2000);
                            Handy.post(new Runnable() {
                                @Override
                                public void run() {
                                    ImageView also = findViewById(R.id.backsensor);
                                    also.setBackgroundColor(Color.RED);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }).start();
        }
            }
        }).start();


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

    public void pauseanimation(View view){

        Log.v(tag, "pauseing animation");
        snackbar= Snackbar.make(viewing,"pauseing animation",Snackbar.LENGTH_SHORT);
        snackbar.show();
        ImageButton Pausebutton = findViewById(R.id.Pausebutton);
        Pausebutton.setVisibility(View.INVISIBLE);
        currentState.paused=true;
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

    }
    public void playanimation(View view){
        Log.v(tag, "playing animaiton");
        snackbar= Snackbar.make(viewing,"playing animaiton",Snackbar.LENGTH_SHORT);
        snackbar.show();
        ImageButton Pausebutton = findViewById(R.id.Pausebutton);
        Pausebutton.setVisibility(View.VISIBLE);
        currentState.paused=false;
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
    }

    /*
    this function calls winning function, used by UI buttons
    */
    public void Go2Winning(View view){

        Winning(12, 0);
        Thread.interrupted();
        this.finish();
    }
    /*
    if robot got out the maze
    */
    public void reachedexit(int g, float f){
        Log.v(tag,"activity "+f);
        Log.v(tag,"activity "+(int)f);
        Winning(g, f);
        Thread.interrupted();

        this.finish();
    }
    /*
    this function jumps straight to winning page
    */
    public void Winning(int g, float fd){
        f=true;
        currentState.stopped=true;
        Log.v(tag,"going to winning" );
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(these,"going to winning", Toast.LENGTH_LONG ).show();
            }
        });

        Log.v(tag,"activity "+fd);
        Log.v(tag,"activity "+(int)fd);
        Intent intent= new Intent("android.intent.action.WINNING");
        intent.putExtra("builder", getIntent().getExtras().getString("builder"));
        intent.putExtra("room", getIntent().getExtras().getBoolean("room"));
        intent.putExtra("skill", getIntent().getExtras().getInt("skill"));
        intent.putExtra("seed", getIntent().getExtras().getInt("seed"));
        boolean bean=true;
        vibrator();
        intent.putExtra("anime",bean);
        intent.putExtra("pathlength", g);
        intent.putExtra("battery", (int)fd);
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
        backgroundMusic.stop();
        startActivity(intent);
    }
    /*
    this function calls losing function, used by UI buttons
    */
    public void Go2Losing(View view){
        Losing(12,0);
        Thread.interrupted();
        this.finish();
    }
    /*
    if robot crashed or lost power
    */
    public void crashed(int g, float f){
        Losing(g, f);
        Thread.interrupted();

        this.finish();
    }
    /*
    this function jumps straight to losing page, if the battery is depleted then
    it informs losing page that robot didn't crash but ran out of battery
    */
    public void Losing(int g, float fd){
        currentState.stopped=true;
        f=true;
        Log.v(tag,"going to losing" );
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(these,"going to losing", Toast.LENGTH_LONG ).show();
            }
        });


        Intent intent= new Intent("android.intent.action.LOSING");
        intent.putExtra("builder", getIntent().getExtras().getString("builder"));
        intent.putExtra("room", getIntent().getExtras().getBoolean("room"));
        intent.putExtra("skill", getIntent().getExtras().getInt("skill"));
        intent.putExtra("seed", getIntent().getExtras().getInt("seed"));
        Boolean bean= false;
        if(batterylevel<=0){
            bean=true;
        }
        vibrator();
        intent.putExtra("isrobottired", bean);
        intent.putExtra("pathlength", g);
        intent.putExtra("battery", (int)fd);
        intent.putExtra("shortest", shortest);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaPlayer backgroundMusic = MediaPlayer.create(these, R.raw.bruh);
                backgroundMusic.start();
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                backgroundMusic.stop();
            }
        }).start();
        backgroundMusic.stop();
        startActivity(intent);

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
