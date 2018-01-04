package com.data.tools;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.data.tools.listener.XListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Created by xiongbiao on 18/1/3.
 */
public class BData {

    public String objectId;
    private static JSONObject increment;

    public BData() {
    }

    public String getObjectId() {
        return this.objectId;
    }




    public void increment(String var1) {
        this.increment(var1, Integer.valueOf(1));
    }

    public void increment(String var1, Number var2) {
        increment = new JSONObject();

        try {
            increment.put("__op", "Increment");
            increment.put("amount", var2);
            increment.put("key", var1);
        } catch (JSONException var3) {
            var3.printStackTrace();
        }
    }

    protected JSONObject disposePointerType(JSONObject var1) throws JSONException {
        Field[] var5;
        int var4 = (var5 = this.getClass().getDeclaredFields()).length;

        log("var4 : " + var4);
        log("var all : " + this.getClass().getFields().length);
        for(int var3 = 0; var3 < var4; ++var3) {
            Field var2 = var5[var3];
            JSONObject var6;
            log("var3 : " + var2.getName() + " , "+var2.getType().getSimpleName());
            log("var3 : " +var2.getType().getSuperclass());

            if(BData.class.equals(var2.getType().getSuperclass()) && !var1.isNull(var2.getName())) {
                (var6 = new JSONObject()).put("__type", "Pointer");
                log("var4 : " +var2.getType().getSuperclass());
                var6.put("objectId", var1.optJSONObject(var2.getName()).optString("objectId", "null"));
                var6.put("className", var2.getType().getSimpleName());
                var1.put(var2.getName(), var6);
            }
        }

        return var1;
    }

    public void insertObject(Context var1) {
        this.insertObject(var1, (XListener) null);
    }

    private void log(String msg){
        Log.d("TAG",msg);

    }

    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     * */
    private JSONArray getFiledsInfo(Object o) throws JSONException,IllegalAccessException{
        Field[] fields=o.getClass().getFields();
        String[] fieldNames=new String[fields.length];
        JSONArray list = new JSONArray();
        JSONObject infoMap=null;
        for(int i=0;i<fields.length;i++){
            infoMap = new JSONObject();
            infoMap.put("type", fields[i].getType().getSimpleName());
            infoMap.put("name", fields[i].getName());
            infoMap.put("value",fields[i].get(o));
            list.put(infoMap);
        }
        return list;
    }

//    /**
//     * 根据属性名获取属性值
//     * */
//    private Object getFieldValueByName(String fieldName, Object o) {
//        try {
//            String firstLetter = fieldName.substring(0, 1).toUpperCase();
//            String getter = "get" + firstLetter + fieldName.substring(1);
//            Method method = o.getClass().getMethod(getter, new Class[] {});
//            Object value = method.invoke(o, new Object[] {});
//            return value;
//        } catch (Exception e) {
//            log(e.getMessage());
//            return null;
//        }
//    }

    public void insertObject(Context var1, final XListener var2) {
        JSONObject var3 = new JSONObject();

        try {
            var3.put("row",  getFiledsInfo(this).toString());
            var3.put("c", this.getClass().getSimpleName());
        } catch (Exception var5) {
            var5.printStackTrace();
            var2.onFailure(var5.toString());
        }


        log(var3.toString());
        Map<String,Object> var7 = new HashMap<>();
        var7.put("dd",""+var3.toString());
        for (String key: var7.keySet()) {
            log("key : "+ key +", "+var7.get(key));
        }

        HttpUtils.doPost("http://192.168.1.152:8080/api/v1/8/create",var7,var2);
//        a.thing var6 = new a.thing(var1, 1, "http://open.bmob.cn/7/create", var7, var3);
//        cn.bmob.v3.requestmanager.thing.Code(var1).Code(var6).Code(new XListener() {
//            public final void onSuccess(String var1) {
//                if(var2 != null) {
//                    var2.onSuccess();
//                }
//
//            }
//
//            public final void onFailure(String var1) {
//                if(var2 != null) {
//                    var2.onFailure(var1);
//                }
//
//            }
//        });
    }

    public void insertBatch(Context var1, List<BData> var2, final XListener var3) {
        this.Code(var1, var2, "POST", var3);
    }

    public void updateBatch(Context var1, List<BData> var2, final XListener var3) {
        this.Code(var1, var2, "PUT",  var3);
    }

