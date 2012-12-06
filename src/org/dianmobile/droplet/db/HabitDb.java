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
 * 习惯相关的数据库
 * 
 * @author FreeTymeKiyan
 * @version 0.0.3
 */
public class HabitDb extends SQLiteOpenHelper {

	/**习惯数据库的名字*/
	private static final String HABIT_DB_NAME = "habit";
	/**习惯数据库的版本*/
	private static final int DB_VERSION = 1;
	/**应用当前的上下文环境*/
	private Context mContext;
	
	/*习惯相关数据库*/
	/**习惯相关数据库的名字*/
	public static final String HABIT = "habit";
	/**习惯的全局唯一ID*/
	public static final String UUID = "h_uuid";
	/**习惯的名字*/
	public static final String NAME = "h_name";
	/**习惯的惩罚内容*/
	public static final String PUNISH = "h_punish";
	/**习惯@的人*/
	public static final String FOLLOWERS = "h_followers";
	
	/*日志数据库*/
	/**日志数据库的名字*/
	public static final String LOG = "log";
	/**日志数据库的文字栏*/
	public static final String WORDS = "l_words";
	/**日志数据库的图片路径*/
	public static final String PIC_PATH = "l_picpath";
	/**日志数据库的时间戳*/
	public static final String TIMESTAMP = "l_timestamp";
	
	/*闹钟数据库*/
	/**闹钟数据库的名字*/
	public static final String ALARM = "alarm";
	/**闹钟的状态*/
	public static final String STATE = "a_state";
	/**闹钟的时间*/
	public static final String TIME = "a_time";
	/**闹钟开启*/
	public static final int STATE_ON = 1;
	/**闹钟关闭*/
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
		db.execSQL(crtHabitDb); // 创建习惯相关数据库
		String crtLogDb = "create table " + LOG + " ("
				+ _ID + " integer primary key autoincrement, "
				+ UUID + " text not null, "
				+ WORDS + " text not null, "
				+ PIC_PATH + " text not null, "
				+ TIMESTAMP + " DATETIME DEFAULT (datetime('now', 'localtime')))";
		db.execSQL(crtLogDb); // 创建日志数据库
		String crtAlarmDb = "create table " + ALARM + " ("
				+ _ID + " integer primary key autoincrement, "
				+ UUID + " text not null, "
				+ STATE + " bit, "
				+ TIME + " text not null)";
		db.execSQL(crtAlarmDb); // 创建闹钟数据库
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
	 * 查询habit的总个数
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
	 * 创建习惯的时候插入数据库 
	 * 将UUID、NAME、PUNISH、FOLLOWERS插入到HABIT数据库
	 * 将STATE、TIME插入到ALARM数据库
	 * @return	true	插入成功
	 * 			false	插入失败
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
		cv3.put(WORDS, "习惯大挑战开始了！");
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
	 * 用ViewFlow的position得到uuid
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
	 * 用uuid查询详细日志
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
	 * 用uuid查询到习惯相关的信息
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