package org.dianmobile.droplet.activity;

import org.dianmobile.droplet.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * ����ҳ��
 * ���Թ���������֤
 * ���Է���ϰ��
 * ����һ��������������
 * ���Ե������Ƹ�������
 * 
 * @author FreeTymeKiyan
 * @version 0.0.1
 */
public class PrefActivity extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pref);
	}
}
