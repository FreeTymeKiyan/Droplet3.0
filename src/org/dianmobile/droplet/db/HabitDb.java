package org.dianmobile.droplet.db;

import static android.provider.BaseColumns._ID;
import static org.dianmobile.droplet.constants.Constants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dianmobile.droplet.models.Habit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;

/**
 * ϰ����ص����ݿ�
 * 
 * @author FreeTymeKiyan
 * @version 0.0.9
 */
public class HabitDb extends SQLiteOpenHelper {

	/**ϰ�����ݿ������*/
	private static final String HABIT_DB_NAME = "habit";
	/**ϰ�����ݿ�İ汾*/
	private static final int DB_VERSION = 1;
	/**Ӧ�õ�ǰ�������Ļ���*/
	private Context mContext;
	
	/*ϰ��������ݿ�*/
	/**ϰ��������ݿ������*/
	public static final String HABIT = "habit";
	/**ϰ�ߵ�ȫ��ΨһID*/
	public static final String UUID = "h_uuid";
	/**ϰ�ߵ�����*/
	public static final String NAME = "h_name";
	/**ϰ�ߵĳͷ�����*/
	public static final String PUNISH = "h_punish";
	/**ϰ��@����*/
	public static final String FOLLOWERS = "h_followers";
	
	/*��־���ݿ�*/
	/**��־���ݿ������*/
	public static final String LOG = "log";
	/**��־���ݿ��������*/
	public static final String WORDS = "l_words";
	/**��־���ݿ��ͼƬ·��*/
	public static final String PIC_PATH = "l_picpath";
	/**��־���ݿ��ʱ���*/
	public static final String TIMESTAMP = "l_timestamp";
	
	/*�������ݿ�*/
	/**�������ݿ������*/
	public static final String ALARM = "alarm";
	/**���ӵ�״̬*/
	public static final String STATE = "a_state";
	/**���ӵ�ʱ��*/
	public static final String TIME = "a_time";
	
	public HabitDb(Context context) {
		super(context, HABIT_DB_NAME, null, DB_VERSION);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String crtHabitDb = "create table " + HABIT + " ("
				+ _ID + " integer primary key autoincrement, "
				+ UUID + " text not null, "
				+ NAME + " text not null, "
				+ PUNISH + " text not null, "
				+ FOLLOWERS + " text not null)";
		db.execSQL(crtHabitDb); // ����ϰ��������ݿ�
		String crtLogDb = "create table " + LOG + " ("
				+ _ID + " integer primary key autoincrement, "
				+ UUID + " text not null, "
				+ WORDS + " text not null, "
				+ PIC_PATH + " text not null, "
				+ TIMESTAMP + " DATETIME DEFAULT (datetime('now', 'localtime')))";
		db.execSQL(crtLogDb); // ������־���ݿ�
		String crtAlarmDb = "create table " + ALARM + " ("
				+ _ID + " integer primary key autoincrement, "
				+ UUID + " text not null, "
				+ STATE + " bit, "
				+ TIME + " text not null)";
		db.execSQL(crtAlarmDb); // �����������ݿ�
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String dropHabitDb = "drop table if exists " 
				+ HABIT;
		db.execSQL(dropHabitDb);
		String dropLogDb = "drop table if exists " + LOG;
		db.execSQL(dropLogDb);
		String dropAlarmDb = "drop table if exists " + ALARM;
		db.execSQL(dropAlarmDb);
	}
	
	/**
	 * ��ѯhabit���ܸ���
	 * @return count
	 */
	public int queryHabitCount() {
		int count = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query(HABIT, null, null, null, null, null, 
				null);
		c.moveToFirst();
		count = c.getCount();
		c.close();
		db.close();
		return count;
	}
	
