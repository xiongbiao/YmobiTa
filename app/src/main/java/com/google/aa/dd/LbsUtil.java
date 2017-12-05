package com.google.aa.dd;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ta.aie.mobi.ymobita.R;


public class LbsUtil {
	public static final String PREFS_NAME = "PrefsFile";
    private static final String TAG = LogUtils.getTag(LbsUtil.class);
    private static String[] SU_COMMAD = { "/system/xbin/which", "su" };
	public static final SimpleDateFormat sdf_hm = new SimpleDateFormat("yyyyMMddHHmm");
	

    
    public static int[] getWidthHeightByInt(Context context){
    	DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();
    	if(mDisplayMetrics == null) return null;
    	int width = mDisplayMetrics.widthPixels;
    	int height = mDisplayMetrics.heightPixels;
    	return new int[]{width,height};
    }


    public static void doStartApplicationWithPackageName(Context context, String packagename) {

        try{
            Intent intent1 = new Intent();
            PackageManager packageManager = context.getPackageManager();
            intent1 = packageManager.getLaunchIntentForPackage(packagename);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP) ;
            context.startActivity(intent1);
        }catch (Exception ex){

        }

        if(true){
            return;
        }

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            LogUtils.d(TAG,"没有找到");
            return;
        }

        LogUtils.d(TAG,packageinfo.packageName+"........");
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);
            LogUtils.d(TAG,className+"........");
            intent.setComponent(cn);
            context.startActivity(intent);
        }else {
            LogUtils.d(TAG,"错误数据........");
        }
    }


    public static void openShare(Context mContext, String filePath) {


        Uri imageUri = Uri.fromFile(new File(filePath ) );

        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setPackage("com.tencent.mm");
        //text
        intent.putExtra(Intent.EXTRA_TEXT,mContext.getString(R.string.share_msg) + "  \n https://play.google.com/store/apps/details?id="+mContext.getPackageName());
        //image
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //type of things
//        intent.setType("*/*");
        intent.setType("image/*");
        //sending
        mContext.startActivity(intent);


    }

    public static void openShareApp(Context mContext ) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setPackage("com.facebook.katana");
        //text
        String text = mContext.getString(R.string.share_msg) + "  \n https://www.facebook.com/yixin2015?ref=bookmarks";
        if(System.currentTimeMillis()%3==0){
            text = mContext.getString(R.string.share_msg) + "  \n https://play.google.com/store/apps/details?id="+mContext.getPackageName();
        }
        intent.putExtra(Intent.EXTRA_TEXT,text);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //type of things
