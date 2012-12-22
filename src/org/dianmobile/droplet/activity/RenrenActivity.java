package org.dianmobile.droplet.activity;

import static org.dianmobile.droplet.constants.Constants.AUTH_PREF_NAME;
import static org.dianmobile.droplet.constants.Constants.AUTH_RENREN_ACCESS_TOKEN;
import static org.dianmobile.droplet.constants.Constants.RENREN_AUTH_URL;
import static org.dianmobile.droplet.constants.Constants.RENREN_PERMISSIONS;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.dianmobile.droplet.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * 通过WebView进行人人认证
 * 认证成功则保存accesstoken到数据库
 * 认证失败则返回对应失败提示 
 * 
 * @author FreeTymeKiyan
 * @version 0.0.2
 */
public class RenrenActivity extends Activity {
	
	/**认证失败，登录被拒绝*/
	private static final String LOGIN_DENIED = "login_denied";
	/**认证成功后获取url中的access_token标识*/
	private static final String ACCESS_TOKEN = "access_token";
	/**人人认证的页面*/
	private WebView webView;
	/**读取网页的进度显示*/
	private ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_renren);
		progressBar = (ProgressBar) findViewById(R.id.pb_authorize);
		webView = (WebView) findViewById(R.id.wv_authorize);
		webView.setVisibility(View.GONE);
		WebSettings ws = webView.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setSupportZoom(true);
		ws.setBuiltInZoomControls(true);
		webView.requestFocusFromTouch();
		openBrowser();
	}

	/**
	 * 打开人人认证的页面进行认证
	 */
	private void openBrowser() {
		String authUrl = RENREN_AUTH_URL + URLEncoder
				.encode(RENREN_PERMISSIONS);
		webView.loadUrl(authUrl);
		webView.setWebViewClient(new WebViewClient() {
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				progressBar.setVisibility(View.GONE);
				webView.setVisibility(View.VISIBLE);
				webView.requestFocus(); // 避免不弹出输入法
				System.out.println("onPageFinished URL:" + url);
				if (url.contains(ACCESS_TOKEN)) {
					String accessToken = URLDecoder.decode(
							url.substring(url.indexOf("=")
							+ 1, url.indexOf("&")));
					System.out.println("renren accessToken:"
							+ accessToken);
					SharedPreferences sp = 
							getSharedPreferences
							(AUTH_PREF_NAME, MODE_PRIVATE);
					Editor edit = sp.edit();
					edit.putString(AUTH_RENREN_ACCESS_TOKEN, 
							accessToken);
					edit.commit();
					setResult(RESULT_OK);
					finish();
				} else if (url.contains(LOGIN_DENIED)) {
					Toast.makeText(RenrenActivity.this, 
							R.string.toast_login_denied,
							Toast.LENGTH_SHORT).show();
				} 
			}
			
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				System.err.println("error code: " + errorCode);
				System.err.println("description: " + description);
				System.err.println("failing url: " + failingUrl);
				setResult(RESULT_CANCELED);
				finish();
			}
			
		});
	}
}