	/**
	 * ����ϰ�ߵ�ʱ��������ݿ� 
	 * ��UUID��NAME��PUNISH��FOLLOWERS���뵽HABIT���ݿ�
	 * ��STATE��TIME���뵽ALARM���ݿ�
	 * @return	true	����ɹ�
	 * 			false	����ʧ��
	 */
	public boolean insertWhenCreate(Habit h) {
		boolean insertOutcome = false;
		SQLiteDatabase db = getReadableDatabase();
		ContentValues cv1 = new ContentValues();
		cv1.put(UUID, h.uuid);
		cv1.put(NAME, h.name);
		cv1.put(PUNISH, h.punish);
		cv1.put(FOLLOWERS, h.followers);
		ContentValues cv2 = new ContentValues();
		cv2.put(UUID, h.uuid);
		cv2.put(STATE, h.alarmState);
		cv2.put(TIME, h.alarmTime);
		ContentValues cv3 = new ContentValues();
		cv3.put(UUID, h.uuid);
		cv3.put(WORDS, "ϰ�ߴ���ս��ʼ�ˣ�");
		cv3.put(PIC_PATH, "");
		if (db.insert(HABIT, null, cv1) != -1
				&& db.insert(ALARM, null, cv2) != -1
				&& db.insert(LOG, null, cv3) != -1) {
			insertOutcome = true;
		}
		db.close();
		return insertOutcome;
	}
	
	/**
	 * @deprecated ���ɾ��ϰ�߻ᵼ�¶�ӦID����һ��û�����ݳ���
	 * ��ViewFlow��position�õ�uuid
	 * 
	 * @param 	int		position
	 * @return	String
	 */
	public String queryUuidWithPosition(int position) {
		String uuid = null;
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(HABIT, new String[]{UUID}, _ID + "=?", 
				new String[]{position + ""}, null, null, 
				"_id DESC");
		c.moveToFirst();
		uuid = c.getString(0);
//		System.out.println("uuid" + uuid);
		c.close();
		return uuid;
	}

