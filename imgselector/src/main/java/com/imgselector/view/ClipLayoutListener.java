package com.imgselector.view;

import android.widget.ImageView;

/**
 * 裁剪Layout对外接口
 */
public interface ClipLayoutListener {
    /**
     * 设置加载图片的加载方式
     * @param view 图片的容器
     * @param url 图片的路径
     */
    void Imageloader(ImageView view, String url);

    /**
     * 裁剪框上方
     * @param text
     * @param position
     */
    void BottomChildClick(String text, int position);
}
