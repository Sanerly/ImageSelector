package com.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.imgselector.ISMain;
import com.imgselector.loader.Imageloader;
import com.imgselector.observer.IObserver;
import com.imgselector.uitl.LogUtil;
import com.imgselector.uitl.SelConf;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button button;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.bt_selector);
        imageView = findViewById(R.id.iv_view);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelConf mConf = new SelConf.Builder()
                        .setMaxCount(9)
                        .setMultiSelected(false)
                        .setColumns(3)
                        .setClip(true)
                        .setObserver(String.valueOf(RESULT_OK), iObserver)
                        .setImageloader(imageloader)
                        .build();
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
                Bitmap bitmap = BitmapFactory.decodeFile(sparseArray.get(i));
                imageView.setImageBitmap(bitmap);
            }
        }
    };

    Imageloader imageloader = new Imageloader() {
        @Override
        public void load(Context context, ImageView image, String path) {
            Glide.with(context)
                    .load(path)
                    .error(R.mipmap.ic_default_image)
                    .placeholder(R.mipmap.ic_default_image)
                    .into(image);
        }

    };


}
