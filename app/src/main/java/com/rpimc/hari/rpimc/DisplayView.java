package com.rpimc.hari.rpimc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Amit on 20-Mar-16.
 */
public class DisplayView extends View {

    private Paint paint;
    private ArrayList<Line> lines;
    float minX, minY, maxX, maxY;

    public DisplayView(Context context) {
        super(context);
        initilize();
    }

    public DisplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initilize();
    }

    public DisplayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initilize();
    }

    private void initilize() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(12);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    boolean first = true;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!first) {
            for (int i = 0; i < lines.size(); i++) {
                canvas.drawLine(lines.get(i).p1.x, lines.get(i).p1.y, lines.get(i).p2.x, lines.get(i).p2.y, paint);
            }
        }
        first = false;
    }

    public void setLines(ArrayList<Line> lines) {
        this.lines = lines;
//        this.maxX = maxX;
//        this.minX = minX;
//        this.maxY = maxY;
//        this.minX = minY;
    }
}
