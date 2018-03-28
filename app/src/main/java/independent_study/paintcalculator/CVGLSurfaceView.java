package independent_study.paintcalculator;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import org.opencv.android.CameraGLSurfaceView;

public class CVGLSurfaceView extends CameraGLSurfaceView implements CameraGLSurfaceView.CameraTextureListener
{
    private static final String LOGTAG = "CVGLSurfaceView";

    public CVGLSurfaceView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    public void onCameraViewStarted(int width, int height)
    {
        Log.d(LOGTAG, String.format("onCameraViewStarted w%d, h%d", width, height));
    }

    @Override
    public void onCameraViewStopped()
    {
        Log.d(LOGTAG, "onCameraViewStopped");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        super.surfaceCreated(holder);
        Log.d(LOGTAG, "surfaceCreated");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        Log.d(LOGTAG, "surfaceDestroyed");
        super.surfaceDestroyed(holder);
    }

    @Override
    public boolean onCameraTexture(int texIn, int texOut, int width, int height)
    {
        //Call Native Processing Code Here - Can Pass Parameters to OpenCV
        return false;
    }
}
