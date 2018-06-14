package com.imgselector.listener;

import android.widget.ImageView;

/**
 * 裁剪Layout对外接口
 */
public interface IClipLayoutListener {
    /**
     * 设置加载图片的加载方式
     * @param view 图片的容器
     * @param path 图片的路径
     */
    void Imageloader(ImageView view, String path);

    /**
     * 裁剪框上方
     * @param text
     * @param position
     */
    void BottomChildClick(String text, int position);

    /**
     * 获取裁剪之后的路径
     * @param view 图片的容器
     * @param path 图片的路径
     */
    void ClipAfterPath(ImageView view, String path);
}
