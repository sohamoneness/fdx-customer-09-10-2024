package com.fdxUser.app.CustomFonts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class ManropeExtraBoldTextView extends TextView {
    public ManropeExtraBoldTextView(Context context) {
        super(context);
        init();
    }

    public ManropeExtraBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ManropeExtraBoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/manrope_extrabold.ttf");
        setTypeface(tf);
        setSingleLine(true);
        setImeOptions(getImeActionId());


    }
}


