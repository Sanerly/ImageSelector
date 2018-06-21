package com.imgselector.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Environment;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imgselector.listener.IClipLayoutListener;
import com.imgselector.uitl.ImgSelUtil;
import com.imgselector.uitl.LogUtil;

import java.io.File;
import java.util.Calendar;
import java.util.List;


public class ClipLayout extends FrameLayout implements ClipBorderView.onBorderListener {
    private Context mContext;
    /**
     * 默认图片裁剪大小
     */
    public static float SPEC_SIZE = 0.5f;
    /**
     * 图片裁剪
     */
    private ClipImageView mClipImage;
    /**
     * 图片裁剪边框
     */
    private ClipBorderView mClipBorder;
    /**
     * 多图状态下显示的图片列表
     */
    private RecyclerView mRecyclerPager;
    /**
     * 底部button父布局
     */
    private LinearLayout buttonLayout;

    private IClipLayoutListener listener;

    public ClipLayout(@NonNull Context context) {
        this(context, null);
    }

    public ClipLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        addChildView();
        setBackgroundColor(Color.BLACK);
    }

    /**
     * 组合边框和图片View
     */
    private void addChildView() {
        mClipImage = new ClipImageView(mContext);
        mClipBorder = new ClipBorderView(mContext);
        mClipBorder.setBorderListener(this);
        addView(mClipImage);
        addView(mClipBorder);
        addBottomLayout();
        setClipSize(SPEC_SIZE);

    }

    private void addBottomLayout() {
        LinearLayout bottomLayout = new LinearLayout(mContext);
        bottomLayout.setBackgroundColor(Color.TRANSPARENT);
        bottomLayout.setOrientation(LinearLayout.VERTICAL);
        FrameLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.BOTTOM;
        bottomLayout.setLayoutParams(layoutParams);
        instanceRecyclerPager();
        bottomLayout.addView(mRecyclerPager);
        bottomLayout.addView(getButtonLayout());

        this.addView(bottomLayout);
    }


    public void setRecyclerPagerAdapter(RecyclerView.Adapter adapter) {
        if (mRecyclerPager == null || adapter == null) return;
        mRecyclerPager.setAdapter(adapter);
    }

    /**
     * 实例化RecyclerPager
     */
    private void instanceRecyclerPager() {
        mRecyclerPager = new RecyclerView(mContext);
        mRecyclerPager.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        LinearLayout.LayoutParams pagerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pagerParams.gravity = Gravity.CENTER;
        mRecyclerPager.setLayoutParams(pagerParams);
    }



    /**
     * 向底部layout中添加底部按钮
     */
    private LinearLayout getButtonLayout() {
        buttonLayout = new LinearLayout(mContext);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        return buttonLayout;
    }

    /**
     * 设置底部取消、裁剪等按钮
     */
    public void setButtonChildView(final List<String> array) {
        if (buttonLayout == null) return;
        for (int i = 0; i < array.size(); i++) {
            Button button = new Button(mContext);
            button.setTextSize(15);
            button.setGravity(Gravity.CENTER);
            button.setTextColor(Color.WHITE);
            button.setText(array.get(i));
            button.setBackgroundColor(Color.TRANSPARENT);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            button.setLayoutParams(params);

            final int pos = i;
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener == null) {
                        return;
                    }
                    listener.BottomChildClick(array.get(pos), pos);
                }
            });

            buttonLayout.addView(button);
        }
    }

    @Override
    public Path getPath(RectF rf) {
        Path path = new Path();
        path.addRect(rf, Path.Direction.CW);
        return path;
    }


    /**
     * 设置图片资源
     */
    public void setImageResource(int resId) {
        if (mClipImage != null) {
            mClipImage.setImageResource(resId);
        }
    }

    /**
     * 设置图片资源，网络图片获取本地SD卡的图片
     */
    public void setImageUrl(String url) {
        if (mClipImage != null && listener != null) {
            mClipImage.setImagePath(url);
            listener.Imageloader(mClipImage, url);
        }
    }

    /**
     * 设置对外接口
     */
    public void setClipLayoutListener(IClipLayoutListener layoutListener) {
        this.listener = layoutListener;
    }

    /**
     * 设置裁剪的大小
     */
    public void setClipSize(@FloatRange(from = 0.0, to = 1.0) float rate) {
        if (mClipImage == null || mClipBorder == null) {
            return;
        }
        mClipImage.setSpec(rate);
        mClipBorder.setSpec(rate);
    }


    /**
     * 启动裁剪
     */
    public void onClip() {
        if (mClipImage == null) {
            return;
        }
        Bitmap bitmap = mClipImage.clip();
        if (bitmap == null) {
            return;
        }
        //保存图片
        String folder = Environment.getExternalStorageDirectory() + "/" + mContext.getPackageName() + "/" + "image";
        ImgSelUtil.createFolder(folder, ImgSelUtil.MODE_UNCOVER);
        String fileName = folder + File.separator + Calendar.getInstance().getTime().toString() + ".png";
        LogUtil.loge("裁剪之后的图片路径 = " + fileName);
        ImgSelUtil.createFile(fileName, ImgSelUtil.MODE_COVER);
        ImgSelUtil.saveBitmaps(mContext, bitmap, new File(fileName));
        if (listener != null) {
            listener.ClipAfterPath(mClipImage, fileName);
        }
    }


    /**
     * 对外返回图片的VIEW
     *
     * @return
     */
    public ClipImageView getClipImage() {
        return mClipImage;
    }

    /**
     * 返回图片的裁剪边框
     */
    public ClipBorderView getClipBorder() {
        return mClipBorder;
    }


}
