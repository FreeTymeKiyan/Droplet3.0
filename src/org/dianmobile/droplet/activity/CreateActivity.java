package org.dianmobile.droplet.activity;

import static org.dianmobile.droplet.constants.Constants.*;
import static org.dianmobile.droplet.db.HabitDb.*;

import java.util.Calendar;

import org.dianmobile.droplet.R;
import org.dianmobile.droplet.db.HabitDb;
import org.dianmobile.droplet.models.Habit;
import org.dianmobile.droplet.utils.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
 * @version 0.0.5
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
	private Button btnAddFollowers;
	/**����״̬�İ�ť*/
	private ToggleButton btnAlarmState;
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
	private String strHabitTitle;
	/**��Լ�������ַ���*/
	private String strHabitPunishment;
	/**��¼����ʱ�������*/
	private Calendar calendar;
	/**���ӵ�ʱ���ַ���*/
	private String strAlarmTime = "09:00";
	
	
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
				/* ץȡ��Ϣ���жϺϷ�����ӵ����ݿ�*/
				if (judgeValidity()) {
					addToDb();
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
		btnAddFollowers = (Button) findViewById(R.id.btn_addFollowers);
		btnAddFollowers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO ��ת�����˻�ȡ�����б�@��ҳ��
				// TODO ����֮���ס@��ص���Ϣ
				System.out.println("btn add followers");
			}
		});
		/*����״̬�İ�ť*/
		btnAlarmState = (ToggleButton) findViewById(R.id.tBtn_createAlarmState);
		btnAlarmState.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				System.out.println("alarmstate--->" + boolAlarmState);
				boolAlarmState = btnAlarmState.isChecked();
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
								strAlarmTime = format
										(hourOfDay) + ":" 
										+ format(minute);
								btnAlarmTime.setText(strAlarmTime);
							}

				}, mHour, mMin, true).show();
			}
		});
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
		cv.put(NAME, strHabitTitle);
		cv.put(PUNISH, strHabitPunishment);
		cv.put(FOLLOWERS, ""); // TODO followersδ���
		if (boolAlarmState) {
			cv.put(STATE, STATE_ON);
		}
		cv.put(TIME, strAlarmTime);
		Habit h = new Habit(cv);
		HabitDb hd = new HabitDb(this);
		if (!hd.insertWhenCreate(h)) {
			Toast.makeText(this, "����ϰ��ʧ�ܣ����ݿ�������", Toast.LENGTH_SHORT)
				.show();
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
						intWhatHabit = WHAT_HABIT_GET_UP;
						etCreateHabit.setText(R.string.get_up);
						break;
					case 1:
						intWhatHabit = WHAT_HABIT_JOGGING;
						etCreateHabit.setText(R.string.jogging);
						break;
					case 2:
						intWhatHabit = WHAT_HABIT_SLEEP;
						etCreateHabit.setText(R.string.sleep);
						break;
					case 3:
						intWhatHabit = WHAT_HABIT_CUSTOM;
						
						break;
					default:
						break;
					}
					etCreateHabit.setGravity(Gravity.CENTER_HORIZONTAL);
				}
			});
		} else if (id == etCreateBet.getId()) {
			builder.setTitle(R.string.choose_habit)
			.setItems(R.array.punishments, new 
					DialogInterface.OnClickListener() {
		
				@Override
				public void onClick(DialogInterface
						dialog, int which) {
					switch (which) {
					case 0:
						intWhatPunishment = WHAT_PUNISHMENT_EAT;
						etCreateBet.setText(R.string.eat);
						break;
					case 1:
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
		strHabitTitle = etCreateHabit.getText().toString();
		strHabitPunishment = etCreateBet.getText().toString();
		if (!strHabitTitle.equals("") && 
				!strHabitPunishment.equals("")) {
			return true;
		}
		Toast.makeText(this, R.string.invalid, 
				Toast.LENGTH_SHORT).show();
		return false;
	}
	
	/** 
	 * ��ʽ���ַ���(7:3->07:03) 
	 * */
	private String format(int x) {
		String s = "" + x;
		if (s.length() == 1)
			s = "0" + s;
		return s;
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
}