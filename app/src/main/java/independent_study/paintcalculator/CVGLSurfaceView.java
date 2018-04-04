package independent_study.paintcalculator;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import org.opencv.android.CameraGLSurfaceView;
import org.opencv.core.Rect;

public class CVGLSurfaceView extends CameraGLSurfaceView implements CameraGLSurfaceView.CameraTextureListener
{
    private static final String LOG_TAG = "CVGLSurfaceView";

    private double focal_length, sizeW, sizeH;

    public CVGLSurfaceView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        Camera c = Camera.open();
        focal_length = c.getParameters().getFocalLength();
        sizeW = Math.tan(c.getParameters().getHorizontalViewAngle()/2)*2*c.getParameters().getFocalLength();
        sizeH = Math.tan(c.getParameters().getVerticalViewAngle()/2)*2*c.getParameters().getFocalLength();
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onCameraViewStarted(int width, int height)
    {
        Log.d(LOG_TAG, String.format("onCameraViewStarted w%d, h%d", width, height));
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
        Log.d(LOG_TAG, "Width " + width + " Height " + height);
        //NativeBridge.testDraw(texIn, texOut, width, height);

        Rect wallBlob = NativeBridge.blobAnalyze(texIn, texOut, width, height, 0, 255, 0, 255, 0, 255);
        Log.d(LOG_TAG, "X " + wallBlob.x + " Y " + wallBlob.y);


        //return true;
        return false;
    }

    public double calculateArea(Rect obj, double distance, int width, int height){
        double w = (((obj.width / width) * sizeW) / focal_length) * distance;
        double h = (((obj.height/height) * sizeH) / focal_length) * distance;
        return  w * h;
    }
}
