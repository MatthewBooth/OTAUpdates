package com.ota.updates.utils.fontawesome;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import com.ota.updates.utils.Constants;

public class DrawableAwesome extends Drawable implements Constants {

    private static final float PADDING_RATIO = 0.88f;
    private final String mFontAwesomeVersion = "4.4.0";

    private final Context context;
    private final int icon;
    private final Paint paint;
    private final int width;
    private final int height;
    private final float size;
    private final int color;
    private final boolean antiAliased;
    private final boolean fakeBold;
    private final float shadowRadius;
    private final float shadowDx;
    private final float shadowDy;
    private final int shadowColor;

    public DrawableAwesome(int icon, int sizeDpi, int color,
                           boolean antiAliased, boolean fakeBold, float shadowRadius,
                           float shadowDx, float shadowDy, int shadowColor, Context context) {
        super();
        this.context = context;
        this.icon = icon;
        this.size = dpToPx(sizeDpi) * PADDING_RATIO;
        this.height = dpToPx(sizeDpi);
        this.width = dpToPx(sizeDpi);
        this.color = color;
        this.antiAliased = antiAliased;
        this.fakeBold = fakeBold;
        this.shadowRadius = shadowRadius;
        this.shadowDx = shadowDx;
        this.shadowDy = shadowDy;
        this.shadowColor = shadowColor;
        this.paint = new Paint();

        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        this.paint.setColor(this.color);
        this.paint.setTextSize(this.size);
        Typeface font = Typeface.createFromAsset(context.getAssets(), FONT_AWESOME);
        this.paint.setTypeface(font);
        this.paint.setAntiAlias(this.antiAliased);
        this.paint.setFakeBoldText(this.fakeBold);
        this.paint.setShadowLayer(this.shadowRadius, this.shadowDx, this.shadowDy, this.shadowColor);
    }

    @Override
    public int getIntrinsicHeight() {
        return height;
    }

    @Override
    public int getIntrinsicWidth() {
        return width;
    }

    @Override
    public void draw(Canvas canvas) {
        float xDiff = (width / 2.0f);
        String stringIcon = this.context.getResources().getString(icon);
        canvas.drawText(stringIcon, xDiff, size, paint);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static class DrawableAwesomeBuilder {
        private Context context;
        private int icon;
        private int sizeDpi = 32;
        private int color = Color.GRAY;
        private boolean antiAliased = true;
        private boolean fakeBold = true;
        private float shadowRadius = 0;
        private float shadowDx = 0;
        private float shadowDy = 0;
        private int shadowColor = Color.WHITE;

        public DrawableAwesomeBuilder(Context context, int icon) {
            this.context = context;
            this.icon = icon;
        }

        public void setSize(int size) {
            this.sizeDpi = size;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public void setAntiAliased(boolean antiAliased) {
            this.antiAliased = antiAliased;
        }

        public void setFakeBold(boolean fakeBold) {
            this.fakeBold = fakeBold;
        }

        public void setShadow(float radius, float dx, float dy, int color) {
            this.shadowRadius = radius;
            this.shadowDx = dx;
            this.shadowDy = dy;
            this.shadowColor = color;
        }

        public DrawableAwesome build() {
            return new DrawableAwesome(icon, sizeDpi, color, antiAliased, fakeBold,
                    shadowRadius, shadowDx, shadowDy, shadowColor, context);
        }
    }
}