package net.ememed.user2.entity;

public class NewsItem {
	private String ID;//=> 3005
	private String TITLE;//=> 医生对病人“自我诊断”很反感 
	private String PIC;//=> http://www.ememed.net/uploads/news/201408/news_219b4b0d07a1066801f27b68794de1d9.jpg
	private String PICEXT1;// =>
	private String PICEXT2;//=>
	private String PICEXT3;//=>
	private String TYPE; //=> news
	private String FURL; //=> http://183.60.177.178:9004/normal/html5/newsDetail/3005
	private String UPDATETIME; //=> 2014-09-16 11:19:08
	private String SUBTITLE;//=> 盘点医生问诊的烦恼
	private String ALLOWCOMMENT;// => 1
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getTITLE() {
		return TITLE;
	}
	public void setTITLE(String tITLE) {
		TITLE = tITLE;
	}
	public String getPIC() {
		return PIC;
	}
	public void setPIC(String pIC) {
		PIC = pIC;
	}
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	public String getFURL() {
		return FURL;
	}
	public void setFURL(String fURL) {
		FURL = fURL;
	}
	public String getUPDATETIME() {
		return UPDATETIME;
	}
	public void setUPDATETIME(String uPDATETIME) {
		UPDATETIME = uPDATETIME;
	}
	public String getSUBTITLE() {
		return SUBTITLE;
	}
	public void setSUBTITLE(String sUBTITLE) {
		SUBTITLE = sUBTITLE;
	}
	public String getPICEXT1() {
		return PICEXT1;
	}
	public void setPICEXT1(String pICEXT1) {
		PICEXT1 = pICEXT1;
	}
	public String getPICEXT2() {
		return PICEXT2;
	}
	public void setPICEXT2(String pICEXT2) {
		PICEXT2 = pICEXT2;
	}
	public String getPICEXT3() {
		return PICEXT3;
	}
	public void setPICEXT3(String pICEXT3) {
		PICEXT3 = pICEXT3;
	}
	public String getALLOWCOMMENT() {
		return ALLOWCOMMENT;
	}
	public void setALLOWCOMMENT(String aLLOWCOMMENT) {
		ALLOWCOMMENT = aLLOWCOMMENT;
	}
	
}
