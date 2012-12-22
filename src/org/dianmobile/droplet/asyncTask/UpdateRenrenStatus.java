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
		status = "�����û" + strHabitTitle + "��" 
				+ "��ô�Ҿ�" + strHabitPunishment 
				+ "����ල��" + processAtString(strAtFriendInfo);
		updateResult = RenrenHelper.updateStatus(status);
		return null;
	}
	
	/**
	 * ����&id|name&id|name��ʽ
	 * ���ؿ���@���ѵ��ַ���
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