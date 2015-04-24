package net.ememed.user2.wxpay;

public interface WxPayCallBack {
		public void onPaySuccess();
		public void onPayFail(int errCode);
}
