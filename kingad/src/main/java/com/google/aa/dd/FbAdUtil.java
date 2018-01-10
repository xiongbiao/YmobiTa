package com.google.aa.dd;

import android.content.Context;
import android.text.TextUtils;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.umeng.analytics.MobclickAgent;

import akk.mobi.ata.AUtil;


public class FbAdUtil {

	private static Context _context;
	private static String TAG = "FbAdUtil";
	private static InterstitialAd interstitialAd;
//	private static InterfaceAa _interfaceAa;

	public static void showAd(final Context context) {
		LogUtils.e(TAG , "showAd");
		_context = context;
		String adUnitId = AUtil.getKey2Value("FB_I_ID");
		if(TextUtils.isEmpty(adUnitId)){
//			adUnitId = "544721652538371_544721875871682";
			LogUtils.i(TAG," Interstitial Id is null ");
			return;
		}

		LogUtils.i(TAG," Interstitial id : " +adUnitId);

		interstitialAd = new InterstitialAd(_context, adUnitId);
	    interstitialAd.setAdListener(adListener);
	    interstitialAd.loadAd();
	}

	static InterstitialAdListener adListener =new InterstitialAdListener() {
		@Override
		public void onLoggingImpression(Ad arg0) {
			LogUtils.d(TAG , "onLoggingImpression");
		}
		
		@Override
		public void onError(Ad ad, AdError error) {
			LogUtils.e(TAG , "onError:"+error.getErrorMessage());
//			_interfaceAa.AdFailedLoaded(aict.ADTYPE_FB_I);
		}

		@Override
		public void onAdLoaded(Ad ad) {
		    // Ad is loaded and ready to be displayed
		    // You can now display the full screen add using this code:
		    interstitialAd.show();
		    MobclickAgent.onEvent(_context, "show");
			LogUtils.d(TAG , "onAdLoaded");
//		    _interfaceAa.AdLoaded(aict.ADTYPE_FB_I);
		}
		 
		
		@Override
		public void onAdClicked(Ad arg0) {
			LogUtils.d(TAG , "onAdClicked");
//			_interfaceAa.AdClicked(aict.ADTYPE_FB_I);
		}
		
		@Override
		public void onInterstitialDisplayed(Ad arg0) {
//			LogUtil.d(TAG , "onInterstitialDisplayed");
		}
		
		@Override
		public void onInterstitialDismissed(Ad arg0) {
			LogUtils.d(TAG , "onInterstitialDismissed");
//			_interfaceAa.AdClosed(aict.ADTYPE_FB_I);
		}
	};
 
}
