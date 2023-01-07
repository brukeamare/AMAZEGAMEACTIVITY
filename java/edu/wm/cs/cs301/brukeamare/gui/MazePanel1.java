package edu.wm.cs.cs301.brukeamare.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import edu.wm.cs.cs301.brukeamare.R;
import edu.wm.cs.cs301.brukeamare.extra.ColorTheme;
import edu.wm.cs.cs301.brukeamare.extra.Constants;

/**
 * Class Name: MazePanel1.java
 * MazePanel1
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
public class MazePanel1 extends View implements P7PanelF22 {
    Paint paint= new Paint();
    Canvas canvas;
    Bitmap bitmap;
    BitmapShader skyshader;
    BitmapShader wallshader;
    BitmapShader floorshader;
    Bitmap wall;
    Bitmap factory;
    Bitmap floor;
    Drawable mazeBackground;


    /*
    constructor with context
    */
    public MazePanel1(Context context) {
        super(context);
        bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();

        wall = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_1);
        wall = Bitmap.createScaledBitmap(wall, Constants.VIEW_WIDTH , Constants.VIEW_HEIGHT , true);
        wallshader = new BitmapShader(wall, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

        factory = BitmapFactory.decodeResource(context.getResources(), R.drawable.sky);
        factory = Bitmap.createScaledBitmap(factory, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, false);
        skyshader = new BitmapShader(factory, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        floor = BitmapFactory.decodeResource(context.getResources(), R.drawable.sky);
        floor = Bitmap.createScaledBitmap(floor, Constants.VIEW_WIDTH , Constants.VIEW_HEIGHT , true);
        floorshader = new BitmapShader(floor, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

    }
    /*
    constructor with context and attributes
    */
    public MazePanel1(Context context, AttributeSet attributes) {
        super(context, attributes);
        bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();


        wall = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_1);
        wall = Bitmap.createScaledBitmap(wall, Constants.VIEW_WIDTH , Constants.VIEW_HEIGHT , true);
        wallshader = new BitmapShader(wall, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        factory = BitmapFactory.decodeResource(context.getResources(), R.drawable.sky);
        factory = Bitmap.createScaledBitmap(factory, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, false);
        skyshader = new BitmapShader(factory, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        floor = BitmapFactory.decodeResource(context.getResources(), R.drawable.img);
        floor = Bitmap.createScaledBitmap(floor, Constants.VIEW_WIDTH , Constants.VIEW_HEIGHT , true);
        floorshader = new BitmapShader(floor, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

    }
    /*
    constructor with context, attributes and style
    */
    public MazePanel1(Context context,AttributeSet attributes ,int style) {
        super(context,attributes,style);
        bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();

        wall = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_1);
        wall = Bitmap.createScaledBitmap(wall, Constants.VIEW_WIDTH , Constants.VIEW_HEIGHT , true);
        wallshader = new BitmapShader(wall, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        factory = BitmapFactory.decodeResource(context.getResources(), R.drawable.sky);
        factory = Bitmap.createScaledBitmap(factory, Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, false);
        skyshader = new BitmapShader(factory, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        floor = BitmapFactory.decodeResource(context.getResources(), R.drawable.img);
        floor = Bitmap.createScaledBitmap(floor, Constants.VIEW_WIDTH , Constants.VIEW_HEIGHT , true);
        floorshader = new BitmapShader(floor, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
    }
    /*
    constructor visual custom view of red dot on black and grey canvas
    */
    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        //addBackground(100);

    }

    @Override
    public void commit() {
        invalidate();
    }

    @Override
    public boolean isOperational() {
        return false;
    }

    @Override
    public void setColor(int argb) {
        paint.setColor(argb);
    }

    @Override
    public int getColor() {
        return paint.getColor();
    }

    @Override
    public void addBackground(float percentToExit) {
      //  paint.setColor((ColorTheme.getColor(ColorTheme.MazeColors.BACKGROUND_TOP,percentToExit)).toArgb());
//        paint.setShader(skyshader);
//        addFilledRectangle(0, 0, getWidth(), getHeight()/2);
//        paint.setShader(null);
//        paint.setColor((ColorTheme.getColor(ColorTheme.MazeColors.BACKGROUND_BOTTOM,percentToExit)).toArgb());
//        paint.setShader(floorshader);
//        addFilledRectangle(0, getHeight()/2, getWidth(), getHeight());
//        paint.setShader(null);

    }
    public void addSky(int x, int y, int width, int height) {
        //  paint.setColor((ColorTheme.getColor(ColorTheme.MazeColors.BACKGROUND_TOP,percentToExit)).toArgb());
        paint.setShader(skyshader);
        addFilledRectangle(x, y, width, height);
        paint.setShader(null);
//        // paint.setColor((ColorTheme.getColor(ColorTheme.MazeColors.BACKGROUND_BOTTOM,percentToExit)).toArgb());
        paint.setShader(floorshader);
        addFilledRectangle(x, height/2, width, height);
        paint.setShader(null);

    }


    @Override
    public void addFilledRectangle(int x, int y, int width, int height) {
        paint.setStyle(Paint.Style.FILL);
        Rect rect = new Rect(x, y, x + width, y + height);
        canvas.drawRect(rect, paint);
    }

    @Override
    public void addFilledPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        //paint.setStyle(Paint.Style.FILL);
        paint.setShader(wallshader);
        Path path = new Path();
        path.reset();
        path.moveTo(xPoints[0], yPoints[0]);
        for (int i = 1; i < nPoints; i++) {
            path.lineTo(xPoints[i], yPoints[i]);
        }
        path.lineTo(xPoints[0], yPoints[0]);
        canvas.drawPath(path, paint);
        paint.setShader(null);
    }

    @Override
    public void addPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        Path path = new Path();
        path.reset();
        path.moveTo(xPoints[0], yPoints[0]);
        for (int i = 1; i < nPoints; i++) {
            path.lineTo(xPoints[i], yPoints[i]);
        }
        path.lineTo(xPoints[0], yPoints[0]);
        canvas.drawPath(path, paint);
    }

    @Override
    public void addLine(int startX, int startY, int endX, int endY) {
        canvas.drawLine(startX, startY, endX, endY, paint);
    }

    @Override
    public void addFilledOval(int x, int y, int width, int height) {
        paint.setStyle(Paint.Style.FILL);
        RectF oval = new RectF(x, y, x + width, y + height);
        canvas.drawOval(oval, paint);
    }

    @Override
    public void addArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        canvas.drawArc(x,y,x+width,y+height, startAngle, arcAngle,false, paint);
    }

    @Override
    public void addMarker(float x, float y, String str) {
        paint.setTextSize(25);
        canvas.drawText(str,x, y, paint);
    }

    @Override
    public void setRenderingHint(P7RenderingHints hintKey, P7RenderingHints hintValue) {

    }
}
