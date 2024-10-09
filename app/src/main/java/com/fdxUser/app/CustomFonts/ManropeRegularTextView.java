package com.fdxUser.app.CustomFonts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class ManropeRegularTextView extends TextView {
    public ManropeRegularTextView(Context context) {
        super(context);
        init();
    }

    public ManropeRegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ManropeRegularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/manrope_regular.ttf");
        setTypeface(tf);
        setSingleLine(false);
        setImeOptions(getImeActionId());


    }
}



