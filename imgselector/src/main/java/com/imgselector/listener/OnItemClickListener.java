package com.imgselector.listener;

import com.imgselector.model.ImageModel;

/**
 * Created by sunset on 2018/6/19.
 */

public interface OnItemClickListener {
    void onSelected(ImageModel data, int pos);

    void onItemClick(ImageModel data, int pos);
}
