package org.dianmobile.droplet.activity;

import org.dianmobile.droplet.R;

import android.app.Activity;
import android.os.Bundle;

/**
 * 求监督页面
 * 与习惯创建页面类似
 * 
 * @author FreeTymeKiyan
 * @version 0.0.2
 */
public class BetActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create); // 与创建页面类似
		prepareData();
	}

	/**
	 * 准备页面控件所需的数据
	 * 比如：习惯内容、赌约内容、@列表、闹钟状态、闹钟时间
	 */
	private void prepareData() {
		// TODO
	}
}