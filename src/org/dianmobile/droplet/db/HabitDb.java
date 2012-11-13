package org.dianmobile.droplet.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 习惯相关的数据库
 * 
 * @author FreeTymeKiyan
 * @version 0.0.2
 */
public class HabitDb extends SQLiteOpenHelper{

	/**习惯数据库的名字*/
	private static final String HABIT_DB_NAME = "habit";
	/**习惯数据库的版本*/
	private static final int DB_VERSION = 1;
	/**应用当前的上下文环境*/
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
