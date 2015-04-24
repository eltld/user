package net.ememed.user2.activity.adapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.pingyin.util.SortModel;

import net.ememed.user2.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HotCityAdapter extends BaseAdapter{
	private List<SortModel> gridData;
	private Context context;
	public HotCityAdapter(Context context) {
		super();
		this.context=context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(gridData==null){
			return 0;
		}
		return gridData.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return gridData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view=LayoutInflater.from(context).inflate(R.layout.city_hot_item, null);
		TextView city=(TextView) view.findViewById(R.id.hot_city_txt);
		city.setText(gridData.get(arg0).getName());
		return view;
	}
	
	public void setData(List<SortModel> gridData){
		this.gridData=gridData;
		notifyDataSetChanged();
	}
	
	public SortModel getSortModel(int  position){
		
		return gridData.get(position);
	}
	
}
