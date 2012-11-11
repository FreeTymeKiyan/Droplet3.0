package org.dianmobile.droplet.activity;

import org.dianmobile.droplet.R;
import org.dianmobile.droplet.utils.Utils;
import static org.dianmobile.droplet.constants.Constants.*;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 认证页面
 * 如果有认证存在就不出现
 * 没有认证存在就出现
 * 
 * @author FreeTymeKiyan
 * @version 0.0.2 
 */
public class AuthActivity extends Activity {

	/*页面控件*/
	/**app的logo*/
	private ImageView ivLogo;
	/**账号输入控件*/
	private EditText etUser;
	/**密码输入控件*/
	private EditText etCode;
	/**登录按钮*/
	private Button btnLogIn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*检测人人认证信息*/
		boolean authInfo = checkAuthInfo();
		if (authInfo) {
			logIn(); // 认证信息有效，进入首页
		}
		/*认证信息无效，初始化人人认证界面*/
		setContentView(R.layout.activity_auth);
		initViews();
	}

	/**
	 * 初始化页面控件
	 */
	private void initViews() {
		/*logo*/
		ivLogo = (ImageView) findViewById(R.id.iv_logo);
		ivLogo.setImageResource(R.drawable.logo);
		/*账号输入框*/
		etUser = (EditText) findViewById(R.id.et_user);
		/*密码输入框*/
		etCode = (EditText) findViewById(R.id.et_code);
		/*登录按钮*/
		btnLogIn = (Button) findViewById(R.id.btn_logIn);
		btnLogIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (Utils.checkNetworkState(AuthActivity.this)) {
					/*网络连接成功*/
					/*抓取输入的用户名和密码*/
					String strUser = etUser.getText().toString();  
					String strCode = etCode.getText().toString();
					/*判断输入是否合法*/
					boolean inputValidity = checkInputValidity
							(strUser, strCode);
					if (inputValidity) {
						// TODO 发送人人认证
						
						// TODO 认证成功，登录，加入认证数据库
						// TODO 认证失败，重新输入
					}
				} else {
					/*无网络连接*/
					Toast.makeText(AuthActivity.this, 
							R.string.toast_network_not_connected, 
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	/**
	 * 检查输入的用户名和密码是否合法
	 * 
	 * @param strUser
	 * @param strCode
	 * @return	false	不合法
	 * 			true	合法
	 */
	private boolean checkInputValidity(String strUser, 
			String strCode) {
		boolean temp = false;
		if (strUser.equals("")) { // 用户名为空
			Toast.makeText(AuthActivity.this, R.string
					.toast_username_null, Toast.LENGTH_SHORT)
					.show();
		} else if (strCode.equals("")) { // 密码为空
			Toast.makeText(AuthActivity.this, R.string
					.toast_usercode_null, Toast.LENGTH_SHORT)
					.show();
		} else {
			temp = true;
		}
		return temp;
	}

	/**
	 * 用用户输入的账号密码调用人人API认证
	 */
	private void auth() {
		
	}

	/**
	 * 认证信息可用，登录到首页
	 */
	private void logIn() {
		Intent i = new Intent();
		i.setClass(AuthActivity.this, MainActivity.class);
		/*进入首页，结束自己*/
		startActivity(i);
		finish(); 
	}

	/**
	 * 检查认证信息数据库
	 * 
	 * @return 	true	登录
	 * 			false	要求认证
	 */
	private boolean checkAuthInfo() {
		boolean temp = false;
		SharedPreferences sp = getSharedPreferences(
				AUTH_PREF_NAME, MODE_PRIVATE);
		if (sp.contains(AUTH_RENREN_USER_NAME) && 
				sp.contains(AUTH_RENREN_USER_CODE)) {
			/*成功认证的用户名和密码信息都存在*/
			temp = true;
		}
		return temp;
	}
}