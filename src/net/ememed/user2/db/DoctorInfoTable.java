package net.ememed.user2.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import net.ememed.user2.entity.DoctorEntry;
import net.ememed.user2.entity.DoctorServiceEntry;
import net.ememed.user2.entity.OfferServiceEntry;
import net.ememed.user2.entity.OfferServiceEntry2;
import net.ememed.user2.entity.OfferServicePacketEntry;
import net.ememed.user2.entity.PacketPeriod;

public class DoctorInfoTable {

	private static final String TABLE_NAME = "doctor_info";
	private static final String SEXNAME = "SEXNAME";
	private static final String AVATAR = "AVATAR";
	private static final String DOCTORID = "DOCTORID";
	private static final String REALNAME = "REALNAME";
	private static final String PROFESSIONAL = "PROFESSIONAL";
	private static final String HOSPITALNAME = "HOSPITALNAME";
	private static final String ROOMNAME = "ROOMNAME";
	private static final String MOBILE = "MOBILE";
	private static final String SPECIALITY = "SPECIALITY";
	private static final String ALLOWFREECONSULT = "ALLOWFREECONSULT";
	private static final String DOCTOR_DSRS = "DOCTOR_DSRS";

	private static final String TEXT_IS = "TEXT_IS";
	private static final String TEXT_PRICE = "TEXT_PRICE";

	private static final String CALL_IS = "CALL_IS";
	private static final String CALL_PRICE = "CALL_PRICE";

	private static final String ZHUYUAN_IS = "ZHUYUAN_IS";
	private static final String ZHUYUAN_PRICE = "ZHUYUAN_PRICE";

	private static final String JIAHAO_IS = "JIAHAO_IS";
	private static final String JIAHAO_PRICE = "JIAHAO_PRICE";

	private static final String SHANGMEN_IS = "SHANGMEN_IS";

	private static final String PACKET_ID = "PACKET_ID";
	private static final String PACKET_SERVICE_CONTENT = "PACKET_SERVICE_CONTENT";
	private static final String PACKET_PERIOD_IDS = "PACKET_PERIOD_IDS";

	// --------------------------------------------------------------------------
	private static final String TABLE_NAME_PACKET_PERIOD = "PACKET_PERIOD";
	private static final String packet_daytype = "packet_daytype";
	private static final String packet_period_id = "packet_period_id";
	private static final String packet_daynum = "packet_daynum";
	private static final String packet_period_price = "packet_period_price";
	private static final String packet_period_desc = "packet_period_desc";
	private DBManagerImpl db;

	public DoctorInfoTable() {
		if (db == null) {
			db = DBManager.get();
		}
		if (!db.isTableExits(db.getConnection(), TABLE_NAME)) {
			createDoctorInfoTable();
		}
		if (!db.isTableExits(db.getConnection(), TABLE_NAME_PACKET_PERIOD)) {
			createPacketPeriodTable();
		}
	}

	private void createPacketPeriodTable() {
		String createSql = "create table if not exists " + TABLE_NAME_PACKET_PERIOD + " (" + packet_daytype + " VARCHAR, " + packet_period_id + " VARCHAR PRIMARY KEY, " + packet_daynum + " VARCHAR, " + packet_period_price + " VARCHAR, " + packet_period_desc + " VARCHAR);";
		db.creatTable(db.getConnection(), createSql, TABLE_NAME_PACKET_PERIOD);
	}

