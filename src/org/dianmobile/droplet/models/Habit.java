package org.dianmobile.droplet.models;

import static org.dianmobile.droplet.db.HabitDb.*;

import android.content.ContentValues;

/**
 * 用现有的信息生成Habit对象方便进行传递
 * 
 * @author FreeTymeKiyan
 * @version 0.0.3
 */
public class Habit {
    /*成员变量*/
	/**全局唯一标识ID*/
	public String uuid;
	/**习惯的内容*/
	public String name;
	/**习惯对应的惩罚措施*/
	public String punish;
	/**习惯@的人*/
	public String followers;
	/**习惯的记录*/
	public String words;
	/**习惯的图片*/
	public String picPath;
	/**记录的时间戳*/
	public String timestamp;
	/**闹钟的状态*/
	public int alarmState = STATE_OFF;
	/**闹钟的时间*/
	public String alarmTime;
	
	/*成员变量是否存在的状态*/
	/**全局唯一标识ID的状态*/
	public boolean boolUuid = false;
	/**习惯的内容的状态*/
	public boolean boolName = false;
	/**习惯对应的惩罚措施的状态*/
	public boolean boolPunish = false;
	/**习惯@的人的状态*/
	public boolean boolFollowers = false;
	/**习惯的记录的状态*/
	public boolean boolWords = false;
	/**习惯的图片的状态*/
	public boolean boolPicPath = false;
	/**记录的时间戳的状态*/
	public boolean boolTimestamp = false;
	/**闹钟的状态的状态*/
	public boolean boolAlarmState = false;
	/**闹钟的时间的状态*/
	public boolean boolAlarmTime = false;
	
	public Habit(ContentValues cv) {
		if (cv.containsKey(UUID)) {
			uuid = cv.getAsString(UUID);
			boolUuid = true;
		}
		if (cv.containsKey(NAME)) {
			name = cv.getAsString(NAME);
			boolName = true;
		}
		if (cv.containsKey(PUNISH)) {
			punish = cv.getAsString(PUNISH);
			boolPunish = true;
		}
		if (cv.containsKey(FOLLOWERS)) {
			followers = cv.getAsString(FOLLOWERS);
			boolFollowers = true;
		}
		if (cv.containsKey(WORDS)) {
			words = cv.getAsString(WORDS);
			boolWords = true;
		}
		if (cv.containsKey(TIMESTAMP)) {
			timestamp = cv.getAsString(TIMESTAMP);
			boolTimestamp = true;
		}
		if (cv.containsKey(PIC_PATH)) {
			picPath = cv.getAsString(PIC_PATH);
			boolPicPath = true;
		}
		if (cv.containsKey(STATE)) {
			alarmState = cv.getAsInteger(STATE);
			boolAlarmState = true;
		}
		if (cv.containsKey(TIME)) {
			alarmTime = cv.getAsString(TIME);
			boolAlarmTime = true;
		}
	}
}