/**
 * 
 */
package net.ememed.user2.entity;

import java.io.Serializable;

/**
 * @author ASIMO
 *
 */
public class Geo implements Serializable{

	private String areaid;//": "",
	private String  parent_areaid;//": "",
	private String areaname;//": "",
	private String areatype;//": ""
	
	public void setAreaid(String Areaid) {
		areaid = Areaid;
	}
	public String getAreaid() {
		return areaid;
	}
	
	public void setParent_areaid(String Parent_areaid) {
		parent_areaid = Parent_areaid;
	}
	public String getParent_areaid() {
		return parent_areaid;
	}
	public void setAreaname(String Areaname) {
		areaname = Areaname;
	}
	public String getAreaname() {
		return areaname;
	}
	
	public void setAreatype(String Areatype) {
		areatype = Areatype;
	}
	public String getAreatype() {
		return areatype;
	}
}
