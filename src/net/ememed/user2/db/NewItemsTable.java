package net.ememed.user2.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.ememed.user2.entity.NewsItem;
import android.content.ContentValues;
import android.database.Cursor;

public class NewItemsTable {

	public static String TABLE_NAME = "New_Items";
	public static String COLUMN_NAME_ID = "ID";
	public static String COLUMN_NAME_TITLE = "TITLE";
	public static String COLUMN_NAME_SUBTITLE = "SUBTITLE";
	public static String COLUMN_NAME_PIC = "PIC";
	public static String COLUMN_NAME_PICEXT1 = "PICEXT1";
	public static String COLUMN_NAME_PICEXT2 = "PICEXT2";
	public static String COLUMN_NAME_PICEXT3 = "PICEXT3";
	public static String COLUMN_NAME_TYPE = "TYPE";
	public static String COLUMN_NAME_FURL = "FURL";
	public static String COLUMN_NAME_UPDATETIME = "UPDATETIME";
	public static String COLUMN_NAME_TYPEID = "TYPEID";
	public static String COLUMN_NAME_TAG = "TAG";
	public static String COLUMN_NAME_ALLOWCOMMENT = "ALLOWCOMMENT";
	private static String tag_Focus = "Focus";
	private static String tag_News = "News";

	private DBManagerImpl db = null;

	public NewItemsTable() {
		if (db == null) {
			db = DBManager.get();
		}
		if (!db.isTableExits(db.getConnection(), TABLE_NAME)) {
			createNewItemsTable();
		}
		if (!db.isColumnExits(db.getConnection(), TABLE_NAME, COLUMN_NAME_ALLOWCOMMENT)) {
			db.addColumn(db.getConnection(), TABLE_NAME, COLUMN_NAME_ALLOWCOMMENT, "VARCHAR");
		}

	}

	private void createNewItemsTable() {
		String createSql = "create table if not exists " + TABLE_NAME + " (" + COLUMN_NAME_ID + " INTEGER, " + COLUMN_NAME_TITLE + " VARCHAR, " + COLUMN_NAME_SUBTITLE + " VARCHAR, " + COLUMN_NAME_PIC + " VARCHAR, " + COLUMN_NAME_PICEXT1 + " VARCHAR, " + COLUMN_NAME_PICEXT2 + " VARCHAR, " + COLUMN_NAME_PICEXT3 + " VARCHAR, " + COLUMN_NAME_TYPE + " VARCHAR, " + COLUMN_NAME_FURL + " VARCHAR, " + COLUMN_NAME_TAG + " VARCHAR, " + COLUMN_NAME_UPDATETIME + " VARCHAR, " + COLUMN_NAME_TYPEID + " INTEGER, "+COLUMN_NAME_ALLOWCOMMENT+" VARCHAR); ";
		// + "primary key ("+COLUMN_NAME_ID+","+COLUMN_NAME_TYPEID+"));";
		db.creatTable(db.getConnection(), createSql, TABLE_NAME);
	}

	public void saveFocuslist(List<NewsItem> focuslist, Integer TYPEID) {
		if (focuslist == null || focuslist.size() == 0)
			return;
		savelist(focuslist, TYPEID, tag_Focus);
	}

	public void saveNewslist(List<NewsItem> newslist, Integer TYPEID) {
		if (newslist == null || newslist.size() == 0)
			return;
		savelist(newslist, TYPEID, tag_News);
	}

	public void savelist(List<NewsItem> newslist, Integer TYPEID, String TAG) {
		// db.delete(db.getConnection(), TABLE_NAME,
		// COLUMN_NAME_TYPEID+"="+TYPEID,null);
		ContentValues values = new ContentValues();
		
		for (NewsItem newsItem : newslist) {
			values.clear();
			
			values.put(COLUMN_NAME_ID, newsItem.getID());
			values.put(COLUMN_NAME_TITLE, newsItem.getTITLE());
			values.put(COLUMN_NAME_SUBTITLE, newsItem.getSUBTITLE());
			values.put(COLUMN_NAME_PIC, newsItem.getPIC());
			values.put(COLUMN_NAME_PICEXT1, newsItem.getPICEXT1());
			// values.put(COLUMN_NAME_PICEXT2, newsItem.get);
			// values.put(COLUMN_NAME_PICEXT3, newsItem.get);
			values.put(COLUMN_NAME_TYPE, newsItem.getTYPE());
			values.put(COLUMN_NAME_FURL, newsItem.getFURL());
			values.put(COLUMN_NAME_UPDATETIME, newsItem.getUPDATETIME());
			values.put(COLUMN_NAME_TYPEID, TYPEID);
			values.put(COLUMN_NAME_TAG, TAG);
			values.put(COLUMN_NAME_ALLOWCOMMENT, newsItem.getALLOWCOMMENT());
			db.save(db.getConnection(), TABLE_NAME, values);
		}
	}

	public ArrayList<NewsItem> getNewslist(Integer TYPEID) {
		return getlist(TYPEID, tag_News);
	}

	public ArrayList<NewsItem> getFocuslist(Integer TYPEID) {
		return getlist(TYPEID, tag_Focus);
	}

	public ArrayList<NewsItem> getlist(Integer TYPEID, String TAG) {
		String findSql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_TYPEID + "=? and " + COLUMN_NAME_TAG + "=?;";
		Cursor find = null;
		ArrayList<NewsItem> items = null;
		try {
			find = db.find(db.getConnection(), findSql, new String[] { TYPEID.toString(), TAG });
			items = new ArrayList<NewsItem>();
			NewsItem item = null;
			if (find == null) {
				return null;
			}
			int num = 0;
			int MAX_CACHE_SIZE = 20;
			if (TAG.equals(tag_Focus)) {
				MAX_CACHE_SIZE = 4;
			}
			while (find.moveToNext() && num < MAX_CACHE_SIZE) {
				item = new NewsItem();
				item.setID(find.getString(find.getColumnIndex(COLUMN_NAME_ID)));
				item.setTITLE(find.getString(find.getColumnIndex(COLUMN_NAME_TITLE)));
				item.setSUBTITLE(find.getString(find.getColumnIndex(COLUMN_NAME_SUBTITLE)));
				item.setPIC(find.getString(find.getColumnIndex(COLUMN_NAME_PIC)));
				item.setPICEXT1(find.getString(find.getColumnIndex(COLUMN_NAME_PICEXT1)));
				item.setTYPE(find.getString(find.getColumnIndex(COLUMN_NAME_TYPE)));
				item.setFURL(find.getString(find.getColumnIndex(COLUMN_NAME_FURL)));
				item.setUPDATETIME(find.getString(find.getColumnIndex(COLUMN_NAME_UPDATETIME)));
				item.setALLOWCOMMENT(find.getString(find.getColumnIndex(COLUMN_NAME_ALLOWCOMMENT)));
				items.add(item);
				num++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (find != null)
				find.close();
		}
		return items;
	}

	public void clearAtTypePage(Integer TYPEID) {
		try {
			db.delete(db.getConnection(), TABLE_NAME, COLUMN_NAME_TYPEID + " = ?; ", new String[] { TYPEID.toString() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearTable() {
		db.delete(db.getConnection(), TABLE_NAME, null, null);
	}
}
