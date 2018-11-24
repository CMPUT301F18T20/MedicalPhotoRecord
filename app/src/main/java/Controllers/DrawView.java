package Controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

//Created by User Bahu from
// https://stackoverflow.com/questions/36812567/android-draw-text-on-imageview-with-finger-touch
//TODO proper source citation

public class DrawView extends android.support.v7.widget.AppCompatImageView {
    private int color = Color.parseColor("#FF0000");
    private float width = 10f;
    private List<Holder> holderList = new ArrayList<Holder>();

    private class Holder {
        Path path;
        Paint paint;

        Holder(int color,float width){
            path = new Path();
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(width);
            paint.setColor(color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
        }
    }
    public DrawView(Context context) {
        super(context);
        init();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        holderList.add(new Holder(color, width));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Holder holder : holderList) {
            canvas.drawPath(holder.path, holder.paint);
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                holderList.add(new Holder(color,width));
                holderList.get(holderList.size() - 1).path.moveTo(eventX, eventY);
                return true;
            case MotionEvent.ACTION_MOVE:
                holderList.get(holderList.size() - 1).path.lineTo(eventX, eventY);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void resetPaths() {
        for (Holder holder : holderList) {
            holder.path.reset();
        }
        invalidate();
    }

    public void setBrushColor(int color) {
        this.color = color;
    }

    public void setWidth(float width) {
        this.width = width;
    }
}

