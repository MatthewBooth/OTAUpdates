package com.ota.updates.utils.fontdrawing;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ota.updates.utils.constants.App;

public class FontAwesomeText extends TextView implements App {
    private final static String NAME = "TEXTFONT";
    private static LruCache<String, Typeface> sTypefaceCache = new LruCache<String, Typeface>(12);

    public FontAwesomeText(Context context) {
        super(context);
        init();
    }

    public FontAwesomeText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        Typeface typeface = sTypefaceCache.get(NAME);

        if (typeface == null) {
            typeface = Typeface.createFromAsset(getContext().getAssets(), FONT_AWESOME);
            sTypefaceCache.put(NAME, typeface);
        }
        setTypeface(typeface);
    }
}
