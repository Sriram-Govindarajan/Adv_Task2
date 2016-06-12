package com.example.sriram.adv_task2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int width, height,small,check=0;
    private float x,y,vx=10,vy=10,r=50;
    private int t=1;
    private Canvas c;
    private Paint paint;
    private ImageView imageview;
    private static final int REQUEST_CODE=1234;
    private ListView wordsList;
    private Button speak;
    private ArrayList<String > matches;
    //t=1 for circle t=2 for diamond t=3 for square

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageview=(ImageView) findViewById(R.id.imageView);
        Display display=getWindowManager().getDefaultDisplay();
        Point size=new Point();
        display.getSize(size);
        width=size.x;
        height=size.y;
        if(height>width){small=width;check=1;}
        else small=height;
        x=width/2;
        y=height/2;
        Bitmap b = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        imageview.setImageBitmap(b);
        c = new Canvas(b);
        c.drawColor(Color.BLACK);
        paint = new Paint();
        paint.setAntiAlias(false);
        paint.setStyle(Paint.Style.FILL);

        paint.setColor(Color.BLUE);
        c.drawCircle(x, y, r, paint);

        speak=(Button) findViewById(R.id.speak);
        wordsList=(ListView) findViewById(R.id.listView);
        PackageManager pm =getPackageManager();
        List<ResolveInfo> activities =pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),0);
        if(activities.size() == 0)
        {
            speak.setEnabled(false);
            speak.setText("Recognizer not present" );
        }
    }

    public void onClick(View v)
    { startVoiceRecognitionActivity(); }
    private void startVoiceRecognitionActivity()
    {
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"VOICE RECOGNITION DEMO");
        startActivityForResult(intent,REQUEST_CODE);     }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK)
        {
            matches =data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            wordsList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,matches));

        }

        if ( matches.contains("left"))
            Left(speak);
        else if(matches.contains("right"))
            Right(speak);
        if ( matches.contains("top"))
            Up(speak);
        else if(matches.contains("bottom"))
            Down(speak);
        if ( matches.contains("circle"))
            Circle(speak);
        else if(matches.contains("diamond"))
            Diamond(speak);
        else if ( matches.contains("square"))
            Square(speak);
        else if(matches.contains("increase"))
            Increase(speak);
        else if(matches.contains("decrease"))
            Decrease(speak);

        super.onActivityResult(requestCode,resultCode,data);

    }
    public void Increase(View v){
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        increase();
                    }
                }
        );
    }
    public void Decrease(View v){
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        decrease();
                    }
                }
        );
    }
    void increase()
    { if(check==1)
        {
        if(x+r<=small-10 && x-r>=10 && y-r>=10 && y+r<=height-10)
            r=r+10;
        }
        if(check==0)
        {if(y+r<=small-10 && y-r>=10 && x-r>=10 && x+r<=width-10 )
            r=r+10;}
        switch(t)
        {
            case 1: paint.setColor(Color.YELLOW);
                c.drawRect(0,0,width,height,paint);
                paint.setColor(Color.RED);
                c.drawCircle(x,y,r,paint);break;

            case 2: paint.setColor(Color.YELLOW);
                c.drawRect(0,0,width,height,paint);
                paint.setColor(Color.RED);
                c.save();
                c.rotate(45,x,y);
                c.drawRect((float)(x-(r/Math.sqrt(2))), (float)(y-(r/Math.sqrt(2))), (float)(x+(r/Math.sqrt(2))), (float)(y+(r/Math.sqrt(2))), paint);
                c.restore();break;

            case 3: paint.setColor(Color.YELLOW);
                c.drawRect(0,0,width,height,paint);
                paint.setColor(Color.RED);
                c.drawRect(x-r,y-r,x+r,y+r,paint); break;
        }
        imageview.invalidate();
    }
    void decrease()
    {   if(r>=20)
        r=r-10;
        switch(t)
        {
            case 1: paint.setColor(Color.YELLOW);
                c.drawRect(0,0,width,height,paint);
                paint.setColor(Color.RED);
                c.drawCircle(x,y,r,paint);break;

            case 2: paint.setColor(Color.YELLOW);
                c.drawRect(0,0,width,height,paint);
                paint.setColor(Color.RED);
                c.save();
                c.rotate(45,x,y);
                c.drawRect((float)(x-(r/Math.sqrt(2))), (float)(y-(r/Math.sqrt(2))), (float)(x+(r/Math.sqrt(2))), (float)(y+(r/Math.sqrt(2))), paint);
                c.restore();break;

            case 3: paint.setColor(Color.YELLOW);
                c.drawRect(0,0,width,height,paint);
                paint.setColor(Color.RED);
                c.drawRect(x-r,y-r,x+r,y+r,paint); break;
        }
        imageview.invalidate();
    }
    public void Left(View v) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        left();
                    }
                }
        );
    }
    public void Right(View v) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        right();
                    }
                }
        );
    }
    void left(){
        paint.setColor(Color.BLACK);
        c.drawRect(0,0,width,height,paint);
        if(x-r<=0)vx=0;
        else if(x+r<width || x-r>0) vx=10;
        else if(x+r>=width)vx=0;
        x=x-vx;
        paint.setColor(Color.RED);
        switch(t) {
            case 1:
                c.drawCircle(x, y, r, paint);break;
            case 2 :c.save();
                c.rotate(45,x,y);
                c.drawRect((float)(x-(r/Math.sqrt(2))), (float)(y-(r/Math.sqrt(2))), (float)(x+(r/Math.sqrt(2))), (float)(y+(r/Math.sqrt(2))), paint);
                c.restore();break;
            case 3:
                c.drawRect(x - r, y - r, x + r, y + r, paint);break;
        }
        imageview.invalidate();
    }
    void right() {
        paint.setColor(Color.BLACK);
        c.drawRect(0,0,width,height,paint);
        if(x+r>=width)vx=0;
        else if(x+r<width|| x-r>0) vx=10;
        else if(x-r<=0)vx=10;
        paint.setColor(Color.GREEN);
        x = x + vx;
        switch (t) {
            case 1:
                c.drawCircle(x, y, r, paint);                break;
            case 2:c.save();
                c.rotate(45,x,y);
                c.drawRect((float)(x-(r/Math.sqrt(2))), (float)(y-(r/Math.sqrt(2))), (float)(x+(r/Math.sqrt(2))), (float)(y+(r/Math.sqrt(2))), paint);
                c.restore();            break;
            case 3:
                c.drawRect(x - r, y - r, x + r, y + r, paint);                break;

        }
        imageview.invalidate();
    }
    public void Up(View v){
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        up();
                    }
                }
        );

    }
    void up(){
        paint.setColor(Color.BLACK);
        c.drawRect(0,0,width,height,paint);
        if(y+r>=height)vy=10;
        else if(y-r<=0)vy=0;
        else if(y-r>0 || y+r<height)vy=10;
        y=y-vy;
        paint.setColor(Color.MAGENTA);
        switch(t) {
            case 1:
                c.drawCircle(x, y, r, paint);break;
            case 2 :c.save();
                c.rotate(45,x,y);
                c.drawRect((float)(x-(r/Math.sqrt(2))), (float)(y-(r/Math.sqrt(2))), (float)(x+(r/Math.sqrt(2))), (float)(y+(r/Math.sqrt(2))), paint);
                c.restore();break;
            case 3:
                c.drawRect(x - r, y - r, x + r, y + r, paint);break;
        }
        imageview.invalidate();
    }
    public void Down(View v){
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        down();
                    }
                }
        );
    }
    void down(){
        paint.setColor(Color.BLACK);
        c.drawRect(0,0,width,height,paint);
        if(y+r>=height)vy=0;
        else if(y-r<=0)vy=10;
        else if(y-r>0 || y+r<height)vy=10;
        y=y+vy;
        paint.setColor(Color.YELLOW);
        switch(t) {
            case 1:
                c.drawCircle(x, y, r, paint);break;
            case 2 :c.save();
                c.rotate(45,x,y);
                c.drawRect((float)(x-(r/Math.sqrt(2))), (float)(y-(r/Math.sqrt(2))), (float)(x+(r/Math.sqrt(2))), (float)(y+(r/Math.sqrt(2))), paint);
                c.restore();break;
            case 3:
                c.drawRect(x - r, y - r, x + r, y + r, paint);break;
        }
        imageview.invalidate();
    }
    public void Circle(View v)
    { runOnUiThread(
            new Runnable() {
                @Override
                public void run() {
                    circle();
                }
            }
    );}
    void circle()
    {
        t=1;
        paint.setColor(Color.BLACK);
        c.drawRect(0,0,width,height,paint);
        paint.setColor(Color.BLUE);
        c.drawCircle(x,y,r,paint);
        imageview.invalidate();
    }
    public void Diamond(View v)
    { runOnUiThread(
            new Runnable() {
                @Override
                public void run() {
                    diamond();
                }
            }
    );}
    void diamond()
    {
        t=2;
        paint.setColor(Color.BLACK);
        c.drawRect(0,0,width,height,paint);
        paint.setColor(Color.WHITE);
        c.save();
        c.rotate(45,x,y);
        c.drawRect((float)(x-(r/Math.sqrt(2))), (float)(y-(r/Math.sqrt(2))), (float)(x+(r/Math.sqrt(2))), (float)(y+(r/Math.sqrt(2))), paint);
        imageview.invalidate();
        c.restore();

    }
    public void Square(View v)
    { runOnUiThread(
            new Runnable() {
                @Override
                public void run() {
                    square();
                }
            }
    );}
    void square()
    {
        t=3;
        paint.setColor(Color.BLACK);
        c.drawRect(0,0,width,height,paint);
        paint.setColor(Color.CYAN);
        c.drawRect(x-r,y-r,x+r,y+r,paint);
        imageview.invalidate();
    }
}

