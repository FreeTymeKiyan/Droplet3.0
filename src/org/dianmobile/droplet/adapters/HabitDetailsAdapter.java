package org.dianmobile.droplet.adapters;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 习惯详细记录的adapter
 * 
 * @author FreeTymeKiyan
 * @version 0.0.2
 */
public class HabitDetailsAdapter extends BaseAdapter {
	
	/**布局填充器*/
	private LayoutInflater mInflater;
	/**数据源*/
    private List<Map<String, Object>> list;
    /**控件Id*/
    private int viewId;
    /**控件名*/
    private String flag[];
    /**每一项控件的Id*/
    private int itemIds[];
	
	public HabitDetailsAdapter(Context context, List<Map<String, Object>> list,
            int layoutID, String flag[], int itemIds[]) {
		this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.viewId = layoutID;
        this.flag = flag;
        this.itemIds = itemIds;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup
			parent) {
		if(viewId == 0) {
			convertView = null;
		} else {
			convertView = mInflater.inflate(viewId, null);
		}
		for(int i = 0; i < flag.length; i++) {
			if (convertView.findViewById(itemIds[i]) instanceof
					ImageView) {
                ImageView iv = (ImageView) convertView
                		.findViewById(itemIds[i]);
                // TODO 将用户拍摄过后的图片显示在ListView当中
//                iv.setBackgroundResource((Integer) list
//                		.get(position).get(flag[i]));
            } else if (convertView.findViewById(itemIds[i])
            		instanceof TextView) {
                TextView tv = (TextView) convertView
                		.findViewById(itemIds[i]);
                tv.setText((String) list.get(position)
                		.get(flag[i]));
            } else {
                // ...备注2
            }
			addListener(convertView);
		}
        return convertView;
	}
	
	/**
	 * 只需要将需要设置监听事件的组件写在下面这方法里 
	 * 别的不需要修改
	 */
	public void addListener(View convertView) {
		
	}
}