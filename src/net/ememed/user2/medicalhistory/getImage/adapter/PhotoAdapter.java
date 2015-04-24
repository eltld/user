package net.ememed.user2.medicalhistory.getImage.adapter;

import java.util.List;
import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.medicalhistory.getImage.bean.PhotoInfo;
import net.ememed.user2.medicalhistory.getImage.imageaware.RotateImageViewAware;
import net.ememed.user2.medicalhistory.getImage.util.ThumbnailsUtil;
import net.ememed.user2.medicalhistory.getImage.util.UniversalImageLoadTool;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * 相片适配器
 * @author GuiLin
 */
public class PhotoAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<PhotoInfo> list;
	private ViewHolder viewHolder;
	private GridView gridView;
	private int width = MyApplication.getDisplayMetrics().widthPixels/3;

	public PhotoAdapter(Context context,List<PhotoInfo> list,GridView gridView){
		mInflater = LayoutInflater.from(context);
		this.list = list;
		this.gridView = gridView; 
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
	/**
	 * 刷新view
	 * @param index
	 */
	public void refreshView(int index){
		int visiblePos = gridView.getFirstVisiblePosition();
		View view = gridView.getChildAt(index-visiblePos);
		ViewHolder holder = (ViewHolder)view.getTag();

		if(list.get(index).isChoose()){
			holder.selectImage.setImageResource(R.drawable.gou_selected);
		}else{
			holder.selectImage.setImageResource(R.drawable.gou_normal);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_selectphoto, null);
			viewHolder.image = (ImageView)convertView.findViewById(R.id.imageView);
			viewHolder.selectImage = (ImageView)convertView.findViewById(R.id.selectImage);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(list.get(position).isChoose()){
			viewHolder.selectImage.setImageResource(R.drawable.gou_selected);
		}else{
			viewHolder.selectImage.setImageResource(R.drawable.gou_normal);
		}
		
		LayoutParams layoutParams = viewHolder.image.getLayoutParams();
		layoutParams.width = width;
		layoutParams.height = width;
		viewHolder.image.setLayoutParams(layoutParams);
		final PhotoInfo photoInfo = list.get(position);
		if(photoInfo!=null){
			UniversalImageLoadTool.disPlay(ThumbnailsUtil.MapgetHashValue(photoInfo.getImage_id(),photoInfo.getPath_file()), 
					new RotateImageViewAware(viewHolder.image,photoInfo.getPath_absolute()), R.drawable.common_defalt_bg);
//			UniversalImageLoadTool.disPlay(ThumbnailsUtil.MapgetHashValue(photoInfo.getImage_id(),photoInfo.getPath_file()),
//					viewHolder.image, R.drawable.common_defalt_bg);
		}
		return convertView;
	}
	public class ViewHolder{
		public ImageView image;
		public ImageView selectImage;
	}
}
