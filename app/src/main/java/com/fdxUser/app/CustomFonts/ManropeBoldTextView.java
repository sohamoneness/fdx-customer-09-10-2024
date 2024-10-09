package com.fdxUser.app.CustomFonts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class ManropeBoldTextView extends TextView {
    public ManropeBoldTextView(Context context) {
        super(context);
        init();
    }

    public ManropeBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ManropeBoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/manrope_bold.ttf");
        setTypeface(tf);
        setSingleLine(false);
        setMaxLines(2);
        setImeOptions(getImeActionId());


    }
}

