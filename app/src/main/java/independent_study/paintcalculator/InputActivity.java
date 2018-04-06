package independent_study.paintcalculator;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class InputActivity extends AppCompatActivity
{
    public static boolean isHeightNotDistanceSelected;
    public static boolean isManualNotFixedSelected;
    public static double lengthInserted;

    protected EditText editTextInches;
    protected RadioButton radioButtonWallDistance;
    protected RadioButton radioButtonPersonHeight;
    protected RadioGroup radioGroupMeasurement;
    protected RadioButton radioButtonManual;
    protected RadioButton radioButtonAuto;
    protected RadioGroup radioGroupSelection;

    /**
     * Sets up the InputActivity
     *  Grabs UI elements, sets up listeners, and displays them
     * @param savedInstanceState - data passed over
     *                           - only super uses it
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        lengthInserted = Double.NaN;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Stores all of the (possibly) necessary UI Components as State Variables
        editTextInches = findViewById(R.id.editTextInches);
        radioButtonWallDistance = findViewById(R.id.radioButtonWallDistance);
        radioButtonPersonHeight = findViewById(R.id.radioButtonPersonHeight);
        radioGroupMeasurement = findViewById(R.id.radioGroup);

        radioButtonManual = findViewById(R.id.radioButtonManual);
        radioButtonAuto = findViewById(R.id.radioButtonAuto);
        radioGroupSelection = findViewById(R.id.radioGroup2);

        //Adds the Listener that makes the button go to the camera (when applicable)
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(allFormsFilled())
                {
                    Log.d("InputActivity", "Length " + lengthInserted + " isManual " + isManualNotFixedSelected + " isDistance " + !isHeightNotDistanceSelected);
                    switchToCamera();
                }
                else
                {
                    Snackbar.make(view, "Please Fill In All Data", Snackbar.LENGTH_LONG)
                            .setAction("Does Nothing", null).show();
                }
            }
        });
    }

    /**
     * Makes sure that all of the UI info entry is filled out
     * Also loads up the static variables (for lazy outside class reading) with responses
     * @return Whether or not all of the info entries are filled properly
     */
    private boolean allFormsFilled()
    {
        //Parses Length from String
        double length = Double.NaN;
        try
        {
            length = Double.parseDouble(editTextInches.getText().toString());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }

        //Determines Boolean State By Matching the Id of the Selected Radio Button to R.id
        boolean isHeightNotDistance;
        boolean isManualNotAuto;
        try
        {
            int selectedIdHeightNotDistance = radioGroupMeasurement.getCheckedRadioButtonId();
            int selectedIdManualNotAuto = radioGroupSelection.getCheckedRadioButtonId();

            if(selectedIdHeightNotDistance == -1 || selectedIdManualNotAuto == -1)
                return false;

            if(selectedIdHeightNotDistance == R.id.radioButtonWallDistance)
                isHeightNotDistance = false;
            else if(selectedIdHeightNotDistance == R.id.radioButtonPersonHeight)
                isHeightNotDistance = true;
            else
                return false;


            if(selectedIdManualNotAuto == R.id.radioButtonManual)
                isManualNotAuto = true;
            else if(selectedIdManualNotAuto == R.id.radioButtonAuto)
                isManualNotAuto = false;
            else
                return false;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }

        //Sets Static Variables (if it makes it that far)
        isHeightNotDistanceSelected = isHeightNotDistance;
        isManualNotFixedSelected = isManualNotAuto;
        lengthInserted = length;

        return true;
    }

    /**
     * Starts the Camera Viewing Activity with An Intent
     */
    private void switchToCamera()
    {
        Intent intent = new Intent(this, PaintCameraActivity.class);
        startActivity(intent);
    }
}
