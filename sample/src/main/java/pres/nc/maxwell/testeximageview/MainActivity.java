package pres.nc.maxwell.testeximageview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pres.nc.maxwell.extendedimageview.ExImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExImageView exImageView = (ExImageView) findViewById(R.id.eiv_test);
        exImageView.setWebImage("https://avatars3.githubusercontent.com/u/14196813?v=3&s=460");

    }
}
