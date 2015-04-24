package net.ememed.user2.entity;

import java.util.List;

public class ScheduleEntry {

	private String Date;// => 08-21 星期四
	private String Datekey;// => 20140821
	private List<ScheduleItem> Am;// =>
	private List<ScheduleItem> Pm;// =>
	private List<ScheduleItem> Night;// =>
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public String getDatekey() {
		return Datekey;
	}
	public void setDatekey(String datekey) {
		Datekey = datekey;
	}
	public List<ScheduleItem> getAm() {
		return Am;
	}
	public void setAm(List<ScheduleItem> am) {
		Am = am;
	}
	public List<ScheduleItem> getPm() {
		return Pm;
	}
	public void setPm(List<ScheduleItem> pm) {
		Pm = pm;
	}
	public List<ScheduleItem> getNight() {
		return Night;
	}
	public void setNight(List<ScheduleItem> night) {
		Night = night;
	}
	
}
