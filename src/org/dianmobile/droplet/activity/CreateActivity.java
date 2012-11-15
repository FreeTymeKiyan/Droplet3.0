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
 * 习惯创建页面
 * 填写习惯、赌约，设置闹钟
 * 
 * @author FreeTymeKiyan
 * @version 0.0.2
 */
public class CreateActivity extends Activity {
	
	/**返回上一页的按钮*/
	private ImageButton btnRtn;
	/**发布的按钮*/
	private ImageButton btnPublish;
	/**创建习惯的输入框*/
	private EditText etCreateHabit;
	/**创建赌约的输入框*/
	private EditText etCreateBet;
	/**请人监督的按钮*/
	private Button btnAddFollowers;
	/**闹钟状态的按钮*/
	private ToggleButton btnAlarmState;
	/**闹钟时间的按钮*/
	private Button btnAlarmTime;
	
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
				/*TODO 弹出Dialog问是否取消*/
				finish();
			}	
		});
		/*发布的按钮*/
		btnPublish = (ImageButton) findViewById(R.id.ib_createPublish);
		btnPublish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/* TODO
				 * 抓取信息
				 * 判断合法
				 * 添加到数据库*/
				System.out.println("btn publish");
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
				/*TODO 弹出预设习惯和自定义选项*/
				/*TODO 获取选择的内容，设置文字*/
				System.out.println("et create habit");
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
				/*TODO 弹出预设赌约和自定义选项*/
				/*TODO 获取选择的内容，设置文字*/
				System.out.println("et create bet");
			}
		});
		/*请人监督的按钮*/
		btnAddFollowers = (Button) findViewById(R.id.btn_addFollowers);
		btnAddFollowers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 跳转到人人获取好友列表并@的页面
				// TODO 返回之后记住@相关的信息
				System.out.println("btn add followers");
			}
		});
		/*闹钟状态的按钮*/
		btnAlarmState = (ToggleButton) findViewById(R.id.tBtn_createAlarmState);
		btnAlarmState.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				boolean boolAlarmState = btnAlarmState.isChecked();
				System.out.println("alarmstate--->" + boolAlarmState);
			}
		});
		/*闹钟时间的按钮*/
		btnAlarmTime = (Button) findViewById(R.id.btn_createAlarmTime);
		btnAlarmTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 设置时间
				System.out.println("btn alarm time");
			}
		});
	}
	
	/**
	 * 判断输入信息是否合法
	 * 不为空
	 * 
	 * @return	true	都不为空
	 * 			false	有一个为空
	 */
	private boolean judgeValidity() {
		if (!etCreateBet.getText().toString().equals("")
				&& !etCreateHabit.getText().toString().equals("")) {
			return true;
		}
		return false;
	}
}
