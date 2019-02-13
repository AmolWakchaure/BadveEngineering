package engineering.badve.badveengineering.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import engineering.badve.badveengineering.R;
import engineering.badve.badveengineering.dashboard.HomeActivity;


/**
 * Created by snsystem_amol on 06-Apr-17.
 */

public class FloatingFaceBubbleService extends Service
{
    private WindowManager windowManager;
    private ImageView floatingFaceBubble;

    public final static String MY_ACTION = "MY_ACTION";

    boolean mAllowRebind;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        floatingFaceBubble = new ImageView(this);
        //a face floating bubble as imageView
        floatingFaceBubble.setImageResource(R.drawable.ic_launcher_badve);
        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        //here is all the science of params
        final WindowManager.LayoutParams myParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        myParams.gravity = Gravity.TOP | Gravity.LEFT;
        myParams.x=0;
        myParams.y=100;
        // add a floatingfacebubble icon in window
        windowManager.addView(floatingFaceBubble, myParams);
        try{
            //for moving the picture on touch and slide
//            floatingFaceBubble.setOnTouchListener(new View.OnTouchListener() {
//                WindowManager.LayoutParams paramsT = myParams;
//                private int initialX;
//                private int initialY;
//                private float initialTouchX;
//                private float initialTouchY;
//                private long touchStartTime = 0;
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    //remove face bubble on long press
//                    if(System.currentTimeMillis()-touchStartTime> ViewConfiguration.getLongPressTimeout() && initialTouchX== event.getX()){
//                        windowManager.removeView(floatingFaceBubble);
//                        stopSelf();
//                        return false;
//                    }
//                    switch(event.getAction()){
//                        case MotionEvent.ACTION_DOWN:
//                            touchStartTime = System.currentTimeMillis();
//                            initialX = myParams.x;
//                            initialY = myParams.y;
//                            initialTouchX = event.getRawX();
//                            initialTouchY = event.getRawY();
//                            break;
//                        case MotionEvent.ACTION_UP:
//                            break;
//                        case MotionEvent.ACTION_MOVE:
//                            myParams.x = initialX + (int) (event.getRawX() - initialTouchX);
//                            myParams.y = initialY + (int) (event.getRawY() - initialTouchY);
//                            windowManager.updateViewLayout(v, myParams);
//                            break;
//                    }
//                    return false;
//                }
//            });
            floatingFaceBubble.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    Intent i = new Intent(view.getContext(), HomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                }
            });
            floatingFaceBubble.setLongClickable(false);

//            floatingFaceBubble.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//
//                    Intent i = new Intent(view.getContext(),FloatingFaceBubbleService.class);
//                    startService(i);
//
//
//                    return false;
//
//
//                }
//            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return START_STICKY;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

//    public void onCreate()
//    {
//        super.onCreate();
//        floatingFaceBubble = new ImageView(this);
//        //a face floating bubble as imageView
//        floatingFaceBubble.setImageResource(R.drawable.ic_launcher_badve);
//        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
//        //here is all the science of params
//        final WindowManager.LayoutParams myParams = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_TOAST,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT);
//        myParams.gravity = Gravity.TOP | Gravity.LEFT;
//        myParams.x=0;
//        myParams.y=100;
//        // add a floatingfacebubble icon in window
//        windowManager.addView(floatingFaceBubble, myParams);
//        try{
//            //for moving the picture on touch and slide
//            floatingFaceBubble.setOnTouchListener(new View.OnTouchListener() {
//                WindowManager.LayoutParams paramsT = myParams;
//                private int initialX;
//                private int initialY;
//                private float initialTouchX;
//                private float initialTouchY;
//                private long touchStartTime = 0;
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    //remove face bubble on long press
//                    if(System.currentTimeMillis()-touchStartTime> ViewConfiguration.getLongPressTimeout() && initialTouchX== event.getX()){
//                        windowManager.removeView(floatingFaceBubble);
//                        stopSelf();
//                        return false;
//                    }
//                    switch(event.getAction()){
//                        case MotionEvent.ACTION_DOWN:
//                            touchStartTime = System.currentTimeMillis();
//                            initialX = myParams.x;
//                            initialY = myParams.y;
//                            initialTouchX = event.getRawX();
//                            initialTouchY = event.getRawY();
//                            break;
//                        case MotionEvent.ACTION_UP:
//                            break;
//                        case MotionEvent.ACTION_MOVE:
//                            myParams.x = initialX + (int) (event.getRawX() - initialTouchX);
//                            myParams.y = initialY + (int) (event.getRawY() - initialTouchY);
//                            windowManager.updateViewLayout(v, myParams);
//                            break;
//                    }
//                    return false;
//                }
//            });
//
//            floatingFaceBubble.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//
//
//                    Intent i = new Intent(view.getContext(), MainActivity_One.class);
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(i);
//
//
//                    return false;
//
//
//                }
//            });
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
