package org.dianmobile.droplet.activity;

import static org.dianmobile.droplet.constants.Constants.*;
import static org.dianmobile.droplet.db.HabitDb.*;

import java.util.Calendar;
import java.util.Iterator;

import org.dianmobile.droplet.R;
import org.dianmobile.droplet.asyncTask.UpdateRenrenStatus;
import org.dianmobile.droplet.db.HabitDb;
import org.dianmobile.droplet.models.Habit;
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
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * 习惯创建页面
 * 填写习惯、赌约，设置闹钟
 * 
 * @author FreeTymeKiyan
 * @version 0.0.12
 */
public class CreateActivity extends Activity {
	
	/*控件*/
	/**返回上一页的按钮*/
	private ImageButton btnRtn;
	/**发布的按钮*/
	private ImageButton btnPublish;
	/**创建习惯的输入框*/
	private EditText etCreateHabit;
	/**创建赌约的输入框*/
	private EditText etCreateBet;
	/**请人监督的按钮*/
	private Button btnAtFriends;
	/**闹钟状态的按钮*/
	private ToggleButton tbtnAlarmState;
	/**闹钟时间的按钮*/
	private Button btnAlarmTime;
	/**取消创建的对话框*/
	private AlertDialog cancelDlg = null;
	/*数据*/
	/**习惯的编号*/
	private int intWhatHabit = -1;
	/**惩罚措施的编号*/
	private int intWhatPunishment = -1;
	/**闹钟的状态*/
	private boolean boolAlarmState = false;
	/**习惯的内容字符串*/
	private String strHabitName;
	/**赌约的内容字符串*/
	private String strHabitBet;
	/**记录闹钟时间的日历*/
	private Calendar calendar;
	/**闹钟的时间字符串*/
	private String strAlarmTime = "09:00";
	/**at好友的bundle信息*/
	private Bundle atFriendInfo = new Bundle();
	/**at好友的字符串*/
	private String strAtFriendInfo = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create);
		initViews();
	}

	/**
	 * 初始化控件
	 */
	private void initViews() {
		/*返回上一页的按钮*/
		btnRtn = (ImageButton) findViewById(R.id.ib_createRtn);
		btnRtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("btn return");
				ifCancel();
			}	
		});
		/*发布的按钮*/
		btnPublish = (ImageButton) findViewById(R.id.ib_createPublish);
		btnPublish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("btn publish");
				/*抓取信息，判断合法，添加到数据库*/
				if (judgeValidity()) {
					addToDb();
					syncToRenren();
					return2Main();
				}
			}
		});
		/*创建习惯的输入框*/
		etCreateHabit = (EditText) findViewById(R.id.et_createHabit);
		etCreateHabit.setFocusable(false);
		etCreateHabit.setClickable(true);
		etCreateHabit.setInputType(InputType.TYPE_NULL);
		etCreateHabit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				System.out.println("et create habit");
				showMenu(etCreateHabit.getId());
			}
		});
		/*创建赌约的输入框*/
		etCreateBet = (EditText) findViewById(R.id.et_createBet);
		etCreateBet.setFocusable(false);
		etCreateBet.setClickable(true);
		etCreateBet.setInputType(InputType.TYPE_NULL);
		etCreateBet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				System.out.println("et create bet");
				showMenu(etCreateBet.getId());
			}
		});
		/*请人监督的按钮*/
		btnAtFriends = (Button) findViewById(R.id.btn_addFollowers);
		btnAtFriends.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("btn at friends");
				if (Utils.checkNetworkState(CreateActivity.this)) {
					/*跳转到人人获取好友列表并@的页面*/
					Intent i = new Intent();
					i.setClass(CreateActivity.this, AtRenrenFriendsActivity.class);
					i.putExtras(atFriendInfo); // 传已有的@好友数据
					// 返回之后记住@相关的信息
					startActivityForResult(i, REQUSET_CODE_AT_RENREN_FRIENDS);
				} else { // 网络未连接
					Toast.makeText(CreateActivity.this, 
							R.string.toast_network_not_connected, 
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		/*闹钟状态的按钮*/
		tbtnAlarmState = (ToggleButton) findViewById(R.id.tBtn_createAlarmState);
		tbtnAlarmState.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				System.out.println("alarmstate--->" + boolAlarmState);
				boolAlarmState = isChecked;
			}
		});
		calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 9);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		/*闹钟时间的按钮*/
		btnAlarmTime = (Button) findViewById(R.id.btn_createAlarmTime);
		btnAlarmTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*设置时间*/
