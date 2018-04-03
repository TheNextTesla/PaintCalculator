package independent_study.paintcalculator;

import org.opencv.core.Rect;

public class NativeBridge
{
    static
    {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("native-lib");
    }

    private NativeBridge() { }

    public static native void testDraw(int texIn, int texOut, int width, int height);

    public static native Rect blobAnalyze(int texIn, int texOut, int width, int height, int hMin, int hMax, int sMin, int sMax, int vMin, int vMax);

    public static native void cannyAnalyze(int texIn, int texOut, int width, int height);
}
