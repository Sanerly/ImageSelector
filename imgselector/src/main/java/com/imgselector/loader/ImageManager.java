package com.imgselector.loader;

import android.content.Context;
import android.widget.ImageView;

import com.imgselector.ISMain;
import com.imgselector.uitl.LogUtil;


/**
 * 图片加载管理
 */
public class ImageManager {
    private  Imageloader mImageloader;
    private static ImageManager instance;

    public static ImageManager getInstance(){
        if (instance==null){
            synchronized (ISMain.class){
                if (instance==null){
                    instance=new ImageManager();
                }
            }
        }
        return instance;
    }



    /**
     * 加载图片的对外接口
     * @param imageloader
     */
    public  void setImageloader(Imageloader imageloader){
        this.mImageloader=imageloader;
    }

    /**
     * 加载图片
     * @param view  加载图片容器
     * @param path 图片路径
     */
    public  void load(Context context , ImageView view, String path){
        if (mImageloader==null){
            LogUtil.loge("没有实现加载图片的接口");
            return;
        }
        mImageloader.load(context,view,path);
    }
}
