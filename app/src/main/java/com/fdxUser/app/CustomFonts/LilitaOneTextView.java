package com.fdxUser.app.CustomFonts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class LilitaOneTextView extends TextView {

    public LilitaOneTextView(Context context) {
        super(context);
        init();
    }

    public LilitaOneTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LilitaOneTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/lilitaone_regular.ttf");
        setTypeface(tf);
        setSingleLine(true);
        setImeOptions(getImeActionId());


    }
}
