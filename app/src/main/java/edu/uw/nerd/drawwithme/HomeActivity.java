package edu.uw.nerd.drawwithme;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by DANG on 5/23/2016.
 */
public class HomeActivity extends AppCompatActivity {
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
//
//        Button new_btn = (Button) getActivity().findViewById(R.id.new_draw_btn);
//        new_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                intent.putExtra("edu.uw.nerd.drawwithme", "New Drawing Initiated");
//                startActivity(intent);
//            }
//        });
    }

    public void initiateDraw(View v) {
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.putExtra("edu.uw.nerd.drawwithme", "New Drawing Initiated");
        startActivity(intent);
    }

}
