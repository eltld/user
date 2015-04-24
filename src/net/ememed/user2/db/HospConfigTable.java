package net.ememed.user2.db;

import java.util.ArrayList;
import java.util.List;

import net.ememed.user2.entity.HospEntry;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

/**
 * 保存医院的列表
 * @author huangjk
 *
 */
public class HospConfigTable {

private final static String TABLE_HOSP = "tb_hospitals";
	
	private final static String HOSPITALID = "hospital_id";
	private final static String HOSPITALNAME = "hospital_name";
	private final static String AREAID = "area_id";

	
	private DBManagerImpl db = null;
	
	public HospConfigTable() {
		if (db == null) {
			db = DBManager.get();
		}
		if (!db.isTableExits(db.getConnection(), TABLE_HOSP)) {
			createHospTable();
		}
	}
	
	public HospConfigTable(Context context) {
		if (db == null) {
			db = DBManager.get(context);
		}
		if (!db.isTableExits(db.getConnection(), TABLE_HOSP)) {
			createHospTable();
		}
	}
	
	private void createHospTable() {
		String createSql = "create table if not exists "+TABLE_HOSP+" (id integer primary key autoincrement,"
				+  HOSPITALID +" varchar,"+ AREAID +" varchar,"+ HOSPITALNAME +" varchar)";
		db.creatTable(db.getConnection(), createSql, TABLE_HOSP);
	}
	
	public String[] getAllHospNames() {
		Cursor cursor = null;
		String[] positionnames = null;
		try {
			cursor = db.find(db.getConnection(), "select * from " + TABLE_HOSP , null);
			if (cursor != null) {
				positionnames = new String[cursor.getCount()];
				while (cursor.moveToNext()) {
					positionnames[cursor.getPosition()] = cursor.getString(cursor.getColumnIndex(HOSPITALNAME));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return positionnames;
	}
	public boolean isLocked(){
		return db.isLocked();
	}
	/**
	 * 根据地区Id获取医院信息
	 * @param areaid
	 * @return
	 */
	public ArrayList<HospEntry> getAllHospNames(String areaid) {
		Cursor cursor = null;
		ArrayList<HospEntry>  positionnames = null;

		try {
			cursor = db.find(db.getConnection(), "select * from " + TABLE_HOSP +" where "+areaid+"=?",new String[]{HOSPITALID});
			if (cursor != null) {
				positionnames = new ArrayList<HospEntry>();
				HospEntry entry = null;
				if(areaid.equals("100000"))
				{
					entry = new HospEntry();
					entry.setHOSPITALID("0");
					entry.setHOSPITALNAME("全部医院");
					positionnames.add(entry);
				}
				while (cursor.moveToNext()) {
					entry = new HospEntry();
					entry.setHOSPITALID(cursor.getString(cursor.getColumnIndex(HOSPITALID)));
					entry.setHOSPITALNAME(cursor.getString(cursor.getColumnIndex(HOSPITALNAME)));
					positionnames.add(entry);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return positionnames;
	}
	/**
	 * 为空时获取所有医院数据
	 * */
	public ArrayList<HospEntry> getProvinces(String area_id) {
		Cursor cursor = null;
		ArrayList<HospEntry> departmentgroups = null;
		String sql = null;
		if(TextUtils.isEmpty(area_id)){
			sql = "select distinct "+HOSPITALID +","+HOSPITALNAME+" from " + TABLE_HOSP+" order by "+HOSPITALID;
		}else{
			sql = "select distinct "+HOSPITALID +","+HOSPITALNAME+" from " + TABLE_HOSP+" where "+AREAID+"="+area_id+" order by "+HOSPITALID;
			
		}
		try {
			cursor = db.find(db.getConnection(), sql, null);
			if (cursor != null) {
				departmentgroups = new ArrayList<HospEntry>();
				HospEntry entry = new HospEntry();
				entry.setHOSPITALID("0");
				entry.setHOSPITALNAME("全部医院");
				departmentgroups.add(entry);
				while (cursor.moveToNext()) {
					entry = new HospEntry();
					entry.setHOSPITALID(cursor.getString(cursor.getColumnIndex(HOSPITALID)));
					entry.setHOSPITALNAME(cursor.getString(cursor.getColumnIndex(HOSPITALNAME)));
					departmentgroups.add(entry);
				}


			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();

			}
		}
		return departmentgroups;
	}
	
	public void saveSortName(List<HospEntry> HospEntrys,String AREAID) {
		if(HospEntrys==null){
			return;
		}
		if(TextUtils.isEmpty(AREAID)){
			AREAID = "289";
		}
		ContentValues values = null;
			for (int i = 0; i < HospEntrys.size(); i++) {
				values = new ContentValues();
				values.put(HOSPITALID, HospEntrys.get(i).getHOSPITALID());
				values.put(HOSPITALNAME,  HospEntrys.get(i).getHOSPITALNAME());
				values.put(this.AREAID, AREAID);
				db.save(db.getConnection(), TABLE_HOSP, values);
			}
	}
	/**
	 * 清除数据，传null时为清除所有
	 * @param Areaid
	 */
	public void clearTable(String Areaid) {
		if(TextUtils.isEmpty(Areaid))
			db.delete(db.getConnection(), TABLE_HOSP, null, null);
		else{
			db.delete(db.getConnection(), TABLE_HOSP, AREAID+"=?", new String[]{Areaid});
		}
	}

}
