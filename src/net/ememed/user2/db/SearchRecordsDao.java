package net.ememed.user2.db;

import java.util.ArrayList;
import java.util.List;

import net.ememed.user2.entity.InviteMessage;
import net.ememed.user2.entity.InviteMessage.InviteMesageStatus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class SearchRecordsDao {
	public static final String TABLE_NAME = "search_records";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_CONTENT = "content";
	
	private DbOpenHelper dbHelper;
	
	public SearchRecordsDao(Context context){
		dbHelper = DbOpenHelper.getInstance(context);
	}
	
	/**
	 * 保存搜素记录
	 * @param str
	 * @return  返回这条记录在db中的id
	 */
	public synchronized Integer saveSearchRecord(String str){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int id = -1;
		if(db.isOpen()){
			ContentValues values = new ContentValues();
			values.put(COLUMN_CONTENT, str);
			db.insert(TABLE_NAME, null, values);
			
			Cursor cursor = db.rawQuery("select last_insert_rowid() from " + TABLE_NAME,null); 
            if(cursor.moveToFirst()){
                id = cursor.getInt(0);
            }
		}
		return id;
	}
	
	
	/**
	 * 获取搜索记录
	 * @return
	 */
	public List<String> getSearchRecords(){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<String> records = new ArrayList<String>();
		if(db.isOpen()){
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " order by " + COLUMN_ID + " desc",null);
			while(cursor.moveToNext()){
				String record = null;
				int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
				record = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));
			
				records.add(record);
			}
			cursor.close();
		}
		return records;
	}
	
	public void deleteSearchRecord(String record){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.delete(TABLE_NAME, COLUMN_CONTENT + " = ?", new String[]{record});
		}
	}
}
