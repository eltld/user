package net.ememed.user2.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.entity.Drug;
import net.ememed.user2.util.Util;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DrugAdapter extends BaseAdapter {
	private List<Drug> drugs;
	private ListView lvDrugs;
	private BasicActivity mContext;

	public DrugAdapter() {
		super();

	}

	public DrugAdapter(BasicActivity context, ArrayList<Drug> drugs, ListView lvDrugs) {
		if (drugs == null) {
			drugs = new ArrayList<Drug>();
		}
		this.mContext = context;
		this.drugs = drugs;
		this.lvDrugs = lvDrugs;

	}

	@Override
	public int getCount() {

		return drugs.size();
	}

	@Override
	public Object getItem(int position) {

		return drugs.get(position - 1);
	}

	@Override
	public long getItemId(int position) {

		return drugs.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.u_drug_item, null);
			holder.ivIcon = (ImageView) convertView
					.findViewById(R.id.iv_drug_icon);
			holder.tvDrugName = (TextView) convertView
					.findViewById(R.id.tv_drug_name);
			holder.tvDrugPrice = (TextView) convertView
					.findViewById(R.id.tv_drug_price);
			holder.tvDrugYhPrice = (TextView) convertView
					.findViewById(R.id.tv_drug_yhPrice);
			holder.tvFunction = (TextView) convertView
					.findViewById(R.id.tv_drug_function);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Drug drug = drugs.get(position);
		String path = drug.getPicPath();
		holder.ivIcon.setTag(path);
//		new MyTask().execute(holder.ivIcon);
		if (TextUtils.isEmpty(path)) {
			holder.ivIcon.setImageResource(R.drawable.default_doctor);
		} else {
			try {
				mContext.imageLoader.displayImage(path, holder.ivIcon, Util.getOptions_little_pic());
			} catch (Exception e) {
				e.printStackTrace();
				holder.ivIcon.setImageResource(R.drawable.default_doctor);
			}	
		}
		holder.tvDrugName.setText(drug.getCnName());
		holder.tvDrugPrice.setText(drug.getPrice());
		holder.tvDrugYhPrice.setText(drug.getYhPrice());
		holder.tvFunction.setText(drug.getFunction());
		return convertView;
	}

	public void change(List<Drug> drugs) {
		if (drugs == null) {
			drugs = new ArrayList<Drug>();
		}
		this.drugs = drugs;
		notifyDataSetChanged();
	}

	public void change(ArrayList<Drug> drugs) {
		if (drugs == null) {
			drugs = new ArrayList<Drug>();
		}
		this.drugs = drugs;
		notifyDataSetChanged();
	}

	public void add(List<Drug> drugs) {
		this.drugs.addAll(drugs);
		notifyDataSetChanged();

	}

	class ViewHolder {
		ImageView ivIcon;
		TextView tvDrugName;
		TextView tvDrugPrice;
		TextView tvDrugYhPrice;
		TextView tvFunction;
	}
}
