package net.ememed.user2.entity;

import com.google.gson.annotations.SerializedName;

public class GuahaoFollowHospitalListItem {

	@SerializedName("ADDRESS")
	private String address;
	@SerializedName("AREACODE")
	private Integer areaCode;
	@SerializedName("AREANAME")
	private String areaName;
	@SerializedName("CONTEXT")
	private String content;
	@SerializedName("GRADE")
	private String grade;
	@SerializedName("HAS_SCHEDULE")
	private Integer hasSchedule;
	@SerializedName("HFROM")
	private String from;
	@SerializedName("HOSPITALCODE")
	private Integer hospitalCode;
	@SerializedName("HOSPITALNAME")
	private String hospitalName;
	@SerializedName("HOSTEL")
	private String tel;
	@SerializedName("IMGURL")
	private String imageUrl;
	@SerializedName("IMGURL_BANNER")
	private String imageUrlBanner;
	@SerializedName("ISOPEN")
	private Integer isOpen;
	@SerializedName("LEFTOUTTIME")
	private String leftOutTime;
	@SerializedName("NOTES")
	private String notes;
	@SerializedName("POSTCODE")
	private String postCode;
	@SerializedName("SPECIALITY")
	private String speciality;
	@SerializedName("STATUS")
	private Integer status;
	@SerializedName("TRAFFICLINE")
	private String trafficLine;
	@SerializedName("VISITSGUIDE")
	private String visitsGuide;
	@SerializedName("WEBSITE")
	private String webSite;

	public String getAddress() {
		return address;
	}

	public Integer getAreaCode() {
		return areaCode;
	}

	public String getAreaName() {
		return areaName;
	}

	public String getContent() {
		return content;
	}

	public String getGrade() {
		return grade;
	}

	public Integer getHasSchedule() {
		return hasSchedule;
	}

	public String getFrom() {
		return from;
	}

	public Integer getHospitalCode() {
		return hospitalCode;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public String getTel() {
		return tel;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getImageUrlBanner() {
		return imageUrlBanner;
	}

	public Integer getIsOpen() {
		return isOpen;
	}

	public String getLeftOutTime() {
		return leftOutTime;
	}

	public String getNotes() {
		return notes;
	}

	public String getPostCode() {
		return postCode;
	}

	public String getSpeciality() {
		return speciality;
	}

	public Integer getStatus() {
		return status;
	}

	public String getTrafficLine() {
		return trafficLine;
	}

	public String getVisitsGuide() {
		return visitsGuide;
	}

	public String getWebSite() {
		return webSite;
	}

}
