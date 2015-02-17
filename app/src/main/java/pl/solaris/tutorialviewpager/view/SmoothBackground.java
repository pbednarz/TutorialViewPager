package pl.solaris.tutorialviewpager.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by pbednarz on 2015-02-17.
 */
public class SmoothBackground extends View {

    private int mSelectedIndicatorColor;
    private int mSelectedPosition;
    private float mSelectionOffset;
    private int[] colors;

    public SmoothBackground(Context context) {
        this(context, null);
    }

    public SmoothBackground(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        mSelectedIndicatorColor = Color.WHITE;
    }

    private static int blendColors(int color1, int color2, float ratio) {
        final float inverseRation = 1f - ratio;
        final int r = (int) ((((color1 >> 16) & 0xFF) * ratio) + (((color2 >> 16) & 0xFF) * inverseRation));
        final int g = (int) ((((color1 >> 8) & 0xFF) * ratio) + (((color2 >> 8) & 0xFF) * inverseRation));
        final int b = (int) (((color1 & 0xFF) * ratio) + ((color2 & 0xFF) * inverseRation));
        return (0xFF << 24) | (r << 16) | (g << 8) | b;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
        invalidate();
    }

    public void onViewPagerPageChanged(int position, float positionOffset) {
        mSelectedPosition = position;
        mSelectionOffset = positionOffset;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (colors != null) {
            int color = colors[mSelectedPosition];
            if (mSelectionOffset > 0f && mSelectedPosition < (colors.length - 1)) {
                int nextColor = colors[(mSelectedPosition + 1) & colors.length];
                if (color != nextColor) {
                    color = blendColors(nextColor, color, mSelectionOffset);
                }
            }
            mSelectedIndicatorColor = color;
        }
        canvas.drawColor(mSelectedIndicatorColor);
    }
}