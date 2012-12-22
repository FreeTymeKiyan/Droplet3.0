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
 * �������ѵĹ㲥
 * 
 * @author FreeTymeKiyan
 * @version 0.0.4
 */
public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ALARM_INTENT_ACTION)) {
			// ��1�������õ�����ʱ�䵽��������Ե���������ʾ����������
			showNotification(context, intent);
            // ���Լ���������һ������ʱ��;
			setNextAlarm(context, intent);
            return;
		}
	}
	
	/**
	 * ������һ������
	 * @param context
	 */
	private void setNextAlarm(Context context, Intent intent) {
		System.out.println("set next alarm");
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		PendingIntent sender = PendingIntent.getBroadcast(
				context, 0, intent, PendingIntent
				.FLAG_CANCEL_CURRENT);
		int interval = 24 * 60 * 60 * 1000; // һ��
		String time = intent.getStringExtra(TIME);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get
				(Calendar.DAY_OF_YEAR) + 1); // ����
		calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf
				(time.split(":")[0])); // Сʱ
		calendar.set(Calendar.MINUTE, Integer.valueOf
				(time.split(":")[1])); // ����
		calendar.set(Calendar.SECOND, 0); // ��
		calendar.set(Calendar.MILLISECOND, 0); // ΢��
		am.setRepeating(AlarmManager.RTC_WAKEUP, 
				calendar.getTimeInMillis(), interval, sender);
	}
	
	/**
	 * ����֪ͨ
	 * 
	 * @param context
	 */
	private void showNotification(Context context, Intent intent) {
		System.out.println("show notification");
		// �õ�NotificationManager
		NotificationManager nm = (NotificationManager) 
				context.getSystemService(Context.NOTIFICATION_SERVICE);
		// ����һ���µ�Notification����
		int icon = R.drawable.logo;
		long when = System.currentTimeMillis();
		String contentText = "�����û" + intent.getStringExtra
				(NAME) + "����ô�Ҿ�" + intent.getStringExtra(PUNISH);
		Notification n = new Notification();
		n.flags |= Notification.FLAG_AUTO_CANCEL;
		n.icon = icon;
		n.tickerText = "��ȥ��ս�����ϰ�߰ѣ�";
		n.when = when;
		Intent notificaitonIntent = new Intent();
		notificaitonIntent.putExtra(UUID, intent.getStringExtra(UUID));
		notificaitonIntent.setClass(context, ShareActivity.class);
		PendingIntent contentIntent = PendingIntent
				.getActivity(context, 0, notificaitonIntent, Intent
						.FLAG_ACTIVITY_NEW_TASK);
		n.setLatestEventInfo(context, "��ȥ��ս�����ϰ�߰ɣ�", 
				contentText, contentIntent);
		nm.notify(0, n);
	}
}