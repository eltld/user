package net.ememed.user2.entity;

import java.io.Serializable;

public class ScheduleTime implements Serializable{

	private String schedule;// => 205869;2014-08-23;1;上午
    private String schedule_desc;// => 08/23 上午
    private String free_num;// => 30
	public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	public String getSchedule_desc() {
		return schedule_desc;
	}
	public void setSchedule_desc(String schedule_desc) {
		this.schedule_desc = schedule_desc;
	}
	public String getFree_num() {
		return free_num;
	}
	public void setFree_num(String free_num) {
		this.free_num = free_num;
	}
    
}
