package yazilim.hilal.yesil.yhycamera.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import androidx.constraintlayout.solver.widgets.Rectangle;

import yazilim.hilal.yesil.yhycamera.R;

public class FocusView extends View {


    private int x = 0;
    private int y = 0;



    public FocusView(Context context) {
        super(context);
    }

    public FocusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FocusView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FocusView(Context context,int x, int y) {
        super(context);
        this.x = x;
        this.y = y;
        this.setPivotX(x);
        this.setPivotY(y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();

        int radius = (int)getResources().getDimension(R.dimen.radius);
        int stroke = (int)getResources().getDimension(R.dimen.stroke);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(stroke);

        paint.setColor(Color.WHITE);









        canvas.drawCircle(x,y,radius,paint);




    }



}
