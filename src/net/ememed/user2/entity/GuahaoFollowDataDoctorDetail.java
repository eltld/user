package net.ememed.user2.entity;

import java.util.List;

public class GuahaoFollowDataDoctorDetail {
	private int pages;
	private int curpage;
	private int count;
	private List<GuahaoFollowDoctorList> list;

	public int getPages() {
		return pages;
	}

	public int getCurpage() {
		return curpage;
	}

	public int getCount() {
		return count;
	}

	public List<GuahaoFollowDoctorList> getList() {
		return list;
	}
}