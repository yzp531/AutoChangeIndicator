package com.example.lx.myproject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.libindicator.view.AutoChangeIndicator;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private AutoChangeIndicator autoChangeIndicator;

    private ViewPager mViewPager;

    private List<String> titles = Arrays.asList(
            new String[]{"短", "很短", "我长吗", "我很长的", "不短", "我是很短的"});

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
    }

    private void findView() {
        autoChangeIndicator = (AutoChangeIndicator) findViewById(R.id.msi_content);
        mViewPager = (ViewPager) findViewById(R.id.vp_content);
        mViewPager.setAdapter(new MyPagerAdapter());
        autoChangeIndicator.setTitles(titles);
        autoChangeIndicator.setViewPager(mViewPager);
    }

    class MyPagerAdapter extends PagerAdapter {

        private View views[] = new View[6];
        private int[] colors = {Color.BLUE, Color.DKGRAY, Color.GREEN,
                Color.YELLOW, Color.LTGRAY, Color.YELLOW};

        public MyPagerAdapter() {
            for (int i = 0; i < views.length; i++) {
                TextView textView = new TextView(MainActivity.this);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(45);
                textView.setBackgroundColor(colors[i]);
                textView.setTextColor(Color.BLACK);
                textView.setText("hello" + i);
                views[i] = textView;
            }
        }

        @Override
        public int getCount() {
            return views.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views[position]);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views[position]);
            return views[position];
        }
    }

}
