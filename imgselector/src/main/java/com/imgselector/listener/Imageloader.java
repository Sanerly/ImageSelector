package com.imgselector.listener;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by sunset on 2018/6/14.
 */

public  interface Imageloader {

    void  load(Context context,ImageView image, String path);
}
