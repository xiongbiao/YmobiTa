package com.google.aa.dd;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;


import akk.mobi.ata.AUtil;


/**
 * Created by xiongbiao on 17/6/30.
 */
public class AdUtil {
    public static String TAG = LogUtils.getTag(AdUtil.class);



    public static void addAdBanner(Context context , LinearLayout linearLayout){
        String adUnitId = AUtil.getKey2Value("ADMOB_B_ID1");
        if(TextUtils.isEmpty(adUnitId)){
            adUnitId = "ca-app-pub-3695017886464038/8001477909";
        }
        LogUtils.i(TAG," Banner id : " +adUnitId);
        AdView mAdView = new AdView(context);
        mAdView.setAdUnitId(adUnitId);
        mAdView.setAdSize(AdSize.BANNER);
        AdRequest.Builder adRequest = new AdRequest.Builder();
        mAdView.loadAd(adRequest.build());
        linearLayout.addView(mAdView);

    }
    static  InterstitialAd interstitialAd;
    public static void addInterstitialAd(final Context context){

        String adUnitId = AUtil.getKey2Value("ADMOB_I_ID1");

        if(TextUtils.isEmpty(adUnitId)){
            adUnitId = "ca-app-pub-3695017886464038/4062232897";
        }
        LogUtils.i(TAG," Interstitia id : " +adUnitId);

        if(interstitialAd==null){
            interstitialAd = new InterstitialAd(context);
            interstitialAd.setAdUnitId(adUnitId);
        }

        if(!interstitialAd.isLoaded()){
            AdRequest.Builder adRequest = new AdRequest.Builder();
            interstitialAd.loadAd(adRequest.build());
            interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                interstitialAd.show();
            }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }
            });
        }else{
            interstitialAd.show();
        }


//

    }

    public static void loadRewardedVideoAd3(final Context context){

        String adUnitId = AUtil.getKey2Value("ADMOB_V_ID1");

        if(TextUtils.isEmpty(adUnitId)){
            adUnitId = "ca-app-pub-3695017886464038/2118595835";
        }

        LogUtils.i(TAG," video id : " +adUnitId);

        final RewardedVideoAd mAd = MobileAds.getRewardedVideoAdInstance(context);
        mAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            // Required to reward the user.
            @Override
            public void onRewarded(RewardItem reward) {
//                Toast.makeText(context,  "onRewarded! currency: " + reward.getType() + "  amount: " +
//                        reward.getAmount(), Toast.LENGTH_SHORT).show();
                // Reward the user.
            }

            // The following listener methods are optional.
            @Override
            public void onRewardedVideoAdLeftApplication() {
//                Toast.makeText(context, "onRewardedVideoAdLeftApplication",
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed() {
//                Toast.makeText(context,  "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int errorCode) {
//                Toast.makeText(context, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdLoaded() {
//                Toast.makeText(context,  "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
                mAd.show();
            }

            @Override
            public void onRewardedVideoAdOpened() {
//                Toast.makeText(context,  "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoStarted() {
//                Toast.makeText(context,  "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
            }
        });
        mAd.loadAd(adUnitId, new AdRequest.Builder().build());

    }



    public static void addNativeExpressAdView(Context context, final LinearLayout adView_ll) {
        if(adView_ll==null) return;
        String adUnitId = AUtil.getKey2Value("ADMOB_N_ID1");

        if(TextUtils.isEmpty(adUnitId)){
            adUnitId = "ca-app-pub-3695017886464038/5186440164";
        }
        LogUtils.i(TAG," Native id : " +adUnitId);
        if(adView_ll.getChildCount()>=1){
            adView_ll.setVisibility(View.VISIBLE);
            return;
        }

        NativeExpressAdView mNativeExpressAdView = new NativeExpressAdView(context);
        mNativeExpressAdView.setAdSize(new AdSize(320,150));
        mNativeExpressAdView.setAdUnitId(adUnitId);

        // Create an ad request.
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();

        // Optionally populate the ad request builder.
//        adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
        mNativeExpressAdView.setTag(100);
        // Add the NativeExpressAdView to the view hierarchy.
        adView_ll.addView(mNativeExpressAdView);
        adView_ll.setVisibility(View.GONE);
        // Start loading the ad.
        mNativeExpressAdView.loadAd(adRequestBuilder.build());

        mNativeExpressAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                LogUtils.e(TAG, "AdLoad onAdLoaded");
                adView_ll.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                LogUtils.e(TAG, "AdLoad onAdFailedToLoad");
            }
        });
    }

}
