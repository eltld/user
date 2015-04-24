package de.greenrobot.event;

/**
 * Created by kevintanhongann on 11/27/13.
 */
public class UploadImgSuccessEvent {
	public enum PhotoType {
		/**执业证*/
		CER_PHOTO,
		/**身份证*//**执业证背面*/
		CARD_PHOTO, CER_PHOTO_OPPOSITE,
	}
	private byte[]  bytes;
	private PhotoType photoType;
    public UploadImgSuccessEvent(byte[]  bytes,PhotoType photoType) {
    	this.setBytes(bytes);
    	this.setPhotoType(photoType);
    }

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public PhotoType getPhotoType() {
		return photoType;
	}

	public void setPhotoType(PhotoType photoType) {
		this.photoType = photoType;
	}
    
}
