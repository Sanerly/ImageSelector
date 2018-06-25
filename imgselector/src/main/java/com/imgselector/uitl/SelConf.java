package com.imgselector.uitl;

import com.imgselector.loader.ImageManager;
import com.imgselector.loader.Imageloader;
import com.imgselector.observer.IObserver;
import com.imgselector.observer.ObserverManager;

import java.io.Serializable;

/**
 *@介绍： 传递设置的参数
 *@作者： sunset
 *@日期： 2018/3/30
 */

public class SelConf implements Serializable {


    SelConf(Builder builder) {
        this.multiSelected = builder.multiSelected;
        this.maxCount = builder.maxCount;
        this.columns = builder.columns;
        this.isClip = builder.isClip;
        this.observerKey=builder.observerKey;
        this.borderSize=builder.borderSize;
        this.toolbarBackground=builder.toolbarBackground;
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

    /**
     * 边框尺寸
     */
    private float borderSize;

    /**
     *选择页面顶部背景颜色
     */
    private int toolbarBackground;

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

    public float getBorderSize() {
        return borderSize;
    }

    public int getToolbarBackground() {
        return toolbarBackground;
    }

    public static class Builder implements Serializable {

        private boolean multiSelected;

        private int maxCount;

        private int columns;

        private boolean isClip;

        private String observerKey;

        private float borderSize;

        private int toolbarBackground;
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
         * @param imageloader 图片加载接口
         * @return Builder
         */
        public Builder setImageloader(Imageloader imageloader){
            ImageManager.getInstance().setImageloader(imageloader);
            return this;
        }

        /**
         * 边框尺寸
         * @param borderSize 浮点类型
         * @return Builder
         */
        public Builder setBorderSize(float borderSize) {
            this.borderSize = borderSize;
            return this;
        }

        /**
         * 设置选择页面顶部背景颜色
         * @param toolbarBackground color资源
         */
        public Builder setToolbarBackground(int toolbarBackground) {
            this.toolbarBackground = toolbarBackground;
            return this;
        }

        public SelConf build() {
            return new SelConf(this);
        }

    }
}
