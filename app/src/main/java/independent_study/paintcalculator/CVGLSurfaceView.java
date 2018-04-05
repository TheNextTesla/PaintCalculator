package independent_study.paintcalculator;

import android.content.Context;
import android.graphics.RectF;
import android.hardware.camera2.CameraCharacteristics;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SizeF;
import android.view.SurfaceHolder;

import org.opencv.core.Rect;

public class CVGLSurfaceView extends CameraGLSurfaceViewImproved implements CameraGLSurfaceViewImproved.CameraTextureListener
{
    //String for Logs
    private static final String LOG_TAG = "CVGLSurfaceView";

    private static final double SIZE_DIFFERENCE_TRESHHOLD_FOR_DISPLAY = 5;

    //focal_length stores the focal length of the camera being used, sizeW stores the sensor width, sizeH stores the sensor height
    private double focal_length, sizeW, sizeH, verticleViewAngel;

    private double prevSize = Double.MIN_VALUE;

    private RectangleView rectView;

    public CVGLSurfaceView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        rectView = findViewById(R.id.RectangleView);
        /*
        Can not bind Camera if CameraGLSurfaceView is in use, because it is already bound
        Finds focal length and the sensor width and height
        Camera c = Camera.open();
        focal_length = c.getParameters().getFocalLength();
        sizeW = Math.tan(c.getParameters().getHorizontalViewAngle()/2)*2*c.getParameters().getFocalLength();
        sizeH = Math.tan(c.getParameters().getVerticalViewAngle()/2)*2*c.getParameters().getFocalLength();
        c.release();
        */
    }


    @Override
    //Initializes focal_length, sizeW, and sizeH if CameraCharacteristics provide non null returns. In the case of a null return focal_length, sizeW, or sizeH will be set to Double.NaN
    public void onCameraViewStarted(int width, int height)
    {
        Log.d(LOG_TAG, String.format("onCameraViewStarted w%d, h%d", width, height));
        float[] focalLengths = getCameraCharacteristics().get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);

        if(focalLengths != null && focalLengths.length > 0)
            focal_length = focalLengths[0];
        else
            focal_length = Double.NaN;

        SizeF physicalSize = getCameraCharacteristics().get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE);

        if(physicalSize != null)
        {
            sizeH = physicalSize.getHeight();
            sizeW = physicalSize.getWidth();
            verticleViewAngel = Math.atan(sizeH/focal_length/2) * 2;
        }
        else
        {
            sizeH = Double.NaN;
            sizeW = Double.NaN;
        }
    }

    @Override
    public void onCameraViewStopped()
    {
        Log.d(LOG_TAG, "onCameraViewStopped");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        super.surfaceCreated(holder);
        Log.d(LOG_TAG, "surfaceCreated");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        Log.d(LOG_TAG, "surfaceDestroyed");
        super.surfaceDestroyed(holder);
    }

    @Override
    public boolean onCameraTexture(int texIn, int texOut, int width, int height)
    {
        //Call Native Processing Code Here - Can Pass Parameters to OpenCV
        //Log.d(LOG_TAG, "Width " + width + " Height " + height);
        //NativeBridge.testDraw(texIn, texOut, width, height);

        //TODO switch with actual boolean
        if(false) {
            Rect wallBlob = NativeBridge.blobAnalyze(texIn, texOut, width, height, 0, 255, 0, 255, 0, 255);
            Log.d(LOG_TAG, "X " + wallBlob.x + " Y " + wallBlob.y);
            rectView.setRectToDraw(new RectF(wallBlob.x / width, wallBlob.y / height, (wallBlob.x + wallBlob.width) / width, (wallBlob.y + wallBlob.height) / height));
            if (Math.abs(prevSize - calculateArea(wallBlob, 0, width, height)) > SIZE_DIFFERENCE_TRESHHOLD_FOR_DISPLAY)
            {
               prevSize = calculateArea(wallBlob, 0, width, height);
               displayArea(prevSize, true, 0,0, false);
            }
        }
        //rectView.setRectToDraw(new RectF(wallBlob.x/width, wallBlob.y/width, (wallBlob.x + wallBlob.width )/ width, (wallBlob.height + wallBlob.y) / height));
        //return true;
        return false;
    }

    //Calculates the area of the object using the formula obj size on sensor / focal length = obj size / distance Uses width and height to compute the size of the area on the sensor
    public double calculateArea(Rect obj, double distance, int width, int height)
    {
        double w = (((obj.width / width) * sizeW) / focal_length) * distance;
        double h = (((obj.height / height) * sizeH) / focal_length) * distance;
        return  w * h;
    }

    //Calculates the area of the object using the formula obj size on sensor / focal length = obj size / distance
    public double calculateArea(RectF obj, double distance)
    {
        double w = (((obj.width()) * sizeW) / focal_length) * distance;
        double h = (((obj.height()) * sizeH) / focal_length) * distance;
        return  w * h;
    }

    /**
     * Calculates Width of an object given the percentage of the width of the sensor that it occupies and the distance from the object
     * Object Size on Sensor / Focal Length = Object Size / Distance
     * Object Size on Sensor * Distance / Focal Length = Object Size
     * @param percentSensor
     * percent of image taken up by object 1.00 = 100% 0.0 = 0%
     * @param distance
     * distance of object from camera
     **/
    public double calculateWidth(double percentSensor, double distance)
    {
        return (((percentSensor) * sizeW) / focal_length) * distance;
    }

    /**
     * Calculates Height of an object given the percentage of the height of the sensor that it occupies and the distance from the object
     * Object Size on Sensor / Focal Length = Object Size / Distance
     * Object Size on Sensor * Distance / Focal Length = Object Size
     * @param percentSensor
     * percent of image taken up by object 1.00 = 100% 0.0 = 0%
     * @param distance
     * distance of object from camera
     **/
    public double calculateHeight(double percentSensor, double distance)
    {
        return (((percentSensor) * sizeH) / focal_length) * distance;
    }

    /**
     * Calculates the distance of the camera from the wall using by finding the angel of botton wall towards the camera
     * @param height
     * height of camera
     * @param percentageHeight
     * the y location of the ground in the image expressed as a percentage of the total size of the image
     * @return
     * the distance of the camera from the object
     */
    public double calculateDistance(double height, double percentageHeight)
    {
        return Math.tan((percentageHeight - 0.5) * verticleViewAngel) * height;
    }

    //Displays a snackbar with the area shown if length is true the duration is Snackbar.LENGTH_LONG if it is false the duration is Snackbar.LENGTH_SHORT
    public void displayArea(double area, boolean length, double width, double height, boolean displayWH)
    {
        Snackbar.make(this, area + " ft^2" + (displayWH ? " Width: " + width + "ft " + " Height: " + height + "ft" : ""), (length ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_SHORT)) .setAction("Does Nothing", null).show();
    }
}
