package com.gg.yx.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.google.aa.dd.FbNUtil;


/**
 * Created by xiongbiao on 17/11/20.
 */
public class AdFbNative extends  LinearLayout {


    public AdFbNative(Context context) {
        super(context);
        FbNUtil.showAd(context,this);
    }

    public AdFbNative(Context context, AttributeSet attrs) {
        super(context, attrs);
        FbNUtil.showAd(context,this);
    }

    public AdFbNative(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        FbNUtil.showAd(context,this);
    }


}
