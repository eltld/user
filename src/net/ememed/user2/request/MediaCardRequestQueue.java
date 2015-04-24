package net.ememed.user2.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.ememed.user2.MyApplication;
import net.ememed.user2.entity.AddCurdcardInfo;
import net.ememed.user2.entity.CurdcardListEntry;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 诊疗卡上传队列
 */
public class MediaCardRequestQueue {

	private int mIndex;
	private Context mContext;
	private List<CurdcardListEntry> mMediaCardList;
	private OnRequestQueueListener mOnRequestQueueListener;
	private String mMemberId;

	public MediaCardRequestQueue(Context context) {
		mContext = context;
		mMediaCardList = new ArrayList<CurdcardListEntry>();
	}

	public MediaCardRequestQueue(Context context, List<CurdcardListEntry> mediaCards,
			String memberId, OnRequestQueueListener listener) {
		mContext = context;
		mMediaCardList = mediaCards;
		mMemberId = memberId;
		mOnRequestQueueListener = listener;
	}

	public void setMemberId(String memberId) {
		this.mMemberId = memberId;
	}

	public void add(CurdcardListEntry mediaCard) {
		mMediaCardList.add(mediaCard);
	}

	public void remove(int index) {
		if (index < mMediaCardList.size()) {
			mMediaCardList.remove(index);
		}
	}

	public int size() {
		return mMediaCardList.size();
	}

	/**
	 * 保存诊疗卡
	 * 
	 * @param curedmember_id
	 */
	private void requestMedicalCardAdd(String hospital, String card) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
		params.put("curedmember_id", mMemberId);
		params.put("hospital", hospital);
		params.put("curdcardid", card);
		MyApplication.volleyHttpClient.postWithParams(HttpUtil.MEDICAL_CARD_ADD,
				AddCurdcardInfo.class, params, new Response.Listener() {
					@Override
					public void onResponse(Object response) {
						nextRequest();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						mOnRequestQueueListener.onRequestQueueFail();
					}
				});
	}

	public void request() {
		CurdcardListEntry mediaCard = mMediaCardList.get(mIndex);
		requestMedicalCardAdd(mediaCard.getHOSPITAL(), mediaCard.getCURDCARDID());
	}

	public void nextRequest() {
		mIndex++;
		if (mIndex < mMediaCardList.size()) {
			request();
		} else {
			// hideProgress();
			// ToastUtil.showShort(mContext, "图片上传完毕");
			mOnRequestQueueListener.onRequestQueueSuccess();
		}
	}

	/**
	 * 开始提交
	 */
	public void start() {
		if (mMediaCardList.size() == 0) {
			mOnRequestQueueListener.onRequestQueueSuccess();
			return;
		}
		mIndex = 0;
		request();
		mOnRequestQueueListener.onRequestQueueStart();
	}

	public interface OnRequestQueueListener {
		public void onRequestQueueStart();

		public void onRequestQueueSuccess();

		public void onRequestQueueFail();
	}
}