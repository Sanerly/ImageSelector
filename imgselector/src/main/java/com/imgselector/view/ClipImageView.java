package com.imgselector.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import com.imgselector.uitl.LogUtil;

/**
 * Created by sunset on 2018/4/11.
 */

public class ClipImageView extends android.support.v7.widget.AppCompatImageView {
    private Context mContext;


    private RectF mBorderRect;
    //测试时使用的大小
    private int spec;

    private Bitmap mBitmap;

    private PointF mStartPointF, mMidPointF;
    private Matrix mSaveMatrix;
    private Matrix mCurrentMatrix;
    //平移
    private int TRANSLATE_DRAG_FLAG = 0;
    //默认
    private int NONE_FLAG = 1;
    //缩放
    private int SCALE_FLAG = 1;

    private int mFlag;
    //两个触摸点的距离
    private float calSpace;
    //初始的缩放比例
    private float mInitialScale = 1;

    public ClipImageView(Context context) {
        this(context, null);
    }

    public ClipImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        setScaleType(ScaleType.MATRIX);
        mStartPointF = new PointF();
        mMidPointF = new PointF();
        mSaveMatrix = new Matrix();
        mCurrentMatrix = new Matrix();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        //设置正方形大小
        setBorderRect(new RectF(centerX - spec, centerY - spec, centerX + spec, centerY + spec));
        setPostCenter();
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mSaveMatrix.set(mCurrentMatrix);
                mStartPointF.set(event.getX(), event.getY());
                mFlag = TRANSLATE_DRAG_FLAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                calSpace = calSpacing(event);
                boolean isArea = mBorderRect.contains(event.getX(), event.getY());
                if (calSpace > 10f) {
                    mSaveMatrix.set(mCurrentMatrix);
                    calMidPoint(mMidPointF, event);
                    mFlag = SCALE_FLAG;
                }
                break;
            case MotionEvent.ACTION_MOVE:

