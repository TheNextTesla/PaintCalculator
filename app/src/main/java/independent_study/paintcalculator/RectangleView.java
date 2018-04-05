package independent_study.paintcalculator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class RectangleView extends View
{

    //Paint for the view
    private Paint paint;

    //RectF that will be drawn on the screen
    private RectF rect;

    /**View that draws a rectangle on the screen**/
    public RectangleView(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(10);
    }

    /**
     * @param rect
     * sets this.rect equal to rect
     */
    public void setRectToDraw(RectF rect)
    {
        this.rect = rect;
    }

    @Override
    /** Draws the rectF if it is not null by multiplying the rectF values by the canvas width and height
    **/
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if(rect == null)
        {
            Log.d("Rectangle", "W "+ canvas.getWidth() + " H " + canvas.getHeight());
            return;
        }

        Log.d("Rectangle", "Going the distance");

        canvas.drawRect(rect.left*canvas.getWidth(), rect.top*canvas.getHeight(), rect.right*canvas.getWidth(), rect.bottom*canvas.getHeight(), paint);
    }
}
