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
    /**当前的上下文环境*/
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
				/*将用户拍摄的图片显示在ListView当中*/
                ImageView iv = (ImageView) convertView
                		.findViewById(itemIds[i]);
                // 有图片插图片，没有图片不显示图片
                String picPath = (String)list.get(position).get(flag[i]);
                if (picPath.equals("")) { // 无图片
					iv.setVisibility(View.GONE);
				} else { // 有图片
					File pic = new File(picPath);
					if (pic.exists()) { // 图片存在
						InputStream is;
						try {
							is = new FileInputStream(pic );
							Drawable d = new BitmapDrawable(is);
							iv.setImageDrawable(d);
//							iv.setBackgroundDrawable(d);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					} else { // 图片不存在
						// 避免阻塞，用临时的toast
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