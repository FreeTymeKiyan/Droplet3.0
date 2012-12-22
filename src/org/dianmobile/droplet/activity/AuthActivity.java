package org.dianmobile.droplet.activity;

import org.dianmobile.droplet.R;
import org.dianmobile.droplet.utils.RenrenHelper;
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
 * ��֤ҳ��
 * �������֤���ھͲ�����
 * û����֤���ھͳ���
 * 
 * @author FreeTymeKiyan
 * @version 0.0.2 
 */
public class AuthActivity extends Activity {

	/**��ת��WebViewҳ�������֤*/
	private static final int REQUEST_RENREN_AUTHORIZATION = 0;
	/*ҳ��ؼ�*/
	/**app��logo*/
	private ImageView ivLogo;
	/**�˺�����ؼ�*/
	private EditText etUser;
	/**��������ؼ�*/
	private EditText etCode;
	/**��¼��ť*/
	private Button btnLogIn;
	/**������֤��ť*/
	private Button btnRenrenAuth;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*���������֤��Ϣ*/
		boolean authInfo = checkAuthInfo();
		if (authInfo) {
			logIn(); // ��֤��Ϣ��Ч��������ҳ
		}
		/*��֤��Ϣ��Ч����ʼ��������֤����*/
		setContentView(R.layout.activity_auth);
		initViews();
	}

	/**
	 * ��ʼ��ҳ��ؼ�
	 */
	private void initViews() {
		/*������֤��ť*/
		btnRenrenAuth = (Button) findViewById(R.id.btn_renrenAuth);
		btnRenrenAuth.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (Utils.checkNetworkState(AuthActivity.this)) {
					Intent intent = new Intent(AuthActivity.this, 
							RenrenActivity.class);
					startActivityForResult(intent, REQUEST_RENREN_AUTHORIZATION);
				} else {
					Toast.makeText(AuthActivity.this, 
							R.string.toast_network_not_connected,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	
	/**
	 * ��һ��ҳ��
	 */
	private void anotherView() {
		/*logo*/
		ivLogo = (ImageView) findViewById(R.id.iv_logo);
		ivLogo.setImageResource(R.drawable.logo);
		/*�˺������*/
		etUser = (EditText) findViewById(R.id.et_user);
		/*���������*/
		etCode = (EditText) findViewById(R.id.et_code);
		/*��¼��ť*/
		btnLogIn = (Button) findViewById(R.id.btn_logIn);
		btnLogIn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (Utils.checkNetworkState(AuthActivity.this)) {
					/*�������ӳɹ�*/
					/*ץȡ������û���������*/
					String strUser = etUser.getText().toString();  
					String strCode = etCode.getText().toString();
					/*�ж������Ƿ�Ϸ�*/
					boolean inputValidity = checkInputValidity
							(strUser, strCode);
					if (inputValidity) {
						// TODO ����������֤
						
						// TODO ��֤�ɹ�����¼��������֤���ݿ�
						// TODO ��֤ʧ�ܣ���������
					}
				} else {
					/*����������*/
					Toast.makeText(AuthActivity.this, 
							R.string.toast_network_not_connected, 
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	/**
	 * ���������û����������Ƿ�Ϸ�
	 * 
	 * @param strUser
	 * @param strCode
	 * @return	false	���Ϸ�
	 * 			true	�Ϸ�
	 */
	private boolean checkInputValidity(String strUser, 
			String strCode) {
		boolean temp = false;
		if (strUser.equals("")) { // �û���Ϊ��
			Toast.makeText(AuthActivity.this, R.string
					.toast_username_null, Toast.LENGTH_SHORT)
					.show();
		} else if (strCode.equals("")) { // ����Ϊ��
			Toast.makeText(AuthActivity.this, R.string
					.toast_usercode_null, Toast.LENGTH_SHORT)
					.show();
		} else {
			temp = true;
		}
		return temp;
	}

	/**
	 * ���û�������˺������������API��֤
	 */
	private void auth() {
		
	}

	/**
	 * ��֤��Ϣ���ã���¼����ҳ
	 */
	private void logIn() {
		Intent i = new Intent();
		i.setClass(AuthActivity.this, MainActivity.class);
		/*������ҳ�������Լ�*/
		startActivity(i);
		finish(); 
	}

	/**
	 * �����֤��Ϣ���ݿ�
	 * 
	 * @return 	true	��¼
	 * 			false	Ҫ����֤
	 */
	private boolean checkAuthInfo() {
//		boolean temp = false; 
//		SharedPreferences sp = getSharedPreferences(
//				AUTH_PREF_NAME, MODE_PRIVATE);
//		if (sp.contains(AUTH_RENREN_USER_NAME) && 
//				sp.contains(AUTH_RENREN_USER_CODE)) {
//			/*�ɹ���֤���û�����������Ϣ������*/
//			temp = true;
//		}
//		if (sp.contains(AUTH_RENREN_ACCESS_TOKEN) 
//				&& !sp.getString(AUTH_RENREN_ACCESS_TOKEN, "")
//				.equals("")) {
//			temp = true;
//		}
		return RenrenHelper.isAuthorized(AuthActivity.this); 
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_RENREN_AUTHORIZATION:
			switch (resultCode) {
			case RESULT_OK:
				logIn();
				break;
			case RESULT_CANCELED:
				Toast.makeText(AuthActivity.this, 
						R.string.toast_cancel_authorize, 
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			break;

		default:
			break;
		}
	}
}