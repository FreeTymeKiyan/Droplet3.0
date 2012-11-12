package org.dianmobile.droplet.activity;

import org.dianmobile.droplet.R;
import org.dianmobile.droplet.adapters.MainActivityAdapter;
import org.taptwo.android.widget.CircleFlowIndicator;
import org.taptwo.android.widget.ViewFlow;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * ��ҳ
 * ��ʾϰ�߼����¼
 * ϰ�����������ĸ�ʱ��ʾ��ӽ���
 * 
 * @author FreeTymeKiyan
 * @version 0.0.2
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

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
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
				/*TODO ��ת������ҳ��*/
			}
		});
		/*�·���������ල�İ�ť*/
		btnSupervise = (Button) findViewById(R.id.btn_left);
		btnSupervise.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*TODO ��ת����ලҳ��*/
			}
		});
		/*�·�������̸���ܵİ�ť*/
		btnFeelings = (Button) findViewById(R.id.btn_center);
		btnFeelings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*TODO ��ת��̸����ҳ��*/
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
		viewFlow.setAdapter(new MainActivityAdapter());
		/*viewFlow��ָʾ��*/
		CircleFlowIndicator flowIndicator = (CircleFlowIndicator) 
				findViewById(R.id.vfi_circles);
		viewFlow.setFlowIndicator(flowIndicator);
	}

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_main, menu);
//        return true;
//    }
}
