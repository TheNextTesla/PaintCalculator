package independent_study.paintcalculator;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import org.opencv.android.CameraGLSurfaceView;

public class CVGLSurfaceView extends CameraGLSurfaceView implements CameraGLSurfaceView.CameraTextureListener
{
    private static final String LOG_TAG = "CVGLSurfaceView";

    public CVGLSurfaceView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
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
        NativeBridge.testDraw(texIn, texOut, width, height);
        //return true;
        return false;
    }
}
