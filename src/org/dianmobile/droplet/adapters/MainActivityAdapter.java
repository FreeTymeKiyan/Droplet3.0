package org.dianmobile.droplet.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.dianmobile.droplet.db.HabitDb.*;
import static org.dianmobile.droplet.constants.Constants.*;

import org.dianmobile.droplet.R;
import org.dianmobile.droplet.activity.CreateActivity;
import org.dianmobile.droplet.db.HabitDb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ������ViewFlow��������
 * ��Ҫ���䲻ͬ�Ľ��浽�����浱��
 * ��Ҫ��һ��ϰ�߽��棬һ����ӽ���
 * 
 * @author FreeTymeKiyan
 * @version 0.0.11
 */
public class MainActivityAdapter extends BaseAdapter {
	/**��ʾ��@���ѵ������ַ�������*/
	private static final int FOLLOWER_STR_LENGTH = 20;
	
	/**�����ļ������*/
	private LayoutInflater mInflater;
	/**view������*/
	private int viewCount;
	/**��ǰ�������Ļ���*/
	private Context mContext;
	/**ϰ����ϸ��¼������Դ����*/
	private Map<Integer, List<Map<String, Object>>> 
			habitDetailsItems = new HashMap<Integer, 
					List<Map<String,Object>>>();
	/**MainActivity��һ��ʵ��*/
	private Activity mActivity;
	/**λ�ú�uuid��һһ��Ӧ*/
	private ArrayList<String> uuids;
	/**�б�ÿһ�������ݵĿؼ���*/
	private final static String[] from = new String[]{"tv_habitWords", 
			"iv_habitPic", "tv_timestamp"}; 
	/**�б�ÿһ�������ݵĿռ���*/
	private final static int[] to = new int[]{R.id.tv_habitWords, 
    		R.id.iv_habitPic, R.id.tv_timestamp};
	
	public MainActivityAdapter(Context context, int habitCount,
			Activity activity) {
		mContext = context;
		mInflater = (LayoutInflater) context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		viewCount = habitCount + 1; // ���������ϰ�߽��棬����+1
		mActivity = activity;
		/*�õ�position��UUID��һһ��Ӧ*/
		if (uuids == null) {
			HabitDb hd = new HabitDb(mContext);
			uuids = hd.queryUuids(); // �����˳������ϰ��UUID������
		}
	}

	/**
	 * ���ض�Ӧitem��view����
	 */
	@Override
	public int getItemViewType(int position) {
		return position;
	}
	
	/**
	 * ���ز�ͬview����������
	 */
	@Override
	public int getViewTypeCount() {
		return viewCount;
	}
	
	/**
	 * ����view������
	 */
	@Override
	public int getCount() {
		return viewCount;
	}

