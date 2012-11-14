package org.dianmobile.droplet.db;

import static android.provider.BaseColumns._ID;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
	private static final String HABIT = "habit";
	/**习惯的全局唯一ID*/
	public static final String UUID = "h_uuid";
	/**习惯的名字*/
	private static final String NAME = "h_name";
	/**习惯的惩罚内容*/
	private static final String PUNISH = "h_punish";
	/**习惯@的人*/
	private static final String FOLLOWERS = "h_followers";
	
	/*日志数据库*/
	/**日志数据库的名字*/
	private static final String LOG = "log";
	/**日志数据库的文字栏*/
	private static final String WORDS = "l_words";
	/**日志数据库的图片路径*/
	private static final String PIC_PATH = "l_picpath";
	/**日志数据库的时间戳*/
	private static final String TIMESTAMP = "l_timestamp";
	
	/*闹钟数据库*/
	/**闹钟数据库的名字*/
	private static final String ALARM = "alarm";
	/**闹钟的状态*/
	public static final String STATE = "a_state";
	/**闹钟的时间*/
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
		Cursor c = db.query(HABIT, null, null, null, null, null, null);
		c.moveToFirst();
		count = c.getCount();
		c.close();
		db.close();
		return count;
	}
}
