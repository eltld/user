package net.ememed.user2.entity;

import java.io.Serializable;

public class DoctorServiceEntry implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1606928652940477048L;
		
	private OfferServiceEntry2 OFFER_TEXT;
	private OfferServiceEntry OFFER_CALL;
	private OfferServiceEntry OFFER_ZHUYUAN;
	private OfferServiceEntry OFFER_JIAHAO;
	private OfferServiceEntry OFFER_SHANGMEN;
	private OfferServiceEntry OFFER_CUSTOM;
	private OfferServicePacketEntry OFFER_PACKET;
	
	public OfferServiceEntry2 getOFFER_TEXT() {
		return OFFER_TEXT;
	}
	public void setOFFER_TEXT(OfferServiceEntry2 oFFER_TEXT) {
		this.OFFER_TEXT = oFFER_TEXT;
	}
	public OfferServiceEntry getOFFER_CALL() {
		return OFFER_CALL;
	}
	public void setOFFER_CALL(OfferServiceEntry oFFER_CALL) {
		OFFER_CALL = oFFER_CALL;
	}
	public OfferServiceEntry getOFFER_ZHUYUAN() {
		return OFFER_ZHUYUAN;
	}
	public void setOFFER_ZHUYUAN(OfferServiceEntry oFFER_ZHUYUAN) {
		OFFER_ZHUYUAN = oFFER_ZHUYUAN;
	}
	public OfferServiceEntry getOFFER_JIAHAO() {
		return OFFER_JIAHAO;
	}
	public void setOFFER_JIAHAO(OfferServiceEntry oFFER_JIAHAO) {
		OFFER_JIAHAO = oFFER_JIAHAO;
	}
	public OfferServiceEntry getOFFER_SHANGMEN() {
		return OFFER_SHANGMEN;
	}
	public void setOFFER_SHANGMEN(OfferServiceEntry oFFER_SHANGMEN) {
		OFFER_SHANGMEN = oFFER_SHANGMEN;
	}
	public OfferServicePacketEntry getOFFER_PACKET() {
		return OFFER_PACKET;
	}
	public void setOFFER_PACKET(OfferServicePacketEntry oFFER_PACKET) {
		OFFER_PACKET = oFFER_PACKET;
	}
	public OfferServiceEntry getOFFER_CUSTOM() {
		return OFFER_CUSTOM;
	}
	public void setOFFER_CUSTOM(OfferServiceEntry oFFER_CUSTOM) {
		OFFER_CUSTOM = oFFER_CUSTOM;
	}
	
	/**
	 * 是否存一个以上服务
	 * @return 0不存在， 其他存在
	 */
	public int hasAnyService() {
		int result = 0;
		result += OFFER_TEXT == null ? 0:OFFER_TEXT.getIS_OFFER();
		result += OFFER_CALL == null ? 0:OFFER_CALL.getIS_OFFER();
		result += OFFER_ZHUYUAN == null ? 0:OFFER_ZHUYUAN.getIS_OFFER();
		result += OFFER_JIAHAO == null ? 0:OFFER_JIAHAO.getIS_OFFER();
		result += OFFER_SHANGMEN == null ? 0:OFFER_SHANGMEN.getIS_OFFER();
		result += OFFER_CUSTOM == null ? 0:OFFER_CUSTOM.getIS_OFFER();
		result += OFFER_PACKET == null ? 0:Integer.valueOf(OFFER_PACKET.getIS_OFFER());
		return result;
	}
	
	
}