	private void createDoctorInfoTable() {
		String createSql = "create table if not exists " + TABLE_NAME + " (" + SEXNAME + " VARCHAR, " + AVATAR + " VARCHAR, " + DOCTORID + " VARCHAR PRIMARY KEY, " + REALNAME + " VARCHAR, " + PROFESSIONAL + " VARCHAR, " + HOSPITALNAME + " VARCHAR, " + ROOMNAME + " VARCHAR, " + MOBILE + " VARCHAR, " + SPECIALITY + " VARCHAR, " + ALLOWFREECONSULT + " VARCHAR, " + DOCTOR_DSRS + " VARCHAR, " + TEXT_IS + " VARCHAR, " + TEXT_PRICE + " VARCHAR, " + CALL_IS + " VARCHAR, " + CALL_PRICE + " VARCHAR, " + ZHUYUAN_IS + " VARCHAR, " + ZHUYUAN_PRICE + " VARCHAR, " + JIAHAO_IS + " VARCHAR, " + JIAHAO_PRICE + " VARCHAR, " + SHANGMEN_IS + " VARCHAR, " + PACKET_ID + " VARCHAR, " + PACKET_SERVICE_CONTENT + " VARCHAR, " + PACKET_PERIOD_IDS + " VARCHAR);";

		db.creatTable(db.getConnection(), createSql, TABLE_NAME);
	}

