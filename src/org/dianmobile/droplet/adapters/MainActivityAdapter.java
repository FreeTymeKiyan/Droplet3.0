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
 * 主界面ViewFlow的适配器
 * 需要适配不同的界面到主界面当中
 * 主要是一个习惯界面，一个添加界面
 * 
 * @author FreeTymeKiyan
 * @version 0.0.11
 */
public class MainActivityAdapter extends BaseAdapter {
	/**显示的@好友的名字字符串长度*/
	private static final int FOLLOWER_STR_LENGTH = 20;
	
	/**布局文件填充器*/
	private LayoutInflater mInflater;
	/**view的数量*/
	private int viewCount;
	/**当前的上下文环境*/
	private Context mContext;
	/**习惯详细记录的数据源集合*/
	private Map<Integer, List<Map<String, Object>>> 
			habitDetailsItems = new HashMap<Integer, 
					List<Map<String,Object>>>();
	/**MainActivity的一个实例*/
	private Activity mActivity;
	/**位置和uuid的一一对应*/
	private ArrayList<String> uuids;
	/**列表每一栏的内容的控件名*/
	private final static String[] from = new String[]{"tv_habitWords", 
			"iv_habitPic", "tv_timestamp"}; 
	/**列表每一栏的内容的空间编号*/
	private final static int[] to = new int[]{R.id.tv_habitWords, 
    		R.id.iv_habitPic, R.id.tv_timestamp};
	
	public MainActivityAdapter(Context context, int habitCount,
			Activity activity) {
		mContext = context;
		mInflater = (LayoutInflater) context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		viewCount = habitCount + 1; // 还有添加新习惯界面，所以+1
		mActivity = activity;
		/*得到position和UUID的一一对应*/
		if (uuids == null) {
			HabitDb hd = new HabitDb(mContext);
			uuids = hd.queryUuids(); // 解决按顺序排列习惯UUID的问题
		}
	}

	/**
	 * 返回对应item的view类型
	 */
	@Override
	public int getItemViewType(int position) {
		return position;
	}
	
	/**
	 * 返回不同view的类型数量
	 */
	@Override
	public int getViewTypeCount() {
		return viewCount;
	}
	
	/**
	 * 返回view的数量
	 */
	@Override
	public int getCount() {
		return viewCount;
	}

	/**
	 * 返回对应position页面的内容，即item
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
	 * 获取对应页面的view
	 */
	@Override
	public View getView(int position, View convertView, 
			ViewGroup parent) {
//		System.out.println("getView--->start");
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
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			if (position == viewCount - 1) { // 最后一页，添加习惯
				convertView = mInflater.inflate(R.layout
						.add_habit_view, null);
				/*添加新习惯的按钮*/
        		holder.ibAddNew = (ImageButton) convertView
        				.findViewById(R.id.ib_addNewHabit);
        		holder.ibAddNew.setOnClickListener(new 
        				OnClickListener() {
        			
        			@Override
        			public void onClick(View v) {
        				if (viewCount < 6) { // 习惯数量没达到五个
        					/*跳转到添加新习惯的页面*/
        					Intent i = new Intent();
        					i.setClass(mContext, CreateActivity.class);
        					mActivity.startActivityForResult(i, 
        							REQUEST_CODE_REFRESH_VIEW);
						} else { // 习惯数量达到五个
							Toast.makeText(mContext, 
									R.string.toast_max_reached,
									Toast.LENGTH_SHORT).show();
						}
        			}		
        		});
//        		System.out.println("getView--->add habit view");
			} else { // 习惯页
				convertView = mInflater.inflate(R.layout
						.habit_view, null);
				holder.mProgressBar = (ProgressBar) convertView
						.findViewById(R.id.progress);
				/*习惯的详细记录*/
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
	 * @version 0.0.6
	 */
	private class LoadHabitLogItem extends AsyncTask<Object, Object, Object> {

		/**当前页面的位置*/
		private Integer position;
		/**当前页面的view*/
		private View convertView;
		/**当前页面的viewHolder*/
		private ViewHolder holder;
		/**用于存当前页面header信息的bundle*/
		private Bundle bundle = new Bundle();
		private String followers = "";
		
		@Override
		protected Object doInBackground(Object... params) {
			position = (Integer) params[0];
			convertView = (View) params[1];
			holder = (ViewHolder) params[2];
			/*根据position得到习惯的uuid*/
//			System.out.println("position" + position);
			String uuid = uuids.get(position);
//			System.out.println("UUID " + uuid);
			if (uuid != null) {
				/* 得到uuid，查询具体日志
				 * 得到的结果存储到habitDetailsItem中 */
				HabitDb hd = new HabitDb(mContext);
				/*把habitDetailsItem存到habitDetailsItems中*/
				habitDetailsItems.put(position, hd
						.queryLogWithUuid(uuid));
				bundle = hd.queryHeaderWithUuid(uuid);
				/*处理followers为名字的格式*/
				followers = processFollowers(bundle.getString(FOLLOWERS));
			} else { // uuid查询失败
				System.out.println("MainActivityAdapter: " +
						"uuid query failed");
			}
			return null;
		}
		
		/**
		 * 处理followers为"名字"的格式
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
			/*加载headerView的各项内容*/			
			View headerView = mInflater.inflate(
					R.layout.list_view_header, null);
			holder.lvHabitDetails.addHeaderView(headerView, null,
					false); // 给listView添加header
			TextView tvHeaderHabit = (TextView)headerView
					.findViewById(R.id.tv_habitName);
			TextView tvHeaderPunish = (TextView)headerView
					.findViewById(R.id.tv_punish);
			TextView tvHeaderFollower = (TextView) headerView
					.findViewById(R.id.tv_follower);
			tvHeaderHabit.setText(bundle.getString(NAME));
			tvHeaderPunish.setText(bundle.getString(PUNISH));
			tvHeaderFollower.setText(followers);
			/*重新绘制view*/
			refreshView(position, convertView, holder);
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
        List<Map<String, Object>> item = getItem(position);
        if (item != null) { // 已经获取到对应页面的数据
//        	System.out.println("item != null");
        	holder.mProgressBar.setVisibility(View.GONE);
        	holder.lvHabitDetails.setVisibility(View.VISIBLE);
        	/*绑定habitDetails的适配器*/
	        System.out.println("habitDetailsItem " + position);
			HabitDetailsAdapter habitDetailsAdapter = new 
					HabitDetailsAdapter(mContext, item, 
							R.layout.list_view_item, from, to);
			holder.lvHabitDetails.setAdapter(habitDetailsAdapter);
		} else { // 对应页面的数据还未获取
			/*改变ProgressBar和ListView的Visibility*/
			holder.lvHabitDetails.setVisibility(View.GONE);
			holder.mProgressBar.setVisibility(View.VISIBLE);
//			System.out.println("position:" + position);
			new LoadHabitLogItem().execute(position, 
					convertView, holder);
		}
        convertView.postInvalidate();
	}
	
	/**
	 * 获取UUID和Position一一对应的列表
	 * 
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getUuids() {
		return uuids;
	}
}