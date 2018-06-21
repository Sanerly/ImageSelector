package com.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.imgselector.ISMain;
import com.imgselector.loader.Imageloader;
import com.imgselector.observer.IObserver;
import com.imgselector.ui.adapter.ImageAdapter;
import com.imgselector.uitl.LogUtil;
import com.imgselector.uitl.SelConf;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button button;
    LinearLayout linearLayout;
    SelConf mConf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.bt_selector);
        linearLayout = findViewById(R.id.recycler);
         mConf = new SelConf.Builder()
                .setMaxCount(9)
                .setMultiSelected(true)
                .setColumns(3)
                .setClip(true  )
                .setObserver(String.valueOf(RESULT_OK), iObserver)
                .setImageloader(imageloader)
                .build();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ISMain.getInstance().startSelectedActivity(MainActivity.this, mConf);
            }
        });


    }

    IObserver iObserver = new IObserver() {
        @Override
        public void ObserverUpdate(String key, Object obj) {
            if (!key.equals(String.valueOf(RESULT_OK))) return;
            ArrayList<String> sparseArray = (ArrayList<String>) obj;
            for (int i = 0; i < sparseArray.size(); i++) {
                LogUtil.logd("key = " + key + "   value = " + sparseArray.get(i));
                ImageView imageView=new ImageView(MainActivity.this);
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                imageView.setLayoutParams(params);
                loader(MainActivity.this, imageView, sparseArray.get(i));
                final int pos = i;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> sparseArray = new ArrayList<>();
                        sparseArray.add(sparseArray.get(pos));
                        ISMain.getInstance().startClipImageActivity(MainActivity.this,sparseArray,mConf);
                    }
                });
                linearLayout.addView(imageView);
            }
        }
    };

    Imageloader imageloader = new Imageloader() {
        @Override
        public void load(Context context, ImageView image, String path) {
            loader(context, image, path);
        }

    };

    private void loader(Context context, ImageView image, String path) {
        Glide.with(context)
                .load(path)
                .error(R.mipmap.ic_default_image)
                .placeholder(R.mipmap.ic_default_image)
                .into(image);
    }


}
