package com.imgselector.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.imgselector.R;
import com.imgselector.uitl.ImgSelUtil;
import com.imgselector.uitl.LogUtil;
import com.imgselector.view.ClipLayout;
import com.imgselector.view.ClipLayoutListener;

public class ClipImageActivity extends AppCompatActivity implements ClipLayoutListener {
    private static String IMAGE_PATH = "image_path";
    private ClipLayout clipLayout;
    public static void start(Context context, String imagePath) {
        Activity activity = (Activity) context;
        Intent intent = new Intent(activity, ClipImageActivity.class);
        intent.putExtra(IMAGE_PATH, imagePath);
        activity.startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_clip);
        clipLayout = findViewById(R.id.clip_layout);
        String path = getIntent().getStringExtra(IMAGE_PATH);
        clipLayout.setClipLayoutListener(this);
        clipLayout.setImageUrl(path);
        clipLayout.setClipSize(0.7f);
    }

    @Override
    public void Imageloader(ImageView view, String url) {
        LogUtil.logd("------url-----" + url);
        ImgSelUtil.load(view, url);
    }

    @Override
    public void BottomChildClick(String text, int position) {
        switch (position) {
            case 0:
                finish();
                break;
            case 1:
                clipLayout.onClip();
                finish();
                break;
        }

    }
}
