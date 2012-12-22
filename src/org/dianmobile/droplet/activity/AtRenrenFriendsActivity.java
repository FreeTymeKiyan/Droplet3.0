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
 * 获取用户的人人好友列表
 * 实现@好友功能
 * 
 * @author FreeTymeKiyan
 * @version 0.0.3
 */
public class AtRenrenFriendsActivity extends Activity {
	/*数据*/
	/**人人的accessToken*/
	private String accessToken;
	/**从人人网获取的好友信息*/
	private List<Map<String, Object>> friendInfo;
	/**人人好友列表的数据适配器*/
	private FriendsListAdapter friendsListAdapter;
	/**已有的@好友信息*/
	private Bundle atFriendInfo;
	/*控件*/
	/**人人好友的列表*/
	private ListView friendsList;
	/**at好友的输入框*/
	private EditText etAtFriends;
	/**返回的按钮*/
	private ImageButton ibReturn;
	/**确认的按钮*/
	private ImageButton ibAck;
	/**读取网络数据时显示的进度条*/
	private ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_at_renren_friends);
		prepareData();
	}

	/**
	 * 准备数据
	 * 从数据库获取人人的accessToken
	 * 调用api获取人人的好友数据
	 */
	private void prepareData() {
		/*从数据库获取人人的accessToken*/
		SharedPreferences sp = getSharedPreferences
				(AUTH_PREF_NAME, MODE_PRIVATE);
		accessToken = sp.getString(AUTH_RENREN_ACCESS_TOKEN, "");
		if (accessToken.equals("")) {
			Toast.makeText(AtRenrenFriendsActivity.this, 
					R.string.toast_access_token_not_found, 
					Toast.LENGTH_SHORT).show();
		}
		atFriendInfo = getIntent().getExtras();
		/*调用api获取人人的好友数据*/
		System.out.println("get friends begin");
		new PrepareData().execute();
	}
	
	/**
	 * 初始化界面控件
	 */
	private void initViews() {
		getWindow().setSoftInputMode(WindowManager
				.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		/*人人好友的列表*/
		friendsList = (ListView) findViewById(R.id.lv_renrenFriends);
		friendsListAdapter = new FriendsListAdapter
				(friendInfo, AtRenrenFriendsActivity.this, 
						refreshEditTextHandler, atFriendInfo);
		friendsList.setAdapter(friendsListAdapter);
		/*at好友的输入框*/
		etAtFriends = (EditText) findViewById(R.id.et_atedFriends);
		etAtFriends.setEnabled(false);
		etAtFriends.setLines(1);
		/*返回的按钮*/
		ibReturn = (ImageButton) findViewById(R.id.ib_atFriendsRtn);
		ibReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		/*确认的按钮*/
		ibAck = (ImageButton) findViewById(R.id.ib_atFriendsAck);
		ibAck.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent data = new Intent();
				// 返回信息给上一界面
				data.putExtra(BUNDLE_AT_INFO, atFriendInfo);
				setResult(RESULT_OK, data);
				finish();
			}
		});
	}

	/**
	 * 刷新输入框的Handler
	 */
	private Handler refreshEditTextHandler = new Handler(
			Looper.getMainLooper()) {
		
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			switch (msg.what) {
			case MSG_REFRESH_EDITTEXT:
				// 刷新输入框
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
					"读取人人好友列表", "请稍后…", false, false);
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
	 * 刷新上面显示@好友的EditText，
	 * TODO 改善鲁棒性
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