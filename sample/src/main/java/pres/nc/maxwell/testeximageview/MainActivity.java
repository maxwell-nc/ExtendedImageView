package pres.nc.maxwell.testeximageview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import pres.nc.maxwell.extendedimageview.ImageViewExpander;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageViewExpander
                .extend()
                .activity(this)
                .viewId(R.id.siv_test)//控件id
                .url("https://avatars3.githubusercontent.com/u/14196813?v=3&s=460")//图片地址
                .loadingResId(R.mipmap.ic_launcher)//加载时显示的图片
                .failedLoadResId(R.mipmap.ic_launcher)//失败时显示的图片
                .minScale(0.5f)//最小缩放比例
                .maxScale(3f)//最大缩放比例
                .build()
                .execute();
    }
}
