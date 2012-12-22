package org.dianmobile.droplet.receivers;
import static org.dianmobile.droplet.constants.Constants.*;
import static org.dianmobile.droplet.db.HabitDb.*;

import java.util.Calendar;

import org.dianmobile.droplet.R;
import org.dianmobile.droplet.activity.ShareActivity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 接收提醒的广播
 * 
 * @author FreeTymeKiyan
 * @version 0.0.4
 */
public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ALARM_INTENT_ACTION)) {
			// 第1步中设置的闹铃时间到，这里可以弹出闹铃提示并播放响铃
			showNotification(context, intent);
            // 可以继续设置下一次闹铃时间;
			setNextAlarm(context, intent);
            return;
		}
	}
	
	/**
	 * 设置下一次闹钟
	 * @param context
	 */
	private void setNextAlarm(Context context, Intent intent) {
		System.out.println("set next alarm");
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		PendingIntent sender = PendingIntent.getBroadcast(
				context, 0, intent, PendingIntent
				.FLAG_CANCEL_CURRENT);
		int interval = 24 * 60 * 60 * 1000; // 一天
		String time = intent.getStringExtra(TIME);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get
				(Calendar.DAY_OF_YEAR) + 1); // 明天
		calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf
				(time.split(":")[0])); // 小时
		calendar.set(Calendar.MINUTE, Integer.valueOf
				(time.split(":")[1])); // 分钟
		calendar.set(Calendar.SECOND, 0); // 秒
		calendar.set(Calendar.MILLISECOND, 0); // 微秒
		am.setRepeating(AlarmManager.RTC_WAKEUP, 
				calendar.getTimeInMillis(), interval, sender);
	}
	
	/**
	 * 弹出通知
	 * 
	 * @param context
	 */
	private void showNotification(Context context, Intent intent) {
		System.out.println("show notification");
		// 得到NotificationManager
		NotificationManager nm = (NotificationManager) 
				context.getSystemService(Context.NOTIFICATION_SERVICE);
		// 创建一个新的Notification对象
		int icon = R.drawable.logo;
		long when = System.currentTimeMillis();
		String contentText = "如果我没" + intent.getStringExtra
				(NAME) + "，那么我就" + intent.getStringExtra(PUNISH);
		Notification n = new Notification();
		n.flags |= Notification.FLAG_AUTO_CANCEL;
		n.icon = icon;
		n.tickerText = "快去挑战今天的习惯把！";
		n.when = when;
		Intent notificaitonIntent = new Intent();
		notificaitonIntent.putExtra(UUID, intent.getStringExtra(UUID));
		notificaitonIntent.setClass(context, ShareActivity.class);
		PendingIntent contentIntent = PendingIntent
				.getActivity(context, 0, notificaitonIntent, Intent
						.FLAG_ACTIVITY_NEW_TASK);
		n.setLatestEventInfo(context, "快去挑战今天的习惯吧！", 
				contentText, contentIntent);
		nm.notify(0, n);
	}
}