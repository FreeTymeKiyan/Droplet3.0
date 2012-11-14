package org.dianmobile.droplet.db;

import static android.provider.BaseColumns._ID;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
	private static final String HABIT = "habit";
	/**ϰ�ߵ�ȫ��ΨһID*/
	public static final String UUID = "h_uuid";
	/**ϰ�ߵ�����*/
	private static final String NAME = "h_name";
	/**ϰ�ߵĳͷ�����*/
	private static final String PUNISH = "h_punish";
	/**ϰ��@����*/
	private static final String FOLLOWERS = "h_followers";
	
	/*��־���ݿ�*/
	/**��־���ݿ������*/
	private static final String LOG = "log";
	/**��־���ݿ��������*/
	private static final String WORDS = "l_words";
	/**��־���ݿ��ͼƬ·��*/
	private static final String PIC_PATH = "l_picpath";
	/**��־���ݿ��ʱ���*/
	private static final String TIMESTAMP = "l_timestamp";
	
	/*�������ݿ�*/
	/**�������ݿ������*/
	private static final String ALARM = "alarm";
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
		Cursor c = db.query(HABIT, null, null, null, null, null, null);
		c.moveToFirst();
		count = c.getCount();
		c.close();
		db.close();
		return count;
	}
}
