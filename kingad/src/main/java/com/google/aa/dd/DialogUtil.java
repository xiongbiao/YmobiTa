package com.google.aa.dd;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;


import java.io.File;

import ta.aie.mobi.ymobita.R;

/**
 * Created by xiongbiao on 17/7/6.
 */
public class DialogUtil {

    public static boolean isshowInsAppDialog = false;

    public static void installAPK(Context context, String saveFileName) {
        try {
            if (TextUtils.isEmpty(saveFileName)) return;
            File apkFile = new File(saveFileName);
            if (!apkFile.exists()) {
                return;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + apkFile.toString()),
                    "application/vnd.android.package-archive");
            context.startActivity(intent);
        } catch (Exception ex) {

        }
    }

    public static boolean isShowStar = false;
    public static int isShowCount = 0;
    static long showTime = 0;

    public static void showStar(final Context context, boolean isCancel, boolean show) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.star_title))
                .setMessage(context.getString(R.string.star_msg))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        isShowStar = false;
                        isShowCount = isShowCount + 1;
                        LbsUtil.showMarket(context, context.getPackageName());
                    }
                });
        if(isShowCount%5>1&&isShowCount>0){
            builder = new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.share_title))
                    .setMessage(context.getString(R.string.share_msg))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            isShowStar = false;
                            isShowCount = isShowCount + 1;
                            LbsUtil.openShareApp(context);
                        }
                    });
        }

        if (isCancel) {
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    isShowStar = false;

                }
            });
        }

        builder.setCancelable(false);


        builder.show();

        isShowStar = true;
    }
    static String TAG = "DialogUtil";
    public static void showStar(final Context context, boolean isCancel) {

        LogUtils.d(TAG,"show dialog " + isShowCount  + "  .. "  + isShowStar);
        long nowtime = System.currentTimeMillis();
        if (Math.abs(nowtime - showTime) < 1000 * 60 || isShowCount > 10 ) {
            return;
        }
        if(isShowStar)return;

        LogUtils.d(TAG,"show dialog ");

        showTime = nowtime;
        isShowCount++;
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.star_title))
                .setMessage(context.getString(R.string.star_msg))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        isShowStar = false;
                        isShowCount = isShowCount + 3;
                        LbsUtil.showMarket(context, context.getPackageName());
                    }
                });

        if(isShowCount%5>1&&isShowCount>0){
            builder = new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.share_title))
                    .setMessage(context.getString(R.string.share_msg))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            isShowStar = false;
                            isShowCount = isShowCount + 3;
                            LbsUtil.openShareApp(context);
                        }
                    });
        }

        if (isCancel) {
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    isShowStar = false;

                }
            });
        }

        builder.setCancelable(false);


        builder.show();

        isShowStar = true;
    }
}
