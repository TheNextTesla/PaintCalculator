package independent_study.paintcalculator;

public final class VisionMathUtilities
{
    private VisionMathUtilities() {}

    /**
     * Focal Length Vision Math
     * FL = (LengthPixels * Distance) / LengthReal
     * @param focalLength
     * @return
     */
    public static double calculateRealLength(double focalLength, double lengthInPixels, double distanceInFeet)
    {
        return (lengthInPixels * distanceInFeet) / focalLength;
    }
}
