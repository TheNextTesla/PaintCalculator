package independent_study.paintcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
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
    public static boolean isManualNotAutoSelected;
    public static double lengthInserted;

    protected EditText editTextInches;
    protected RadioButton radioButtonWallDistance;
    protected RadioButton radioButtonPersonHeight;
    protected RadioGroup radioGroupMeasurement;
    protected RadioButton radioButtonManual;
    protected RadioButton radioButtonAuto;
    protected RadioGroup radioGroupSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        lengthInserted = Double.NaN;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTextInches = findViewById(R.id.editTextInches);
        radioButtonWallDistance = findViewById(R.id.radioButtonWallDistance);
        radioButtonPersonHeight = findViewById(R.id.radioButtonPersonHeight);
        radioGroupMeasurement = findViewById(R.id.radioGroup);

        radioButtonManual = findViewById(R.id.radioButtonManual);
        radioButtonAuto = findViewById(R.id.radioButtonAuto);
        radioGroupSelection = findViewById(R.id.radioGroup2);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(allFormsFilled())
                {
                    Log.d("InputActivity", "Length " + lengthInserted + " isManual " + isManualNotAutoSelected + " isDistance " + !isHeightNotDistanceSelected);
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


    private boolean allFormsFilled()
    {
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

        isHeightNotDistanceSelected = isHeightNotDistance;
        isManualNotAutoSelected = isManualNotAuto;
        lengthInserted = length;

        return true;
    }

    private void switchToCamera()
    {
        Intent intent = new Intent(this, PaintCameraActivity.class);
        startActivity(intent);
    }
}
