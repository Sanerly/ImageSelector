package com.imgselector.uitl;

import com.imgselector.loader.ImageManager;
import com.imgselector.loader.Imageloader;
import com.imgselector.observer.IObserver;
import com.imgselector.observer.ObserverManager;

import java.io.Serializable;

/**
 * Created by sunset on 2018/3/30.
 */

public class SelConf implements Serializable {


    SelConf(Builder builder) {
        this.multiSelected = builder.multiSelected;
        this.maxCount = builder.maxCount;
        this.columns = builder.columns;
        this.isClip = builder.isClip;
        this.observerKey=builder.observerKey;
    }


    /**
     * 是否允许多种选择
     */
    private boolean multiSelected;

    /**
     * 多选的数量
     */
    private int maxCount;


    /**
     * 一行显示的列数
     */
    private int columns;

    /**
     * 是否裁剪图片
     */
    private boolean isClip;

    /**
     * 获取选中数据后的返回值的key
     */
    private String observerKey;



    public boolean isClip() {
        return isClip;
    }

    public boolean isMultiSelected() {
        return multiSelected;
    }


    public int getMaxCount() {
        return maxCount;
    }

    public int getColumns() {
        return columns;
    }

    public String getObserverKey() {
        return observerKey;
    }


    public static class Builder implements Serializable {

        private boolean multiSelected;

        private int maxCount;

        private int columns;

        private boolean isClip;

        private String observerKey;


        public Builder() {

        }


        /**
         * 是否允许多种选择
         */
        public Builder setMultiSelected(boolean multiSelected) {
            this.multiSelected = multiSelected;
            return this;
        }

        /**
         * 多选的数量
         */
        public Builder setMaxCount(int maxCount) {
            this.maxCount = maxCount;
            return this;
        }

        /**
         * 一行显示的列数
         */
        public Builder setColumns(int columns) {
            this.columns = columns;
            return this;
        }

        /**
         * 设置是否裁剪图片
         */
        public Builder setClip(boolean isClip) {
            this.isClip = isClip;
            return this;
        }

        /**
         * 设置选择图片成功后返回给用户的数据
         * @param key 返回数据的key
         * @param observer 返回数据的观察者
         * @return Builder
         */
        public Builder setObserver(String key, IObserver observer) {
            this.observerKey=key;
            ObserverManager.getInstance().addObserver(key,observer);
            return this;
        }

        /**
         * 设置图片加载器
         * @param imageloader
         * @return
         */
        public Builder setImageloader(Imageloader imageloader){
            ImageManager.getInstance().setImageloader(imageloader);
            return this;
        }
        public SelConf build() {
            return new SelConf(this);
        }

    }
}
