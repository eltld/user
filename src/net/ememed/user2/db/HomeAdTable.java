package net.ememed.user2.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import net.ememed.user2.entity.AdsEntry;

public class HomeAdTable {

	public static String TABLE_HOME_AD = "home_ads";
	
	public static String TITLE = "TITLE";
	public static String URL = "URL";
	public static String THUMB = "THUMB";
	public static String POSITION = "POSITION";
	public static String STATUS = "STATUS";
	public static String CREATETIME = "CREATETIME";
	public static String UPDATETIME = "UPDATETIME";
	public static String ORDERBY = "ORDERBY";
	
	private DBManagerImpl db = null;

	public HomeAdTable() {
		if (db == null) {
			db = DBManager.get();
		}
		if (!db.isTableExits(db.getConnection(), TABLE_HOME_AD)) {
			createHomeAdTable();
		}
	}

	private void createHomeAdTable() {
		String createSql = "create table if not exists " + TABLE_HOME_AD + " ("
				+ TITLE + " VARCHAR, " + URL + " VARCHAR, " + THUMB + " VARCHAR, "
				+ POSITION + " VARCHAR, " + STATUS + " VARCHAR, " 
				+ CREATETIME + " VARCHAR, "	+ UPDATETIME + " VARCHAR, " 
				+ ORDERBY + " VARCHAR) ";
		db.creatTable(db.getConnection(), createSql, TABLE_HOME_AD);
	}
	
	public List<AdsEntry> getAdsList() {
		ArrayList<AdsEntry> adsList = null;
		Cursor cursor = null;
		String sql = "select * from " + TABLE_HOME_AD;
		try {
			cursor = db.find(db.getConnection(), sql, null);
			if(cursor != null) {
				adsList = new ArrayList<AdsEntry>();
				AdsEntry entry = null;
				while (cursor.moveToNext()) {
					entry = new AdsEntry();
					entry.setTITLE(cursor.getString(cursor.getColumnIndex(TITLE)));
					entry.setURL(cursor.getString(cursor.getColumnIndex(URL)));
					entry.setTHUMB(cursor.getString(cursor.getColumnIndex(THUMB)));
					entry.setPOSITION(cursor.getString(cursor.getColumnIndex(POSITION)));
					entry.setSTATUS(cursor.getString(cursor.getColumnIndex(STATUS)));
					entry.setCREATETIME(cursor.getString(cursor.getColumnIndex(CREATETIME)));
					entry.setUPDATETIME(cursor.getString(cursor.getColumnIndex(UPDATETIME)));
					entry.setORDERBY(cursor.getString(cursor.getColumnIndex(ORDERBY)));
					
					adsList.add(entry);
				}
			}
			return adsList;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
	}
	
	public void saveAdsList(List<AdsEntry> list) {
		ContentValues values = new ContentValues();
		AdsEntry entry;
		//0位置为本地项，跳过
		for(int i = 0; i < list.size();  i++) {
			entry = list.get(i);
			values.clear();
			values.put(TITLE, entry.getTITLE());
			values.put(URL, entry.getURL());
			values.put(THUMB, entry.getTHUMB());
			values.put(POSITION, entry.getPOSITION());
			values.put(STATUS, entry.getSTATUS());
			values.put(CREATETIME, entry.getCREATETIME());
			values.put(UPDATETIME, entry.getUPDATETIME());
			values.put(ORDERBY, entry.getORDERBY());
			
			db.save(db.getConnection(), TABLE_HOME_AD, values);	
		}
	}
	
	
	public void clearTable() {
		db.delete(db.getConnection(), TABLE_HOME_AD, null, null);
	}
}
