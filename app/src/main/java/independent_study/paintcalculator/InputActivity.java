package independent_study.paintcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class InputActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(allFormsFilled())
                {
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
        return true;
    }

    private void switchToCamera()
    {
        Intent intent = new Intent(this, PaintCameraActivity.class);
        startActivity(intent);
    }
}
