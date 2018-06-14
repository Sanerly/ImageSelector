package com.imgselector.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.imgselector.ISMain;
import com.imgselector.R;
import com.imgselector.listener.Imageloader;
import com.imgselector.observer.ObserverManager;
import com.imgselector.uitl.ImgSelUtil;
import com.imgselector.uitl.LogUtil;
import com.imgselector.uitl.SelConf;
import com.imgselector.view.ClipLayout;
import com.imgselector.listener.IClipLayoutListener;

import java.util.ArrayList;

public class ClipImageActivity extends AppCompatActivity implements IClipLayoutListener {


    private static String IMAGES_PATH_ARRAY = "images_path_array";
    private ClipLayout clipLayout;
    private ArrayList<String> imagesArray;

    private SelConf mConf;
    private String observerKey = String.valueOf(RESULT_OK);
    private boolean isMultiSelected = true;
    private boolean isClip = true;


    public static <T> void start(T t, ArrayList<String> imagesArray, SelConf conf) {
        if (t instanceof Activity) {
            Activity activity = (Activity) t;
            Intent intent = new Intent(activity, ClipImageActivity.class);
            intent.putExtra(IMAGES_PATH_ARRAY, imagesArray);
            intent.putExtra(SelectedActivity.SELECTED_CONF, conf);
            activity.startActivity(intent);
        }
        if (t instanceof Fragment) {
            Fragment fragment = (Fragment) t;
            Intent intent = new Intent(fragment.getActivity(), ClipImageActivity.class);
            intent.putExtra(IMAGES_PATH_ARRAY, imagesArray);
            intent.putExtra(SelectedActivity.SELECTED_CONF, conf);
            fragment.startActivity(intent);
        }
    }

//    public static void start(Context context, ArrayList<String> imagesArray, SelConf conf) {
//        Activity activity = (Activity) context;
//        Intent intent = new Intent(activity, ClipImageActivity.class);
//        intent.putExtra(IMAGES_PATH_ARRAY, imagesArray);
//        intent.putExtra(SelectedActivity.SELECTED_CONF, conf);
//        activity.startActivity(intent);
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_clip);
        init();
        intentExtra();
        initView();
        initData();
    }


    private void init() {
        imagesArray = new ArrayList<>();
        imagesArray = getIntent().getStringArrayListExtra(IMAGES_PATH_ARRAY);
    }

    private void intentExtra() {
        mConf = (SelConf) getIntent().getSerializableExtra(SelectedActivity.SELECTED_CONF);
        if (mConf == null) {
            mConf = new SelConf.Builder()
                    .build();
        } else {
            isMultiSelected = mConf.isMultiSelected();
            isClip = mConf.isClip();
            observerKey = mConf.getObserverKey();
        }
    }

    private void initData() {
        if (imagesArray == null || imagesArray.size() <= 0) return;
        clipLayout.setImageUrl(imagesArray.get(0));
        clipLayout.setClipSize(0.7f);
    }

    private void initView() {
        clipLayout = findViewById(R.id.clip_layout);
        clipLayout.setClipLayoutListener(this);
    }

    @Override
    public void Imageloader(ImageView view, String path) {
//        LogUtil.logd("------url-----" + path);
//        ImgSelUtil.load(view, path);
        ISMain.getInstance().load(this,view,path);
    }

    @Override
    public void BottomChildClick(String text, int position) {
        switch (position) {
            case 0:
                finish();
                break;
            case 1:
                clipLayout.onClip();
                break;
        }

    }

    @Override
    public void ClipAfterPath(ImageView view, String path) {

        if (isMultiSelected) {
            //多选
//            ImgSelUtil.load(view, path);
            ISMain.getInstance().load(this,view,path);
            clipLayout.getmClipImage().setImagePath(path);
            clipLayout.getmClipImage().setPostCenter();
        } else {
            //单选
            imagesArray.clear();
            imagesArray.add(path);
            ObserverManager.getInstance().sendObserver(observerKey, imagesArray);
            SelectedActivity.Instance.finish();
            finish();
        }


    }

}
