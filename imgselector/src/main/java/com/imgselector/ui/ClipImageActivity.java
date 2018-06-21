package com.imgselector.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.imgselector.R;
import com.imgselector.listener.OnClipImageItemListener;
import com.imgselector.loader.ImageManager;
import com.imgselector.observer.ObserverManager;
import com.imgselector.ui.adapter.ImageAdapter;
import com.imgselector.uitl.SelConf;
import com.imgselector.view.ClipLayout;
import com.imgselector.listener.IClipLayoutListener;

import java.util.ArrayList;
import java.util.List;

public class ClipImageActivity extends AppCompatActivity implements IClipLayoutListener, OnClipImageItemListener {


    private static String IMAGES_PATH_ARRAY = "images_path_array";
    private ClipLayout clipLayout;
    private ArrayList<String> imagesArray;
    private SelConf mConf;
    private String observerKey = String.valueOf(RESULT_OK);
    private boolean isMultiSelected = true;
    private List<String> mButtonArray;
    private ImageAdapter imagePagerAdapter;
    private int mPosition = 0;
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
        mButtonArray=new ArrayList<>();
        mButtonArray.add("取消");
        mButtonArray.add("裁剪");
    }

    private void intentExtra() {
        mConf = (SelConf) getIntent().getSerializableExtra(SelectedActivity.SELECTED_CONF);
        if (mConf == null) {
            mConf = new SelConf.Builder()
                    .build();
        } else {
            isMultiSelected = mConf.isMultiSelected();
            observerKey = mConf.getObserverKey();
        }
    }


    private void initData() {
        if (imagesArray == null || imagesArray.size() <= 0) return;
        for (String str : imagesArray) {
            clipLayout.setImageUrl(str);
        }
        if (isMultiSelected) {
            mButtonArray.add("完成") ;
            imagePagerAdapter = new ImageAdapter(this, imagesArray);
            imagePagerAdapter.setClipImageItemListener(this);
            clipLayout.setRecyclerPagerAdapter(imagePagerAdapter);
        }
        clipLayout.setButtonChildView(mButtonArray);
    }

    private void initView() {
        clipLayout = findViewById(R.id.clip_layout);
        clipLayout.setClipLayoutListener(this);
        clipLayout.setClipSize(0.7f);
    }

    /**
     * 用户点击完成按钮，返回数据，结束裁剪和选择的活动
     */
    private void onComplete() {
        ObserverManager.getInstance().sendObserver(observerKey, imagesArray);
        SelectedActivity.Instance.finish();
        finish();
    }

    @Override
    public void Imageloader(ImageView view, String path) {
        ImageManager.getInstance().load(this, view, path);
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
            case 2:
                onComplete();
                break;
        }

    }



    @Override
    public void ClipAfterPath(ImageView view, String path) {

        if (isMultiSelected) {
            //多选
            if (imagePagerAdapter!=null){
                imagePagerAdapter.notifyItemChanged(mPosition);
            }
            imagesArray.set(mPosition, path);
            clipLayout.setImageUrl(path);
            clipLayout.getClipImage().setPostCenter();
        } else {
            //单选
            imagesArray.clear();
            imagesArray.add(path);
            onComplete();
        }


    }



    @Override
    public void onItemClick(String data, int pos) {
        mPosition = pos;
        clipLayout.setImageUrl(data);
        clipLayout.getClipImage().setPostCenter();
    }
}