	/**
	 * ���ض�Ӧpositionҳ������ݣ���item
	 */
	@Override
	public List<Map<String, Object>> getItem(int position) {
		if (habitDetailsItems.containsKey(position)) {
			return habitDetailsItems.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * ��ȡ��Ӧҳ���view
	 */
	@Override
	public View getView(int position, View convertView, 
			ViewGroup parent) {
//		System.out.println("getView--->start");
		return drawView(position, convertView);
	}
	
	/**
	 * ���������convertView
	 * 
	 * @param position
	 * @param convertView
	 * @return	View
	 */
	private View drawView(int position, View convertView) {
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			if (position == viewCount - 1) { // ���һҳ�����ϰ��
				convertView = mInflater.inflate(R.layout
						.add_habit_view, null);
				/*�����ϰ�ߵİ�ť*/
        		holder.ibAddNew = (ImageButton) convertView
        				.findViewById(R.id.ib_addNewHabit);
        		holder.ibAddNew.setOnClickListener(new 
        				OnClickListener() {
        			
        			@Override
        			public void onClick(View v) {
        				if (viewCount < 6) { // ϰ������û�ﵽ���
        					/*��ת�������ϰ�ߵ�ҳ��*/
        					Intent i = new Intent();
        					i.setClass(mContext, CreateActivity.class);
        					mActivity.startActivityForResult(i, 
        							REQUEST_CODE_REFRESH_VIEW);
						} else { // ϰ�������ﵽ���
							Toast.makeText(mContext, 
									R.string.toast_max_reached,
									Toast.LENGTH_SHORT).show();
						}
        			}		
        		});
//        		System.out.println("getView--->add habit view");
			} else { // ϰ��ҳ
				convertView = mInflater.inflate(R.layout
						.habit_view, null);
				holder.mProgressBar = (ProgressBar) convertView
						.findViewById(R.id.progress);
				/*ϰ�ߵ���ϸ��¼*/
				holder.lvHabitDetails = (ListView) 
						convertView.findViewById(R.id
								.lv_habitDetails);
				refreshView(position, convertView, holder);
//				System.out.println("getView--->habit view");
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	/**
	 * ������ʱ�洢view����
	 * 
	 * @author FreeTymeKiyan
	 * @version 0.0.1
	 */
	private class ViewHolder {
		ProgressBar mProgressBar;
		ListView lvHabitDetails;
		ImageButton ibAddNew;
	}
	
	/**
	 * �첽��ȡ���ݿ����ݵ���
	 * 
	 * @author FreeTymeKiyan
	 * @version 0.0.6
	 */
	private class LoadHabitLogItem extends AsyncTask<Object, Object, Object> {

		/**��ǰҳ���λ��*/
		private Integer position;
		/**��ǰҳ���view*/
		private View convertView;
		/**��ǰҳ���viewHolder*/
		private ViewHolder holder;
		/**���ڴ浱ǰҳ��header��Ϣ��bundle*/
		private Bundle bundle = new Bundle();
		private String followers = "";
		
		@Override
		protected Object doInBackground(Object... params) {
			position = (Integer) params[0];
			convertView = (View) params[1];
			holder = (ViewHolder) params[2];
			/*����position�õ�ϰ�ߵ�uuid*/
//			System.out.println("position" + position);
			String uuid = uuids.get(position);
//			System.out.println("UUID " + uuid);
			if (uuid != null) {
				/* �õ�uuid����ѯ������־
				 * �õ��Ľ���洢��habitDetailsItem�� */
				HabitDb hd = new HabitDb(mContext);
				/*��habitDetailsItem�浽habitDetailsItems��*/
				habitDetailsItems.put(position, hd
						.queryLogWithUuid(uuid));
				bundle = hd.queryHeaderWithUuid(uuid);
				/*����followersΪ���ֵĸ�ʽ*/
				followers = processFollowers(bundle.getString(FOLLOWERS));
			} else { // uuid��ѯʧ��
				System.out.println("MainActivityAdapter: " +
						"uuid query failed");
			}
			return null;
		}
		
		/**
		 * ����followersΪ"����"�ĸ�ʽ
		 * 
		 * @param string
		 * @return
		 */
		private String processFollowers(String string) {
			String result = "";
			String[] infoArray = string.split("&");
			for (int i = 0; i < infoArray.length - 1; i++) {
				result = infoArray[i + 1].split("\\|")[1] + " ";
			}
			if (result.length() > FOLLOWER_STR_LENGTH) {
				result = result.substring(0, 19) + "...";
			}
			result = "@" + result;
			return result;
		}

		@Override
		protected void onPostExecute(Object result) {
			/*����headerView�ĸ�������*/			
			View headerView = mInflater.inflate(
					R.layout.list_view_header, null);
			holder.lvHabitDetails.addHeaderView(headerView, null,
					false); // ��listView���header
			TextView tvHeaderHabit = (TextView)headerView
					.findViewById(R.id.tv_habitName);
			TextView tvHeaderPunish = (TextView)headerView
					.findViewById(R.id.tv_punish);
			TextView tvHeaderFollower = (TextView) headerView
					.findViewById(R.id.tv_follower);
			tvHeaderHabit.setText(bundle.getString(NAME));
			tvHeaderPunish.setText(bundle.getString(PUNISH));
			tvHeaderFollower.setText(followers);
			/*���»���view*/
			refreshView(position, convertView, holder);
		}
		
	}

	/**
	 * �����ݶ�ȡ֮��ˢ�½���
	 * 
	 * @param position
	 * @param convertView
	 */
	private void refreshView(Integer position, View convertView,
			ViewHolder holder) {
        List<Map<String, Object>> item = getItem(position);
        if (item != null) { // �Ѿ���ȡ����Ӧҳ�������
//        	System.out.println("item != null");
        	holder.mProgressBar.setVisibility(View.GONE);
        	holder.lvHabitDetails.setVisibility(View.VISIBLE);
        	/*��habitDetails��������*/
	        System.out.println("habitDetailsItem " + position);
			HabitDetailsAdapter habitDetailsAdapter = new 
					HabitDetailsAdapter(mContext, item, 
							R.layout.list_view_item, from, to);
			holder.lvHabitDetails.setAdapter(habitDetailsAdapter);
		} else { // ��Ӧҳ������ݻ�δ��ȡ
			/*�ı�ProgressBar��ListView��Visibility*/
			holder.lvHabitDetails.setVisibility(View.GONE);
			holder.mProgressBar.setVisibility(View.VISIBLE);
//			System.out.println("position:" + position);
			new LoadHabitLogItem().execute(position, 
					convertView, holder);
		}
        convertView.postInvalidate();
	}
	
	/**
	 * ��ȡUUID��Positionһһ��Ӧ���б�
	 * 
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getUuids() {
		return uuids;
	}
}