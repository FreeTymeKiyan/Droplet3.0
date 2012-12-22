package org.dianmobile.droplet.adapters;

import static org.dianmobile.droplet.constants.Constants.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dianmobile.droplet.R;
import org.dianmobile.droplet.utils.Utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ���˺����б������������
 * 
 * @author FreeTymeKiyan
 * @version 0.0.1
 */
public class FriendsListAdapter extends BaseAdapter {
	
	/**����������ӳ��*/
	private HashMap<Integer, View> viewMap = new HashMap
			<Integer, View>();
	/**���������*/
	private LayoutInflater mInflater;
	/**����Դ*/
    private List<Map<String, Object>> list;
    /**��ǰ�������Ļ���*/
    private Context mContext;
    /**�����������淢����Ϣ��Handler*/
    private Handler mHandler;
    /**���˻����Ŀ¼*/
	private String renrenCache;
	/**���ѱ�@��״̬*/
	private Bundle atFriendsStateMap;
	/**���@��������*/
	private static final int MAX_FRIENDS_NO = 6;
	
	/**
	 * ������
	 * 
	 * @param friendsInfo
	 * @param context
	 * @param handler
	 * @param atFriendInfo 
	 */
	public FriendsListAdapter(List<Map<String, Object>> 
			friendsInfo, Context context, Handler handler, Bundle atFriendInfo) {
		list = friendsInfo;
		mContext = context;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mHandler = handler;
		Utils.createRenrenCache(mContext);
		renrenCache = Utils.getRenrenCacheDir();
		atFriendsStateMap = atFriendInfo;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (viewMap.get(position) == null) { // û���г�ʼ��
			convertView = mInflater.inflate(
					R.layout.list_item_renren_friends, null);
			holder.ivFriendHead = (ImageView) convertView
					.findViewById(R.id.iv_friendHead);
			int id = (Integer) list.get(position).get(RENREN_ID);
			File head = new File(renrenCache + id);
			if (head.exists()) { // ��������ͷ���ļ�
				// �õ�bmp������ͷ��
				holder.ivFriendHead.setImageURI(Uri.fromFile(head));
			} else { // ������û��ͷ���ļ�
				new LoadFriendHead().execute(
						holder.ivFriendHead,
						id, 
	            		list.get(position).get(RENREN_HEAD_URL));
			}
			holder.tvFriendName = (TextView) convertView
					.findViewById(R.id.tv_friendName);
			holder.tvFriendName.setText((String)list.get(position)
            		.get(RENREN_NAME));
			holder.ivChooseState = (ImageView) convertView
					.findViewById(R.id.iv_chooseState);
			String strAtState = atFriendsStateMap.getString(position + "");
			if (strAtState == null) {
				holder.ivChooseState.setVisibility(View.GONE);
			} else {
				holder.ivChooseState.setVisibility(View.VISIBLE);
			}
			addListener(convertView, position, holder.ivChooseState);
			convertView.setTag(holder);
			viewMap.put(position, convertView);
		} else { // �Ѿ���ʼ������
			convertView = viewMap.get(position);
			holder = (ViewHolder) viewMap.get(position).getTag();
		}
		return convertView;
	}
	
	/**
	 * �ı����ѱ�@��״̬
	 * 
	 * @param position
	 * @param item
	 */
	private void changeAtFriendState(int position, Map<String, 
			Object> item, ImageView iv) {
		String strPos = position + "";
		if (atFriendsStateMap.getString(strPos) == null
				&& atFriendsStateMap.size() < MAX_FRIENDS_NO) {
			iv.setVisibility(View.VISIBLE);
			String info = (Integer)item.get(RENREN_ID) + "|" +
					(String)item.get(RENREN_NAME);
			System.out.println("info:" + info);
			atFriendsStateMap.putString(strPos, info);
		} else if (atFriendsStateMap.getString(strPos) != null) {
			iv.setVisibility(View.GONE);
			atFriendsStateMap.remove(strPos);
			System.out.println("removed!");
		} else {
			Toast.makeText(mContext, "���@" + MAX_FRIENDS_NO
					+ "������", Toast.LENGTH_SHORT).show();
		}
		refreshEditText();
	}
	
	/**
	 * ֻ��Ҫ����Ҫ���ü����¼������д�������ⷽ���� 
	 * ��Ĳ���Ҫ�޸�
	 * 
	 * @param position 
	 */
	public void addListener(View convertView, int position,
			final ImageView iv) {
		final int p = position;
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("convert view cb ischecked");
				changeAtFriendState(p, list.get(p), iv);
			}
		});
	}
	
	/**
	 * ͨ��handler����淢��ˢ�µ���Ϣ
	 */
	private void refreshEditText() {
		mHandler.sendEmptyMessage(MSG_REFRESH_EDITTEXT);
	}
	
	/**
	 * ����@������ص�����
	 * 
	 * @return
	 */
	public Bundle getAtFriendsState() {
		return atFriendsStateMap;
	}

	private class ViewHolder {
		ImageView ivFriendHead;
		TextView tvFriendName;
		ImageView ivChooseState;
	}
	
	private class LoadFriendHead extends AsyncTask<Object, Object, Object> {
		
		private ImageView iv;
		private int id;
		private String headUrl;
		private File head;

		@Override
		protected Object doInBackground(Object... params) {
			iv = (ImageView) params[0];
			id = (Integer) params[1];
			headUrl = (String) params[2];
			try {
				URL url = new URL(headUrl);
				HttpURLConnection conn = (HttpURLConnection) 
						url.openConnection();
				conn.setConnectTimeout(10000);
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				if (conn.getResponseCode() == 200) {
					InputStream is = conn.getInputStream();
					head = new File(renrenCache + id);
					if (!head.exists()) {
						head.createNewFile();
						FileOutputStream fos = new 
								FileOutputStream(head);
						byte[] buf = new byte[1024];
						int len;
						while((len = is.read(buf)) > 0) {
							fos.write(buf, 0, len);
						}
						fos.flush();
						fos.close();
					}
					is.close();
				}
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			iv.setImageURI(Uri.fromFile(head));
			super.onPostExecute(result);
		}
	}
}