                if (mFlag == TRANSLATE_DRAG_FLAG) {
                    mCurrentMatrix.set(mSaveMatrix);
                    float dx = event.getX() - mStartPointF.x;
                    float dy = event.getY() - mStartPointF.y;
                    onCheckDrag(dx, dy);
                } else if (mFlag == SCALE_FLAG && event.getPointerCount() == 2) {
                    mCurrentMatrix.set(mSaveMatrix);
                    float currentMove = calSpacing(event);
                    if (currentMove > 10f) {
                        float scale = currentMove / calSpace;
                        float[] values = new float[9];
                        mCurrentMatrix.getValues(values);
                        scale = checkFitScale(scale, values);
                        mCurrentMatrix.postScale(scale, scale, mMidPointF.x, mMidPointF.y);

                    }
                }
                break;
            case MotionEvent.ACTION_UP:// 单点离开屏幕时
            case MotionEvent.ACTION_POINTER_UP:// 第二个点离开屏幕时
                mFlag = NONE_FLAG;
                break;
        }
        setImageMatrix(mCurrentMatrix);
        return true;
    }


    private float checkFitScale(float scale, float[] values) {
        if (scale * values[Matrix.MSCALE_X] > mInitialScale * 4)
            scale = mInitialScale * 4 / values[Matrix.MSCALE_X];
        if (scale * values[Matrix.MSCALE_X] < mInitialScale)
            scale = mInitialScale / values[Matrix.MSCALE_X];
        return scale;
    }

    /**
     * 拖动图片的边界检查
     *
     * @param moveX
     * @param moveY
     */
    public void onCheckDrag(float moveX, float moveY) {

        RectF rectF = getCurrentRectF();
        if (mBorderRect != null && rectF != null) {

            if (moveX > 0) {
                if (rectF.left + moveX > mBorderRect.left) {
                    moveX = mBorderRect.left - rectF.left;
                }
            } else {
                if (rectF.right + moveX < mBorderRect.right) {
                    moveX = mBorderRect.right - rectF.right;
                }
            }
            if (moveY > 0) {
                if (rectF.top + moveY > mBorderRect.top) {
                    moveY = mBorderRect.top - rectF.top;
                }
            } else {
                if (rectF.bottom + moveY < mBorderRect.bottom) {
                    moveY = mBorderRect.bottom - rectF.bottom;
                }
            }
        }

        mCurrentMatrix.postTranslate(moveX, moveY);
    }


    /**
     * 计算两个触摸点间的距离
     */
    private float calSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 计算两个触摸点的中点坐标
     * <p>
     * 公式 ：x=(x1+x2)/2  y=(y1+y2)/2
     */
    private void calMidPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * 获取图片自己的边界
     */
    private RectF getCurrentRectF() {
        if (getDrawable() == null) {
            LogUtil.logd("image resource is null");
            return null;
        }
        int w = getDrawable().getIntrinsicWidth();
        int h = getDrawable().getIntrinsicHeight();

        RectF rectF = new RectF(0, 0, w, h);
        mCurrentMatrix.mapRect(rectF);
        return rectF;
    }


    /**
     * 在屏幕中心显示,
     */
    public void setPostCenter() {
        if (mBitmap == null) {
            return;
        }
        mCurrentMatrix = new Matrix();
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        float frame = getBorderRect().right - getBorderRect().left;
        float scale;
        if (height < frame) {
            scale = frame / height;
        } else {
            scale = frame / width;
        }
        mCurrentMatrix.postScale(scale, scale);
        mCurrentMatrix.postTranslate(Math.round(getWidth() / 2 - (width * scale * 0.5f)), Math.round(getHeight() / 2 - (height * scale * 0.5f)));
        setImageMatrix(mCurrentMatrix);
        mInitialScale = scale;
    }

    /***
     * 获取边框的边界
     * @return
     */
    public RectF getBorderRect() {
        return mBorderRect;
    }

    /***
     * 设置边框的边界
     * @return
     */
    public void setBorderRect(RectF mBorderRect) {
        this.mBorderRect = mBorderRect;
    }

    /**
     * 设置需要裁剪的大小
     *
     * @param rate
     */
    public void setSpec(@FloatRange(from = 0.0, to = 1.0) float rate) {
        DisplayMetrics dm = mContext.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        spec = (int) (screenWidth / 2 * rate);
    }

    /**
     * 裁剪图片
     */
    public Bitmap clip() {
        if (mBitmap == null) {
            LogUtil.loge("bitmap resource is null");
            return null;
        }

        //以下所有步骤的思路，均是将点或者大小还原到加载图片大小比例后，再进行处理。
        float[] values = new float[9];
        mCurrentMatrix.getValues(values);
        float scale = values[Matrix.MSCALE_X];
        //获取裁剪区域的实际长宽==裁剪框的大小
        int clipWidth = (int) (getClipWidth());
        int clipHeight = (int) (getClipHeight());
        RectF imageRectF = getCurrentRectF();
        float curWidth = imageRectF.width();
        float curHeight = imageRectF.height();
        //重新计算得出最终裁剪起始点
        int clipLeft = (int) (((int) (curWidth / 2 - clipWidth / 2 - getActuallyScrollX())) / scale);
        int clipTop = (int) (((int) (curHeight / 2 - clipHeight / 2 - getActuallyScrollY())) / scale);


        //其中width与height是最终实际裁剪的图片大小,saveBitmap就是最终裁剪的图片
        Bitmap saveBitmap = Bitmap.createBitmap(clipWidth, clipHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(saveBitmap);
        //当裁剪超出图片边界，超出区域以颜色填充
        canvas.drawColor(Color.RED);
        //计算显示与实际裁剪的大小
        int showRight = clipWidth;
        int showBottom = clipHeight;
        int cropRight = (int) (clipLeft + clipWidth / scale);
        int cropBottom = (int) (clipTop + clipHeight / scale);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        Rect cropRect = new Rect(clipLeft, clipTop, cropRight, cropBottom);
        Rect showRect = new Rect(0, 0, showRight, showBottom);
        canvas.drawBitmap(mBitmap, cropRect, showRect, new Paint());
        return saveBitmap;
    }


    /**
     * 获取移动图片后的偏移量
     */
    private float getActuallyScrollY() {
        return getScrollY() + getCenterOffsetScrollY();
    }

    /**
     * 获取移动图片后的偏移量
     */
    private float getActuallyScrollX() {
        return getScrollX() + getCenterOffsetScrollX();
    }


    /**
     * 获取裁剪框的中心点和图片的中心的偏移量
     *
     * @return X 轴的偏移量
     */
    private float getCenterOffsetScrollX() {
        int sourceCenterX = getWidth() / 2;
        int afterCenterX = (int) getCurrentRectF().centerX();
        return (float) (afterCenterX - sourceCenterX);
    }


    /**
     * 获取裁剪框的中心点和图片的中心的偏移量
     *
     * @return Y 轴的偏移量
     */
    private float getCenterOffsetScrollY() {
        double sourceCenterY = getHeight() / 2;
        double afterCenterY = getCurrentRectF().centerY();
        return (float) (afterCenterY - sourceCenterY);
    }

    /**
     * 获取实际裁剪的宽度
     */
    private float getClipWidth() {
        if (getBorderRect() == null) {
            return 0;
        }
        return getBorderRect().width();
    }

    /**
     * 获取实际裁剪的高度
     */
    private float getClipHeight() {
        if (getBorderRect() == null) {
            return 0;
        }
        return getBorderRect().height();
    }

    public void setImagePath(String path) {
        mBitmap = BitmapFactory.decodeFile(path);

    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}
