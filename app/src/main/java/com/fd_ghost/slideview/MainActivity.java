package com.fd_ghost.slideview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{
    ArrayList<View> views;


    /**
     * Tab Icon index
     */
    private final int ICONS_RES[][] = {
            {
                    R.drawable.ic_home_normal,
                    R.drawable.ic_home_focus
            },
            {
                    R.drawable.ic_message_normal,
                    R.drawable.ic_message_focus
            },

            {
                    R.drawable.ic_mine_normal,
                    R.drawable.ic_mine_focus
            }
    };

    /**
     * Tab title index
     */
    private final String TITLE_RES[] = {"Home","Message","My"};

    /**
     * Tab color index
     */
    private final int[] TAB_COLORS = new int []{
            R.color.lightBlue,
            R.color.Blue};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initViews();

    }



    private void initViews() {
        views=new ArrayList<View>();
        views.add(new View(getApplicationContext()));
        views.add(new View(getApplicationContext()));
        views.add(new View(getApplicationContext()));
        SlideViewAdapter mAdapter = new SlideViewAdapter(views);
        ViewPager mPager = (ViewPager) findViewById(R.id.tab_pager);
        //Select viewpager cache number
        mPager.setOffscreenPageLimit(1);
        mPager.setAdapter(mAdapter);

        SlideContainerView mTabLayout = (SlideContainerView) findViewById(R.id.ll_tab_container);
        mTabLayout.setOnPageChangeListener(this);

        mTabLayout.initContainer(TITLE_RES, ICONS_RES, TAB_COLORS, true);

        int width = getResources().getDimensionPixelSize(R.dimen.tab_icon_width);
        int height = getResources().getDimensionPixelSize(R.dimen.tab_icon_height);
        mTabLayout.setContainerLayout(R.layout.slide_container_view, R.id.iv_tab_icon, R.id.tv_tab_text, width, height);
//        mTabLayout.setSingleTextLayout(R.layout.slide_container_view, R.id.tv_tab_text);
//        mTabLayout.setSingleIconLayout(R.layout.slide_container_view, R.id.iv_tab_icon);

        mTabLayout.setViewPager(mPager);

        mPager.setCurrentItem(getIntent().getIntExtra("tab", 0));

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }
}
