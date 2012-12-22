package org.dianmobile.droplet.activity;

import org.dianmobile.droplet.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * 设置页面
 * 可以管理人人认证
 * 可以放弃习惯
 * 可以一键管理所有闹钟
 * 可以单独控制各个闹钟
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
