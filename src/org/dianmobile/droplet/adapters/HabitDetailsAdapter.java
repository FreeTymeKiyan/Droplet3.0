package org.dianmobile.droplet.adapters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.dianmobile.droplet.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    /**��ǰ�������Ļ���*/
    private Context mContext;
	
	public HabitDetailsAdapter(Context context, List<Map<String, Object>> list,
            int layoutID, String flag[], int itemIds[]) {
		this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.viewId = layoutID;
        this.flag = flag;
        this.itemIds = itemIds;
        mContext = context;
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
				/*���û������ͼƬ��ʾ��ListView����*/
                ImageView iv = (ImageView) convertView
                		.findViewById(itemIds[i]);
                // ��ͼƬ��ͼƬ��û��ͼƬ����ʾͼƬ
                String picPath = (String)list.get(position).get(flag[i]);
                if (picPath.equals("")) { // ��ͼƬ
					iv.setVisibility(View.GONE);
				} else { // ��ͼƬ
					File pic = new File(picPath);
					if (pic.exists()) { // ͼƬ����
						InputStream is;
						try {
							is = new FileInputStream(pic );
							Drawable d = new BitmapDrawable(is);
							iv.setImageDrawable(d);
//							iv.setBackgroundDrawable(d);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					} else { // ͼƬ������
						// ��������������ʱ��toast
						Toast toast = new Toast(mContext);
						toast.makeText(mContext, R.string.toast_pic_not_exist,
								Toast.LENGTH_SHORT).show();
					}
				}
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