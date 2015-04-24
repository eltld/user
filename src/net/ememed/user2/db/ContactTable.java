package net.ememed.user2.db;

import java.util.ArrayList;
import java.util.List;
import net.ememed.user2.entity.ContactEntry;
import net.ememed.user2.entity.DoctorFansInfo;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.SharePrefUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
/***
 * 联系人列表 
 * @author chen
 */
public class ContactTable {


	final static String TABLE_CONTACT = "tb_contact";
	
	private final static String USER_ID = "user_id";
	
	private final static String MEMBERID = "member_id";
	private String HAVEORDER= "haveorder";;
	private String MSG_ID = "msg_id";
	private String TYPE = "type";
	private String CONTENT = "content";
	private String SENDTIME = "sendtime";
	private String ISSYSTEMMSG = "is_system_msg";
	private String AVATAR ="avatar";
	private String UTYPE = "utype";
	private String PROFESSIONAL = "professional";
	private String HOSPITALNAME = "hospitalname";
	private String REALNAME = "realname";
	private String MEMBERNAME = "membername";
	private String HAS_NEW_MES = "has_new_msg";
	private String IS_MY_FANS = "is_my_fans";
    private String FANS_NUM = "fans_num";
 	private String ATTENTION_TOTAL_NUM = "attention_total_num";
	
	private DBManagerImpl db = null;
	
	public ContactTable() {
		if (db == null) {
			db = DBManager.get();
		}
		if (!db.isTableExits(db.getConnection(), TABLE_CONTACT)) {
			createConfigTable();
		}
	}
	
	public ContactTable(Context context) {
		if (db == null) {
			db = DBManager.get(context);
		}
		if (!db.isTableExits(db.getConnection(), TABLE_CONTACT)) {
			createConfigTable();
		}
	}
	
	private void createConfigTable() {
		String createSql = "create table if not exists "+TABLE_CONTACT+" (id integer primary key autoincrement,"
				+USER_ID +" varchar,"+ MEMBERID + " varchar,"+HAVEORDER+" varchar,"+MSG_ID+" varchar,"+TYPE+" varchar,"+CONTENT+" varchar,"
				+SENDTIME+" varchar,"+ISSYSTEMMSG+" varchar,"+UTYPE+" varchar,"+PROFESSIONAL+" varchar,"+HOSPITALNAME+" varchar,"
				+REALNAME+" varchar,"+MEMBERNAME+" varchar,"+HAS_NEW_MES+" varchar,"+AVATAR+" varchar,"+IS_MY_FANS+" varchar,"
				+FANS_NUM+" integer,"+ATTENTION_TOTAL_NUM+" integer)";
		db.creatTable(db.getConnection(), createSql, TABLE_CONTACT);
	}
	
	public ArrayList<ContactEntry> getAllContacts() {
		Cursor cursor = null;
		ArrayList<ContactEntry> positionnames = null;
		try {
			cursor = db.find(db.getConnection(), "select * from " + TABLE_CONTACT  +" where "+ USER_ID +"=?", new String[]{SharePrefUtil.getString(Conast.MEMBER_ID)});
			if (cursor != null) {
				positionnames = new ArrayList<ContactEntry>();
				ContactEntry entry = null;
				while (cursor.moveToNext()) {
					entry = new ContactEntry();
					entry.setMEMBERID(cursor.getString(cursor.getColumnIndex(MEMBERID)));
					entry.setAVATAR(cursor.getString(cursor.getColumnIndex(AVATAR)));
					entry.setHAVEORDER(cursor.getString(cursor.getColumnIndex(HAVEORDER)));
					entry.setMSG_ID(cursor.getString(cursor.getColumnIndex(MSG_ID)));
					entry.setTYPE(cursor.getString(cursor.getColumnIndex(TYPE)));
					entry.setCONTENT(cursor.getString(cursor.getColumnIndex(CONTENT)));
					entry.setSENDTIME(cursor.getString(cursor.getColumnIndex(SENDTIME)));
					entry.setISSYSTEMMSG(cursor.getString(cursor.getColumnIndex(ISSYSTEMMSG)));
					entry.setUTYPE(cursor.getString(cursor.getColumnIndex(UTYPE)));
					entry.setPROFESSIONAL(cursor.getString(cursor.getColumnIndex(PROFESSIONAL)));
					entry.setHOSPITALNAME(cursor.getString(cursor.getColumnIndex(HOSPITALNAME)));
					entry.setREALNAME(cursor.getString(cursor.getColumnIndex(REALNAME)));
					entry.setMEMBERNAME(cursor.getString(cursor.getColumnIndex(MEMBERNAME)));
//					entry.setHAS_NEW_MES(cursor.getString(cursor.getColumnIndex(HAS_NEW_MES)));
					
					DoctorFansInfo info = new DoctorFansInfo();
					info.setIS_MY_FANS(cursor.getString(cursor.getColumnIndex(IS_MY_FANS)));
					info.setFANS_NUM(cursor.getInt(cursor.getColumnIndex(FANS_NUM)));
					info.setAttention_total_num(cursor.getInt(cursor.getColumnIndex(ATTENTION_TOTAL_NUM)));
					entry.setDOCTOR_FANS(info);
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
	
	public void savePositionName(List<ContactEntry> list) {
		ContactEntry entry = null;
		for (int i = 0; i < list.size(); i++) {
			entry = list.get(i);
			ContentValues values = new ContentValues();
			values.put(USER_ID, SharePrefUtil.getString(Conast.MEMBER_ID));
			values.put(MEMBERID, entry.getMEMBERID());
			values.put(AVATAR, entry.getAVATAR());
			values.put(HAVEORDER, entry.getHAVEORDER());
			values.put(MSG_ID, entry.getMSG_ID());
			values.put(TYPE, entry.getTYPE());
			values.put(CONTENT, entry.getCONTENT());
			values.put(SENDTIME, entry.getSENDTIME());
			values.put(ISSYSTEMMSG, entry.getISSYSTEMMSG());
			values.put(UTYPE, entry.getUTYPE());
			values.put(PROFESSIONAL, entry.getPROFESSIONAL());
			values.put(HOSPITALNAME, entry.getHOSPITALNAME());
			values.put(REALNAME, entry.getREALNAME());
			values.put(MEMBERNAME, entry.getMEMBERNAME());
			values.put(IS_MY_FANS, entry.getDOCTOR_FANS().getIS_MY_FANS());
			values.put(FANS_NUM, entry.getDOCTOR_FANS().getFANS_NUM());
			values.put(ATTENTION_TOTAL_NUM, entry.getDOCTOR_FANS().getAttention_total_num());
			db.save(db.getConnection(), TABLE_CONTACT, values);	
		}
	}
	
	public void clearTable() {
		db.delete(db.getConnection(), TABLE_CONTACT, null, null);
	}
	/**
	 * 根据ID删除
	 * */
	public boolean deleteByUserId() {
		try {
			String deleteSql = USER_ID +"=?";
			return db.delete(db.getConnection(),TABLE_CONTACT, deleteSql, new String[] {SharePrefUtil.getString(Conast.MEMBER_ID)});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
