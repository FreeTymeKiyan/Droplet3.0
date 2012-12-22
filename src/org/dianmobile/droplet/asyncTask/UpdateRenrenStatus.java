package org.dianmobile.droplet.asyncTask;

import org.dianmobile.droplet.R;
import org.dianmobile.droplet.utils.RenrenHelper;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class UpdateRenrenStatus extends AsyncTask<Object, Object, Object> {

	private Context mContext;
	private String status;
	private boolean updateResult;
	
	@Override
	protected Object doInBackground(Object... params) {
		mContext = (Context)params[0];
		String strHabitTitle = (String)params[1];
		String strHabitPunishment = (String)params[2];
		String strAtFriendInfo = (String)params[3];
		status = "如果我没" + strHabitTitle + "，" 
				+ "那么我就" + strHabitPunishment 
				+ "。求监督！" + processAtString(strAtFriendInfo);
		updateResult = RenrenHelper.updateStatus(status);
		return null;
	}
	
	/**
	 * 处理&id|name&id|name格式
	 * 返回可以@好友的字符串
	 * 
	 * @param strAtFriendInfo
	 */
	private String processAtString(String strAtFriendInfo) {
		String strAtFriends = "";
		String[] temp = strAtFriendInfo.split("&");
		for (int i = 0; i < temp.length - 1; i++) {
			String id = temp[i + 1].split("\\|")[0];
			String name = temp[i + 1].split("\\|")[1];
			strAtFriends = strAtFriends + "@" + name
					+ "(" + id + ") ";
		}
		return strAtFriends ;
	}

	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if (updateResult) {
			Toast.makeText(mContext, 
					R.string.toast_renren_update_succeed, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(mContext, 
					R.string.toast_renren_update_failed, Toast.LENGTH_SHORT).show();
		}
	}
}	