package independent_study.paintcalculator;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Blaine Huey on 3/22/2018.
 * https://docs.opencv.org/3.2.0/d7/dbd/tutorial_android_ocl_intro.html
 */
public abstract class CameraGLRendererBase implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener
{
   protected SurfaceTexture mSTex;
   protected CameraGLSurfaceView mView;

   protected boolean mGLInit;
   protected boolean mTexUpdate;

   public CameraGLRendererBase(CameraGLSurfaceView surfaceView)
   {
      mView = surfaceView;
      mGLInit = false;
      mTexUpdate = false;
   }

   protected abstract void openCamera();
   protected abstract void closeCamera();
   protected abstract void setCameraPreviewSize(int width, int height);

   @Override
   public synchronized void onFrameAvailable(SurfaceTexture surfaceTexture)
   {
      mTexUpdate = true;
      mView.requestRender();
   }

   @Override
   public void onDrawFrame(GL10 g1)
   {
      if(mGLInit)
      {
         synchronized (this)
         {
            if(mTexUpdate)
            {
               mSTex.updateTexImage();
               mTexUpdate = false;
            }
         }
         //NativeGLRenderer.drawFrame();
      }
   }

   @Override
   public void onSurfaceChanged(GL10 gl, int surfaceWidth, int surfaceHeight)
   {
      //NativeGLRenderer.changeSize(surfaceWidth, surfaceHeight);
      setCameraPreviewSize(surfaceWidth, surfaceHeight);
   }

   @Override
   public void onSurfaceCreated(GL10 gl, EGLConfig config)
   {
      String strGLVersion = GLES20.glGetString(GLES20.GL_VERSION);
      int hTex = 0;//ativeGLRenderer.initGL();
      mSTex = new SurfaceTexture(hTex);
      mSTex.setOnFrameAvailableListener(this);
      openCamera();
      mGLInit = true;
   }

   public void onResume()
   {

   }

   public void onPause()
   {
      mGLInit = false;
      mTexUpdate = false;
      closeCamera();

      if(mSTex != null)
      {
         mSTex.release();
         mSTex = null;
         //NativeGLRenderer.closeGL();
      }
   }

}
