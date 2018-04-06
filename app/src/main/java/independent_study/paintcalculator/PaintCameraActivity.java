package independent_study.paintcalculator;

import android.Manifest;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.RectF;
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

/**
 *
 */
public class PaintCameraActivity extends Activity
{
    private static final String LOG_TAG = "PaintCameraActivity";

    private static final int PERMISSIONS_KEY = 42;
    //Stores the touch locations for one movement by the user on the screen index 0 stores the action down location and index 1 stores the action up location
    private MotionEvent[] touchLocations = new MotionEvent[2];
    //Width of the screen in pixels
    private int screenPixelWidth;
    //Height of the screen in pixels
    private int screenPixelHeight;
    //Stores the CVGLSurfaceView from R.id.CVGLSurfaceView
    private CVGLSurfaceView cvView;
    //Stores the RectangleView from R.id.RectangleView
    private RectangleView rectView;
    //Temporary rectangle that stores the current rectangle drawn on the screen by the user
    private RectF tempRect;
    //stores the starting X position on the screen for a touch during an action down event
    private float startX;
    //stores the starting Y position on the screen for a touch during an action down event
    private float startY;


    /**
     * Sets up the Activity's Instance Variables and UI Parameter
     * @param savedInstanceState - Any Parameter Sent to the Activity
     *                           - We don't actually use any of this
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Without a Title Bar, Full Screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Set Up Camera Permission and UI
        requestPermissions();
        setContentView(R.layout.activity_paintcamera);
        rectView = findViewById(R.id.RectangleView);
        cvView = findViewById(R.id.CVGLSurfaceView);
        cvView.setCameraTextureListener(cvView);
        cvView.setRectView(rectView);
        Log.d(LOG_TAG, "onCreate");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) cvView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenPixelWidth = displayMetrics.widthPixels;
        screenPixelHeight = displayMetrics.heightPixels;

        //touch handler
        View.OnTouchListener handleTouch = new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (InputActivity.isManualNotFixedSelected)
                {
                    float x = (float) (int) event.getX();
                    float y = (float) (int) event.getY();

                    switch (event.getAction())
                    {
                        //sets the tempRect to a new RectF with a left and right at current x/screenPixelWidth and a top and bottom at y/screenPixelHeight
                        //sets startX and startY to x/screenPixelWidth and y/screenPixelWidth respectively for future use
                        case MotionEvent.ACTION_DOWN:
                            tempRect = new RectF(x / screenPixelWidth, y / screenPixelHeight, x / screenPixelWidth, y / screenPixelHeight);
                            startX = x / screenPixelWidth;
                            startY = y / screenPixelHeight;
                            touchLocations[0] = event;
                            Log.d(LOG_TAG, "Down Touch X " + x + " Y " + y);
                            break;
                        //sets tempRect to a new RectF that has the same left and top but the right and bottom are set to x/screenPixelWidth and y/screenPixelWidth to update the rectangle to the current touch location on screen
                        case MotionEvent.ACTION_MOVE:
                            if (tempRect != null)
                            {
                                tempRect = new RectF(startX, startY, x / screenPixelWidth, y / screenPixelWidth);
                            }

                        case MotionEvent.ACTION_UP:
                            touchLocations[1] = event;
                            cvView.displayArea(cvView.calculateArea(tempRect,InputActivity.isHeightNotDistanceSelected ? cvView.calculateDistance(InputActivity.lengthInserted, tempRect.bottom): InputActivity.lengthInserted), true, cvView.calculateWidth(Math.abs(tempRect.right - tempRect.left), InputActivity.isHeightNotDistanceSelected ? cvView.calculateDistance(InputActivity.lengthInserted, tempRect.bottom): InputActivity.lengthInserted), cvView.calculateHeight(Math.abs(tempRect.top - tempRect.bottom), InputActivity.isHeightNotDistanceSelected ? cvView.calculateDistance(InputActivity.lengthInserted, tempRect.bottom): InputActivity.lengthInserted), true);
                            Log.d(LOG_TAG, "Up Touch X " + x + " Y " + y);
                            break;
                    }

                    if (tempRect != null)
                        tempRect.sort();

                    rectView.setRectToDraw(tempRect);
                    rectView.invalidate();
                    return true;
                }
                else
                {
                    //Loads the default box if fixed
                    RectF f = new RectF((float) 0.25, (float)0.75, (float) 0.75, (float) 0.25);
                    f.sort();
                    rectView.setRectToDraw(f);
                    rectView.invalidate();
                }
                return false;
            }
        };
        cvView.setOnTouchListener(handleTouch);

        //Loads the default box if fixed
        if(!InputActivity.isManualNotFixedSelected)
        {
            RectF f = new RectF((float) 0.25, (float)0.75, (float) 0.75, (float) 0.25);
            f.sort();
            rectView.setRectToDraw(f);
            rectView.invalidate();
        }
    }

    /**
     * TouchLocations is an array that stores the location of touch down and touch up of the last touch interaction
     * Index 0 is the MotionEvent for touch down and index 1 is the MotionEvent for touch up
     * @return touchLocations
     */
    public MotionEvent[] getTouchCordinates()
    {
        return(touchLocations);
    }

    /**
     * Adjusts screen touch location to camera pixel location
     */
    public int adjustScreenTouchX(int screenX)
    {
        return((screenX/screenPixelWidth)* cvView.getWidth());
    }

    /**
     * Adjusts screen touch location to camera pixel location
     */
     public int adjustScreenTouchY(int screenY)
    {
        return((screenY/screenPixelHeight)* cvView.getHeight());
    }

    /**
     * How to react when we are first given the camera permission
     * @param requestCode - code used when requesting
     * @param permissions - the permission in question
     * @param grantResults - the permission status
     */
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

    /**
     * Requests permissions to access the camera if the app does not currently have permissions to access the camera
     * If the app already has permission to access to camera the no action is preformed
    */
    private void requestPermissions()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_KEY);
        }
    }

}
