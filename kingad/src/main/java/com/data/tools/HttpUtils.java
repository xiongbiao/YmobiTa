package com.data.tools;
import android.util.Log;

import com.data.tools.listener.XListener;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;


public class HttpUtils {



	private static final String charset = "utf-8";
	private static void log(String msg){
		Log.d("TAG",msg);

	}
	private static void post(final String path,final String method,final  String params,final XListener xListener) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					log(String.format("reqeust-->Path[%s], RequestMethod[%s],RequestBody[%s]", path,method,URLDecoder.decode(params, charset)));
					boolean isPost = true;
					String mPath = path;
					if(method.equals("GET")){
						isPost = false;
						if(params != null && !params.equals(""))
							mPath+= "?"+params;
					}

					URL url = new URL(mPath);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(10000);
					conn.setReadTimeout(10000);
					conn.setRequestMethod(method);
					conn.setUseCaches(false);
					conn.setDoOutput(true);
					conn.setRequestProperty("Connection", "Keep-Alive");
					conn.setRequestProperty("Charset", charset);

					if(isPost){
						byte[] data = params.getBytes(charset);
						conn.setRequestProperty("Content-Length", String.valueOf(data.length));
						conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
						DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
						outStream.write(data);
						outStream.flush();
					}

					InputStream in = conn.getInputStream();
					StringBuffer sb = new StringBuffer();
					InputStreamReader reader = new InputStreamReader(in, charset);

					char[] buff = new char[1024];
					int len;
					while ((len = reader.read(buff)) > 0) {
						sb.append(buff, 0, len);
					}
					log(String.format("response-->Path[%s], reqeustParma[%s],ResponseCode[%s], ResponseBody[%s]", path,URLDecoder.decode(params, charset), conn.getResponseCode(), sb.toString().replace("\n", "")));
					if (conn.getResponseCode() == 200) {
						if (!sb.toString().equals("")) {

							xListener.onSuccess(sb.toString());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					xListener.onFailure(e.getMessage());
					log(String.format("Path[%s], Error[%s]", path, e.getMessage()));
				}
			}
		}).start();

	}


	public static void doPost(String path, Map<String, Object> params, XListener xListener) {
		  post(path,"POST", parse(params),xListener);
	}
	public static void doGet(String path, Map<String, Object> params, XListener xListener) {
		 post(path, "GET",parse(params),xListener);
	}

	private static String parse(Map<String, Object> params) {
		if(params == null || params.equals("")) return "";

		StringBuilder builder = new StringBuilder();
		for (String key : params.keySet()) {
			try {
				builder.append(key + "=" + URLEncoder.encode(String.valueOf(params.get(key)), charset) + "&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return builder.toString();
	}

/*
	public static String httpGet(String url) {
		String result = null;
		HttpClient client = new DefaultHttpClient();

		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			int statusCode = response.getStatusLine().getStatusCode();System.out.println(statusCode);
			logger.info("url = "+url+" #statusCode = "+statusCode);
			if(statusCode != HttpStatus.SC_OK)
				return result;

			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);

		} catch (Exception e) {e.printStackTrace();
		logger.info(e.getMessage(), e);
		}finally{
			get.abort();
		}
		return result;

	}*/
	public static void main(String[] args) {
	/*	Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("sendno",111);
		resultMap.put("errcode",0);
		resultMap.put("errmsg",22) ;
		resultMap.put("total_user",33);
		resultMap.put("send_cnt",45);
		map.put("push_result",resultMap);
		System.err.println(parse(map));
		
//		System.out.println(doGet("http://5566ua.com/cf1fe02753cf3f367d7eb65536530513.key", map));
		System.out.println(doGet("http://117.135.160.194:18080/pushMsg/callback", map */
		
	}

}
