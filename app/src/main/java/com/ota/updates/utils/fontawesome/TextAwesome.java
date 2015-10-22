package com.ota.updates.utils.fontawesome;
/**
 * https://github.com/bperin/FontAwesomeAndroid
 */

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ota.updates.utils.Constants;


public class TextAwesome extends TextView implements Constants {

    private final static String NAME = "FONTAWESOME";
    private static LruCache<String, Typeface> sTypefaceCache = new LruCache<String, Typeface>(12);

    public TextAwesome(Context context) {
        super(context);
        init();

    }

    public TextAwesome(Context context, AttributeSet attrs) {
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