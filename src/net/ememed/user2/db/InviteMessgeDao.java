package net.ememed.user2.db;

import java.util.ArrayList;
import java.util.List;

import net.ememed.user2.entity.InviteMessage;
import net.ememed.user2.entity.InviteMessage.InviteMesageStatus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class InviteMessgeDao {
	public static final String TABLE_NAME = "new_friends_msgs";
	public static final String COLUMN_NAME_ID = "id";
	public static final String COLUMN_NAME_FROM = "username";
	public static final String COLUMN_NAME_TIME = "time";
	public static final String COLUMN_NAME_REASON = "reason";
	public static final String COLUMN_NAME_STATUS = "status";
	public static final String COLUMN_NAME_ISINVITEFROMME = "isInviteFromMe";
	
	private DbOpenHelper dbHelper;
	
	public InviteMessgeDao(Context context){
		dbHelper = DbOpenHelper.getInstance(context);
	}
	
	/**
	 * 保存message
	 * @param message
	 * @return  返回这条messaged在db中的id
	 */
	public synchronized Integer saveMessage(InviteMessage message){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int id = -1;
		if(db.isOpen()){
			ContentValues values = new ContentValues();
			values.put(COLUMN_NAME_FROM, message.getFrom());
			values.put(COLUMN_NAME_REASON, message.getReason());
			values.put(COLUMN_NAME_TIME, message.getTime());
			values.put(COLUMN_NAME_STATUS, message.getStatus().ordinal());
			values.put(COLUMN_NAME_ISINVITEFROMME, message.isInviteFromMe());
			db.insert(TABLE_NAME, null, values);
			
			Cursor cursor = db.rawQuery("select last_insert_rowid() from " + TABLE_NAME,null); 
            if(cursor.moveToFirst()){
                id = cursor.getInt(0);
            }
		}
		return id;
	}
	
	/**
	 * 更新message
	 * @param msgId
	 * @param values
	 */
	public void updateMessage(int msgId,ContentValues values){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.update(TABLE_NAME, values, COLUMN_NAME_ID + " = ?", new String[]{String.valueOf(msgId)});
		}
	}
	
	/**
	 * 获取messges
	 * @return
	 */
	public List<InviteMessage> getMessagesList(){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<InviteMessage> msgs = new ArrayList<InviteMessage>();
		if(db.isOpen()){
			Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " desc",null);
			while(cursor.moveToNext()){
				InviteMessage msg = new InviteMessage();
				int id = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID));
				String from = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_FROM));
				String reason = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_REASON));
				long time = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_TIME));
				int isIniviteFromMe = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ISINVITEFROMME));
				int status = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_STATUS));
				
				msg.setId(id);
				msg.setFrom(from);
				msg.setReason(reason);
				msg.setTime(time);
				if(status == InviteMesageStatus.NO_VALIDATION.ordinal())
					msg.setStatus(InviteMesageStatus.NO_VALIDATION);
				else if(status == InviteMesageStatus.IGNORED.ordinal())
					msg.setStatus(InviteMesageStatus.IGNORED);
				else
					msg.setStatus(InviteMesageStatus.AGREED);
				msg.setInviteFromMe(isIniviteFromMe == 0 ? false : true);
				msgs.add(msg);
			}
			cursor.close();
		}
		return msgs;
	}
	
	public void deleteMessage(String from){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.delete(TABLE_NAME, COLUMN_NAME_FROM + " = ?", new String[]{from});
		}
	}
}
