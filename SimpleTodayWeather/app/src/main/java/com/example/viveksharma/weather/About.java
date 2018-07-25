package com.example.viveksharma.weather;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

public class About extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        ImageView iv = (ImageView)findViewById(R.id.ivlogo);
        iv.setImageResource(R.mipmap.ic_launcher_my);
    }
}
