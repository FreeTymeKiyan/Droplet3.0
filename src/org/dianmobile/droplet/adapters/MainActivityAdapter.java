package org.dianmobile.droplet.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dianmobile.droplet.R;
import org.dianmobile.droplet.activity.CreateActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

/**
 * ������ViewFlow��������
 * ��Ҫ���䲻ͬ�Ľ��浽�����浱��
 * ��Ҫ��һ��ϰ�߽��棬һ����ӽ���
 * 
 * @author FreeTymeKiyan
 * @version 0.0.2
 */
public class MainActivityAdapter extends BaseAdapter {
	
	/**�����ļ������*/
	private LayoutInflater mInflater;
	/**view������*/
	private int viewCount;
	/**��ǰ�������Ļ���*/
	private Context mContext;
	/**ϰ����ϸ��¼������Դ*/
	private List<Map<String, Object>> habitDetailsItem
			= new ArrayList<Map<String, Object>>();
	/**ϰ����ϸ��¼������Դ����*/
	private Map<Integer, List<Map<String, Object>>> habitDetailsItems;
	
	public MainActivityAdapter(Context context, int habitCount) {
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context
						.LAYOUT_INFLATER_SERVICE);
		viewCount = habitCount + 1;
	}

	@Override
	public int getItemViewType(int position) {
		return position;
	}
	
	@Override
	public int getViewTypeCount() {
		return viewCount;
	}
	
	@Override
	public int getCount() {
		return viewCount;
	}

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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		System.out.println("getView--->start");
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
		ViewHolder holder = null;
		holder = new ViewHolder();
		if (convertView == null) {
			if (position == viewCount - 1) {
				convertView = mInflater.inflate(R.layout
						.add_habit_view, null);
				/*�����ϰ�ߵİ�ť*/
        		ImageButton addNewHabitBtn = (ImageButton) 
        				convertView.findViewById(R.id
        						.ib_addNewHabit);
        		holder.ibAddNew = addNewHabitBtn;
        		addNewHabitBtn.setOnClickListener(new 
        				OnClickListener() {
        			
        			@Override
        			public void onClick(View v) {
        				/*��ת�������ϰ�ߵ�ҳ��*/
        				Intent i = new Intent();
        				i.setClass(mContext, CreateActivity.class);
        				mContext.startActivity(i);
        			}		
        		});
        		System.out.println("getView--->add habit view");
			} else {
				convertView = mInflater.inflate(R.layout
						.habit_view, null);
				holder.mProgressBar = (ProgressBar) convertView
						.findViewById(R.id.progress);
				/*ϰ�ߵ���ϸ��¼*/
				ListView lvHabitDetails = (ListView) 
						convertView.findViewById(R.id
								.lv_habitDetails);
				View headerView = mInflater.inflate(
						R.layout.list_view_header, null);
				// TODO ����headerView�ĸ�������
				lvHabitDetails.addHeaderView(headerView, null, false);
				/*habitDetails��������*/
				String[] from = new String[]{"tv_habitWords", 
						"iv_habitPic", "tv_timestamp"}; 
		        int[] to = new int[]{R.id.tv_habitWords, 
		        		R.id.iv_habitPic, R.id.tv_timestamp}; 
		        List<Map<String,Object>> item = getItem(position);
		        if (item != null) {
		        	holder.mProgressBar.setVisibility(View.GONE);
		        	holder.lvHabitDetails.setVisibility(View.VISIBLE);
				} else {
					new LoadHabitLogItem().execute(position, 
							convertView);
					holder.lvHabitDetails.setVisibility(View.GONE);
					holder.mProgressBar.setVisibility(View.VISIBLE);
				}
				HabitDetailsAdapter habitDetailsAdapter = new HabitDetailsAdapter(
						mContext, habitDetailsItem, 
						R.layout.list_view_item, from, to);
				lvHabitDetails.setAdapter(habitDetailsAdapter);
				holder.lvHabitDetails = lvHabitDetails;
				System.out.println("getView--->habit view");
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
	 * @version 0.0.2
	 */
	private class LoadHabitLogItem extends AsyncTask<Object, Object, Object> {

		private Integer position;
		private View convertView;
		
		@Override
		protected Object doInBackground(Object... params) {
			position = (Integer) params[0];
			convertView = (View) params[1];
			// TODO ����position�õ�ϰ�ߵ�uuid
			// TODO ����uuid�����ݿ⵱��ȥ��ѯ���
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			// TODO �õ��Ľ���洢��habitDetailsItem��
//			habitDetailsItem
			// ��habitDetailsItem�浽habitDetailsItems��
			habitDetailsItems.put(position, habitDetailsItem);
			// ���»���view
			drawView(position, convertView);
			// view ˢ��
			convertView.postInvalidate();
		}
		
	}
}