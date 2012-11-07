package org.dianmobile.droplet.activity;

import org.dianmobile.droplet.R;

import android.app.Activity;
import android.os.Bundle;

/**
 * 首页
 * 显示习惯及其记录
 * 习惯数量不足四个时显示添加界面
 * 
 * @author FreeTymeKiyan
 * @version 0.0.1
 */
public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_main, menu);
//        return true;
//    }
}