	/**
	 * ��uuid��ѯ��ϸ��־
	 * 
	 * @param uuid
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> queryLogWithUuid(String uuid) {
		SQLiteDatabase db = getReadableDatabase();
		ArrayList<Map<String, Object>> list = new ArrayList<
				Map<String,Object>>();
		Cursor c = db.query(LOG, new String[]{WORDS, PIC_PATH, 
				TIMESTAMP}, UUID + "=?", new String[]{uuid}, 
				null, null, "_id desc");
		if (c != null) {
			if (c.getCount() != 0) {
				System.out.println("c != null c != 0");
				for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("tv_habitWords", c.getString(0));
					map.put("iv_habitPic", c.getString(1));
					map.put("tv_timestamp", c.getString(2));
					list.add(map);
				}
			} else {
				System.out.println("c == 0");
			}
		} else {
			System.out.println("no log found");
		}
		db.close();
		return list;
	}
	
	/**
	 * ��uuid��ѯ��ϰ����ص���Ϣ
	 * 
	 * @param uuid
	 * @return bundle
	 */
	public Bundle queryHeaderWithUuid(String uuid) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(HABIT, null, UUID + "=?", 
				new String[]{uuid}, null, null, "_id asc");
		c.moveToFirst();
		Bundle bundle = new Bundle(); 
		if (c.getCount() != 0) {
			bundle.putString(UUID, c.getString(1));
			bundle.putString(NAME, c.getString(2));
			bundle.putString(PUNISH, c.getString(3));
			bundle.putString(FOLLOWERS, c.getString(4));
		} else {
			System.out.println("queryHeaderWithUuid c.getCount() == 0");
		}
		db.close();
		return bundle;
	}
	
	/**
	 * ����˳�򷵻����е�uuid
	 * ���Ҷ�Ӧposition
	 * 
	 * @return list
	 */
	public ArrayList<String> queryUuids() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(HABIT, new String[]{UUID}, null, 
				null, null, null, "_id ASC");
		ArrayList<String> list = new ArrayList<String>();
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
//			System.out.println("getString " + c.getString(0));
			list.add(c.getString(0));
		}
		db.close();
		return list;
	}
	
	/**
	 * ����Ӧ�ļ�¼��Ϣ����Log���ݿ�
	 * 
	 * @return	true	��Ӽ�¼�ɹ�
	 * 			false	��Ӽ�¼ʧ��
	 */
	public boolean insertLog(Habit h) {
		boolean result = false;
		SQLiteDatabase db = getReadableDatabase();
		/*�ѵõ������ݲ���LOG���ݿ�*/
		ContentValues cv = new ContentValues();
		cv.put(UUID, h.uuid);
		cv.put(WORDS, h.words);
		cv.put(PIC_PATH, h.picPath);
		if (db.insert(LOG, null, cv) != -1) { // ����ɹ�
			result = true;
		}
		db.close();
		return result;
	}
	
	/**
	 * ��ල����ͨ��UUID����ѯ��Ϣ�ķ���
	 * ���ص���Ϣ������ϰ�ߵı��⡢ϰ�ߵ�����״̬��ϰ�ߵ�����ʱ��
	 * 
	 * @return
	 */
	public Bundle queryBetInfoWithUuid(String uuid) {
		Bundle result = new Bundle();
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(HABIT, new String[]{NAME}, 
				UUID + "=?", new String[]{uuid}, null, null,
				"_id asc");
		c.moveToFirst();
		String name = c.getString(0);
		c = db.query(ALARM, new String[]{STATE, TIME}, 
				UUID + "=?" , new String[]{uuid}, null, null, 
				"_id asc");
		c.moveToFirst();
		int state = c.getInt(0);
		String time = c.getString(1);
		c.close();
		db.close();
		result.putString(NAME, name);
		result.putInt(STATE, state);
		result.putString(TIME, time);
//		System.out.println(NAME + ":" + name);
//		System.out.println(STATE + ":" + state);
//		System.out.println(TIME + ":" + time);
		return result;
	}
	
	/**
	 * ��ල���棬��UUID����ϰ����Ϣ
	 * 
	 * @param uuid
	 * @param info
	 * @return	false	���³ɹ�
	 * 			true	����ʧ��
	 */
	public boolean updateHabitInfoWithUuid(String uuid, 
			ContentValues info) {
		boolean result = false;
		SQLiteDatabase db = getReadableDatabase();
		/*����ϰ�ߵĶ�Լ*/
		ContentValues updateHabit = new ContentValues();
		updateHabit.put(PUNISH, info.getAsString(PUNISH));
		updateHabit.put(FOLLOWERS, info.getAsString(FOLLOWERS));
		/*�������ӵ���Ϣ*/
		ContentValues updateAlarm = new ContentValues();
		updateAlarm.put(STATE, info.getAsInteger(STATE));
		updateAlarm.put(TIME, info.getAsString(TIME));
		/*����һ����־*/
		ContentValues insertBetLog = new ContentValues();
		insertBetLog.put(UUID, uuid);
		insertBetLog.put(WORDS, "���������ˣ���ͬ�����˴μල~");
		insertBetLog.put(PIC_PATH, "");
		if (db.update(HABIT, updateHabit, UUID + "=?", 
				new String[]{uuid}) > 0 && db.update(ALARM,
				updateAlarm, UUID + "=?", new String[]{uuid})
				> 0 && db.insert(LOG, null, insertBetLog) != -1) {
			result = true;
		}
		db.close();
		return result;
	}

	/**
	 * ����ʱ��ѯ���ݿ�
	 * ����������ص���Ϣ������UUID��TIME��NAME��PUNISH
	 * 
	 * @return
	 */
	public Bundle queryAlarmInfo() {
		Bundle b = new Bundle();
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(ALARM, new String[]{UUID, TIME}, 
				STATE + "=?", new String[]{ALARM_STATE_ON + ""},
				null, null, "_id asc");
		int i = 0;
		while (c.moveToNext()) {
			Bundle temp = new Bundle();
			temp.putString(UUID, c.getString(0));
			temp.putString(TIME, c.getString(1));
			String uuid = c.getString(0);
			Cursor c1 = db.query(HABIT, new String[]{NAME,
					PUNISH}, UUID + "=?", new String[]{uuid}, null, null, null);
			c1.moveToNext();
			temp.putString(NAME, c1.getString(0));
			temp.putString(PUNISH, c1.getString(1));
			c1.close();
			b.putBundle(i++ + "", temp);
		}
		c.close();
		db.close();
		return b;
	}
}