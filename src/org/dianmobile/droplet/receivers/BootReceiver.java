package org.dianmobile.droplet.receivers;

import static org.dianmobile.droplet.constants.Constants.*;
import static org.dianmobile.droplet.db.HabitDb.NAME;
import static org.dianmobile.droplet.db.HabitDb.PUNISH;
import static org.dianmobile.droplet.db.HabitDb.TIME;
import static org.dianmobile.droplet.db.HabitDb.UUID;

import java.util.Calendar;

import org.dianmobile.droplet.db.HabitDb;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * ���տ����Ĺ㲥
 * 
 * @author FreeTymeKiyan
 * @version 0.0.3
 */
public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			// ���¼�������ʱ�䣬������һ���ķ�����������ʱ�估������ʱ��
			AlarmManager am = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			intent = new Intent(ALARM_INTENT_ACTION);
			HabitDb hd = new HabitDb(context);
			Bundle b = hd.queryAlarmInfo();
			// ����������������
			for (int i = 0; i < b.size(); i++) {
				Bundle info = b.getBundle(i + "");
				intent.putExtra(UUID, info.getString(UUID));
				String time = info.getString(TIME);
				intent.putExtra(TIME, time);
				intent.putExtra(NAME, info.getString(NAME));
				intent.putExtra(PUNISH, info.getString(PUNISH));
				intent.setClass(context, AlarmReceiver.class);
				PendingIntent sender = PendingIntent.getBroadcast(
						context, 0, intent, PendingIntent
						.FLAG_CANCEL_CURRENT);
				int interval = 24 * 60 * 60 * 1000; // һ��
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.HOUR_OF_DAY, Integer
						.valueOf(time.split(":")[0])); // Сʱ
				calendar.set(Calendar.MINUTE, Integer.valueOf
						(time.split(":")[1])); // ����
				calendar.set(Calendar.SECOND, 0); // ��
				calendar.set(Calendar.MILLISECOND, 0); // ΢��
				if (calendar.getTimeInMillis() > System
						.currentTimeMillis()) {
					calendar.set(Calendar.DAY_OF_YEAR, 
							calendar.get(Calendar.DAY_OF_YEAR) + 1); // ����
				}
				am.setRepeating(AlarmManager.RTC_WAKEUP, 
						calendar.getTimeInMillis(), interval,
						sender);
			}
		}
	}
}
