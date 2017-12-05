package com.gg.yx.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.google.aa.dd.AdUtil;


/**
 * Created by xiongbiao on 17/11/20.
 */
public class AdFbBanner extends  LinearLayout {


    public AdFbBanner(Context context) {
        super(context);
        AdUtil.addAdBanner(context,this);
    }

    public AdFbBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        AdUtil.addAdBanner(context,this);
    }

    public AdFbBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        AdUtil.addAdBanner(context,this);
    }


}
