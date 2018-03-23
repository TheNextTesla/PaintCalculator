package independent_study.paintcalculator;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.view.SurfaceHolder;

/**
 * Created by Blaine Huey on 3/22/2018.
 */

public class CameraGLSurfaceView extends GLSurfaceView
{
    CameraGLRendererBase rendererBase;

    public CameraGLSurfaceView(Context context)
    {
        super(context);

        if(Build.VERSION.SDK_INT >= 21)
        {
            rendererBase = new Camera2Renderer(this);
        }
        else
        {
            rendererBase = new CameraRenderer(this);
        }

        setEGLContextClientVersion(2);
        setRenderer(rendererBase);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        super.surfaceCreated(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        super.surfaceDestroyed(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
    {
        super.surfaceChanged(holder, format, w, h);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        rendererBase.onResume();
    }

    @Override
    public void onPause()
    {
        rendererBase.onPause();
        super.onPause();
    }
}
