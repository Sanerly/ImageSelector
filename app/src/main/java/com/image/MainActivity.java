package com.image;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.imgselector.ui.SelectedActivity;
import com.imgselector.uitl.SelConf;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_selector).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "打开", Toast.LENGTH_SHORT).show();
                SelConf mConf = new SelConf.Builder()
                        .setMaxCount(9)
                        .setMultiSelected(false)
                        .setColumns(3)
                        .setClip(true)
                        .build();
                SelectedActivity.start(MainActivity.this, mConf,002);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==002 && resultCode==RESULT_OK){

        }
    }
}
