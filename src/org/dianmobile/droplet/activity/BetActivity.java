package org.dianmobile.droplet.activity;

import static org.dianmobile.droplet.db.HabitDb.*; 
import static org.dianmobile.droplet.constants.Constants.*;

import java.util.Calendar;
import java.util.Iterator;

import org.dianmobile.droplet.R;
import org.dianmobile.droplet.asyncTask.UpdateRenrenStatus;
import org.dianmobile.droplet.db.HabitDb;
import org.dianmobile.droplet.receivers.AlarmReceiver;
import org.dianmobile.droplet.utils.Utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * 求监督页面
 * 
 * @author FreeTymeKiyan
 * @version 0.0.5
 */
public class BetActivity extends Activity {
	/*数据*/
	/**当前习惯的uuid*/
	private String curUuid;
	/**习惯的名字*/
	private String strHabitName;
	/**习惯的赌约*/
	private String strHabitBet;
	/**习惯的闹钟状态*/
	private int intAlarmState;
	/**习惯的闹钟状态*/
	private boolean boolAlarmState;
	/**习惯的时间*/
	private String strAlarmTime;
	/**记录闹钟时间的日历*/
	private Calendar calendar;
	/**at好友的bundle信息*/
	protected Bundle atFriendInfo = new Bundle();
	/**at好友的字符串*/
	private String strAtFriendInfo = "";
	/*控件*/
	/**返回上一页的按钮*/
	private ImageButton ibRtn;
	/**发布求监督信息的按钮*/
	private ImageButton ibPublish;
	/**习惯的名字的文字控件*/
	private TextView tvHabitName;
	/**习惯的赌约的输入框*/
	private EditText etHabitBet;
	/**请人监督的按钮*/
	private Button btnAtFriends;
	/**切换习惯闹钟状态的按钮*/
	private ToggleButton tbtnAlarmState;
	/**改变习惯闹钟时间的按钮*/
	private Button btnAlarmTime;
	/**询问取消求监督的对话框*/
	private AlertDialog cancelDlg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bet); // 与创建页面类似
		prepareData();
		initViews();
	}

	/**
	 * 准备页面控件所需的数据
	 * 比如：习惯内容、赌约内容、@列表、闹钟状态、闹钟时间
	 */
	private void prepareData() {
		Intent i = getIntent();
		curUuid = i.getStringExtra(UUID);
		HabitDb hd = new HabitDb(BetActivity.this);
		Bundle info = hd.queryBetInfoWithUuid(curUuid);
		strHabitName = info.getString(NAME);
		intAlarmState = info.getInt(STATE);
		strAlarmTime = info.getString(TIME);
	}
	
	/**
	 * 初始化页面控件
	 */
	private void initViews() {
		/*返回上一页的按钮*/
		ibRtn = (ImageButton) findViewById(R.id.ib_betRtn);
		ibRtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ifCancel();
			}
		});
		/*发布求监督的按钮*/
		ibPublish = (ImageButton) findViewById(R.id.ib_betPublish);
		ibPublish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (judgeValidity()) {
					if (addToDb()) {
						syncToRenren();
						returnToMain();
					} else {
						Toast.makeText(BetActivity.this, 
								R.string.db_insert_error, 
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		/*习惯的名字的文字控件*/
		tvHabitName = (TextView) findViewById(R.id.tv_betHabit);
		tvHabitName.setText(strHabitName);
		/*习惯的赌约的输入框*/
		etHabitBet = (EditText) findViewById(R.id.et_betBet);
		etHabitBet.setFocusable(false);
		etHabitBet.setClickable(true);
		etHabitBet.setInputType(InputType.TYPE_NULL);
		etHabitBet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showMenu();
			}
		});
		/*请人监督的按钮*/
		btnAtFriends = (Button) findViewById(R.id.btn_addFollowers);
		btnAtFriends.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (Utils.checkNetworkState(BetActivity.this)) {
					/*跳转到人人获取好友列表并@的页面*/
					Intent i = new Intent();
					i.setClass(BetActivity.this, AtRenrenFriendsActivity.class);
					i.putExtras(atFriendInfo); // 传已有的@好友数据
					// 返回之后记住@相关的信息
					startActivityForResult(i, REQUSET_CODE_AT_RENREN_FRIENDS);
				} else { // 网络未连接
					Toast.makeText(BetActivity.this, 
							R.string.toast_network_not_connected, 
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		/*闹钟状态的按钮*/
		tbtnAlarmState = (ToggleButton) findViewById(R.id.tBtn_betAlarmState);
		if (intAlarmState == 1) {
			tbtnAlarmState.setChecked(true);
		} else {
			tbtnAlarmState.setChecked(false);
		}
		tbtnAlarmState.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton 
					buttonView, boolean isChecked) {
				boolAlarmState = isChecked;
				System.out.println("boolAlarmState:" + boolAlarmState);
			}
		});
		/*闹钟时间的按钮*/
		btnAlarmTime = (Button) findViewById(R.id.btn_betAlarmTime);
		btnAlarmTime.setText(strAlarmTime);
		initCalender(); // 根据strAlarmTime设置calendar
		btnAlarmTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int mHour = calendar.get(Calendar.HOUR_OF_DAY);
				int mMin = calendar.get(Calendar.MINUTE);
				new TimePickerDialog(BetActivity.this, 
						new TimePickerDialog
						.OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker
									view, int hourOfDay, 
									int minute) {
								calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
								calendar.set(Calendar.MINUTE, minute);
								calendar.set(Calendar.SECOND, 0);
								calendar.set(Calendar.MILLISECOND, 0);
								strAlarmTime = Utils.format
										(hourOfDay) + ":" 
										+ Utils.format(minute);
								btnAlarmTime.setText(strAlarmTime);
							}

				}, mHour, mMin, true).show();
			}
		});
	}

	/**
	 * 判断用户所输入的信息是否合法
	 * 
	 * @return	true	合法
	 * 			false	不合法
	 */
	private boolean judgeValidity() {
		boolean result = false;
		strHabitBet = etHabitBet.getText().toString(); 
		if (!strHabitBet.equals("")) {
			result = true;
		}
		return result;
	}

	/**
	 * 把该更新的信息更新到数据库中
	 * 包括：赌约内容、监督人、闹钟状态、闹钟时间
	 * @return	false	添加到数据库失败
	 * 			true 	添加到数据库成功
	 */
	private boolean addToDb() {
		HabitDb db = new HabitDb(BetActivity.this);
		ContentValues info = new ContentValues();
		info.put(PUNISH, strHabitBet);
		info.put(FOLLOWERS, strAtFriendInfo); // &id|name&
		if (boolAlarmState) { // 现在有提醒
			info.put(STATE, ALARM_STATE_ON);			
			createOrUpdateAlarm(curUuid, strAlarmTime);
		} else { // 现在没提醒
			info.put(STATE, ALARM_STATE_OFF);
			if (intAlarmState == 1) { // 本来有提醒，取消
				cancelAlarm(curUuid, strAlarmTime);
			}
		}
		info.put(TIME, strAlarmTime);
		return db.updateHabitInfoWithUuid(curUuid, info);
	}
	
	/**
	 * 取消已有的闹钟
	 * 
	 * @param curUuid
	 * @param strAlarmTime
	 */
	private void cancelAlarm(String curUuid, String strAlarmTime) {
		AlarmManager am = (AlarmManager) getSystemService
				(ALARM_SERVICE);
		Intent intent = new Intent(ALARM_INTENT_ACTION);
		intent.setClass(BetActivity.this, AlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(
				BetActivity.this, 0, intent, PendingIntent
				.FLAG_CANCEL_CURRENT);
		am.cancel(sender);
	}

	/**
	 * 闹钟状态开启时，添加闹钟
	 */
	private void createOrUpdateAlarm(String uuid, String time) {
		AlarmManager am = (AlarmManager) getSystemService
				(ALARM_SERVICE);
		Intent intent = new Intent(ALARM_INTENT_ACTION);
		intent.putExtra(UUID, uuid);
		intent.putExtra(TIME, strAlarmTime);
		intent.putExtra(NAME, strHabitName);
		intent.putExtra(PUNISH, strHabitBet);
		intent.setClass(BetActivity.this, AlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(
				BetActivity.this, 0, intent, PendingIntent
				.FLAG_CANCEL_CURRENT);
		int interval = 24 * 60 * 60 * 1000; // 一天
		if (Utils.compareTime(calendar)) { // 时间没过，设置在今天
			System.out.println("alarm today");
			am.setRepeating(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), interval, 
					sender);
		} else { // 时间已过，设置成明天
			System.out.println("alarm tomorrow");
			calendar.set(Calendar.DAY_OF_YEAR, calendar
					.get(Calendar.DAY_OF_YEAR) + 1);
			am.setRepeating(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), interval, 
					sender);
		}
	}

	/**
	 * 把求监督的UGC异步发送到人人上
	 */
	private void syncToRenren() {
		/*异步在人人上更新状态：如果我没…，那么我就…。求监督！@…@…*/
		if (Utils.checkNetworkState(BetActivity.this)) {
			new UpdateRenrenStatus().execute(BetActivity
					.this, strHabitName, strHabitBet, 
					strAtFriendInfo);
		} else {
			Toast.makeText(BetActivity.this, R.string.toast_network_not_connected, 
					Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 根据strAlarmTime对calendar进行初始化
	 */
	private void initCalender() {
		calendar = Calendar.getInstance();
//		System.out.println("strAlarmTime" + strAlarmTime);
		String[] alarmTime = strAlarmTime.split(":");
//		System.out.println("two integers: " + Integer
//				.valueOf(alarmTime[0]) + "|" + 
//				Integer.valueOf(alarmTime[1]));
		calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(alarmTime[0]));
		calendar.set(Calendar.MINUTE, Integer.valueOf(alarmTime[1]));
	}

	/**
	 * 在对话框中显示不同的赌约选项
	 */
	private void showMenu() {
		AlertDialog.Builder builder = new AlertDialog
				.Builder(this);
		builder.setTitle(R.string.choose_punish)
				.setItems(R.array.punishments, new 
				DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface 
							dialog, int which) {
						switch (which) {
						case WHAT_PUNISHMENT_EAT:
							etHabitBet.setText(R.string.eat);
							break;
						case WHAT_PUNISHMENT_CUSTOM:
							// TODO
							etHabitBet.setText("自定义");
							break;
						default:
							break;
						}
					}
				});
		etHabitBet.setGravity(Gravity.CENTER_HORIZONTAL);
		builder.create().show();
	}
	
	/**
	 * 是否取消求监督
	 */
	protected void ifCancel() {
		AlertDialog.Builder builder = new AlertDialog
				.Builder(this);
		builder.setTitle(R.string.cancel_bet)
				.setMessage(R.string.cancel_bet_msg)
				.setPositiveButton(R.string.yes, 
						new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setResult(RESULT_CANCELED);
				finish();
			}
		}).setNegativeButton(R.string.no, 
				new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				cancelDlg.dismiss();
			}
		});
		cancelDlg = builder.create();
		cancelDlg.show();
	}
	
	/**
	 * 返回和取消按钮响应同一个对话框
	 */
	@Override
	public void onBackPressed() {
		ifCancel();
	}
	
	/**
	 * 求监督完毕，返回主页面
	 */
	private void returnToMain() {
		setResult(RESULT_OK);
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUSET_CODE_AT_RENREN_FRIENDS:
			switch (resultCode) {
			case RESULT_CANCELED:
				
				break;
			case RESULT_OK:
				atFriendInfo = data
						.getBundleExtra(BUNDLE_AT_INFO);
				new RefreshButtonText().execute(atFriendInfo);
				break;
			default:
				break;
			}
			break;

		default:
			break;
		}
	}
	
	private class RefreshButtonText extends AsyncTask<Object,
			Object, Object> {

		private Bundle b;
		private String btnText = "@请人监督";

		@Override
		protected Object doInBackground(Object... params) {
			b = (Bundle) params[0];
			Iterator<String> iterator = b.keySet().iterator();
			String temp = "";
			strAtFriendInfo = "";
			while (iterator.hasNext()) {
				temp = b.getString(iterator.next());
				strAtFriendInfo = strAtFriendInfo + "&" + temp;
				btnText = btnText + " " + temp.split("\\|")[1];
			}
			if (btnText.length() > MAX_BTN_TEXT_LENGTH) {
				btnText = btnText.substring(0, 
						MAX_BTN_TEXT_LENGTH - 1) + "...";
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			btnAtFriends.setText(btnText);
		}
	}
}