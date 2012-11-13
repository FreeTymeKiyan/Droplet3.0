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
	
	private Context mContext;
	/**ϰ����ϸ��¼������Դ*/
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
        		/*�����ϰ�ߵİ�ť*/
        		ImageButton addNewHabitBtn = (ImageButton) convertView.findViewById(R.id
        				.ib_addNewHabit);
        		addNewHabitBtn.setOnClickListener(new OnClickListener() {
        			
        			@Override
        			public void onClick(View v) {
        				/*TODO ��ת�������ϰ�ߵ�ҳ��*/
        			}		
        		});
        		System.out.println("getView--->add habit view");
			} else {
				convertView = mInflater.inflate(R.layout
						.habit_view, null);
				/*ϰ�ߵ���ϸ��¼*/
				ListView lvHabitDetails = (ListView) 
						convertView.findViewById(R.id.lv_habitDetails);
				View headerView = mInflater.inflate(
						R.layout.list_view_header, null);
				// TODO ����headerView�ĸ�������
				lvHabitDetails.addHeaderView(headerView, null, false);
				/*habitDetails��������*/
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