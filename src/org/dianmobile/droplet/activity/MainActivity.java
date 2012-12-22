package org.dianmobile.droplet.activity;

import static org.dianmobile.droplet.constants.Constants.*;
import static org.dianmobile.droplet.db.HabitDb.*;

import org.dianmobile.droplet.R;
import org.dianmobile.droplet.adapters.MainActivityAdapter;
import org.dianmobile.droplet.db.HabitDb;
import org.taptwo.android.widget.CircleFlowIndicator;
import org.taptwo.android.widget.ViewFlow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * 首页
 * 显示习惯及其记录
 * 显示添加界面
 * 
 * @author FreeTymeKiyan
 * @version 0.0.8
 */
public class MainActivity extends Activity {
	/*页面控件*/
	/**右上方的设置按钮*/
	private ImageButton btnSettings;
	/**左下方的求监督按钮*/
	private Button btnSupervise;
	/**正下方的谈感受按钮*/
	private Button btnFeelings;
	/**右下方的接受惩罚按钮*/
	private Button btnPunish;
	/**显示习惯和习惯详情的左右滑动控件*/
	private ViewFlow viewFlow;
	
	/**习惯总数*/
	private int habitCount = 0;
	/**主页的适配器*/
	private MainActivityAdapter adapter;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareData();
        initViews();
    }

	/**
	 * 准备所需数据的方法
	 */
    private void prepareData() {
    	HabitDb hdb = new HabitDb(MainActivity.this);
    	habitCount = hdb.queryHabitCount(); // 查询习惯个数
	}

	/**
     * 初始化界面控件的方法
     */
	private void initViews() {
		/*上方导航栏右侧的设置按钮*/
		btnSettings = (ImageButton) findViewById(R.id.ib_settings);
		btnSettings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*跳转到设置页面*/
				Intent i = new Intent();
				i.setClass(MainActivity.this, PrefActivity.class);
				startActivity(i);
			}
		});
		/*下方导航栏求监督的按钮*/
		btnSupervise = (Button) findViewById(R.id.btn_left);
		btnSupervise.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 滑得太快会导致position没有来得及更新
				int position = viewFlow.getSelectedItemPosition();
				if (position != habitCount) {
					String uuid = adapter.getUuids().get(position);
					Intent i = new Intent();
					i.putExtra(UUID, uuid);
					i.setClass(MainActivity.this, BetActivity.class);
					startActivityForResult(i, REQUEST_CODE_REFRESH_VIEW);
				} else {
					Toast.makeText(MainActivity.this, 
							R.string.toast_create_first,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		/*下方导航栏谈感受的按钮*/
		btnFeelings = (Button) findViewById(R.id.btn_center);
		btnFeelings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 获取当前的页面位置
				int position = viewFlow.getSelectedItemPosition();
				if (position != habitCount) {
					String uuid = adapter.getUuids().get(position);
//					System.out.println("uuid " + uuid);
					/*跳转到谈感受页面*/
					Intent i = new Intent();
					i.putExtra(UUID, uuid);
					i.setClass(MainActivity.this, ShareActivity.class);
					startActivityForResult(i, REQUEST_CODE_REFRESH_VIEW);
				} else {
					Toast.makeText(MainActivity.this, 
							R.string.toast_create_first,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		/*下方导航栏接受惩罚的按钮*/
		btnPunish = (Button) findViewById(R.id.btn_right);
		btnPunish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*TODO 跳转接受惩罚页面*/
				
			}
		});
		/*中间的ViewFlow*/
		viewFlow = (ViewFlow) findViewById(R.id.vf_habitDetails);
		adapter = new MainActivityAdapter(
				MainActivity.this, habitCount, this);
		viewFlow.setAdapter(adapter);
		/*viewFlow的指示器*/
		CircleFlowIndicator flowIndicator = (CircleFlowIndicator) 
				findViewById(R.id.vfi_circles);
		viewFlow.setFlowIndicator(flowIndicator);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CODE_REFRESH_VIEW:
			switch (resultCode) {
			case RESULT_CANCELED:
				
				break;
			case RESULT_OK:
				finish();
				Intent i = new Intent();
				i.setClass(this, MainActivity.class);
				startActivity(i);
				break;
			default:
				break;
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}