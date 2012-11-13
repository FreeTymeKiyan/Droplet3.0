package org.dianmobile.droplet.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ϰ����ص����ݿ�
 * 
 * @author FreeTymeKiyan
 * @version 0.0.2
 */
public class HabitDb extends SQLiteOpenHelper{

	/**ϰ�����ݿ������*/
	private static final String HABIT_DB_NAME = "habit";
	/**ϰ�����ݿ�İ汾*/
	private static final int DB_VERSION = 1;
	/**Ӧ�õ�ǰ�������Ļ���*/
	private Context mContext;
	
	public HabitDb(Context context) {
		super(context, HABIT_DB_NAME, null, DB_VERSION);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String crtHabitLogDb = "";
		String crtHabitStatDb = "";
		String crtSingleHabitStatDb = "";
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String dropHabitDb = "drop table if exists " 
				+ HABIT_DB_NAME;
		db.execSQL(dropHabitDb);
	}
}
