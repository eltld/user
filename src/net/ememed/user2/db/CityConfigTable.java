package net.ememed.user2.db;

import java.util.ArrayList;
import java.util.List;

import net.ememed.user2.entity.AreaEntry;
import net.ememed.user2.entity.CityEntry;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
/***
 * 保存城市列表
 * @author chen
 */
public class CityConfigTable {

	private final static String TABLE_CITY = "tb_citys";
	private final static String city_id = "city_id";
	private final static String city_name = "city_name";
	
	private final static String province_id = "province_id";
	private final static String province_name = "province_name";
	
	private DBManagerImpl db = null;
	private  DBManager db_msg = null;
	
	public CityConfigTable() {
		if (db == null) {
			db = DBManager.get();
			db_msg = DBManager.get();
		}
		if (!db.isTableExits(db.getConnection(), TABLE_CITY)) {
			createCityTable();
		}
	}
	
	public CityConfigTable(Context context) {
		if (db == null) {
			db = DBManager.get(context);
		}
		if (!db.isTableExits(db.getConnection(), TABLE_CITY)) {
			createCityTable();
		}
	}
	
	private void createCityTable() {
		String createSql = "create table if not exists "+TABLE_CITY+" (id integer primary key autoincrement,"
				+  province_id +" varchar,"+ province_name + " varchar,"+ city_id +" varchar,"+ city_name +" varchar)";
		db.creatTable(db.getConnection(), createSql, TABLE_CITY);
	}
	
	public String[] getAllSortNames() {
		Cursor cursor = null;
		String[] positionnames = null;
		try {
			cursor = db.find(db.getConnection(), "select * from " + TABLE_CITY , null);
			if (cursor != null) {
				positionnames = new String[cursor.getCount()];
				while (cursor.moveToNext()) {
					positionnames[cursor.getPosition()] = cursor.getString(cursor.getColumnIndex(province_name));
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
	
	public ArrayList<CityEntry> getAllCityNames(String provinceId) {
		Cursor cursor = null;
		ArrayList<CityEntry>  positionnames = null;

		try {
			cursor = db.find(db.getConnection(), "select * from " + TABLE_CITY +" where "+province_id+"=?",new String[]{provinceId});
			if (cursor != null) {
				positionnames = new ArrayList<CityEntry>();
				CityEntry entry = null;
				if(provinceId.equals("100000"))
				{
					entry = new CityEntry();
					entry.setID("0");
					entry.setAREANAME("所有城市");
					positionnames.add(entry);
				}
				while (cursor.moveToNext()) {
					entry = new CityEntry();
					entry.setID(cursor.getString(cursor.getColumnIndex(city_id)));
					entry.setAREANAME(cursor.getString(cursor.getColumnIndex(city_name)));
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
	/**获取省份  过滤重复数据*/
	public ArrayList<AreaEntry> getProvinces() {
		Cursor cursor = null;
		ArrayList<AreaEntry> departmentgroups = null;
		try {
			cursor = db.find(db.getConnection(), "select distinct "+province_id +","+province_name+" from " + TABLE_CITY+" order by province_id", null);
			if (cursor != null) {
				departmentgroups = new ArrayList<AreaEntry>();
				AreaEntry areaEntry = new AreaEntry();
				areaEntry.setID("100000");
				areaEntry.setAREANAME("所有城市");
				departmentgroups.add(areaEntry);
				while (cursor.moveToNext()) {
					areaEntry = new AreaEntry();
					areaEntry.setID(cursor.getInt(cursor.getColumnIndex(province_id))+"");
					areaEntry.setAREANAME(cursor.getString(cursor.getColumnIndex(province_name)));
					departmentgroups.add(areaEntry);
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
	
	
	public void saveSortName(AreaEntry area_result) {
		ContentValues values = null;
		if (null != area_result.getCITY()) {
			for (int i = 0; i < area_result.getCITY().size(); i++) {
				values = new ContentValues();
				values.put(province_id, area_result.getID());
				values.put(province_name, area_result.getAREANAME());
				values.put(city_id, area_result.getCITY().get(i).getID());
				values.put(city_name, area_result.getCITY().get(i).getAREANAME());
				db.save(db.getConnection(), TABLE_CITY, values);
			}
		}
	}
	
	
	public void areaEntryToContentValues(List<AreaEntry> data){
		List<ContentValues> conData = new ArrayList<ContentValues>();
		AreaEntry area_result ;
		ContentValues values = null;
		for (int j = 0; j < data.size(); j++) {
			area_result = data.get(j);
			if (null != area_result.getCITY()) {
				for (int i = 0; i < area_result.getCITY().size(); i++) {
					values = new ContentValues();
					values.put(province_id, area_result.getID());
					values.put(province_name, area_result.getAREANAME());
					values.put(city_id, area_result.getCITY().get(i).getID());
					values.put(city_name, area_result.getCITY().get(i).getAREANAME());
					conData.add(values);
				}
			}
		}
		db.saveAll(db.getConnection(), TABLE_CITY, conData);
	}
	
	
	
	
	
	
	public void clearTable() {
		db.delete(db.getConnection(), TABLE_CITY, null, null);
		
	}
	
	
	public List<CityEntry> getAllCity(){
		
		Cursor cursor = null;
		List<CityEntry> departmentgroups = null;
		try {
			cursor = db.find(db.getConnection(), "select distinct "+city_name +","+city_id+" from " + TABLE_CITY+" order by province_id", null);
			if (cursor != null) {
				departmentgroups = new ArrayList<CityEntry>();
				CityEntry cityEntry ;
				while (cursor.moveToNext()) {
					cityEntry = new CityEntry();
					cityEntry.setID(cursor.getInt(cursor.getColumnIndex(city_id))+"");
					cityEntry.setAREANAME(cursor.getString(cursor.getColumnIndex(city_name)));
					departmentgroups.add(cityEntry);
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

}
