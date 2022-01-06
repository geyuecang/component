package com.gc.sam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.gc.sam.banner.BannerDTO;
import com.gc.sam.banner.VVHomeBannerView;
import com.gc.sam.banner.VVHomeLooperViewHolder;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private VVHomeBannerView bannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}