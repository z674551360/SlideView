package com.fd_ghost.slideview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SlideContainerView extends LinearLayout {

    private ViewPager viewPager;
    private ViewPager.OnPageChangeListener viewPagerPageChangeListener;

    /**
     * Normal text color
     */
    private int textNormalColor;
    /**
     * Selected text color
     */
    private int textSelectedColor;

    /**
     * Preview position
     */
    private int previewPosition;
    /**
     * Selected position
     */
    private int selectedPosition;
    /**
     * Moving position
     */
    private float movingPosition;

    /**
     * Slide title index
     */
    private String[] slideTitles;
    /**
     * Slide icon index
     */
    private int[][] slideIcons;

    /**
     * Slide view index
     */
    private View[] slideViews;

    /**
     * Layout id
     */
    private int layoutId;
    /**
     * textView id
     */
    private int textViewId;
    /**
     * iconView id
     */
    private int iconVIewId;

    /**
     * Icon width
     */
    private int iconWidth;
    /**
     * Icon hight
     */
    private int iconHeight;

    /**
     * Show transition color
     */
    private boolean showTransitionColor = true;

    public SlideContainerView(Context context) {
        this(context, null);
    }

    public SlideContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initContainer(String[] titles, int[][] iconsRes, int[] colors, boolean showTransitionColor) {
        this.slideTitles = titles;
        this.slideIcons = iconsRes;
        this.textNormalColor = getResources().getColor(colors[0]);
        this.textSelectedColor = getResources().getColor(colors[1]);
        this.showTransitionColor = showTransitionColor;
    }

    /**
     * Set layout and layout ids
     * @param layout layout id
     * @param iconId iconView id, hide when id <=0
     * @param textId textView id, hide when id <=0
     * @param width  icon width
     * @param height icon height
     */
    public void setContainerLayout(int layout, int iconId, int textId, int width, int height) {
        layoutId = layout;
        textViewId = textId;
        iconVIewId = iconId;
        iconWidth = width;
        iconHeight = height;
    }

    /**
     * Set layout and layout ids -- Text only
     * @param layout layout id
     * @param textId textView id
     * @param width  icon width
     * @param height icon height
     */
    public void setSingleTextLayout(int layout, int textId, int width, int height) {
        setContainerLayout(layout, 0, textId, width, height);
    }

    /**
     * Set layout and layout ids -- Icon only
     * @param layout layout id
     * @param iconId iconView id
     * @param width  icon width
     * @param height icon height
     */
    public void setSingleIconLayout (int layout, int iconId, int width, int height) {
        setContainerLayout(layout, iconId, 0, width,  height);
    }

    public void setViewPager(ViewPager viewPager) {
        removeAllViews();
        this.viewPager = viewPager;
        if (viewPager != null && viewPager.getAdapter() != null) {
            viewPager.addOnPageChangeListener(new InternalViewPagerListener());
            addTabViewToContainer();
        }
    }

    /**
     * Add slide view to the container
     */
    private void addTabViewToContainer() {
        final PagerAdapter adapter = viewPager.getAdapter();
        slideViews = new View[adapter.getCount()];

        for (int index = 0, len = adapter.getCount(); index < len; index++) {

            final View tabView = LayoutInflater.from(getContext()).inflate(layoutId, this, false);
            slideViews[index] = tabView;

            /* iconView initialization */
            IconView iconView = null;
            if (iconVIewId > 0) {
                iconView = tabView.findViewById(iconVIewId);
                iconView.init(slideIcons[index][0], slideIcons[index][1], iconWidth, iconHeight);
            }

            /* textView initialization */
            TextView textView = null;
            if (textViewId > 0) {
                textView = tabView.findViewById(textViewId);
                textView.setText(slideTitles[index]);

            }

            /* Set the width for each part to divide the layout */
            LayoutParams lp = (LayoutParams) tabView.getLayoutParams();
            lp.width = 0;
            lp.weight = 1;

            /* Add the click event */
            addTabOnClickListener(tabView, index);

            /* Set up current status */
            if (index == viewPager.getCurrentItem()) {
                if (iconView != null) {
                    iconView.percentageChanged(0);
                }
                tabView.setSelected(true);
                if (textView != null) {
                    textView.setTextColor(textSelectedColor);
                }
            }

            addView(tabView);
        }
    }

    /**
     *  viewPager OnPageChangeListener
     */
    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            onViewPagerPageChanged(position, positionOffset);

            if (viewPagerPageChangeListener != null) {
                viewPagerPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {

            /* Update the color for textView and iconView when slide complete*/
            for (int i = 0; i < getChildCount(); i++) {
                if (iconVIewId > 0) {
                    ((IconView) slideViews[i].findViewById(iconVIewId)).percentageChanged(position == i ? 0 : 1);
                }
                if (textViewId > 0) {
                    ((TextView) slideViews[i].findViewById(textViewId)).setTextColor(position == i ? textSelectedColor : textNormalColor);
                }
            }

            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                onViewPagerPageChanged(position, 0f);
            }

            for (int i = 0, size = getChildCount(); i < size; i++) {
                getChildAt(i).setSelected(position == i);
            }


            if (viewPagerPageChangeListener != null) {
                viewPagerPageChangeListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;

            if (viewPagerPageChangeListener != null) {
                viewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    }

    /**
     * Update the container when viewpager slide complete
     *
     * @param position       current position
     * @param movingPosition moving position
     */
    private void onViewPagerPageChanged(int position, float movingPosition) {
        this.selectedPosition = position;
        this.movingPosition = movingPosition;
        if (movingPosition == 0f && previewPosition != selectedPosition) {
            previewPosition = selectedPosition;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int childCount = getChildCount();
        if (childCount > 0) {
            /*Draw the new position when moving*/
            if (movingPosition > 0f && selectedPosition < (getChildCount() - 1) && showTransitionColor) {

                /*get current slide and next slide */
                View selectedTab = getChildAt(selectedPosition);
                View nextTab = getChildAt(selectedPosition + 1);

                /*Refresh alpha for both preview and new icon*/
                if (iconVIewId > 0) {
                    View selectedIconView = selectedTab.findViewById(iconVIewId);
                    View nextIconView = nextTab.findViewById(iconVIewId);

                    //draw icon alpha
                    if (selectedIconView instanceof IconView && nextIconView instanceof IconView) {
                        ((IconView) selectedIconView).percentageChanged(movingPosition);
                        ((IconView) nextIconView).percentageChanged(1 - movingPosition);
                    }
                }

                 /* Show slide text,update alpha*/
                if (textViewId > 0) {
                    View selectedTextView = selectedTab.findViewById(textViewId);
                    View nextTextView = nextTab.findViewById(textViewId);

                    //draw text color
                    Integer selectedColor = (Integer) evaluate(movingPosition, textSelectedColor, textNormalColor);
                    Integer nextColor = (Integer) evaluate(1 - movingPosition, textSelectedColor, textNormalColor);

                    if (selectedTextView instanceof TextView && nextTextView instanceof TextView) {
                        ((TextView) selectedTextView).setTextColor(selectedColor);
                        ((TextView) nextTextView).setTextColor(nextColor);
                    }
                }

            }
        }
    }

    /**
     * slide item click event,switch to corresponding view
     * @param view        View
     * @param position position
     */
    private void addTabOnClickListener(View view, final int position) {
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(position, false);
            }
        };
        view.setOnClickListener(listener);
    }


    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        viewPagerPageChangeListener = listener;
    }

    /**
     * This function returns the calculated in-between value for a color
     * given integers that represent the start and end values in the four
     * bytes of the 32-bit int. Each channel is separately linearly interpolated
     * and the resulting calculated values are recombined into the return value.
     *
     * @param fraction The fraction from the starting to the ending values
     * @param startValue A 32-bit int value representing colors in the
     * separate bytes of the parameter
     * @param endValue A 32-bit int value representing colors in the
     * separate bytes of the parameter
     * @return A value that is calculated to be the linearly interpolated
     * result, derived by separating the start and end values into separate
     * color channels and interpolating each one separately, recombining the
     * resulting values in the same way.
     */
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (startA + (int)(fraction * (endA - startA))) << 24 |
                (startR + (int)(fraction * (endR - startR))) << 16 |
                (startG + (int) (fraction * (endG - startG))) << 8 |
                (startB + (int)(fraction * (endB - startB)));
    }

    public int getTextSelectedColor() {
        return textSelectedColor;
    }

    public void setTextSelectedColor(int textSelectedColor) {
        this.textSelectedColor = textSelectedColor;
    }

    public int getTextNormalColor() {
        return textNormalColor;
    }

    public void setTextNormalColor(int textNormalColor) {
        this.textNormalColor = textNormalColor;
    }

    public int getPreviewPosition() {
        return previewPosition;
    }

    public void setPreviewPosition(int previewPosition) {
        this.previewPosition = previewPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public float getMovingOffset() {
        return movingPosition;
    }

    public void setMovingPosition(float selectionOffset) {
        this.movingPosition = selectionOffset;
    }

    public String[] getTitles() {
        return slideTitles;
    }

    public void setTitles(String[] titles) {
        this.slideTitles = titles;
    }

    public int[][] getIconRes() {
        return slideIcons;
    }

    public void setIconRes(int[][] iconRes) {
        this.slideIcons = iconRes;
    }

    public View[] getSlideViews() {
        return slideViews;
    }

    public void setSlideViews(View[] tabViews) {
        this.slideViews = tabViews;
    }

    public boolean isShowTransitionColor() {
        return showTransitionColor;
    }

    public void setShowTransitionColor(boolean showTransitionColor) {
        this.showTransitionColor = showTransitionColor;
    }
}
