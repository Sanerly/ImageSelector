package com.imgselector.uitl;

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


    public static class Builder implements Serializable {

        private boolean multiSelected;

        private int maxCount;

        private int columns;

        private boolean isClip;

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


        public Builder setClip(boolean isClip) {
            this.isClip = isClip;
            return this;
        }

        public SelConf build() {
            return new SelConf(this);
        }

    }
}
