package org.dianmobile.droplet.activity;

import static org.dianmobile.droplet.constants.Constants.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dianmobile.droplet.R;
import org.dianmobile.droplet.adapters.FriendsListAdapter;
import org.dianmobile.droplet.utils.RenrenHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

/**
 * ��ȡ�û������˺����б�
 * ʵ��@���ѹ���
 * 
 * @author FreeTymeKiyan
 * @version 0.0.3
 */
public class AtRenrenFriendsActivity extends Activity {
	/*����*/
	/**���˵�accessToken*/
	private String accessToken;
	/**����������ȡ�ĺ�����Ϣ*/
	private List<Map<String, Object>> friendInfo;
	/**���˺����б������������*/
	private FriendsListAdapter friendsListAdapter;
	/**���е�@������Ϣ*/
	private Bundle atFriendInfo;
	/*�ؼ�*/
	/**���˺��ѵ��б�*/
	private ListView friendsList;
	/**at���ѵ������*/
	private EditText etAtFriends;
	/**���صİ�ť*/
	private ImageButton ibReturn;
	/**ȷ�ϵİ�ť*/
	private ImageButton ibAck;
	/**��ȡ��������ʱ��ʾ�Ľ�����*/
	private ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_at_renren_friends);
		prepareData();
	}

	/**
	 * ׼������
	 * �����ݿ��ȡ���˵�accessToken
	 * ����api��ȡ���˵ĺ�������
	 */
	private void prepareData() {
		/*�����ݿ��ȡ���˵�accessToken*/
		SharedPreferences sp = getSharedPreferences
				(AUTH_PREF_NAME, MODE_PRIVATE);
		accessToken = sp.getString(AUTH_RENREN_ACCESS_TOKEN, "");
		if (accessToken.equals("")) {
			Toast.makeText(AtRenrenFriendsActivity.this, 
					R.string.toast_access_token_not_found, 
					Toast.LENGTH_SHORT).show();
		}
		atFriendInfo = getIntent().getExtras();
		/*����api��ȡ���˵ĺ�������*/
		System.out.println("get friends begin");
		new PrepareData().execute();
	}
	
	/**
	 * ��ʼ������ؼ�
	 */
	private void initViews() {
		getWindow().setSoftInputMode(WindowManager
				.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		/*���˺��ѵ��б�*/
		friendsList = (ListView) findViewById(R.id.lv_renrenFriends);
		friendsListAdapter = new FriendsListAdapter
				(friendInfo, AtRenrenFriendsActivity.this, 
						refreshEditTextHandler, atFriendInfo);
		friendsList.setAdapter(friendsListAdapter);
		/*at���ѵ������*/
		etAtFriends = (EditText) findViewById(R.id.et_atedFriends);
		etAtFriends.setEnabled(false);
		etAtFriends.setLines(1);
		/*���صİ�ť*/
		ibReturn = (ImageButton) findViewById(R.id.ib_atFriendsRtn);
		ibReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		/*ȷ�ϵİ�ť*/
		ibAck = (ImageButton) findViewById(R.id.ib_atFriendsAck);
		ibAck.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent data = new Intent();
				// ������Ϣ����һ����
				data.putExtra(BUNDLE_AT_INFO, atFriendInfo);
				setResult(RESULT_OK, data);
				finish();
			}
		});
	}

	/**
	 * ˢ��������Handler
	 */
	private Handler refreshEditTextHandler = new Handler(
			Looper.getMainLooper()) {
		
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			switch (msg.what) {
			case MSG_REFRESH_EDITTEXT:
				// ˢ�������
				new RefreshEditText().execute();
				break;
			default:
				break;
			}
		}
	};
	
	private class PrepareData extends AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = ProgressDialog.show(AtRenrenFriendsActivity.this,
					"��ȡ���˺����б�", "���Ժ�", false, false);
		}
		
		@Override
		protected Object doInBackground(Object... params) {
			friendInfo = RenrenHelper.getFriends();
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			initViews();
			pd.dismiss();
		}
	}
	
	/**
	 * ˢ��������ʾ@���ѵ�EditText��
	 * TODO ����³����
	 * 
	 * @author FreeTymeKiyan
	 * @version 0.0.1
	 */
	private class RefreshEditText extends AsyncTask<Object, Object, Object> {

		private SpannableStringBuilder ssb;

		@Override
		protected Object doInBackground(Object... params) {
			atFriendInfo = 
					friendsListAdapter.getAtFriendsState();
			String text = "";
			ssb = new SpannableStringBuilder();
			Iterator<String> keySet = atFriendInfo.keySet().iterator();
			while (keySet.hasNext()) {
				text = atFriendInfo.getString(keySet.next())
						.split("\\|")[1];
				int start = ssb.length();
				ssb.append(text);
				int end = ssb.length();
				int flags = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE;
				ssb.setSpan(new BackgroundColorSpan(Color.GRAY),
						start, end, flags);
			}
			System.out.println("text: " + text);
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			etAtFriends.setText(ssb);
			super.onPostExecute(result);
		}
	}
}