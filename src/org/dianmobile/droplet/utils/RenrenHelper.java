package org.dianmobile.droplet.utils;

import static org.dianmobile.droplet.constants.Constants.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 人人网api操作类
 * 实现功能：	1.获取用户好友信息
 * 			2.发布状态
 * 			3.上传图片
 * 			4.判断是否已经认证
 * 
 * @author FreeTymeKiyan
 * @version 0.0.1
 */
public class RenrenHelper {
	
	/**人人授权的accessToken*/
	private static String accessToken = "";
	
	/**
	 * 判断是否已经获取认证 
	 * 
	 * @return	false	无accessToken
	 * 			true	有accessToken
	 */
	public static boolean isAuthorized(Context context) {
		boolean result = false;
		SharedPreferences sp = context.getSharedPreferences
				(AUTH_PREF_NAME, Context.MODE_PRIVATE);
		accessToken = sp.getString(AUTH_RENREN_ACCESS_TOKEN, "");
		if (!accessToken.equals("")) {
			result = true;
		}
		return result;
	}
	
	/**
	 * 调用人人api获取好友信息
	 * 
	 * @return	List<Map<String, Object>>，id、name、headurl
	 */
	public static List<Map<String, Object>> getFriends() {
		System.out.println("get friends start");
		String method = "friends.getFriends";
		String v = "1.0";
		String format = "JSON";
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("access_token", accessToken);
		paramMap.put("method", method);
		paramMap.put("v", v);
		paramMap.put("format", format);
		String sig = getSignature(paramMap);
		List<BasicNameValuePair> params = Map2List(paramMap, sig);
		String result = "";
		HttpPost hp = new HttpPost(RENREN_API_URL);
		
		try {
			hp.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		HttpClient hc = new DefaultHttpClient();
		List<Map<String, Object>> list = new ArrayList<Map
				<String,Object>>();
		try {
			HttpResponse hr = hc.execute(hp);
			if(hr.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(hr.getEntity());
        	}else 
				result = "Error Response:" + hr.getStatusLine()
						.toString();
			System.out.println("result:" + result);
			JSONArray friends = new JSONArray(result);
			for (int i = 0; i < friends.length(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject friend = friends.getJSONObject(i);
				map.put(RENREN_ID, friend.getInt(RENREN_ID));
				map.put(RENREN_NAME, friend.getString(RENREN_NAME));
				map.put(RENREN_HEAD_URL, friend.getString(RENREN_HEAD_URL));
				list.add(map);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static boolean updateStatus(String status) {
		boolean result = false;
		
		String method = "status.set";
		String v = "1.0";
		String url = RENREN_API_URL;
		String format = "JSON";
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("access_token", accessToken);
		map.put("method", method);
		map.put("v", v);
		map.put("status", status);
		map.put("format", format);
		String sig = getSignature(map);
		
		List<BasicNameValuePair> params = Map2List(map, sig);
		String response = "";
		HttpPost hp = new HttpPost(url);
		try {
			hp.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpClient hc = new DefaultHttpClient();
			HttpResponse hr = hc.execute(hp);
			if (hr.getStatusLine().getStatusCode() == 200) {
				response = EntityUtils.toString(hr.getEntity());
				result = true;
			} else {
				response = "Error response:" + hr.getStatusLine().toString();
			}
			System.out.println("renren update:" + result);
			System.out.println("renren return:" + response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 得到MD5的方法
	 * 
	 * @param paramMap
	 * @return 	result
	 * 			null
	 */
	public static String getSignature(Map<String, String> paramMap) {
		List<String> paramList = new ArrayList<String>(paramMap.size());
		//1、参数格式化
		for(Map.Entry<String, String> param : paramMap.entrySet())
		{
			paramList.add(param.getKey() + "=" + param.getValue());
		}
		//2、排序并接成一个字符串
		Collections.sort(paramList);
		StringBuffer buffer = new StringBuffer();
		for(String param : paramList)
		{
			buffer.append(param);
		}
		//3、追加Secret_Key
		buffer.append(RENREN_SECRET_KEY);
		System.out.println("Signature before MD5------->" + buffer);
		//4、将拼接好的字符串转成MD5值
		try {
		    MessageDigest md = MessageDigest.getInstance("MD5");
		    StringBuffer result = new StringBuffer();
		    try {
		        for (byte b : md.digest(buffer.toString().getBytes("UTF-8"))) {
		            result.append(Integer.toHexString((b & 0xf0) >>> 4));
		            result.append(Integer.toHexString(b & 0x0f));
		        }
		    } catch (UnsupportedEncodingException e) {
		        for (byte b : md.digest(buffer.toString().getBytes())) {
		            result.append(Integer.toHexString((b & 0xf0) >>> 4));
		            result.append(Integer.toHexString(b & 0x0f));
		        }
		    }
		    return result.toString();
		} catch (java.security.NoSuchAlgorithmException ex) {
		    ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 把map转化成List<BasicNameValuePair>并添加sig键值对的方法
	 * 
	 * @param paramMap
	 * @param signature
	 * @return
	 */
	private static List<BasicNameValuePair> Map2List(Map<String, String> paramMap,
			String signature) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		for(Map.Entry<String, String> param : paramMap.entrySet()) {
			params.add(new BasicNameValuePair(param.getKey(), param.getValue()));
		}
		params.add(new BasicNameValuePair("sig", signature));
		return params;
	}
}