	public void saveDoctorInfo(List<DoctorEntry> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		if (db == null)
			db = DBManager.get();
		SQLiteDatabase sqldb = (SQLiteDatabase) db.getConnection();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			DoctorEntry doctorEntry = (DoctorEntry) iterator.next();
			ContentValues values = getDoctorInfoCV(doctorEntry);
			sqldb.replace(TABLE_NAME, null, values);
			List<PacketPeriod> packet_PERIOD_LIST = doctorEntry.getOFFER_SERVICE().getOFFER_PACKET().getPACKET_PERIOD_LIST();
			for (Iterator iterator2 = packet_PERIOD_LIST.iterator(); iterator2.hasNext();) {
				PacketPeriod packetPeriod = (PacketPeriod) iterator2.next();
				ContentValues packet = getPacketPeriodCV(packetPeriod);
				if (packet != null)
					sqldb.replace(TABLE_NAME_PACKET_PERIOD, null, packet);
			}
			// sqldb.replace(table, nullColumnHack, initialValues)
		}

	}

	private ContentValues getPacketPeriodCV(PacketPeriod packetPeriod) {
		if (packetPeriod == null) {
			return null;
		}
		// private static final String packet_daytype = "packet_daytype";
		// private static final String packet_period_id = "packet_period_id";
		// private static final String packet_daynum = "packet_daynum";
		// private static final String packet_period_price =
		// "packet_period_price";
		// private static final String packet_period_desc =
		// "packet_period_desc";
		ContentValues values = new ContentValues();
		values.put(packet_daytype, packetPeriod.getPacket_daytype());
		values.put(packet_period_id, packetPeriod.getPacket_period_id());
		values.put(packet_daynum, packetPeriod.getPacket_daynum());
		values.put(packet_period_price, packetPeriod.getPacket_period_price());
		values.put(packet_period_desc, packetPeriod.getPacket_period_desc());
		return values;
	}

	private ContentValues getDoctorInfoCV(DoctorEntry doctorEntry) {
		// + HOSPITALNAME + " VARCHAR, "
		// + ROOMNAME + " VARCHAR, "
		// + MOBILE + " VARCHAR, "
		// + SPECIALITY + " VARCHAR, "
		// + ALLOWFREECONSULT + " VARCHAR, "
		// + DOCTOR_DSRS + " VARCHAR, "
		// + TEXT_IS + " VARCHAR, "
		// + TEXT_PRICE + " VARCHAR, "
		// + CALL_IS + " VARCHAR, "
		// + CALL_PRICE + " VARCHAR, "
		// + ZHUYUAN_IS + " VARCHAR, "
		// + ZHUYUAN_PRICE + " VARCHAR, "
		// + JIAHAO_IS + " INTEGER, "
		// + JIAHAO_PRICE + " VARCHAR, "
		// + SHANGMEN_IS + " VARCHAR, "
		// + PACKET_ID + " VARCHAR, "
		// + PACKET_SERVICE_CONTENT + " VARCHAR, "
		// + PACKET_PERIOD_IDS + " VARCHAR);";
		ContentValues values = new ContentValues();
		values.put(SEXNAME, doctorEntry.getSEXNAME());
		values.put(AVATAR, doctorEntry.getAVATAR());
		values.put(DOCTORID, doctorEntry.getDOCTORID());
		values.put(REALNAME, doctorEntry.getREALNAME());
		values.put(PROFESSIONAL, doctorEntry.getPROFESSIONAL());
		values.put(HOSPITALNAME, doctorEntry.getHOSPITALNAME());
		values.put(ROOMNAME, doctorEntry.getROOMNAME());
		values.put(MOBILE, doctorEntry.getMOBILE());
		values.put(SPECIALITY, doctorEntry.getSPECIALITY());
		values.put(ALLOWFREECONSULT, doctorEntry.getALLOWFREECONSULT());
		values.put(DOCTOR_DSRS, doctorEntry.getDOCTOR_DSRS());
		values.put(TEXT_IS, doctorEntry.getOFFER_SERVICE().getOFFER_TEXT().getIS_OFFER());
		values.put(TEXT_PRICE, doctorEntry.getOFFER_SERVICE().getOFFER_TEXT().getOFFER_PRICE());
		values.put(CALL_IS, doctorEntry.getOFFER_SERVICE().getOFFER_CALL().getIS_OFFER());
		values.put(CALL_PRICE, doctorEntry.getOFFER_SERVICE().getOFFER_CALL().getOFFER_PRICE());
		values.put(ZHUYUAN_IS, doctorEntry.getOFFER_SERVICE().getOFFER_ZHUYUAN().getIS_OFFER());
		values.put(ZHUYUAN_PRICE, doctorEntry.getOFFER_SERVICE().getOFFER_ZHUYUAN().getOFFER_PRICE());
		values.put(JIAHAO_IS, doctorEntry.getOFFER_SERVICE().getOFFER_JIAHAO().getIS_OFFER());
		values.put(JIAHAO_PRICE, doctorEntry.getOFFER_SERVICE().getOFFER_JIAHAO().getOFFER_PRICE());
		values.put(SHANGMEN_IS, doctorEntry.getOFFER_SERVICE().getOFFER_SHANGMEN().getIS_OFFER());
		values.put(PACKET_ID, doctorEntry.getOFFER_SERVICE().getOFFER_PACKET().getPACKET_ID());
		values.put(PACKET_SERVICE_CONTENT, doctorEntry.getOFFER_SERVICE().getOFFER_PACKET().getSERVICE_CONTENT());

		List<PacketPeriod> packet_PERIOD_LIST = doctorEntry.getOFFER_SERVICE().getOFFER_PACKET().getPACKET_PERIOD_LIST();
		StringBuilder sb = new StringBuilder();
		for (Iterator iterator = packet_PERIOD_LIST.iterator(); iterator.hasNext();) {
			PacketPeriod packetPeriod = (PacketPeriod) iterator.next();
			sb.append(packetPeriod.getPacket_period_id());
			sb.append(",");
		}
		if (sb.length() != 0 && sb.length() > 1)
			values.put(PACKET_PERIOD_IDS, sb.substring(0, sb.length() - 2));

		return values;
	}

	public List<DoctorEntry> findDoctorInfos(String current_service) {

		String findSql = "select * from " + TABLE_NAME + " where ";
		if (TextUtils.isEmpty(current_service)) {
			return null;
		} else if ("1".equals(current_service)) {// 图文
			findSql += TEXT_IS + " = 1";
		} else if ("2".equals(current_service)) {// 电话
			findSql += CALL_IS + " = 1";
		} else if ("3".equals(current_service)) {// 挂号
			findSql += JIAHAO_IS + " = 1";
		} else if ("4".equals(current_service)) {// 上门
			findSql += ZHUYUAN_IS + " = 1";
		} else {// 其他～ 签约私人医生
			findSql = "select * from " + TABLE_NAME;
		}
		return findDoctor(findSql);
	}

	public List<DoctorEntry> findDoctorInfos() {
		String findSql = "select * from " + TABLE_NAME;
		return findDoctor(findSql);
	}

	private List<DoctorEntry> findDoctor(String sql) {

		Cursor find = null;

		List<DoctorEntry> list = null;
		try {
			find = db.find(db.getConnection(), sql, null);
			if (find == null) {
				return null;
			}
			list = new ArrayList<DoctorEntry>();
			while (find.moveToNext()) {
				DoctorEntry doctorInfo = getDoctorInfo(find);
				list.add(doctorInfo);
			}
		} catch (Exception e) {
		} finally {
			if (find != null)
				find.close();
		}

		if (list.size() == 0)
			return null;

		return list;

	}

	private DoctorEntry getDoctorInfo(Cursor find) {
		// TODO Auto-generated method stub
		DoctorEntry doctor = new DoctorEntry();
		doctor.setALLOWFREECONSULT(find.getString(find.getColumnIndex(ALLOWFREECONSULT)));
		doctor.setAVATAR(find.getString(find.getColumnIndex(AVATAR)));
		doctor.setDOCTOR_DSRS(find.getString(find.getColumnIndex(DOCTOR_DSRS)));
		doctor.setDOCTORID(find.getString(find.getColumnIndex(DOCTORID)));
		doctor.setHOSPITALNAME(find.getString(find.getColumnIndex(HOSPITALNAME)));
		doctor.setMOBILE(find.getString(find.getColumnIndex(MOBILE)));
		doctor.setPROFESSIONAL(find.getString(find.getColumnIndex(PROFESSIONAL)));
		doctor.setREALNAME(find.getString(find.getColumnIndex(REALNAME)));
		doctor.setROOMNAME(find.getString(find.getColumnIndex(ROOMNAME)));
		doctor.setSEXNAME(find.getString(find.getColumnIndex(SEXNAME)));
		doctor.setSPECIALITY(find.getString(find.getColumnIndex(SPECIALITY)));

		// rivate static final String TEXT_IS = "TEXT_IS";
		// private static final String TEXT_PRICE = "TEXT_PRICE";
		//
		// private static final String CALL_IS = "CALL_IS";
		// private static final String CALL_PRICE = "CALL_PRICE";
		//
		// private static final String ZHUYUAN_IS = "ZHUYUAN_IS";
		// private static final String ZHUYUAN_PRICE = "ZHUYUAN_PRICE";
		//
		// private static final String JIAHAO_IS = "JIAHAO_IS";
		// private static final String JIAHAO_PRICE = "JIAHAO_PRICE";
		//
		// private static final String SHANGMEN_IS = "JIAHAO_IS";
		//
		// private static final String PACKET_ID = "PACKET_ID";
		// private static final String PACKET_SERVICE_CONTENT =
		// "PACKET_SERVICE_CONTENT";
		// private static final String PACKET_PERIOD_IDS = "PACKET_PERIOD_IDS";

		DoctorServiceEntry oFFER_SERVICE = new DoctorServiceEntry();

		OfferServiceEntry oFFER_CALL = new OfferServiceEntry();
		oFFER_CALL.setIS_OFFER(find.getInt(find.getColumnIndex(CALL_IS)));
		oFFER_CALL.setOFFER_PRICE(find.getFloat(find.getColumnIndex(CALL_PRICE)));
		oFFER_SERVICE.setOFFER_CALL(oFFER_CALL);

		OfferServiceEntry2 oFFER_TEXT = new OfferServiceEntry2();
		oFFER_TEXT.setIS_OFFER(find.getInt(find.getColumnIndex(TEXT_IS)));
		oFFER_TEXT.setOFFER_PRICE(String.valueOf(find.getFloat(find.getColumnIndex(TEXT_PRICE))));
		oFFER_SERVICE.setOFFER_TEXT(oFFER_TEXT);

		OfferServiceEntry oFFER_JIAHAO = new OfferServiceEntry();
		oFFER_JIAHAO.setIS_OFFER(find.getInt(find.getColumnIndex(JIAHAO_IS)));
		oFFER_JIAHAO.setOFFER_PRICE(find.getFloat(find.getColumnIndex(JIAHAO_PRICE)));
		oFFER_SERVICE.setOFFER_JIAHAO(oFFER_JIAHAO);

		OfferServiceEntry oFFER_ZHUYUAN = new OfferServiceEntry();
		oFFER_ZHUYUAN.setIS_OFFER(find.getInt(find.getColumnIndex(ZHUYUAN_IS)));
		oFFER_ZHUYUAN.setOFFER_PRICE(find.getFloat(find.getColumnIndex(ZHUYUAN_PRICE)));
		oFFER_SERVICE.setOFFER_ZHUYUAN(oFFER_ZHUYUAN);

		OfferServiceEntry oFFER_SHANGMEN = new OfferServiceEntry();
		oFFER_SHANGMEN.setIS_OFFER(find.getInt(find.getColumnIndex(SHANGMEN_IS)));
		oFFER_SERVICE.setOFFER_SHANGMEN(oFFER_SHANGMEN);

		OfferServicePacketEntry oFFER_PACKET = new OfferServicePacketEntry();
		oFFER_PACKET.setPACKET_ID(find.getString(find.getColumnIndex(PACKET_ID)));
		oFFER_PACKET.setSERVICE_CONTENT(find.getString(find.getColumnIndex(PACKET_SERVICE_CONTENT)));
		List<PacketPeriod> pACKET_PERIOD_LIST = getPacketPeriodS(find.getString(find.getColumnIndex(PACKET_PERIOD_IDS)));
		oFFER_PACKET.setPACKET_PERIOD_LIST(pACKET_PERIOD_LIST);
		oFFER_SERVICE.setOFFER_PACKET(oFFER_PACKET);

		doctor.setOFFER_SERVICE(oFFER_SERVICE);

		return doctor;

	}

	private List<PacketPeriod> getPacketPeriodS(String ids) {
		if (TextUtils.isEmpty(ids))
			return null;

		List<PacketPeriod> list = new ArrayList<PacketPeriod>();
		String findSql = "select * from " + TABLE_NAME_PACKET_PERIOD + " where " + packet_period_id + " = ? ";
		Cursor find = null;
		try {
			find = db.find(db.getConnection(), findSql, new String[] { ids });
			if (find == null) {
				return null;
			}
			while (find.moveToNext()) {
				PacketPeriod packetPeriodInfo = getPacketPeriodInfo(find);
				list.add(packetPeriodInfo);
			}
		} catch (Exception e) {
		} finally {
			if (find != null)
				find.close();
		}

		if (list.size() == 0)
			return null;

		return list;
	}

	// private static final String packet_daytype = "packet_daytype";
	// private static final String packet_period_id = "packet_period_id";
	// private static final String packet_daynum = "packet_daynum";
	// private static final String packet_period_price = "packet_period_price";
	// private static final String packet_period_desc = "packet_period_desc";
	private PacketPeriod getPacketPeriodInfo(Cursor find) {
		PacketPeriod pp = new PacketPeriod();
		pp.setPacket_daynum(find.getString(find.getColumnIndex(packet_daynum)));
		pp.setPacket_daytype(find.getString(find.getColumnIndex(packet_daytype)));
		pp.setPacket_period_desc(find.getString(find.getColumnIndex(packet_period_desc)));
		pp.setPacket_period_price(find.getString(find.getColumnIndex(packet_period_price)));
		pp.setPacket_period_id(find.getString(find.getColumnIndex(packet_period_id)));
		return pp;
	}

}
