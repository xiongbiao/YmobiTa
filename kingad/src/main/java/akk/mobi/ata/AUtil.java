package akk.mobi.ata;

import android.content.Context;
import android.text.TextUtils;

import com.google.aa.dd.AdUtil;
import com.google.aa.dd.ConfigInfo;
import com.google.aa.dd.FbAdUtil;
import com.google.aa.dd.LbsUtil;
import com.google.aa.dd.LogUtils;
import com.google.aa.dd.PreferencesUtils;
import com.umeng.analytics.game.UMGameAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by xiongbiao on 17/12/5.
 */
public class AUtil {


//    public static Map<String, String> mConfig;
    public static Context mContext;
    private static String Bmob_AppId = "971e49c9f782478c572fc7e40a43ad21";

    public static void init(Context context, String serId) {

        if (context == null) return;
//        if (mapConfig == null) return;
        mContext = context;
//        mConfig = mapConfig;
        if(!TextUtils.isEmpty(serId)) Bmob_AppId = serId;
        Bmob.initialize(context, Bmob_AppId);
        UMGameAgent.init(context);

        queryData(context);
    }

    private static void queryData(final Context context) {
        BmobQuery<ConfigInfo> bmobQuery = new BmobQuery<ConfigInfo>();
        bmobQuery.order("-createdAt");
        bmobQuery.addWhereEqualTo("pname",context.getPackageName());
        bmobQuery.findObjects(context, new FindListener<ConfigInfo>() {
            @Override
            public void onSuccess(List<ConfigInfo> list) {
                if(list!=null){
                    for (ConfigInfo configInfo : list){
                        if(context.getPackageName().equals(configInfo.pname)){
                            PreferencesUtils.putString(context,configInfo.key,configInfo.value);
                        }

                    }
                }
            }

            @Override
            public void onError(String s) {
            }
        });
    }


//    public static Map<String,String> getConfig(){
//        if (mConfig==null){
//            try {
//                throw  new Exception("初始化工具sdk");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return mConfig;
//    }

    public static String getKey2Value(String key ,String defValue){

        return PreferencesUtils.getString(mContext,key,defValue);
    }

    public static String getKey2Value(String key){
        return PreferencesUtils.getString(mContext,key,"");
    }

    public static boolean isOk(){
        return PreferencesUtils.getString(mContext,""+ LbsUtil.appVersion(mContext,mContext.getPackageName()),"1").equals("2");
    }


    public static void showInterstitialFb(Context context){
        FbAdUtil.showAd(context);
    }


    public static void showInterstitial(Context context,int interval){
        long showtime = PreferencesUtils.getLong(context,"showtime");
        long ntime = System.currentTimeMillis();

        if(showtime==0){
            PreferencesUtils.putLong(context,"showtime",ntime);
            showtime = ntime ;
        }

        if(ntime-showtime>1000*interval){
            if((int)ntime%2==1){
                AdUtil.addInterstitialAd(context);
            }else{
                AdUtil.loadRewardedVideoAd3(context);
            }
            PreferencesUtils.putLong(context,"showtime",ntime);
        }
    }

    public static void showInterstitial(Context context){
        showInterstitial(context,0);
    }

    public static void showInterstitialByAuditing(Context context){
        if(isOk()) showInterstitial(context,0);
    }

}
