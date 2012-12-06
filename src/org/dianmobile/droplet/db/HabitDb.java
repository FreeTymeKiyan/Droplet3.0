package org.dianmobile.droplet.db;

import static android.provider.BaseColumns._ID;

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
import android.os.Bundle;

/**
 * ϰ����ص����ݿ�
 * 
 * @author FreeTymeKiyan
 * @version 0.0.3
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
	/**���ӿ���*/
	public static final int STATE_ON = 1;
	/**���ӹر�*/
	public static final int STATE_OFF = 0;
	
	
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
				null, null, "_id asc");
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
}