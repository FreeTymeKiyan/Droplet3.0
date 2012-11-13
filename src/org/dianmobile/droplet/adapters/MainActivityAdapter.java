package org.dianmobile.droplet.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dianmobile.droplet.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

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
	
	private Context mContext;
	/**习惯详细记录的数据源*/
	private List<Map<String, Object>> habitDetailsItem
			= new ArrayList<Map<String,Object>>();
	
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
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		System.out.println("getView--->start");
        if (convertView == null) {
        	if (position == viewCount - 1) {
        		convertView = mInflater.inflate(R.layout
        				.add_habit_view, null);
        		/*添加新习惯的按钮*/
        		ImageButton addNewHabitBtn = (ImageButton) convertView.findViewById(R.id
        				.ib_addNewHabit);
        		addNewHabitBtn.setOnClickListener(new OnClickListener() {
        			
        			@Override
        			public void onClick(View v) {
        				/*TODO 跳转到添加新习惯的页面*/
        			}		
        		});
        		System.out.println("getView--->add habit view");
			} else {
				convertView = mInflater.inflate(R.layout
						.habit_view, null);
				/*习惯的详细记录*/
				ListView lvHabitDetails = (ListView) 
						convertView.findViewById(R.id.lv_habitDetails);
				View headerView = mInflater.inflate(
						R.layout.list_view_header, null);
				// TODO 加载headerView的各项内容
				lvHabitDetails.addHeaderView(headerView, null, false);
				/*habitDetails的适配器*/
				String[] from = new String[]{"tv_habitWords", 
						"iv_habitPic", "tv_timestamp"}; 
		        int[] to = new int[]{R.id.tv_habitWords, 
		        		R.id.iv_habitPic, R.id.tv_timestamp}; 
				HabitDetailsAdapter habitDetailsAdapter = new HabitDetailsAdapter(
						mContext, habitDetailsItem, 
						R.layout.list_view_item, from, to);
				lvHabitDetails.setAdapter(habitDetailsAdapter);
				System.out.println("getView--->habit view");
			}
        }
        return convertView;
	}
}