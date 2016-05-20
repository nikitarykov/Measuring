package rykov.measuring;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Nikita Rykov on 19.05.2016.
 */
public class CustomView extends View {
    private static final float radius = 40;
    private Paint circlePaint;
    private Paint rectPaint;
    private Paint linePaint;
    private Paint rulePaint;
    private Paint textPaint;
    private float startLength;
    private PointF[] circleCenters = new PointF[2];
    private SparseArray<PointF> pointers = new SparseArray<>();

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(40f);
        linePaint.setColor(Color.parseColor("#38CBF8"));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        rulePaint = new Paint();
        rulePaint.setAntiAlias(true);
        rulePaint.setStrokeWidth(60f);
        rulePaint.setColor(Color.BLACK);
        rulePaint.setStyle(Paint.Style.STROKE);
        rulePaint.setStrokeJoin(Paint.Join.ROUND);
        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStrokeWidth(20f);
        rectPaint.setColor(Color.BLACK);
        rectPaint.setStyle(Paint.Style.FILL);
        rectPaint.setStrokeJoin(Paint.Join.ROUND);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStrokeWidth(5f);
        textPaint.setColor(Color.WHITE);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (circlePaint == null) {
            circlePaint = new Paint();
            circlePaint.setAntiAlias(true);
            circlePaint.setStrokeWidth(15f);
            circlePaint.setStrokeJoin(Paint.Join.ROUND);
            circleCenters[0] = new PointF(getMeasuredWidth() / 4, getMeasuredHeight() / 4);
            circleCenters[1] = new PointF(getMeasuredWidth() * 7 / 8, getMeasuredHeight() / 2);
            float dx = circleCenters[1].x - circleCenters[0].x;
            float dy = circleCenters[1].y - circleCenters[0].y;
            startLength = (float) Math.sqrt(dx * dx + dy * dy);
        }
        float dx = circleCenters[1].x - circleCenters[0].x;
        float dy = circleCenters[1].y - circleCenters[0].y;
        float norm = (float) Math.sqrt( dx * dx + dy * dy);
        float offset = 50 / norm;
        float lineWidth = 20 / norm;
        float cos = dx / norm;
        float sin = dy / norm;
        rulePaint.setShadowLayer(5f, -(dy * offset * cos) / 2, (dx * offset * cos) / 2, Color.GRAY);
        canvas.drawLine(circleCenters[0].x + dx * offset, circleCenters[0].y + dy * offset,
                circleCenters[1].x - dx * offset, circleCenters[1].y - dy * offset, rulePaint);
        float ruleDx = dx - 2 * dx * offset;
        float ruleDy = dy - 2 * dy * offset;
        for (int i = 1; i < 10; ++i) {
            PointF point = new PointF(circleCenters[0].x + dx * offset + i * ruleDx / 10,
                    circleCenters[0].y +  + dy * offset + i * ruleDy / 10);
            canvas.drawLine(point.x - dy * 5 / norm, point.y + dx * 5 / norm,
                    point.x + dy * 5 / norm, point.y - dx * 5 / norm, textPaint);
        }
        RectF rect = new RectF((circleCenters[0].x + circleCenters[1].x) / 2 - 100,
                (circleCenters[0].y + circleCenters[1].y) / 2 -180,
                (circleCenters[0].x + circleCenters[1].x) / 2 + 100,
                (circleCenters[0].y + circleCenters[1].y) / 2 -80);
        rectPaint.setShadowLayer(5f, 5 * sin * Math.signum(cos), 5 * Math.abs(cos), Color.GRAY);
        int length = (int) Math.floor(30 / startLength * norm);
        float sizeX = (circleCenters[0].x + circleCenters[1].x) / 2 - 80;
        float sizeY = (circleCenters[0].y + circleCenters[1].y) / 2 - 110;
        float cmX = (circleCenters[0].x + circleCenters[1].x) / 2 + 10;
        float cmY = (circleCenters[0].y + circleCenters[1].y) / 2 -130;
        PointF underlineLeft = new PointF((circleCenters[0].x + circleCenters[1].x) / 2 + 10,
                (circleCenters[0].y + circleCenters[1].y) / 2 -105);
        PointF underlineRight = new PointF((circleCenters[0].x + circleCenters[1].x) / 2 + 80,
                (circleCenters[0].y + circleCenters[1].y) / 2 -105);

        String size = "" + length;
        String cm = "cm";
        canvas.drawLine(circleCenters[0].x + dx * (offset - lineWidth),
                circleCenters[0].y + dy * (offset - lineWidth),
                circleCenters[0].x + dy * 100, circleCenters[0].y - dx * 100, linePaint);
        canvas.drawLine(circleCenters[0].x + dx * (offset - lineWidth),
                circleCenters[0].y + dy * (offset - lineWidth),
                circleCenters[0].x - dy * 100, circleCenters[0].y + dx * 100, linePaint);
        canvas.drawLine(circleCenters[1].x - dx * (offset - lineWidth),
                circleCenters[1].y - dy * (offset - lineWidth),
                circleCenters[1].x + dy * 100, circleCenters[1].y - dx * 100, linePaint);
        canvas.drawLine(circleCenters[1].x - dx * (offset - lineWidth),
                circleCenters[1].y - dy * (offset - lineWidth),
                circleCenters[1].x - dy * 100, circleCenters[1].y + dx * 100, linePaint);
        canvas.save();
        canvas.rotate((float) Math.toDegrees(Math.atan(dy / dx)),
                (circleCenters[0].x + circleCenters[1].x) / 2,
                (circleCenters[0].y + circleCenters[1].y) / 2);
        canvas.drawRoundRect(rect, 10, 10, rectPaint);
        textPaint.setTextSize(70);
        canvas.drawText(size, sizeX, sizeY, textPaint);
        textPaint.setTextSize(50);
        canvas.drawText(cm, cmX, cmY, textPaint);
        linePaint.setStrokeWidth(5f);
        canvas.drawLine(underlineLeft.x, underlineLeft.y, underlineRight.x, underlineRight.y, linePaint);
        linePaint.setStrokeWidth(40f);
        canvas.restore();
        circlePaint.setColor(Color.WHITE);
        circlePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(circleCenters[0].x, circleCenters[0].y, radius, circlePaint);
        canvas.drawCircle(circleCenters[1].x, circleCenters[1].y, radius, circlePaint);
        circlePaint.setColor(Color.BLACK);
        circlePaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(circleCenters[0].x, circleCenters[0].y, radius, circlePaint);
        canvas.drawCircle(circleCenters[1].x, circleCenters[1].y, radius, circlePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        int maskedAction = event.getActionMasked();
        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                PointF point = new PointF(event.getX(pointerIndex), event.getY(pointerIndex));
                if (isInCircle(circleCenters[0], point)) {
                    pointers.put(pointerId, circleCenters[0]);
                }
                if (isInCircle(circleCenters[1], point)) {
                    pointers.put(pointerId, circleCenters[1]);
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                for (int i = 0; i < event.getPointerCount(); ++i) {
                    PointF point = pointers.get(event.getPointerId(i));
                    if (point != null) {
                        point.x = event.getX(i);
                        point.y = event.getY(i);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                pointers.remove(pointerId);
                break;
            }
        }
        invalidate();
        return true;
    }

    private boolean isInCircle(PointF center, PointF point) {
        if ( (center.x - point.x) * (center.x - point.x) +
                (center.y - point.y) * (center.y - point.y) <= radius * radius ) {
            return true;
        }
        return false;
    }
}
