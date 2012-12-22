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
 * ��ҳ
 * ��ʾϰ�߼����¼
 * ��ʾ��ӽ���
 * 
 * @author FreeTymeKiyan
 * @version 0.0.8
 */
public class MainActivity extends Activity {
	/*ҳ��ؼ�*/
	/**���Ϸ������ð�ť*/
	private ImageButton btnSettings;
	/**���·�����ල��ť*/
	private Button btnSupervise;
	/**���·���̸���ܰ�ť*/
	private Button btnFeelings;
	/**���·��Ľ��ܳͷ���ť*/
	private Button btnPunish;
	/**��ʾϰ�ߺ�ϰ����������һ����ؼ�*/
	private ViewFlow viewFlow;
	
	/**ϰ������*/
	private int habitCount = 0;
	/**��ҳ��������*/
	private MainActivityAdapter adapter;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareData();
        initViews();
    }

	/**
	 * ׼���������ݵķ���
	 */
    private void prepareData() {
    	HabitDb hdb = new HabitDb(MainActivity.this);
    	habitCount = hdb.queryHabitCount(); // ��ѯϰ�߸���
	}

	/**
     * ��ʼ������ؼ��ķ���
     */
	private void initViews() {
		/*�Ϸ��������Ҳ�����ð�ť*/
		btnSettings = (ImageButton) findViewById(R.id.ib_settings);
		btnSettings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*��ת������ҳ��*/
				Intent i = new Intent();
				i.setClass(MainActivity.this, PrefActivity.class);
				startActivity(i);
			}
		});
		/*�·���������ල�İ�ť*/
		btnSupervise = (Button) findViewById(R.id.btn_left);
		btnSupervise.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO ����̫��ᵼ��positionû�����ü�����
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
		/*�·�������̸���ܵİ�ť*/
		btnFeelings = (Button) findViewById(R.id.btn_center);
		btnFeelings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// ��ȡ��ǰ��ҳ��λ��
				int position = viewFlow.getSelectedItemPosition();
				if (position != habitCount) {
					String uuid = adapter.getUuids().get(position);
//					System.out.println("uuid " + uuid);
					/*��ת��̸����ҳ��*/
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
		/*�·����������ܳͷ��İ�ť*/
		btnPunish = (Button) findViewById(R.id.btn_right);
		btnPunish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*TODO ��ת���ܳͷ�ҳ��*/
				
			}
		});
		/*�м��ViewFlow*/
		viewFlow = (ViewFlow) findViewById(R.id.vf_habitDetails);
		adapter = new MainActivityAdapter(
				MainActivity.this, habitCount, this);
		viewFlow.setAdapter(adapter);
		/*viewFlow��ָʾ��*/
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