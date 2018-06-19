package com.imgselector.loader;

import android.content.Context;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by sunset on 2018/6/14.
 */

public  interface Imageloader extends Serializable {

    void  load(Context context,ImageView image, String path);
}
