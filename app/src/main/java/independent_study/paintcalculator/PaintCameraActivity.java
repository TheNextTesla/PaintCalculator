package independent_study.paintcalculator;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.opencv.android.CameraGLSurfaceView;

public class PaintCameraActivity extends Activity
{
    private static final String LOG_TAG = "PaintCameraActivity";
    private static final int PERMISSIONS_KEY = 42;
    private MotionEvent[] touchLocations = new MotionEvent[2];
    private int screenPixelWidth;
    private int screenPixelHeight;
    private CVGLSurfaceView view;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        requestPermissions();
        setContentView(R.layout.activity_paintcamera);
        view = findViewById(R.id.CVGLSurfaceView);
        view.setCameraTextureListener(view);
        Log.d(LOG_TAG, "onCreate");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) view.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenPixelWidth = displayMetrics.widthPixels;
        screenPixelHeight = displayMetrics.heightPixels;

        View.OnTouchListener handleTouch = new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {

                int x = (int) event.getX();
                int y = (int) event.getY();

                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        touchLocations[0] = event;
                        Log.d(LOG_TAG, "TouchX"+x);
                        Log.d(LOG_TAG, "TouchY"+y);
                        break;
                    case MotionEvent.ACTION_UP:
                        touchLocations[1] = event;
                        Log.d(LOG_TAG, "TouchX"+x);
                        Log.d(LOG_TAG, "TouchY"+y);
                        break;
                }

                return true;
            }
        };
        view.setOnTouchListener(handleTouch);
    }

    public MotionEvent[] getTouchCordinates()
    {
        return(touchLocations);
    }
    //adjusts screen touch loaction to camera pixel loaction
    public int adjustScreenTouchX(int screenX)
    {
        return((screenX/screenPixelWidth)*view.getWidth());
    }
    //adjusts screen touch loaction to camera pixel loaction
    public int adjustScreenTouchY(int screenY)
    {
        return((screenY/screenPixelHeight)*view.getHeight());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if(requestCode == PERMISSIONS_KEY)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                recreate();
            }
            else
            {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void requestPermissions()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_KEY);
        }
    }

}
