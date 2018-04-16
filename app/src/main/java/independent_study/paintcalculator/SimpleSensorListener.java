package independent_study.paintcalculator;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import java.util.ArrayList;

public class SimpleSensorListener implements SensorEventListener
{
    private Sensor[] localSensors;
    private int[] accuracyValues;
    private ArrayList<float[]> sensorValues;

    public SimpleSensorListener(Sensor... sensors)
    {
        localSensors = sensors;
        accuracyValues = new int[sensors.length];
        sensorValues = new ArrayList<>(sensors.length);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        for(int i = 0; i < localSensors.length; i++)
        {
            if(sensor == localSensors[i])
            {
                accuracyValues[i] = accuracy;
                return;
            }
        }
        Log.d("SimpleSensorListener", "sensorNotListed - accuracy");
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        for(int i = 0; i < localSensors.length; i++)
        {
            if(event.sensor == localSensors[i])
            {
                sensorValues.set(i, event.values);
                return;
            }
        }
        Log.d("SimpleSensorListener", "sensorNotListed - values");
    }

    public int[] getAccuracyValues()
    {
        return accuracyValues;
    }

    public ArrayList<float[]> getSensorValues()
    {
        return sensorValues;
    }
}
