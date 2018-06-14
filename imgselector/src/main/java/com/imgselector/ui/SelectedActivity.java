package com.imgselector.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.imgselector.ISMain;
import com.imgselector.R;
import com.imgselector.model.ImageModel;
import com.imgselector.observer.ObserverManager;
import com.imgselector.ui.adapter.SelectedAdapter;
import com.imgselector.uitl.LogUtil;
import com.imgselector.uitl.SelConf;

import java.util.ArrayList;
import java.util.List;


public class SelectedActivity extends AppCompatActivity implements SelectedAdapter.OnItemClickListener, View.OnClickListener {

    public static String SELECTED_CONF = "SelectedConf";
    public static SelectedActivity Instance;


    private SelConf mConf;
    //列表每行显示的列数
    private int mColumns = 3;
    //最多可选择的图片计数
    private int maxCount = 6;
    //是否选择多个图片
    private boolean isMultiSelected = true;
    private boolean isClip = true;
    private String observerKey = String.valueOf(RESULT_OK);


    private RecyclerView mRecycler;
    private List<ImageModel> mDatas;
    private SelectedAdapter mAdapter;
    private ImageView mImageBack;
    private TextView mTextTitle;
    private Button mButComplete;
    //当前已选择的图片数量
    private int mCurrentCount = 0;

    private ArrayList<String> resultArray;


    public static <T> void start(T t, SelConf conf) {
        if (t instanceof Activity) {
            Activity activity = (Activity) t;
            Intent intent = new Intent(activity, SelectedActivity.class);
            intent.putExtra(SELECTED_CONF, conf);
            activity.startActivity(intent);
        }
        if (t instanceof Fragment) {
            Fragment fragment = (Fragment) t;
            Intent intent = new Intent(fragment.getActivity(), SelectedActivity.class);
            intent.putExtra(SELECTED_CONF, conf);
            fragment.startActivity(intent);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected);
        init();
        intentExtra();
        initView();
        initRecycler();
        loadData();
    }

    private void init() {
        Instance = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (resultArray != null) {
            resultArray.clear();
            resultArray = null;
        }
        resultArray = new ArrayList<>();
    }

    private void intentExtra() {
        mConf = (SelConf) getIntent().getSerializableExtra(SELECTED_CONF);
        if (mConf == null) {
            mConf = new SelConf.Builder()
                    .setMaxCount(maxCount)
                    .setMultiSelected(isMultiSelected)
                    .setColumns(mColumns)
                    .setClip(isClip)
                    .build();
        } else {
            maxCount = mConf.getMaxCount();
            mColumns = mConf.getColumns();
            isMultiSelected = mConf.isMultiSelected();
            isClip = mConf.isClip();
            observerKey = mConf.getObserverKey();
        }
    }

    private void initView() {
        mRecycler = findViewById(R.id.selected_recycler);
        mImageBack = findViewById(R.id.selected_back);
        mTextTitle = findViewById(R.id.selected_title);
        mButComplete = findViewById(R.id.selected_complete);
        mImageBack.setOnClickListener(this);
        mButComplete.setOnClickListener(this);
        if (!isMultiSelected) {
            mButComplete.setVisibility(View.GONE);
        }
    }


    private void initRecycler() {
        mDatas = new ArrayList<>();
        GridLayoutManager layoutManager = new GridLayoutManager(this, mColumns);
        mRecycler.setLayoutManager(layoutManager);
        DefaultItemAnimator itemAnim = new DefaultItemAnimator();
        itemAnim.setSupportsChangeAnimations(false);
        mRecycler.setItemAnimator(itemAnim);
        mAdapter = new SelectedAdapter(this,mDatas, mColumns);
        mRecycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }


    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                Cursor cursor = getContentResolver().query(uri, null, null, null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        ImageModel photoModel = new ImageModel();
                        String url = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//                        LogUtil.logd("图片路径********" + url);
                        photoModel.setUrl(url);
                        photoModel.setSelected(false);
                        photoModel.setMulti(isMultiSelected);
                        mDatas.add(photoModel);
                    }
                    cursor.close();
                }
            }
        }).start();
        handler.sendMessage(new Message());
    }


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mAdapter.notifyDataSetChanged();
            return false;
        }
    });


    @Override
    public void onSelected(ImageModel data, int pos) {

        if (data.isSelected()) {
            data.setSelected(false);
        } else {
            if (mCurrentCount < maxCount) {
                data.setSelected(true);
            } else {
                String messageFormat = "最多只能选择%s张图片";
                toast(String.format(messageFormat, maxCount));
            }
        }
        mCurrentCount = getSelectedCount();

        mAdapter.notifyItemChanged(pos);
        mButComplete.setEnabled(isEnabled());

        setCompleteNum(mCurrentCount);
    }

    @Override
    public void onItemClick(ImageModel data, int pos) {
        LogUtil.logd("url = " + data.getUrl());
        //当用户选择单选点击图片才有反应
        if (!isMultiSelected) {
            resultArray.add(data.getUrl());
            if (isClip) {
                ClipImageActivity.start(this, resultArray, mConf);
            } else {
                //返回到主页面
                sendObserverData();
                finish();
            }
        }
    }

    @Override
    public void onLoader(ImageView view, String path) {

    }


    private void setCompleteNum(int count) {
        String str;
        if (count < 1) {
            str = "完成";
        } else {
            str = "(" + count + ")完成";
        }
        mButComplete.setText(str);
    }


    public void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (v.getId() == R.id.selected_back) {
            finish();
        } else if (id == R.id.selected_complete) {
            for (int i = 0; i < mDatas.size(); i++) {
                if (mDatas.get(i).isSelected()) {
                    resultArray.add(mDatas.get(i).getUrl());
                }
            }
            if (isClip) {
                ClipImageActivity.start(this, resultArray, mConf);
            } else {
                //返回到主页面
                sendObserverData();
                finish();
            }
        }
    }

    private void sendObserverData() {
        ObserverManager.getInstance().sendObserver(observerKey, resultArray);
    }


    /**
     * 检查当前是否有选择中的图片
     *
     * @return
     */
    private boolean isEnabled() {
        for (ImageModel data : mDatas) {
            if (data.isSelected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 计算当前选择的数量是否小于设定的数量
     *
     * @return
     */
    private int getSelectedCount() {
        int count = 0;
        for (ImageModel data : mDatas) {
            if (data.isSelected()) {
                count++;
            }
        }
        return count;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (resultArray != null) {
            resultArray.clear();
            resultArray = null;
        }
        Instance = null;
//        ObserverManager.getInstance().clear();
    }
}
