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
 * ��ලҳ��
 * 
 * @author FreeTymeKiyan
 * @version 0.0.5
 */
public class BetActivity extends Activity {
	/*����*/
	/**��ǰϰ�ߵ�uuid*/
	private String curUuid;
	/**ϰ�ߵ�����*/
	private String strHabitName;
	/**ϰ�ߵĶ�Լ*/
	private String strHabitBet;
	/**ϰ�ߵ�����״̬*/
	private int intAlarmState;
	/**ϰ�ߵ�����״̬*/
	private boolean boolAlarmState;
	/**ϰ�ߵ�ʱ��*/
	private String strAlarmTime;
	/**��¼����ʱ�������*/
	private Calendar calendar;
	/**at���ѵ�bundle��Ϣ*/
	protected Bundle atFriendInfo = new Bundle();
	/**at���ѵ��ַ���*/
	private String strAtFriendInfo = "";
	/*�ؼ�*/
	/**������һҳ�İ�ť*/
	private ImageButton ibRtn;
	/**������ල��Ϣ�İ�ť*/
	private ImageButton ibPublish;
	/**ϰ�ߵ����ֵ����ֿؼ�*/
	private TextView tvHabitName;
	/**ϰ�ߵĶ�Լ�������*/
	private EditText etHabitBet;
	/**���˼ල�İ�ť*/
	private Button btnAtFriends;
	/**�л�ϰ������״̬�İ�ť*/
	private ToggleButton tbtnAlarmState;
	/**�ı�ϰ������ʱ��İ�ť*/
	private Button btnAlarmTime;
	/**ѯ��ȡ����ල�ĶԻ���*/
	private AlertDialog cancelDlg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bet); // �봴��ҳ������
		prepareData();
		initViews();
	}

	/**
	 * ׼��ҳ��ؼ����������
	 * ���磺ϰ�����ݡ���Լ���ݡ�@�б�����״̬������ʱ��
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
	 * ��ʼ��ҳ��ؼ�
	 */
	private void initViews() {
		/*������һҳ�İ�ť*/
		ibRtn = (ImageButton) findViewById(R.id.ib_betRtn);
		ibRtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ifCancel();
			}
		});
		/*������ල�İ�ť*/
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
		/*ϰ�ߵ����ֵ����ֿؼ�*/
		tvHabitName = (TextView) findViewById(R.id.tv_betHabit);
		tvHabitName.setText(strHabitName);
		/*ϰ�ߵĶ�Լ�������*/
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
		/*���˼ල�İ�ť*/
		btnAtFriends = (Button) findViewById(R.id.btn_addFollowers);
		btnAtFriends.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (Utils.checkNetworkState(BetActivity.this)) {
					/*��ת�����˻�ȡ�����б�@��ҳ��*/
					Intent i = new Intent();
					i.setClass(BetActivity.this, AtRenrenFriendsActivity.class);
					i.putExtras(atFriendInfo); // �����е�@��������
					// ����֮���ס@��ص���Ϣ
					startActivityForResult(i, REQUSET_CODE_AT_RENREN_FRIENDS);
				} else { // ����δ����
					Toast.makeText(BetActivity.this, 
							R.string.toast_network_not_connected, 
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		/*����״̬�İ�ť*/
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
		/*����ʱ��İ�ť*/
		btnAlarmTime = (Button) findViewById(R.id.btn_betAlarmTime);
		btnAlarmTime.setText(strAlarmTime);
		initCalender(); // ����strAlarmTime����calendar
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
	 * �ж��û����������Ϣ�Ƿ�Ϸ�
	 * 
	 * @return	true	�Ϸ�
	 * 			false	���Ϸ�
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
	 * �Ѹø��µ���Ϣ���µ����ݿ���
	 * ��������Լ���ݡ��ල�ˡ�����״̬������ʱ��
	 * @return	false	��ӵ����ݿ�ʧ��
	 * 			true 	��ӵ����ݿ�ɹ�
	 */
	private boolean addToDb() {
		HabitDb db = new HabitDb(BetActivity.this);
		ContentValues info = new ContentValues();
		info.put(PUNISH, strHabitBet);
		info.put(FOLLOWERS, strAtFriendInfo); // &id|name&
		if (boolAlarmState) { // ����������
			info.put(STATE, ALARM_STATE_ON);			
			createOrUpdateAlarm(curUuid, strAlarmTime);
		} else { // ����û����
			info.put(STATE, ALARM_STATE_OFF);
			if (intAlarmState == 1) { // ���������ѣ�ȡ��
				cancelAlarm(curUuid, strAlarmTime);
			}
		}
		info.put(TIME, strAlarmTime);
		return db.updateHabitInfoWithUuid(curUuid, info);
	}
	
	/**
	 * ȡ�����е�����
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
	 * ����״̬����ʱ���������
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
		int interval = 24 * 60 * 60 * 1000; // һ��
		if (Utils.compareTime(calendar)) { // ʱ��û���������ڽ���
			System.out.println("alarm today");
			am.setRepeating(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), interval, 
					sender);
		} else { // ʱ���ѹ������ó�����
			System.out.println("alarm tomorrow");
			calendar.set(Calendar.DAY_OF_YEAR, calendar
					.get(Calendar.DAY_OF_YEAR) + 1);
			am.setRepeating(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), interval, 
					sender);
		}
	}

	/**
	 * ����ල��UGC�첽���͵�������
	 */
	private void syncToRenren() {
		/*�첽�������ϸ���״̬�������û������ô�Ҿ͡�����ල��@��@��*/
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
	 * ����strAlarmTime��calendar���г�ʼ��
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
	 * �ڶԻ�������ʾ��ͬ�Ķ�Լѡ��
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
							etHabitBet.setText("�Զ���");
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
	 * �Ƿ�ȡ����ල
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
	 * ���غ�ȡ����ť��Ӧͬһ���Ի���
	 */
	@Override
	public void onBackPressed() {
		ifCancel();
	}
	
	/**
	 * ��ල��ϣ�������ҳ��
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
		private String btnText = "@���˼ල";

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