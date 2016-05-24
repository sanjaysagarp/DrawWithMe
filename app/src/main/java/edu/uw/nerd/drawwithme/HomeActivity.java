package edu.uw.nerd.drawwithme;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by DANG on 5/23/2016.
 */
public class HomeActivity extends AppCompatActivity {
    public static final String FILE_INTENT = "DIRECTORY";
    public static File dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), HomeActivity.this);
        viewPager.setAdapter(fragmentAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);



        dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File root = new File(dir, "Draw With Me");
        if(!root.exists()){
            root.mkdirs();
        }
        dir = root;

    }

    public void initiateDraw(View v) {
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.putExtra(FILE_INTENT, dir);
        startActivity(intent);
    }

}