package org.dianmobile.droplet.activity;

import org.dianmobile.droplet.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ToggleButton;

/**
 * ϰ�ߴ���ҳ��
 * ��дϰ�ߡ���Լ����������
 * 
 * @author FreeTymeKiyan
 * @version 0.0.2
 */
public class CreateActivity extends Activity {
	
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
				/*TODO ����Dialog���Ƿ�ȡ��*/
				finish();
			}	
		});
		/*�����İ�ť*/
		btnPublish = (ImageButton) findViewById(R.id.ib_createPublish);
		btnPublish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/* TODO
				 * ץȡ��Ϣ
				 * �жϺϷ�
				 * ��ӵ����ݿ�*/
				System.out.println("btn publish");
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
				/*TODO ����Ԥ��ϰ�ߺ��Զ���ѡ��*/
				/*TODO ��ȡѡ������ݣ���������*/
				System.out.println("et create habit");
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
				/*TODO ����Ԥ���Լ���Զ���ѡ��*/
				/*TODO ��ȡѡ������ݣ���������*/
				System.out.println("et create bet");
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
				boolean boolAlarmState = btnAlarmState.isChecked();
				System.out.println("alarmstate--->" + boolAlarmState);
			}
		});
		/*����ʱ��İ�ť*/
		btnAlarmTime = (Button) findViewById(R.id.btn_createAlarmTime);
		btnAlarmTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO ����ʱ��
				System.out.println("btn alarm time");
			}
		});
	}
	
	/**
	 * �ж�������Ϣ�Ƿ�Ϸ�
	 * ��Ϊ��
	 * 
	 * @return	true	����Ϊ��
	 * 			false	��һ��Ϊ��
	 */
	private boolean judgeValidity() {
		if (!etCreateBet.getText().toString().equals("")
				&& !etCreateHabit.getText().toString().equals("")) {
			return true;
		}
		return false;
	}
}
