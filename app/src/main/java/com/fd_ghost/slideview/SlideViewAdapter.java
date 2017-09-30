package com.fd_ghost.slideview;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class SlideViewAdapter extends PagerAdapter {
    private ArrayList<View> views;

    public SlideViewAdapter() {
    }

    public SlideViewAdapter(ArrayList<View> views) {
        this.views=views;
    }

    public void addView(View view){
        views.add(view);
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position % views.size()));

    }

}
