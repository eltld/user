package net.ememed.user2.db;

import java.util.ArrayList;

import net.ememed.user2.entity.SortEntry;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
/***
 * 保存医生排序列表
 * @author chen
 */
public class SortConfigTable {


	private final static String TABLE_SORT_NAME = "tb_doctor_sort";
	private final static String sort_id = "sort_id";
	private final static String sort_name = "sort_name";
	
	private DBManagerImpl db = null;
	
	public SortConfigTable() {
		if (db == null) {
			db = DBManager.get();
		}
		if (!db.isTableExits(db.getConnection(), TABLE_SORT_NAME)) {
			createConfigTable();
		}
	}
	
	public SortConfigTable(Context context) {
		if (db == null) {
			db = DBManager.get(context);
		}
		if (!db.isTableExits(db.getConnection(), TABLE_SORT_NAME)) {
			createConfigTable();
		}
	}
	
	private void createConfigTable() {
		String createSql = "create table if not exists "+TABLE_SORT_NAME+" (id integer primary key autoincrement,"
				+ sort_id + " varchar,"+ sort_name + " varchar)";
		db.creatTable(db.getConnection(), createSql, TABLE_SORT_NAME);
	}
	
	public ArrayList<SortEntry> getAllSortNames() {
		Cursor cursor = null;
		ArrayList<SortEntry> positionnames = null;
		try {
			cursor = db.find(db.getConnection(), "select * from " + TABLE_SORT_NAME +" order by sort_id" , null);
			if (cursor != null) {
				positionnames = new ArrayList<SortEntry>();
				SortEntry entry = null;
				while (cursor.moveToNext()) {
					entry = new SortEntry();
					entry.setSort_id(cursor.getString(cursor.getColumnIndex(sort_id)));
					entry.setSort_name(cursor.getString(cursor.getColumnIndex(sort_name)));
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
	
	public void saveSortName(String sort_id_result,String sort_name_result) {
		ContentValues values = new ContentValues();
		values.put(sort_id, sort_id_result);
		values.put(sort_name, sort_name_result);
		db.save(db.getConnection(), TABLE_SORT_NAME, values);
	}
	
	public void clearTable() {
		db.delete(db.getConnection(), TABLE_SORT_NAME, null, null);
	}

}
