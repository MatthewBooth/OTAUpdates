package com.ota.updates.utils.fontdrawing;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.util.AttributeSet;
import android.widget.Button;

import com.ota.updates.utils.constants.App;

public class FontAwesomeButton extends Button implements App {

    private final static String NAME = "FONTAWESOME";
    private static LruCache<String, Typeface> sTypefaceCache = new LruCache<>(12);


    public FontAwesomeButton(Context context) {
        super(context);
        init();

    }

    public FontAwesomeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontAwesomeButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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

