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
 * ϰ����ϸ��¼��adapter
 * 
 * @author FreeTymeKiyan
 * @version 0.0.2
 */
public class HabitDetailsAdapter extends BaseAdapter {
	
	/**���������*/
	private LayoutInflater mInflater;
	/**����Դ*/
    private List<Map<String, Object>> list;
    /**�ؼ�Id*/
    private int viewId;
    /**�ؼ���*/
    private String flag[];
    /**ÿһ��ؼ���Id*/
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
                // TODO ���û���������ͼƬ��ʾ��ListView����
//                iv.setBackgroundResource((Integer) list
//                		.get(position).get(flag[i]));
            } else if (convertView.findViewById(itemIds[i])
            		instanceof TextView) {
                TextView tv = (TextView) convertView
                		.findViewById(itemIds[i]);
                tv.setText((String) list.get(position)
                		.get(flag[i]));
            } else {
                // ...��ע2
            }
			addListener(convertView);
		}
        return convertView;
	}
	
	/**
	 * ֻ��Ҫ����Ҫ���ü����¼������д�������ⷽ���� 
	 * ��Ĳ���Ҫ�޸�
	 */
	public void addListener(View convertView) {
		
	}
}