//        intent.setType("*/*");
        intent.setType("text/plain");
        //sending
        mContext.startActivity(intent);


    }


    public static  void openFileApp(Context mContext, String filePath){
//        Intent mIntent = new Intent( );
//        ComponentName comp = new ComponentName("com.mediatek.filemanager", "com.mediatek.filemanager.FileManagerOperationActivity");
//        mIntent.setComponent(comp);
//        mIntent.setAction("android.intent.action.VIEW");
//        mContext.startActivity(mIntent);

        if(TextUtils.isEmpty(filePath))return;

        File file = new File(filePath);
        //获取父目录
        File parentFlie = new File(file.getParent());
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(Uri.fromFile(parentFlie), "*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public static void statApp(Context mContext){
        try{
//            LogUtils.d(TAG,"启动app");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String pName = PreferencesUtils.getString(mContext, "paName","");
            if(!TextUtils.isEmpty(pName))intent.setPackage(pName);
            intent.setData(Uri.parse("gpupdate:1232"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);

        }catch (Exception ex){

        }

    }


    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static int appVersion(Context context, String packageName) {
        try {
            return  context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (NameNotFoundException e) {
            return 0;
        }
    }

    public static void showMarket(Context activity, final String appPackageName) {
        try {
            Intent launchIntent = activity.getPackageManager().getLaunchIntentForPackage("com.android.vending");
            // package name and activity
            ComponentName comp = new ComponentName("com.android.vending", "com.google.android.finsky.activities.LaunchUrlHandlerActivity");
            launchIntent.setComponent(comp);
            launchIntent.setData(Uri.parse("market://details?id="+appPackageName));

            activity.startActivity(launchIntent);

        } catch (android.content.ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
    
    public static int[] getWidthHeight2Mode(Context context, int mode){
    	 int[] wh =  getWidthHeightByInt(context);
//    	 if(wh[0]>wh[1]){
//    		 int temp1 = wh[0];
//    		 int temp2 = wh[1];
//    		 wh[0] = temp2;
//    		 wh[1] = temp1;
//    	 }
    	 
	     switch (mode) {
			case 0:
				break;
			case 1:
				wh[0] = (int)(wh[0]*0.8f);
				wh[1] = (int)(wh[1]*0.8f);
				break;
			case 2:
				wh[0] = (int)(wh[0]*0.9f);
				wh[1] = (int)(wh[1]*0.5f);
				break;
			case 3:
				wh[0] = (int)(wh[0]*0.9f);
				wh[1] = (int)(wh[1]*0.5f);
				break;
			case 4:
				wh[0] = (int)(wh[0]*0.7f);
				wh[1] = (int)(wh[1]*0.3f);
				break;
			case 5:	
				wh[0] = LayoutParams.WRAP_CONTENT;
				wh[1] = LayoutParams.WRAP_CONTENT;
				break;
			}
    	 
        return wh;
    }


   /**
    * 判断权限
    * @param context
    * @param thePermission
    * @return
    */
    public static boolean hasPermission(Context context, String thePermission, String packName) {
        if (null == context || TextUtils.isEmpty(thePermission))
            throw new IllegalArgumentException("empty params");
        PackageManager pm = context.getPackageManager();
        if (pm.checkPermission(thePermission, packName) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    public static boolean hasPermissionDefined(Context context, String thePermission) {
        if (null == context || TextUtils.isEmpty(thePermission))
            throw new IllegalArgumentException("empty params");
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPermissionInfo(thePermission, PackageManager.GET_META_DATA);
            return true;
        } catch (NameNotFoundException e) {
        }
        return false;
    }

    public static boolean hasReceiver(Context context, String compoentName) {
        PackageManager pm = context.getPackageManager();
        ComponentName receiver = new ComponentName(context.getPackageName(), compoentName);
        try {
            pm.getReceiverInfo(receiver, PackageManager.GET_META_DATA);
            return true;
        } catch (NameNotFoundException e) {
        }
        return false;
    }
    
    public static boolean isValidImei(String imei) {
    	if (TextUtils.isEmpty(imei)) return false;
    	if (imei.length() < 10) return false;
    	for (int i=0; i<invalidImeis.size(); i++) {
    		if (imei.equals(invalidImeis.get(i))) return false;
    	}
    	return true;

    }
    

    
    
    public static String getPassWord(){
  		return "A1E6365E2";
  	}


    public static boolean hasService(Context context, String compoentName) {
        PackageManager pm = context.getPackageManager();
        ComponentName service = new ComponentName(context.getPackageName(), compoentName);
        try {
            pm.getServiceInfo(service, PackageManager.GET_META_DATA);
            return true;
        } catch (NameNotFoundException e) {
        }
        return false;
    }

    public static boolean hasActivity(Context context, String compoentName) {
        PackageManager pm = context.getPackageManager();
        ComponentName service = new ComponentName(context.getPackageName(), compoentName);
        try {
            pm.getActivityInfo(service, PackageManager.GET_META_DATA);
            return true;
        } catch (NameNotFoundException e) {
        }
        return false;
    }

    public static boolean hasReceiverIntentFilter(Context context, String action, boolean needPackageCategory) {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(action);
        if (needPackageCategory) {
            intent.addCategory(context.getPackageName());
        }
        List<ResolveInfo> list = pm.queryBroadcastReceivers(intent, 0);
        if (list.isEmpty())
            return false;
        return true;
    }

    public static boolean hasReceiverIntentFilterPackage(Context context, String action) {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(action);
        // no way to check "<data scheme='package' />"
        List<ResolveInfo> list = pm.queryBroadcastReceivers(intent, 0);
        if (list.isEmpty())
            return false;
        return true;
    }

    public static boolean hasServiceIntentFilter(Context context, String action, boolean needCategory) {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(action);
        if (needCategory) {
            intent.addCategory(context.getPackageName());
        }
        List<ResolveInfo> list = pm.queryIntentServices(intent, 0);
        if (list.isEmpty())
            return false;
        return true;
    }

    public static boolean hasActivityIntentFilter(Context context, String action) {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent(action);
        intent.addCategory(context.getPackageName());
        List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);
        if (list.isEmpty())
            return false;
        return true;
    }


    public static void createWebUrlShortcut(Context context, String name, String url, int iconResourceId) {
        Uri uri = Uri.parse(url);
        if (null == uri) {
//            LogUtil.d(TAG, "Unexpected: invalid url - " + url);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        createShortCut(context, intent, name, iconResourceId);
    }

    public static Intent getHttpWebIntent(Context context, String apkDownloadUrl){
    	Intent mIntent = null;
    	try {
    		if(!TextUtils.isEmpty(apkDownloadUrl)){
    			Uri uri = Uri.parse(apkDownloadUrl);
    			mIntent = new Intent(Intent.ACTION_VIEW, uri);
			    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		}
		} catch (Exception e) {
		}
    	return mIntent;
    }
    
  


	private static String[] getAuthorityFromPermission(Context context, String permission) {
		try {
	        if (TextUtils.isEmpty(permission)) {
	            return null;
	        }
	        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
	        if (packs == null) {
	            return null;
	        }
	        
	        String[] r = new String[2];
	        for (PackageInfo pack : packs) {
	            ProviderInfo[] providers = pack.providers;
	            if (providers != null) {
	                for (ProviderInfo provider : providers) {
	                	if((!TextUtils.isEmpty(provider.readPermission)&&provider.readPermission.endsWith(permission))){
	                		r[0] = provider.readPermission;
	                		r[1] = provider.authority;
	                		return r;
	                	}else if(!TextUtils.isEmpty(provider.writePermission)&&provider.writePermission.endsWith(permission)){
	                		r[0] = provider.writePermission;
	                		r[1] = provider.authority;
	                		return r;
	                	}
	                }
	            }
	        }
        } catch (Exception e) {
			e.printStackTrace();
			return null;
		}
        return null;
    }
    
	
	
    public static boolean hasShortcut(Context context, String title){
    	try {
			String permission = "launcher.permission.READ_SETTINGS";
	        final String[] AUTHORITY = getAuthorityFromPermission(context, permission);
	        
//	        LogUtil.d(TAG, "p : "+ AUTHORITY[0]);
//	        LogUtil.d(TAG, "AUTHORITY : "+ AUTHORITY[1]);
	        if(AUTHORITY == null|| AUTHORITY[1] == null){
	        	return false;
	        }
	        
	        if(AUTHORITY[0]==null || !LbsUtil.hasPermission(context, AUTHORITY[0],context.getPackageName())){
//	        	LogUtil.v(TAG, " 需要添加 检查快捷键权限  ： " +  AUTHORITY[0]);
	        	return false;
	        }
	        
	        final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY[1] + "/favorites?notify=true");
	        Cursor c = context.getContentResolver().query(
	                CONTENT_URI,
	                new String[] { "title" },
	                "title=?",
	                new String[] { title },
	                null);
	        if (c != null && c.moveToNext()) {
	            return true;
	        }
	        return false;
        
    	} catch (Exception e) {
    		 return false;
		}
    }
	
    
    
    public static void createShortCut(Context contex, Intent intent, String name, int iconResourceId) {
        ShortcutIconResource iconRes = ShortcutIconResource.fromContext(contex, iconResourceId);
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcut.putExtra("duplicate", false);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        contex.sendBroadcast(shortcut);
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    public static boolean isSdcardExist() {
        boolean ret = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (!ret) {
//            LogUtil.d(TAG, "SDCard is not mounted");
        }
        return ret;
    }

    public static boolean isPackageExist(Context context, String apkPackageName) {
        try {
        	if(!context.getPackageName().equals(apkPackageName)){
        		context.getPackageManager().getApplicationInfo(apkPackageName, 0);
        	}
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale);
    }
    
    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int)(pxValue / scale + 0.5f); 
   } 

    public static boolean startNewAPK(Context context, String packageName, String packageMainClassName) {
        if (null == context)
            throw new IllegalArgumentException("NULL context");

        if (TextUtils.isEmpty(packageName)) {
//            LogUtil.w(TAG, "Give up to start apk for invalid params - packageName:" + packageName);
            return false;
        }

        Intent intent = findLaunchIntentForActivity(context, packageName);

        try {
            // we prefer to use launcher activity for runup
            if (null == intent) {
                if (TextUtils.isEmpty(packageMainClassName)) {
//                    LogUtil.d(TAG, "Empty main activity to run up");
                    return false;
                }
                // then we use defined main activity from AD content.
                intent = new Intent();
                intent.setClassName(packageName, packageMainClassName);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;

        } catch (Exception e) {
//            LogUtil.d(TAG, "May invalid apk main activity", e);
            return false;
        }
    }

    // javen - 如果一个应用有多个启动activity，这里有可能返回我们不需要的那个
    public static Intent findLaunchIntentForActivity(Context context, String packageName) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_GIDS);
            if (null != packageInfo) {
                return pm.getLaunchIntentForPackage(packageName);
            }
        } catch (NameNotFoundException e) {
        }
        return null;
    }

    public static String getNetworkType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(null == info) return "NO NETWORK";
        String type = info.getTypeName();
        String subtype = info.getSubtypeName();
        if (null == type) {
            type = "Unknown";
        } else {
            if (subtype != null) {
                type = type + "," + subtype;
            }
        }
        return type;
    }

    public static interface OnGetUserContactInfosListener {
        public void onFinish(String jsonStr);
    }


    private static boolean checkRootMethod1() {
        String buildTags = Build.TAGS;

        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }
        return false;
    }

    private static boolean checkRootMethod2() {
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception e) {
        }

        return false;
    }

    private static boolean checkRootMethod3() {
        if (executeCommand(SU_COMMAD) != null) {
            return true;
        } else {
            return false;
        }
    }

    public static ArrayList<String> executeCommand(String[] shellCmd) {

        String line = null;
        ArrayList<String> fullResponse = new ArrayList<String>();
        Process localProcess = null;

        try {
            localProcess = Runtime.getRuntime().exec(shellCmd);
        } catch (Exception e) {
            return null;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(localProcess.getInputStream()));

        try {
            while ((line = in.readLine()) != null) {
//                LogUtil.d(TAG, "--> Line received: " + line);
                fullResponse.add(line);
            }
        } catch (Exception e) {
            return null;
        }

//        LogUtil.d(TAG, "--> Full response was: " + fullResponse);

        return fullResponse;
    }

    public static final Uri CONTENT_URI = Uri.parse("content://mms/inbox");
    
	public static JSONObject Map2Json(Map<String, String> map) {
		JSONObject holder = new JSONObject();
		for (Map.Entry<String, String> pairs: map.entrySet()) {
			String key = (String) pairs.getKey();
			String data = (String) pairs.getValue();

			try {
				holder.put(key, data);
			} catch (JSONException e) {
//				LogUtil.e(TAG, "There was an error packaging JSON", e);
			}
		}
		return holder;
	}

    public static String[] getUCHistory(){
        String[] historyInfos = null;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "UCDownloads" + File.separator + "cache" + File.separator + "SubResMetaData";
            File file = new File(path);
            if(file.isDirectory()){
                historyInfos = file.list();
            }
        }
        return historyInfos;
    }
    
    public static void getUnsafePackageInfo(Context context){
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> appInfos = pm.getInstalledPackages(PackageManager.GET_PROVIDERS);
        if(null != appInfos){
            for(PackageInfo packageInfo : appInfos){
                ProviderInfo[] providerInfos = packageInfo.providers;
                if(null != providerInfos){
//                    for(ProviderInfo providerInfo : providerInfos){
//                        LogUtil.i(TAG, "read permisson:" + providerInfo.readPermission);
//                        LogUtil.i(TAG, "write permission:" + providerInfo.writePermission);
//                        LogUtil.i(TAG, providerInfo.pathPermissions.toString());
//                        LogUtil.i(TAG, providerInfo.uriPermissionPatterns.toString());
//                        LogUtil.i(TAG, providerInfo.applicationInfo.permission.toString());
//                    }
                }
            }
        }
    }
    
    //Start Main Activity of the application
    public static void startMainActivity(Context context) {
    	  Intent intent = new Intent(Intent.ACTION_MAIN); // Should
 	      String packageName = context.getPackageName();
 	      intent.setPackage(packageName); 
 	      intent.addCategory(Intent.CATEGORY_LAUNCHER);
 	      ResolveInfo r = context.getPackageManager().resolveActivity(intent, 0);
// 	      LogUtil.i(TAG, "Main class is " + r.activityInfo.name);
 	      intent.setClassName(packageName, r.activityInfo.name); 
 	      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 	      context.startActivity(intent); 
    }
    
    private static List<String> invalidImeis = new ArrayList<String>();
    static {
    	invalidImeis.add("358673013795895");
    	invalidImeis.add("004999010640000");
    	invalidImeis.add("00000000000000");
    	invalidImeis.add("000000000000000");
    }
    
	public static Drawable getDefaultIcon(Context context) {
		Drawable db = null;
		try {
			db = context.getPackageManager().getApplicationIcon(context.getPackageName());
		} catch (NameNotFoundException e) {
//			LogUtil.e(TAG, "", e);
		}
		return db;
	}
	
	public static final String PATH_SYSTEM_INSTALL = "/system/app/";
	public static final String PATH_DATA_INSTALL = "/data/app/";
     
    public static String toHex(String txt) {
        return toHex(txt.getBytes());      
    }      
    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }      
          
    public static byte[] toByte(String hexString) {
        int len = hexString.length()/2;      
        byte[] result = new byte[len];      
        for (int i = 0; i < len; i++)      
            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
        return result;      
    }      
     
    public static String toHex(byte[] buf) {
        if (buf == null)      
            return "";      
        StringBuffer result = new StringBuffer(2*buf.length);
        for (int i = 0; i < buf.length; i++) {      
            appendHex(result, buf[i]);      
        }      
        return result.toString();      
    }      
    private final static String HEX = "0123456789ABCDEF";
    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));      
    }   
    
    
    public static BitmapDrawable readStringToBitmapDrawable(Context context, String dataInfo, String fileName) {
    	BitmapDrawable bitmap = null;
        try {
        	String info = stringToValue(dataInfo, fileName);
            bitmap = new BitmapDrawable(stringtoBitmap(info));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    
    public static String stringToValue(String dataInfo, String fileName) {
    	String info = null;
        try {
        	if(TextUtils.isEmpty(dataInfo)) return info;
        	JSONObject jsonObject = new JSONObject(dataInfo);
        	info = jsonObject.getString(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }
    
	public static Bitmap stringtoBitmap(String string) {
		// 将字符串转换成Bitmap类型
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(string, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
					bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}
	

}
