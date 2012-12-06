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

/**
 * 主界面ViewFlow的适配器
 * 需要适配不同的界面到主界面当中
 * 主要是一个习惯界面，一个添加界面
 * 
 * @author FreeTymeKiyan
 * @version 0.0.3
 */
public class MainActivityAdapter extends BaseAdapter {
	
	/**布局文件填充器*/
	private LayoutInflater mInflater;
	/**view的数量*/
	private int viewCount;
	/**当前的上下文环境*/
	private Context mContext;
	/**习惯详细记录的数据源*/
	private List<Map<String, Object>> habitDetailsItem
			= new ArrayList<Map<String, Object>>();
	/**习惯详细记录的数据源集合*/
	private Map<Integer, List<Map<String, Object>>> 
			habitDetailsItems = new HashMap<Integer, List<Map<String,Object>>>();
	/**MainActivity的一个实例*/
	private Activity mActivity;
	
	public MainActivityAdapter(Context context, int habitCount,
			Activity activity) {
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context
						.LAYOUT_INFLATER_SERVICE);
		viewCount = habitCount + 1;
		mActivity = activity;
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
	public View getView(int position, View convertView, 
			ViewGroup parent) {
		System.out.println("getView--->start");
		return drawView(position, convertView);
	}
	
	/**
	 * 生成所需的convertView
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
				/*添加新习惯的按钮*/
        		ImageButton addNewHabitBtn = (ImageButton) 
        				convertView.findViewById(R.id
        						.ib_addNewHabit);
        		holder.ibAddNew = addNewHabitBtn;
        		addNewHabitBtn.setOnClickListener(new 
        				OnClickListener() {
        			
        			@Override
        			public void onClick(View v) {
        				/*跳转到添加新习惯的页面*/
        				Intent i = new Intent();
        				i.setClass(mContext, CreateActivity.class);
        				mActivity.startActivityForResult(i, 
        						REQUEST_CODE_REFRESH_VIEW);
        			}		
        		});
        		System.out.println("getView--->add habit view");
			} else {
				convertView = mInflater.inflate(R.layout
						.habit_view, null);
				holder.mProgressBar = (ProgressBar) convertView
						.findViewById(R.id.progress);
				/*习惯的详细记录*/
				holder.lvHabitDetails = (ListView) 
						convertView.findViewById(R.id
								.lv_habitDetails);
		        List<Map<String,Object>> item = getItem(position);
		        if (item != null) {
		        	System.out.println("item != null");
		        	holder.mProgressBar.setVisibility(View.GONE);
		        	holder.lvHabitDetails.setVisibility(View.VISIBLE);
				} else {
					System.out.println("position:" + position);
					new LoadHabitLogItem().execute(position, 
							convertView, holder);
				}
				
				System.out.println("getView--->habit view");
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	/**
	 * 用于临时存储view的类
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
	 * 异步读取数据库内容的类
	 * 
	 * @author FreeTymeKiyan
	 * @version 0.0.2
	 */
	private class LoadHabitLogItem extends AsyncTask<Object, Object, Object> {

		private Integer position;
		private View convertView;
		private ViewHolder holder;
		private Bundle bundle = new Bundle();
		
		@Override
		protected Object doInBackground(Object... params) {
			position = (Integer) params[0];
			convertView = (View) params[1];
			holder = (ViewHolder) params[2];
			// 根据position得到习惯的uuid
			HabitDb hd = new HabitDb(mContext);
//			System.out.println("position" + position);
			String uuid = hd.queryUuidWithPosition(position + 1);
			if (uuid != null) {
				// 得到uuid，查询具体日志
				habitDetailsItem = hd.queryLogWithUuid(uuid);
				bundle = hd.queryHeaderWithUuid(uuid);
			} else {
				// uuid查询失败
				System.out.println("MainActivityAdapter: " +
						"uuid query failed");
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			// TODO 得到的结果存储到habitDetailsItem中
//			habitDetailsItem
			// 把habitDetailsItem存到habitDetailsItems中
			habitDetailsItems.put(position, habitDetailsItem);
			// TODO 加载headerView的各项内容
			View headerView = mInflater.inflate(
					R.layout.list_view_header, null);
			holder.lvHabitDetails.addHeaderView(headerView, 
					null, false);
			TextView tvHeaderHabit = (TextView)headerView
					.findViewById(R.id.tv_habitName);
			TextView tvHeaderPunish = (TextView)headerView
					.findViewById(R.id.tv_punish);
			TextView tvHeaderFollower = (TextView) headerView
					.findViewById(R.id.tv_follower);
			tvHeaderHabit.setText(bundle.getString(NAME));
			tvHeaderPunish.setText(bundle.getString(PUNISH));
			tvHeaderFollower.setText(bundle.getString(FOLLOWERS));
			/*habitDetails的适配器*/
			String[] from = new String[]{"tv_habitWords", 
					"iv_habitPic", "tv_timestamp"}; 
	        int[] to = new int[]{R.id.tv_habitWords, 
	        		R.id.iv_habitPic, R.id.tv_timestamp}; 
			HabitDetailsAdapter habitDetailsAdapter = new 
					HabitDetailsAdapter(mContext, 
							habitDetailsItem, 
							R.layout.list_view_item, from, 
							to);
			holder.lvHabitDetails.setAdapter(habitDetailsAdapter);
			holder.lvHabitDetails.setVisibility(View.GONE);
			holder.mProgressBar.setVisibility(View.VISIBLE);
			// 重新绘制view
			System.out.println("draw another view");
			refreshView(position, convertView, holder);
			// view 刷新
			convertView.postInvalidate();
		}
		
	}

	/**
	 * 在数据读取之后刷新界面
	 * 
	 * @param position
	 * @param convertView
	 */
	private void refreshView(Integer position, View convertView,
			ViewHolder holder) {
		List<Map<String,Object>> item = getItem(position);
        if (item != null) {
        	System.out.println("item != null");
        	holder.mProgressBar.setVisibility(View.GONE);
        	holder.lvHabitDetails.setVisibility(View.VISIBLE);
		} else {
			System.out.println("position:" + position);
			new LoadHabitLogItem().execute(position, 
					convertView, holder);
		}
	}
}