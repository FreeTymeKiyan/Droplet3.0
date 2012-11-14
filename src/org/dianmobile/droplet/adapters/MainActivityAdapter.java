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
 * 主界面ViewFlow的适配器
 * 需要适配不同的界面到主界面当中
 * 主要是一个习惯界面，一个添加界面
 * 
 * @author FreeTymeKiyan
 * @version 0.0.2
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
        				mContext.startActivity(i);
        			}		
        		});
        		System.out.println("getView--->add habit view");
			} else {
				convertView = mInflater.inflate(R.layout
						.habit_view, null);
				holder.mProgressBar = (ProgressBar) convertView
						.findViewById(R.id.progress);
				/*习惯的详细记录*/
				ListView lvHabitDetails = (ListView) 
						convertView.findViewById(R.id
								.lv_habitDetails);
				View headerView = mInflater.inflate(
						R.layout.list_view_header, null);
				// TODO 加载headerView的各项内容
				lvHabitDetails.addHeaderView(headerView, null, false);
				/*habitDetails的适配器*/
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
		
		@Override
		protected Object doInBackground(Object... params) {
			position = (Integer) params[0];
			convertView = (View) params[1];
			// TODO 根据position得到习惯的uuid
			// TODO 根据uuid到数据库当中去查询结果
			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			// TODO 得到的结果存储到habitDetailsItem中
//			habitDetailsItem
			// 把habitDetailsItem存到habitDetailsItems中
			habitDetailsItems.put(position, habitDetailsItem);
			// 重新绘制view
			drawView(position, convertView);
			// view 刷新
			convertView.postInvalidate();
		}
		
	}
}