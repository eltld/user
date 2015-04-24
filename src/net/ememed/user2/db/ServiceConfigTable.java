package net.ememed.user2.db;

import java.lang.reflect.Array;
import java.util.ArrayList;

import net.ememed.user2.entity.ServiceEntry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
/**
 * 业务db：图文咨询、预约通话、签约私人医生、上门会诊、预约加号、预约住院、其他服务
 * @author ASIMO
 *
 */
public class ServiceConfigTable {


	private final static String TABLE_CONFIG_POSITION = "config_position";
	
	private final static String position_id = "position_id";
	private final static String position_name = "position_name";
	
	public ServiceConfigTable() {
		DBManagerImpl db = DBManager.get();
		if (!db.isTableExits(db.getConnection(), TABLE_CONFIG_POSITION)) {
			createConfigTable();
		}
	}
	
	public ServiceConfigTable(Context context) {
		DBManagerImpl db = DBManager.get(context);
		if (!db.isTableExits(db.getConnection(), TABLE_CONFIG_POSITION)) {
			createConfigTable();
		}
	}
	
	private void createConfigTable() {
		DBManagerImpl db = DBManager.get();
		String createSql = "create table if not exists "+TABLE_CONFIG_POSITION+" (id integer primary key autoincrement,"
				+ position_id + " integer,"+position_name+" varchar)";
		db.creatTable(db.getConnection(), createSql, TABLE_CONFIG_POSITION);
	}
	
	public String getServiceName(String str_position_id) {
		Cursor cursor = null;
		String positionnames = null;
		try {
			DBManagerImpl db = DBManager.get();
			cursor = db.find(db.getConnection(), "select * from " + TABLE_CONFIG_POSITION + " where "+position_id+" = ?", new String[]{ str_position_id });
			if (cursor != null) {
				while (cursor.moveToNext()) {
					positionnames = cursor.getString(cursor.getColumnIndex(position_name));
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
	
	public ArrayList<ServiceEntry> getAllPositionNames() {
		Cursor cursor = null;
		ArrayList<ServiceEntry> positionnames = null;
		try {
			DBManagerImpl db = DBManager.get();
			cursor = db.find(db.getConnection(), "select * from " + TABLE_CONFIG_POSITION +" order by "+position_id, null);
			if (cursor != null) {
				positionnames = new ArrayList<ServiceEntry>();
				ServiceEntry entry = null;
				while (cursor.moveToNext()) {
					entry = new ServiceEntry();
					entry.setService_id(cursor.getString(cursor.getColumnIndex(position_id)));
					entry.setService_name(cursor.getString(cursor.getColumnIndex(position_name)));
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
	
	public void savePositionName(String str_position_group,String str_position_name) {
		DBManagerImpl db = DBManager.get();
		ContentValues values = new ContentValues();
		values.put(position_id, Integer.valueOf(str_position_group));
		values.put(position_name, str_position_name);
		db.save(db.getConnection(), TABLE_CONFIG_POSITION, values);
	}
	
	public void clearTable() {
		DBManagerImpl db = DBManager.get();
		db.delete(db.getConnection(), TABLE_CONFIG_POSITION, null, null);
	}

}
