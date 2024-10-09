package com.fdxUser.app.Utills;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    Context context;

    public Prefs(Context context) {
        this.context = context;
    }


    public void saveData(String key,String val){
        SharedPreferences preferences=context.getSharedPreferences("MYDATA",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(key,val);
        editor.apply();
        editor.commit();
    }
    public String getData(String key){
        SharedPreferences preferences=context.getSharedPreferences("MYDATA",Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    public void clearAllData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MYDATA", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }
}
