package com.imgselector.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;


/**
 * 裁剪边框
 */
public class ClipBorderView extends View {
    public ClipBorderView(Context context) {
        this(context, null);
    }

    public ClipBorderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipBorderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private Context mContext;
    private onBorderListener onBorderListener;

    private int spec;
    private Paint mPaint;
    private RectF mRectF;

    private void init(Context context) {
        this.mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setStyle(Paint.Style.FILL);
//       setSpec(ClipLayout.SPEC_SIZE);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Path path = null;
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        mRectF = new RectF(centerX - spec, centerY - spec, centerX + spec, centerY + spec);
        if (onBorderListener != null) {
            path = onBorderListener.getPath(mRectF);
        }
        if (path == null || onBorderListener == null) {
            path = new Path();
            path.addOval(mRectF, Path.Direction.CW);
        }
        canvas.save();
        canvas.clipPath(path, Region.Op.DIFFERENCE);
        //绘画半透明遮罩
        canvas.drawColor(Color.parseColor("#90000000"));
        //还原画布状态
        canvas.restore();

        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }


    public void setSpec(@FloatRange(from = 0.0, to = 1.0) float rate) {
        DisplayMetrics dm = mContext.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        spec = (int) (screenWidth / 2 * rate);
        invalidate();
    }

    public void setBorderListener(onBorderListener listener) {
        this.onBorderListener = listener;
    }

    public interface onBorderListener {
        Path getPath(RectF rf);
    }
}
