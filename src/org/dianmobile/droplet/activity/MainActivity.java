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
 * 首页
 * 显示习惯及其记录
 * 习惯数量不足四个时显示添加界面
 * 
 * @author FreeTymeKiyan
 * @version 0.0.2
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

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
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
				/*TODO 跳转到设置页面*/
			}
		});
		/*下方导航栏求监督的按钮*/
		btnSupervise = (Button) findViewById(R.id.btn_left);
		btnSupervise.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*TODO 跳转到求监督页面*/
			}
		});
		/*下方导航栏谈感受的按钮*/
		btnFeelings = (Button) findViewById(R.id.btn_center);
		btnFeelings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*TODO 跳转到谈感受页面*/
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
		viewFlow.setAdapter(new MainActivityAdapter());
		/*viewFlow的指示器*/
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
