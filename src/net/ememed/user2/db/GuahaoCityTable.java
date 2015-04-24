package net.ememed.user2.db;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class GuahaoCityTable {
	/**
	 * 保存挂号城市
	 * @author chenhj
	 */
	private final static String TABLE_GUAHAO_CITY = "guahao_city";
	
	private final static String CITYID = "city_id";
	private final static String CITYNAME = "city_name";
	
	private DBManagerImpl db = null;
	
	public GuahaoCityTable() {
		if (db == null) {
			db = DBManager.get();
		}
		if (!db.isTableExits(db.getConnection(), TABLE_GUAHAO_CITY)) {
			createConfigTable();
		}
	}
	
	public GuahaoCityTable(Context context) {
		if (db == null) {
			db = DBManager.get(context);
		}
		if (!db.isTableExits(db.getConnection(), TABLE_GUAHAO_CITY)) {
			createConfigTable();
		}
	}
	
	private void createConfigTable() {
		String createSql = "create table if not exists "+TABLE_GUAHAO_CITY+" (_id integer primary key autoincrement,"
				+ CITYID + " varchar,"+CITYNAME+" varchar)";
		db.creatTable(db.getConnection(), createSql, TABLE_GUAHAO_CITY);
	}
	
	public Map<String, String> getCities() {
		/*
		 * 无数据时或数据库操作失败时返回null
		 * 有数据返回map<ID, CITYNAME>
		 */
		Cursor cursor = null;
		Map<String, String> cities = null;
		try {
			cursor = db.find(db.getConnection(), "select * from " + TABLE_GUAHAO_CITY, null);
			if (cursor != null) {
				cities = new HashMap<String, String>();
				
				while (cursor.moveToNext()) {

					String key = cursor.getString(1);
					String value = cursor.getString(2);
					
					cities.put(key, value);
				}
			}
		} catch (Exception e) {
			cities = null;
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return cities;
	}
	
	
	public void saveCities(Map<String, String> cities) {
		
		clearTable();
		
		Set<String> city_ids = cities.keySet();
		
		try {
			for (String id : city_ids) {
				String name = cities.get(id);
				ContentValues values = new ContentValues();
				values.put(CITYID, id);
				values.put(CITYNAME, name);
				db.save(db.getConnection(), TABLE_GUAHAO_CITY, values);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void clearTable() {
		db.delete(db.getConnection(), TABLE_GUAHAO_CITY, null, null);
	}
}
