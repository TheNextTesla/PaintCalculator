package independent_study.paintcalculator;

public class NativeBridge
{
    static
    {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("native-lib");
    }

    private NativeBridge() { }

    public static native void drawBox();
}
