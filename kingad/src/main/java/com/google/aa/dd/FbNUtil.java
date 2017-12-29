package com.google.aa.dd;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import akk.mobi.ata.AUtil;
import ta.aie.mobi.ymobita.R;

public class FbNUtil {

	private static Context _context;
//	private static String adkey2 = "1952627528330436_1952627891663733";
	private static String TAG = "FbNUtil";

	private static NativeAd nativeAd;
	private static LinearLayout nativeAdContainer;

	public static void showAd(final Context context,LinearLayout _nativeAdContainer) {
//		if (!MyApp.isShenhe())return;
		LogUtils.e(TAG , "showAd");
		_context = context;
		nativeAdContainer = _nativeAdContainer;
		String adUnitId = AUtil.getKey2Value("FB_N_ID");
		if(TextUtils.isEmpty(adUnitId)){
			adUnitId = "1952627528330436_1952627891663733";
		}
		LogUtils.i(TAG," Native id : " +adUnitId);
		nativeAd = new NativeAd(_context, adUnitId);
		nativeAd.setAdListener(adListener);
		nativeAd.loadAd();
	}





	static AdListener adListener =new AdListener() {
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
			if (ad != nativeAd) {
				return;
			}

			nativeAdContainer.removeAllViews();

			// Add the Ad view into the ad container.

			LayoutInflater inflater = LayoutInflater.from(_context);
			// Inflate the Ad view.  The layout referenced should be the one you created in the last step.
			LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, nativeAdContainer, false);
			nativeAdContainer.addView(adView);

			// Create native UI using the ad metadata.
			ImageView nativeAdIcon = (ImageView) adView.findViewById(R.id.native_ad_icon);
			TextView nativeAdTitle = (TextView) adView.findViewById(R.id.native_ad_title);
			MediaView nativeAdMedia = (MediaView) adView.findViewById(R.id.native_ad_media);
			TextView nativeAdSocialContext = (TextView) adView.findViewById(R.id.native_ad_social_context);
			TextView nativeAdBody = (TextView) adView.findViewById(R.id.native_ad_body);
			Button nativeAdCallToAction = (Button) adView.findViewById(R.id.native_ad_call_to_action);

			// Set the Text.
			nativeAdTitle.setText(nativeAd.getAdTitle());
			nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
			nativeAdBody.setText(nativeAd.getAdBody());
			nativeAdCallToAction.setText(nativeAd.getAdCallToAction());

			// Download and display the ad icon.
			NativeAd.Image adIcon = nativeAd.getAdIcon();
			NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

			// Download and display the cover image.
			nativeAdMedia.setNativeAd(nativeAd);

			// Add the AdChoices icon
			LinearLayout adChoicesContainer = (LinearLayout) adView.findViewById(R.id.ad_choices_container);
			AdChoicesView adChoicesView = new AdChoicesView(_context, nativeAd, true);
			adChoicesContainer.addView(adChoicesView);

			// Register the Title and CTA button to listen for clicks.
			List<View> clickableViews = new ArrayList<>();
			clickableViews.add(nativeAdTitle);
			clickableViews.add(nativeAdCallToAction);
			nativeAd.registerViewForInteraction(nativeAdContainer,clickableViews);




		    MobclickAgent.onEvent(_context, "show");
			LogUtils.d(TAG , "onAdLoaded");
//		    _interfaceAa.AdLoaded(aict.ADTYPE_FB_I);
		}
		 
		
		@Override
		public void onAdClicked(Ad arg0) {
			LogUtils.d(TAG , "onAdClicked");
//			_interfaceAa.AdClicked(aict.ADTYPE_FB_I);
		}
		

	};
 
}
