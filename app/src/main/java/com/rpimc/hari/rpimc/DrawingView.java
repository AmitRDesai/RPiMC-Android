package com.rpimc.hari.rpimc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Hari on 10/30/2015.
 */
public class DrawingView extends View {

    private static final float TOUCH_TOLERANCE = 4;
    public int width;
    public int height;
    public Canvas canvas;
    Context context;
    ArrayList<Point> points = new ArrayList<Point>();
    ArrayList<Line> lines = new ArrayList<Line>();
    int i = 0;
    private Bitmap bitmap;
    private Path path;
    private Paint bitmapPaint;
    private Paint circlePaint;
    private Path circlePath;
    private Paint paint;
    private float mX, mY;
    Handler handler;

    public DrawingView(Context context) {
        super(context);
        this.context = context;
        initilize();
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initilize();
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initilize();
    }

    private void initilize() {
        path = new Path();
        bitmapPaint = new Paint(Paint.DITHER_FLAG);
        circlePaint = new Paint();
        circlePath = new Path();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4f);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(12);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                new MessageDialog(context, "Message", "Please draw continuous line").alertDialog.show();
                canvas.drawColor(Color.WHITE);
                points = new ArrayList<Point>();
                reset();
                invalidate();
            }
        };
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
        canvas.drawPath(path, paint);
        canvas.drawPath(circlePath, circlePaint);
    }

    private void touch_start(float x, float y) {
        path.reset();
        path.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
            circlePath.reset();
            circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
        }
    }

    private void touch_up() {
        path.lineTo(mX, mY);
        circlePath.reset();
        canvas.drawPath(path, paint);
        path.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                points.add(new Point(x, y));
                i++;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                points.add(new Point(x, y));
                i++;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                new Thread(new PointsToLines()).start();
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    public ArrayList<Line> getLines() {
        correct();
        correct();
        correct();
        return lines;
    }

    public void reset() {
        lines = new ArrayList<Line>();
    }

    private void correct() {
        for (int i = 1; i < lines.size(); i++) {
            Line l1 = lines.get(i - 1);
            Line l2 = lines.get(i);
            if (l1.lenght() < 40 || l2.lenght() < 40) {
                lines.remove(i - 1);
                lines.add(i - 1, Line.combineLines(l1, l2));
                lines.remove(i);
            }
        }
        Log.i("After", lines.size() + "");
    }

    class PointsToLines extends Thread {
        @Override
        public void run() {
            try {
                int counter = 0;
                for (int j = 0; j < points.size() - 2; ) {
                    lines.add(new Line(points.get(j), points.get(j + 1)));
                    while (lines.get(counter).has(points.get(j))) {
                        if (points.size() - 1 == j) {
                            break;
                        }
                        j++;
                    }
                    j--;
                    counter++;
                }
                points = new ArrayList<Point>();
            } catch (Exception e) {
                handler.sendEmptyMessage(0);
                Log.i("AndroidRuntime", "Error Generated");
            }
        }
    }
}