    public void deleteBatch(Context var1, List<BData> var2, final XListener var3) {
        this.Code(var1, var2, "DELETE", var3);
    }

    private void Code(Context var1, List<BData> var2, String var3, XListener var4) {
        if(var2 != null && var2.size() <= 50) {
            JSONObject var5 = new JSONObject();
            JSONArray var6 = new JSONArray();

            try {
                Iterator var7 = var2.iterator();

                while(var7.hasNext()) {
                    BData var11;
                    var11 = (BData)var7.next();


                    JSONObject var8 = new JSONObject();
                    JSONObject var9;
                    (var9 = new JSONObject()).put("method", var3);
                    if(!var3.equals("PUT") && !var3.equals("DELETE")) {
                        var9.put("path", "/1/classes/" + var11.getClass().getSimpleName());
                    } else {
                        var9.put("path", "/1/classes/" + var11.getClass().getSimpleName() + "/" + var11.getObjectId());
                        var8.remove("createdAt");
                        var8.remove("updatedAt");
                        var8.remove("objectId");
                    }

                    var9.put("body", this.disposePointerType(var8));
                    var6.put(var9);
                }

                var5.put("data", (new JSONObject()).put("requests", var6));
            } catch (JSONException var10) {
                var10.printStackTrace();
            }

            Map var12 = cn.bmob.v3.requestmanager.thing.V(var1);
            a.thing var13 = new a.thing(var1, 1, "http://open.bmob.cn/7/batch", var12, var5);
//            cn.bmob.v3.requestmanager.thing.Code(var1).Code(var13).Code(var4);
        } else {
            var4.onFailure("A batch operation can not be more than 50");
        }
    }

    public void updateObject(Context var1, XListener var2) {
        this.updateObject(var1, this.getObjectId(), var2);
    }

    public void updateObject(Context var1, String var2, final XListener var3) {
        if(TextUtils.isEmpty(var2)) {
            Looper.prepare();
            var3.onFailure("objectId is null");
            Looper.loop();
        } else {
            JSONObject var4 = new JSONObject();

            try {
                JSONObject var5;
                (var5 = new JSONObject()).remove("createdAt");
                var5.remove("updatedAt");
                var5.remove("objectId");
                var5 = this.disposePointerType(var5);
                if(increment != null) {
                    String var6 = increment.optString("key");
                    increment.remove("key");
                    var5.put(var6, increment);
                }

                var4.put("data", var5);
//                if(BmobInstallation.class.getSimpleName().equals(this.getClass().getSimpleName())) {
//                    var4.put("c", "_Installation");
//                } else {
                    var4.put("c", this.getClass().getSimpleName());
//                }

                var4.put("objectId", var2);
            } catch (JSONException var7) {
                var7.printStackTrace();
            }

            Map var8 = cn.bmob.v3.requestmanager.thing.V(var1);
            a.thing var9 = new a.thing(var1, 1, "http://open.bmob.cn/7/update", var8, var4);
//            cn.bmob.v3.requestmanager.thing.Code(var1).Code(var9).Code(new XListener() {
//                public final void onSuccess(String var1) {
//                    this.D.setUpdatedAt(var1.x().c("updatedAt").getAsString());
//                    var3.onSuccess();
//                }
//
//                public final void onFailure(String var1) {
//                    var3.onFailure(var1);
//                }
//            });
        }
    }

    public void deleteObject(Context var1, XListener var2) {
        this.deleteObject(var1, this.getObjectId(), var2);
    }

    public void deleteObject(Context var1, String var2, final XListener var3) {
        if(TextUtils.isEmpty(var2)) {
            Looper.prepare();
            var3.onFailure("objectId is null");
            Looper.loop();
        } else {
            JSONObject var4 = new JSONObject();

            try {
                var4.put("data", new JSONObject());
                var4.put("objectId", var2);
                var4.put("c", this.getClass().getSimpleName());
            } catch (JSONException var5) {
                var5.printStackTrace();
            }

            Map var6 = cn.bmob.v3.requestmanager.thing.V(var1);
            a.thing var7 = new a.thing(var1, 1, "http://open.bmob.cn/7/delete", var6, var4);
//            cn.bmob.v3.requestmanager.thing.Code(var1).Code(var7).Code(new XListener() {
//                public final void onSuccess(String var1) {
//                    var3.onSuccess();
//                }
//
//                public final void onFailure(String var1) {
//                    var3.onFailure(var1);
//                }
//            });
        }
    }
}
