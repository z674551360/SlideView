
package com.fd_ghost.slideview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class IconView extends View {

    private Paint paint;
    private Bitmap selectedIcon;
    private Bitmap normalIcon;
    private Rect selectedRect;
    private Rect normalRect;
    private int selectedAlpha = 0;

    public IconView(Context context) {
        super(context);
    }

    public IconView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public final void init(int normal, int selected, int width, int height) {
        this.normalIcon = BitmapFactory.decodeResource(getResources(), normal);
        this.selectedIcon = BitmapFactory.decodeResource(getResources(), selected);
        this.normalRect = new Rect(0, 0, width, height);
        this.selectedRect = new Rect(0, 0, width, height);
        this.paint = new Paint(1);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.paint == null) {
            return;
        }
        this.paint.setAlpha(255 - this.selectedAlpha);
        canvas.drawBitmap(this.normalIcon, null, this.normalRect, this.paint);
        this.paint.setAlpha(this.selectedAlpha);
        canvas.drawBitmap(this.selectedIcon, null, this.selectedRect, this.paint);
    }

    /**
     * Change the alpha value
     * @param alpha alpha value
     */
    public final void changeSelectedAlpha(int alpha) {
        this.selectedAlpha = alpha;
        invalidate();
    }

    /**
     * Change the percentage of alpha value
     * @param percentage Percentage
     */
    public final void percentageChanged(float percentage) {
        changeSelectedAlpha((int) (255 * (1 - percentage)));
    }
}
