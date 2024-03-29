package com.imgselector;

import com.imgselector.ui.ClipImageActivity;
import com.imgselector.ui.SelectedActivity;
import com.imgselector.uitl.SelConf;

import java.util.ArrayList;

/**
 * Created by sunset on 2018/6/14.
 */

public class ISMain {

    private static ISMain instance;

    public static ISMain getInstance(){
        if (instance==null){
            synchronized (ISMain.class){
                if (instance==null){
                    instance=new ISMain();
                }
            }
        }
        return instance;
    }

    /**
     * 进入图片选择模式
     * @param t 上下文
     * @param conf 选择图片的设置
     * @param <T> 上下文
     */
    public  <T> void startSelectedActivity(T t, SelConf conf) {
        SelectedActivity.start(t, conf);
    }

    /**
     * 进入图片裁剪模式
     * @param t 上下文
     * @param resultArray 图片路径集合
     * @param conf 裁剪图片的设置
     * @param <T> 上下文
     */
    public  <T> void startClipImageActivity(T t, ArrayList<String> resultArray,SelConf conf) {
        ClipImageActivity.start(t, resultArray, conf);
    }

}
