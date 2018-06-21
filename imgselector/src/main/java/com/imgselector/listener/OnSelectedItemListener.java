package com.imgselector.listener;

import com.imgselector.model.ImageModel;

/**
 * Created by sunset on 2018/6/19.
 */

public interface OnSelectedItemListener {
    /**
     * 选择图片
     * @param data
     * @param pos
     */
    void onSelected(ImageModel data, int pos);

    /**
     * 点击图片
     * @param data
     * @param pos
     */
    void onItemClick(ImageModel data, int pos);
}
