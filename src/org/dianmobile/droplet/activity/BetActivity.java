package org.dianmobile.droplet.activity;

import org.dianmobile.droplet.R;

import android.app.Activity;
import android.os.Bundle;

/**
 * ��ලҳ��
 * ��ϰ�ߴ���ҳ������
 * 
 * @author FreeTymeKiyan
 * @version 0.0.2
 */
public class BetActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create); // �봴��ҳ������
		prepareData();
	}

	/**
	 * ׼��ҳ��ؼ����������
	 * ���磺ϰ�����ݡ���Լ���ݡ�@�б�����״̬������ʱ��
	 */
	private void prepareData() {
		// TODO
	}
}