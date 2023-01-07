package edu.wm.cs.cs301.brukeamare.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Class Name: MazePanel2.java
 * MazePanel2
 * OnDraw
 *
 * Collaborators:
 * PlayingManuallyActivity.java
 * PlayAnimationActivity.java
 * View.java
 *
 * @author BRUKE AMARE
 *
 */
public class MazePanel2 extends View {
        Paint paint= new Paint();

        Path dapath= new Path();
        /*
        constructor with context
         */
        public MazePanel2(Context context) {
            super(context);
        }
        /*
        constructor with context and attributes
        */
        public MazePanel2(Context context, AttributeSet attributes) {
            super(context, attributes);
        }
        /*
        constructor with context, attributes and style
        */
        public MazePanel2(Context context,AttributeSet attributes ,int style) {
            super(context,attributes,style);
        }
        /*
        constructor visual custom view of maze with walls
        */
        @Override
        public void onDraw(Canvas canvas){
            paint.setColor(Color.GRAY);
            canvas.drawRect(0,0,getWidth(), getHeight(),paint);

            paint.setColor(Color.BLACK);
            canvas.drawRect(0,350,getWidth(), getHeight(),paint);

            paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.FILL);

            dapath.reset();
            dapath.moveTo(0,0);
            dapath.lineTo(getWidth()/4, getHeight()/4);
            dapath.lineTo(getWidth()/4, (getHeight()/4)*3);
            dapath.lineTo(0, getHeight());
            dapath.lineTo(0, 0);
            canvas.drawPath(dapath, paint);

            paint.setColor(Color.YELLOW);
            dapath.reset();
            dapath.moveTo(getWidth(),0);
            dapath.lineTo((getWidth()/4)*3, getHeight()/4);
            dapath.lineTo((getWidth()/4)*3, (getHeight()/4)*3);
            dapath.lineTo(getWidth(), getHeight());
            dapath.lineTo(getWidth(), 0);
            canvas.drawPath(dapath, paint);

//            paint.setColor(Color.RED);
//            canvas.drawCircle(getWidth()/2, (getHeight()/6)*4,getWidth()/8, paint);

        }
}

