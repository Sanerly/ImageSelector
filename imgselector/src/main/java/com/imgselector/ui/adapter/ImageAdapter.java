package com.imgselector.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.imgselector.R;
import com.imgselector.listener.OnClipImageItemListener;
import com.imgselector.loader.ImageManager;
import com.imgselector.uitl.ImgSelUtil;

import java.util.List;

/**
 * Created by sunset on 2018/3/21.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private List<String> mDatas;
    private OnClipImageItemListener listener;
    private Context mContext;

    public ImageAdapter(Context context, List<String> mDatas) {
        this.mDatas = mDatas;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_pager, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final String data = mDatas.get(position);
        ImgSelUtil.showImageLayoutMeasure(holder.mImage, 4);
        ImageManager.getInstance().load(mContext, holder.mImage, data);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(data, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImage;

        ViewHolder(View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.item_pager_image);
        }


    }


    public void setClipImageItemListener(OnClipImageItemListener imageItemListener) {
        this.listener = imageItemListener;
    }

}
