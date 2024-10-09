package com.fdxUser.app.CustomFonts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class ManropeSingleLineBoldTV extends TextView {
    public ManropeSingleLineBoldTV(Context context) {
        super(context);
        init();
    }

    public ManropeSingleLineBoldTV(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ManropeSingleLineBoldTV(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/manrope_bold.ttf");
        setTypeface(tf);
        setSingleLine(true);
        //setMaxLines(2);
        setImeOptions(getImeActionId());


    }
}
