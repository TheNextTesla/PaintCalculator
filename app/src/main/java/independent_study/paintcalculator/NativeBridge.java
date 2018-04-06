package independent_study.paintcalculator;

import org.opencv.core.Rect;

/**
 * Class that Links Up With C++ Code so We can run methods there if needed
 */
public class NativeBridge
{
    //Sets Up the C++ Libraries with Java
    static
    {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("native-lib");
    }

    private NativeBridge() { }

    //The Methods that we have in C++ (Not Used at the Moment)
    //We had originally hoped to make it all automatic

    public static native void testDraw(int texIn, int texOut, int width, int height);

    public static native Rect blobAnalyze(int texIn, int texOut, int width, int height, int hMin, int hMax, int sMin, int sMax, int vMin, int vMax);

    public static native void cannyAnalyze(int texIn, int texOut, int width, int height);
}
