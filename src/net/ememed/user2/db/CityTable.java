package net.ememed.user2.db;

import java.util.ArrayList;
import java.util.List;

import net.ememed.user2.entity.AreaEntry;
import net.ememed.user2.entity.CityEntry;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class CityTable {

	private final static String TABLE_CITY = "ememed_citys";
	private final static String city_id = "city_id";
	private final static String city_name = "city_name";

	private DBManagerImpl db = null;
	private DBManager db_msg = null;

	public CityTable() {
		if (db == null) {
			db = DBManager.get();
			db_msg = DBManager.get();
		}
		if (!db.isTableExits(db.getConnection(), TABLE_CITY)) {
			createCityTable();
		}
	}

	public CityTable(Context context) {
		if (db == null) {
			db = DBManager.get(context);
		}
		if (!db.isTableExits(db.getConnection(), TABLE_CITY)) {
			createCityTable();
		}
	}

	private void createCityTable() {
		String createSql = "create table if not exists " + TABLE_CITY
				+ " (id integer primary key autoincrement," + city_id
				+ " varchar," + city_name + " varchar)";
		db.creatTable(db.getConnection(), createSql, TABLE_CITY);
	}

	public void saveSortName(AreaEntry area_result) {
		ContentValues values = null;
		if (null != area_result.getCITY()) {
			if (area_result.getZHIXIA().equals("1")) {
				values = new ContentValues();
				values.put(city_id, area_result.getID());
				values.put(city_name, area_result.getAREANAME());
				db.save(db.getConnection(), TABLE_CITY, values);
			} else {
				for (int i = 1; i < area_result.getCITY().size(); i++) {
					values = new ContentValues();
					values.put(city_id, area_result.getCITY().get(i).getID());
					values.put(city_name, area_result.getCITY().get(i)
							.getAREANAME());
					db.save(db.getConnection(), TABLE_CITY, values);
				}
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
				if (area_result.getZHIXIA().equals("1")) {
					values = new ContentValues();
					values.put(city_id, area_result.getID());
					values.put(city_name, area_result.getAREANAME());
					conData.add(values);
				} else {
					for (int i = 1; i < area_result.getCITY().size(); i++) {
						values = new ContentValues();
						values.put(city_id, area_result.getCITY().get(i).getID());
						values.put(city_name, area_result.getCITY().get(i)
								.getAREANAME());
						conData.add(values);
					}
				}
			}
			
		}
		db.saveAll(db.getConnection(), TABLE_CITY, conData);
	}
	
	
	public void clearTable() {
		db.delete(db.getConnection(), TABLE_CITY, null, null);
	}

	public List<CityEntry> getAllCity() {

		Cursor cursor = null;
		List<CityEntry> departmentgroups = null;
		try {
			cursor = db.find(db.getConnection(), "select distinct " + city_name
					+ "," + city_id + " from " + TABLE_CITY, null);
			if (cursor != null) {
				departmentgroups = new ArrayList<CityEntry>();
				CityEntry cityEntry;
				while (cursor.moveToNext()) {
					cityEntry = new CityEntry();
					cityEntry.setID(cursor.getInt(cursor
							.getColumnIndex(city_id)) + "");
					cityEntry.setAREANAME(cursor.getString(cursor
							.getColumnIndex(city_name)));
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
	
	public CityEntry findName(String name){
		Cursor cursor = null;
		List<CityEntry> departmentgroups = null;
		try {
			cursor = db.find(db.getConnection(), "select distinct " + city_name
					+ "," + city_id + " from " + TABLE_CITY+" where "+city_name+" like '%" + name + "%'", null);
			if (cursor != null) {
				departmentgroups = new ArrayList<CityEntry>();
				CityEntry cityEntry;
				while (cursor.moveToNext()) {
					cityEntry = new CityEntry();
					cityEntry.setID(cursor.getInt(cursor
							.getColumnIndex(city_id)) + "");
					cityEntry.setAREANAME(cursor.getString(cursor
							.getColumnIndex(city_name)));
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
		if(departmentgroups!=null && departmentgroups.size()>0){
		
			return departmentgroups.get(0);
		}
		return null;
	}
	public CityEntry findId(String id){
		Cursor cursor = null;
		List<CityEntry> departmentgroups = null;
		try {
			cursor = db.find(db.getConnection(), "select distinct " + city_name
					+ "," + city_id + " from " + TABLE_CITY+" where "+city_id+"=?", new String[]{id});
			if (cursor != null) {
				departmentgroups = new ArrayList<CityEntry>();
				CityEntry cityEntry;
				while (cursor.moveToNext()) {
					cityEntry = new CityEntry();
					cityEntry.setID(cursor.getInt(cursor
							.getColumnIndex(city_id)) + "");
					cityEntry.setAREANAME(cursor.getString(cursor
							.getColumnIndex(city_name)));
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
		if(departmentgroups!=null&&departmentgroups.size()>0){
			
			return departmentgroups.get(0);
		}
		return null;
	}
}
