package net.ememed.user2.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.ememed.user2.entity.AreaGuahaoEntry;
import net.ememed.user2.entity.CityGuahaoEntry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class GuahaoAreaTable {
	/**
	 * 保存挂号地区
	 * @author chenhj
	 */
	private final static String TABLE_GUAHAO_AREA = "guahao_area";
	
	private final static String AREAID = "area_id";
	private final static String AREANAME = "area_name";
	private final static String CITYID = "city_id";
	private final static String CITYNAME = "city_name";
	
	private DBManagerImpl db = null;
	
	public GuahaoAreaTable() {
		if (db == null) {
			db = DBManager.get();
		}
		if (!db.isTableExits(db.getConnection(), TABLE_GUAHAO_AREA)) {
			createConfigTable();
		}
	}
	
	public GuahaoAreaTable(Context context) {
		if (db == null) {
			db = DBManager.get(context);
		}
		if (!db.isTableExits(db.getConnection(), TABLE_GUAHAO_AREA)) {
			createConfigTable();
		}
	}
	
	private void createConfigTable() {
		String createSql = "create table if not exists "+TABLE_GUAHAO_AREA+" (_id integer primary key autoincrement,"
				+ CITYID + " varchar," + CITYNAME + " varchar,"+ AREAID + " varchar,"+ AREANAME + " varchar)";
		db.creatTable(db.getConnection(), createSql, TABLE_GUAHAO_AREA);
	}
	
	public List<String> getCitiesName() {
		/*
		 * 无数据时或数据库操作失败时返回null
		 */
		Cursor cursor = null;
		List<String> list = null;
		try {
			cursor = db.find(db.getConnection(), "select distinct " + CITYNAME + " from " + TABLE_GUAHAO_AREA , null);
			if (cursor != null) {
				list = new ArrayList<String>();
				
				while (cursor.moveToNext()) {
					list.add(cursor.getString(0));
				}
			}
		} catch (Exception e) {
			list = null;
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return list;
	}
	public List<String> getCitiesId() {
		/*
		 * 无数据时或数据库操作失败时返回null
		 */
		Cursor cursor = null;
		List<String> list = null;
		try {
			cursor = db.find(db.getConnection(), "select distinct " + CITYID + " from " + TABLE_GUAHAO_AREA , null);
			if (cursor != null) {
				list = new ArrayList<String>();
				
				while (cursor.moveToNext()) {
					list.add(cursor.getString(0));
				}
			}
		} catch (Exception e) {
			list = null;
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return list;
	}
	
	public List<String> getAreasName(String cityID) {
		/*
		 * 无数据时或数据库操作失败时返回null
		 */
		Cursor cursor = null;
		List<String> list = null;
		try {
			cursor = db.find(db.getConnection(), "select distinct " + AREANAME + " from " + TABLE_GUAHAO_AREA + " where "
					+ CITYID + " = ?", new String[]{cityID});
			if (cursor != null) {
				list = new ArrayList<String>();
				
				while (cursor.moveToNext()) {
					list.add(cursor.getString(0));
				}
			}
		} catch (Exception e) {
			list = null;
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return list;
	}
	
	public List<AreaGuahaoEntry> getAreaEntry(String cityID) {
		/*
		 * 无数据时或数据库操作失败时返回null
		 */
		Cursor cursor = null;
		List<AreaGuahaoEntry> list = null;
		try {
			cursor = db.find(db.getConnection(), "select distinct " + AREAID + " , " + AREANAME + " from " + TABLE_GUAHAO_AREA + " where "
					+ CITYID + " = ?", new String[]{cityID});
			if (cursor != null) {
				list = new ArrayList<AreaGuahaoEntry>();
				
				while (cursor.moveToNext()) {
					AreaGuahaoEntry entry = new AreaGuahaoEntry();
					entry.setID(cursor.getString(0));
					entry.setAREA_NAME(cursor.getString(1));
					list.add(entry);
				}
			}
		} catch (Exception e) {
			list = null;
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return list;
	}
	
	
	public synchronized void saveAreas(List<CityGuahaoEntry> cityList) {
		
		clearTable();
		if(cityList == null) {
			return;
		}
		
		try {
			for (CityGuahaoEntry entry1 : cityList) {
				List<AreaGuahaoEntry> areaList = entry1.getAREA();
				for(AreaGuahaoEntry entry2 : areaList) {
					ContentValues values = new ContentValues();
					values.put(CITYID, entry1.getID());
					values.put(CITYNAME, entry1.getCITY_NAME());
					values.put(AREAID, entry2.getID());
					values.put(AREANAME, entry2.getAREA_NAME());
					db.save(db.getConnection(), TABLE_GUAHAO_AREA, values);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void clearAreaAtCity(String cityName) {
		db.delete(db.getConnection(), TABLE_GUAHAO_AREA, CITYNAME + " = ? ", new String[]{cityName});
	}
	
	public void clearTable() {
		db.delete(db.getConnection(), TABLE_GUAHAO_AREA, null, null);
	}
}
