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
 * ϰ�ߴ���ҳ��
 * ��дϰ�ߡ���Լ����������
 * 
 * @author FreeTymeKiyan
 * @version 0.0.12
 */
public class CreateActivity extends Activity {
	
	/*�ؼ�*/
	/**������һҳ�İ�ť*/
	private ImageButton btnRtn;
	/**�����İ�ť*/
	private ImageButton btnPublish;
	/**����ϰ�ߵ������*/
	private EditText etCreateHabit;
	/**������Լ�������*/
	private EditText etCreateBet;
	/**���˼ල�İ�ť*/
	private Button btnAtFriends;
	/**����״̬�İ�ť*/
	private ToggleButton tbtnAlarmState;
	/**����ʱ��İ�ť*/
	private Button btnAlarmTime;
	/**ȡ�������ĶԻ���*/
	private AlertDialog cancelDlg = null;
	/*����*/
	/**ϰ�ߵı��*/
	private int intWhatHabit = -1;
	/**�ͷ���ʩ�ı��*/
	private int intWhatPunishment = -1;
	/**���ӵ�״̬*/
	private boolean boolAlarmState = false;
	/**ϰ�ߵ������ַ���*/
	private String strHabitName;
	/**��Լ�������ַ���*/
	private String strHabitBet;
	/**��¼����ʱ�������*/
	private Calendar calendar;
	/**���ӵ�ʱ���ַ���*/
	private String strAlarmTime = "09:00";
	/**at���ѵ�bundle��Ϣ*/
	private Bundle atFriendInfo = new Bundle();
	/**at���ѵ��ַ���*/
	private String strAtFriendInfo = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create);
		initViews();
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initViews() {
		/*������һҳ�İ�ť*/
		btnRtn = (ImageButton) findViewById(R.id.ib_createRtn);
		btnRtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("btn return");
				ifCancel();
			}	
		});
		/*�����İ�ť*/
		btnPublish = (ImageButton) findViewById(R.id.ib_createPublish);
		btnPublish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("btn publish");
				/*ץȡ��Ϣ���жϺϷ�����ӵ����ݿ�*/
				if (judgeValidity()) {
					addToDb();
					syncToRenren();
					return2Main();
				}
			}
		});
		/*����ϰ�ߵ������*/
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
		/*������Լ�������*/
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
		/*���˼ල�İ�ť*/
		btnAtFriends = (Button) findViewById(R.id.btn_addFollowers);
		btnAtFriends.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("btn at friends");
				if (Utils.checkNetworkState(CreateActivity.this)) {
					/*��ת�����˻�ȡ�����б�@��ҳ��*/
					Intent i = new Intent();
					i.setClass(CreateActivity.this, AtRenrenFriendsActivity.class);
					i.putExtras(atFriendInfo); // �����е�@��������
					// ����֮���ס@��ص���Ϣ
					startActivityForResult(i, REQUSET_CODE_AT_RENREN_FRIENDS);
				} else { // ����δ����
					Toast.makeText(CreateActivity.this, 
							R.string.toast_network_not_connected, 
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		/*����״̬�İ�ť*/
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
		/*����ʱ��İ�ť*/
		btnAlarmTime = (Button) findViewById(R.id.btn_createAlarmTime);
		btnAlarmTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*����ʱ��*/
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
	 * ͬ�����ݵ�������
	 */
	private void syncToRenren() {
		/*�첽�������ϸ���״̬�������û������ô�Ҿ͡�����ල��@��@��*/
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
	 * ���Ϸ�����Ϣ��ӵ����ݿ���
	 * ��Ϣ������UUID��NAME��PUNISH��FOLLOWERS
	 * �Լ����ӵ�STATE��TIME
	 */
	protected void addToDb() {
		String uuid = Utils.getUuid(); // ���һ��uuid
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
			Toast.makeText(this, "����ϰ��ʧ�ܣ����ݿ�������", Toast.LENGTH_SHORT)
				.show();
		}
	}
	
	/**
	 * ����״̬����ʱ���������
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
	 * �Ƿ�ȡ������ϰ��
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
	 * ����view��id��ʾ��ͬ�Ĳ˵�
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
	 * �ж�������Ϣ�Ƿ�Ϸ�
	 * ��Ϊ��
	 * 
	 * @return	true	����Ϊ��
	 * 			false	��һ��Ϊ��
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
	 * ������ϣ�������ҳ��
	 */
	private void return2Main() {
		setResult(RESULT_OK);
		finish();
	}
	
	/**
	 * ���غ�ȡ����ť��Ӧͬһ���Ի���
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