package uoec.findr;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by rushil.perera on 2017-01-14.
 */

public class LineView extends View {

    private Paint paint;
    private float start, end, lineWidth, angle, positionY;

    public LineView(Context context) {
        super(context);
        init();
    }

    public LineView(Context context, Point point1, Point point2) {
        super(context);
        init();

        start = point1.getXCord();
        end = point2.getXCord();
        lineWidth = 10;
        positionY = point1.getYCord();
        angle = (float) (Math.atan2(point1.getYCord(), point2.getXCord()) * 180 / Math.PI);
    }

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LineView);

        try {
            start = a.getFloat(R.styleable.LineView_start, 0.0f);
            end = a.getFloat(R.styleable.LineView_end, 0.0f);
            lineWidth = a.getFloat(R.styleable.LineView_width, 0.0f);
            angle = a.getInteger(R.styleable.LineView_angle, 0);
            positionY = a.getFloat(R.styleable.LineView_position_y, 0.0f);
        } finally {
            a.recycle();
        }
    }

    public void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float left = start, right = end, bottom = positionY, top = positionY + lineWidth;

        super.onDraw(canvas);
        canvas.save();
        canvas.rotate(angle, (end + start) / 2, positionY);
        canvas.drawRect(left, top, right, bottom, paint);
        canvas.drawCircle(left, positionY + lineWidth / 2, lineWidth + 5, paint);
        canvas.drawCircle(right, positionY + lineWidth / 2, lineWidth + 5, paint);
        canvas.restore();
    }

    public void setStart(float start) {
        this.start = start;
    }

    public void setEnd(float end) {
        this.end = end;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }
}
