package com.github.qianniancc.mcbbs.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.github.qianniancc.mcbbs.MainActivity;

public class MyPreferenceCategory extends PreferenceCategory {
    TextView tv;
    SharedPreferences preferences;

    public MyPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
        preferences= PreferenceManager.getDefaultSharedPreferences(context);

    }
    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        MainActivity.getMyHandler().sendEmptyMessage(3);
        if (view instanceof TextView) {
            tv = (TextView) view;
            String skin=preferences.getString("skin","0");
            if(skin.equalsIgnoreCase("0")){
                ((TextView) view).setTextColor(Color.GRAY);
            }else if(skin.equalsIgnoreCase("1")){
                ((TextView) view).setTextColor(Color.parseColor("#87ceeb"));
            }
        }
    }

    public TextView getTv() {
        return tv;
    }
}