//				System.out.println("btn alarm time");
				int mHour = calendar.get(Calendar.HOUR_OF_DAY);
				int mMin = calendar.get(Calendar.MINUTE);
				new TimePickerDialog(CreateActivity.this, 
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
	 * 同步内容到人人上
	 */
	private void syncToRenren() {
		/*异步在人人上更新状态：如果我没…，那么我就…。求监督！@…@…*/
		if (Utils.checkNetworkState(CreateActivity.this)) {
			new UpdateRenrenStatus().execute(CreateActivity
					.this, strHabitName, strHabitBet, 
					strAtFriendInfo);
		} else {
			Toast.makeText(CreateActivity.this, R.string.toast_network_not_connected, 
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 将合法的信息添加到数据库中
	 * 信息包括：UUID、NAME、PUNISH、FOLLOWERS
	 * 以及闹钟的STATE、TIME
	 */
	protected void addToDb() {
		String uuid = Utils.getUuid(); // 获得一个uuid
		ContentValues cv = new ContentValues();
		cv.put(UUID, uuid);
		cv.put(NAME, strHabitName);
		cv.put(PUNISH, strHabitBet);
		cv.put(FOLLOWERS, strAtFriendInfo); // &id|name&
		if (boolAlarmState) {
			createAlarm(uuid, strAlarmTime);
			cv.put(STATE, ALARM_STATE_ON);
			cv.put(TIME, strAlarmTime);
		}
		Habit h = new Habit(cv);
		HabitDb hd = new HabitDb(this);
		if (!hd.insertWhenCreate(h)) {
			Toast.makeText(this, "创建习惯失败：数据库插入错误", Toast.LENGTH_SHORT)
				.show();
		}
	}
	
	/**
	 * 闹钟状态开启时，添加闹钟
	 */
	private void createAlarm(String uuid, String time) {
		AlarmManager am = (AlarmManager) CreateActivity.this
				.getSystemService(ALARM_SERVICE);
		Intent intent = new Intent(ALARM_INTENT_ACTION);
		intent.putExtra(UUID, uuid);
		intent.putExtra(TIME, strAlarmTime);
		intent.putExtra(NAME, strHabitName);
		intent.putExtra(PUNISH, strHabitBet);
		intent.setClass(CreateActivity.this, AlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(
				CreateActivity.this, 0, intent, PendingIntent
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
	 * 是否取消创建习惯
	 */
	protected void ifCancel() {
		AlertDialog.Builder builder = new AlertDialog
				.Builder(this);
		builder.setTitle(R.string.cancel_create)
				.setMessage(R.string.cancel_msg)
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
	 * 根据view的id显示不同的菜单
	 * 
	 * @param id
	 */
	protected void showMenu(int id) {
		AlertDialog.Builder builder = new AlertDialog
				.Builder(this);
		if (id == etCreateHabit.getId()) {
			builder.setTitle(R.string.choose_habit)
					.setItems(R.array.habits, new 
							DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface
						dialog, int which) {
					switch (which) {
					case 0:
						intWhatHabit = WHAT_HABIT_RECITE_WORDS;
						etCreateHabit.setText(R.string.recite_words);
						break;
					case 1:
						intWhatHabit = WHAT_HABIT_GET_UP;
						etCreateHabit.setText(R.string.get_up);
						break;
					case 2:
						intWhatHabit = WHAT_HABIT_JOGGING;
						etCreateHabit.setText(R.string.jogging);
						break;
					case 3:
						intWhatHabit = WHAT_HABIT_CUSTOM;
						// TODO
						break;
					default:
						break;
					}
					etCreateHabit.setGravity(Gravity.CENTER_HORIZONTAL);
				}
			});
		} else if (id == etCreateBet.getId()) {
			builder.setTitle(R.string.choose_punish)
					.setItems(R.array.punishments, new 
					DialogInterface.OnClickListener() {
		
				@Override
				public void onClick(DialogInterface
						dialog, int which) {
					switch (which) {
					case WHAT_PUNISHMENT_EAT:
						intWhatPunishment = WHAT_PUNISHMENT_EAT;
						etCreateBet.setText(R.string.eat);
						break;
					case WHAT_PUNISHMENT_CUSTOM:
						// TODO
						intWhatPunishment = WHAT_PUNISHMENT_CUSTOM;
						break;
					default:
						break;
					}
					etCreateBet.setGravity(Gravity.CENTER_HORIZONTAL);
				}
			});
		}
		builder.create().show();
	}

	/**
	 * 判断输入信息是否合法
	 * 不为空
	 * 
	 * @return	true	都不为空
	 * 			false	有一个为空
	 */
	private boolean judgeValidity() {
		strHabitName = etCreateHabit.getText().toString();
		strHabitBet = etCreateBet.getText().toString();
		if (!strHabitName.equals("") && 
				!strHabitBet.equals("")) {
			return true;
		}
		Toast.makeText(this, R.string.invalid, 
				Toast.LENGTH_SHORT).show();
		return false;
	}
	
	/**
	 * 创建完毕，返回主页面
	 */
	private void return2Main() {
		setResult(RESULT_OK);
		finish();
	}
	
	/**
	 * 返回和取消按钮响应同一个对话框
	 */
	@Override
	public void onBackPressed() {
		ifCancel();
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