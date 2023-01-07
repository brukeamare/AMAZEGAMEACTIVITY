
package edu.wm.cs.cs301.brukeamare.gui;

import static android.app.PendingIntent.getActivity;
import static android.content.Context.VIBRATOR_SERVICE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.util.Log;
import android.view.View;
import android.os.Vibrator;

import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

import edu.wm.cs.cs301.brukeamare.R;
/**
 * Class Name: AMazeActivity.java
 * OnCreate
 * loadoldmaze
 * gennewmaze
 * genewmaze
 * rooms
 *
 * Collaborators:
 * WinningActivity.java
 * LosingActivity.java
 * GeneratingActivity.java
 * PlayingAnimationActivity.java
 * PlayingManuallyActivity.java
 *
 * @author BRUKE AMARE
 *
 */

public class AMazeActivity extends AppCompatActivity {

    public boolean firsttime=false;
    public static boolean Daroom=true;
    public static String prevbuilder;
    public static boolean prevroom;
    public static int prevskill;
    public static int prevseed=0;
    public MediaPlayer backgroundMusic;

    public Snackbar snackbar;
    public View viewing;
    public AMazeActivity these=this;

     private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    /*
    Creates state for state title layout
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewing=findViewById(R.id.activity_main);

        backgroundMusic = MediaPlayer.create(this, R.raw.chrome);
        backgroundMusic.start();

        pref= getApplicationContext().getSharedPreferences("edu.wm.cs.cs301.brukeamare.preferences",0); // 0 - for private mode;
        editor = pref.edit();

        SeekBar seekbar = (SeekBar)findViewById(R.id.skillbar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MediaPlayer backgroundMusic = MediaPlayer.create(these, R.raw.chrome);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        backgroundMusic.start();
                    }
                }).start();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    /*
    loads old maze configurations and starts generating state
     */
    public void loadoldmaze(View view){


        if (pref.getInt("skill", -1) == -1){
            Toast.makeText(this,"can not load old maze on new startup", Toast.LENGTH_LONG ).show();
            genewmaze();
            return;
        }
        vibrator();

        Intent intent= new Intent(AMazeActivity.this, GeneratingActivity.class);
        intent.putExtra("builder", pref.getString("builder",""));
        intent.putExtra("room", pref.getBoolean("room", prevroom));
        intent.putExtra("skill", pref.getInt("skill", -1));
        intent.putExtra("seed", pref.getInt("seed", prevseed ));
        Log.v(Apptesting.tag, "loading old maze for generation");
        Toast.makeText(this,"loading old maze for generation", Toast.LENGTH_LONG ).show();
        backgroundMusic.stop();
        startActivity(intent);

        this.finish();
    }
    /*
    starts generating state by calling other function
     */
    public void gennewmaze(View view){
        genewmaze();
    }
    /*
    loads new maze configurations and starts generating state
     */
    public void genewmaze(){
        Spinner builder= findViewById(R.id.generatoroptions);
        prevbuilder=builder.getSelectedItem().toString();
        prevroom=Daroom;
        SeekBar skillz= findViewById(R.id.skillbar);
        prevskill=skillz.getProgress();
        Random random = new Random();
        prevseed = random.nextInt();

        savepref();

        Intent intent= new Intent(AMazeActivity.this, GeneratingActivity.class);
        intent.putExtra("builder", prevbuilder);
        intent.putExtra("room", prevroom);
        intent.putExtra("skill", prevskill);
        intent.putExtra("seed", prevseed);

        Log.v(Apptesting.tag, ""+prevseed);

        vibrator();

        Log.v(Apptesting.tag, "loading new maze for generation");
        Toast.makeText(this, "loading new maze for generation", Toast.LENGTH_LONG ).show();
        backgroundMusic.stop();
        startActivity(intent);
        this.finish();
    }



    /*
    function to save boolean for rooms
     */
    public void rooms(View view){

        if (Daroom){
            Daroom=false;
            Log.v(Apptesting.tag, "rooms turned off");
            snackbar= Snackbar.make(viewing,"rooms turned off",Snackbar.LENGTH_SHORT);
            snackbar.show();


        }
        else{
            Daroom=true;
            Log.v(Apptesting.tag, "rooms turned on");
            snackbar= Snackbar.make(viewing,"rooms turned on",Snackbar.LENGTH_SHORT);
            snackbar.show();
        }


    }
    /*
    function to save boolean for rooms
     */
    public void savepref(){


        editor.clear();
        editor.commit();
        editor.putBoolean("room", prevroom);
        editor.putString("builder", prevbuilder);
        editor.putInt("skill", prevskill);
        editor.putInt("seed", prevseed);
        editor.commit();
    }
    /*
makes the back button go to state title
 */
    @Override
    public void onBackPressed(){
        backgroundMusic.stop();
        vibrator();
        super.onBackPressed();

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
