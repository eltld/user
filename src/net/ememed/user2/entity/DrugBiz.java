package net.ememed.user2.entity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DrugBiz {

	public static HashMap<String, Object> getDrugs(InputStream is) throws ParserConfigurationException,
			SAXException, IOException {
		SAXParserFactory sf = SAXParserFactory.newInstance();

		SAXParser parser = sf.newSAXParser();
		MyHandler myHandler = new MyHandler();
		parser.parse(is, myHandler);
		return myHandler.map;
	}

	static class MyHandler extends DefaultHandler {
		private StringBuffer buf = new StringBuffer();
		HashMap<String, Object> map;
		List<Drug> drugs;
		String tagName;
		Drug drug;

		@Override
		public void startDocument() throws SAXException {
			map = new HashMap<String, Object>();

		}

		@Override
		public void endDocument() throws SAXException {

		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {

			tagName = localName;
			if ("records".equals(localName)) {
				drugs = new ArrayList<Drug>();
			} else if ("data".equals(localName)) {
				drug = new Drug();
			}

		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {

			if ("data".equals(localName)) {
				drugs.add(drug);
			} else if ("records".equals(localName)) {
				map.put("drugs", drugs);
			}
			tagName = null;
			buf.setLength(0);
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			buf.append(ch, start, length);
			String data = buf.toString().trim();
			if ("state".equals(tagName)) {
				map.put("success", Integer.parseInt(data));
			} else if ("state_desc".equals(tagName)) {
				map.put("errormsg", data);
			} else if ("ID".equals(tagName)) {
				drug.setId(Integer.parseInt(data));
			} else if ("Sku".equals(tagName)) {
				drug.setSku(data);
			} else if ("CnName".equals(tagName)) {
				drug.setCnName(data);
			} else if ("Price".equals(tagName)) {
				drug.setPrice(data);
			} else if ("yhPrice".equals(tagName)) {
				drug.setYhPrice(data);
			} else if ("PicUrl".equals(tagName)) {
				drug.setPicPath(data);
			} else if ("GNZY".equals(tagName)) {
				drug.setFunction(data);
			} else if ("PageCount".equals(tagName)) {
				map.put("pages", Integer.parseInt(data));
			}
		}
	}
}
