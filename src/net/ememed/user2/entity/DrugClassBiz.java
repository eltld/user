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

//获取药品分类
public class DrugClassBiz {
	public static HashMap<String, Object> getDrugClass(InputStream in)
			throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		MyHandler handler = new MyHandler();
		parser.parse(in, handler);
		return handler.map;
	}

	static class MyHandler extends DefaultHandler {
		private StringBuffer buf = new StringBuffer();
		HashMap<String, Object> map;
		List<DrugClass> drugClasses;
		String tagName;
		DrugClass drugClass;

		@Override
		public void startDocument() throws SAXException {
			map = new HashMap<String, Object>();

			super.startDocument();
		}

		@Override
		public void endDocument() throws SAXException {

			super.endDocument();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			tagName = localName;
			if ("records".equals(localName)) {
				drugClasses = new ArrayList<DrugClass>();
			} else if ("data".equals(localName)) {
				drugClass = new DrugClass();
			}

		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if ("data".equals(localName)) {
				drugClasses.add(drugClass);
			} else if ("records".equals(localName)) {
				map.put("drugClasses", drugClasses);
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
			} else if ("state_success".equals(tagName)) {
				map.put("errormsg", data);
			} else if ("SCID".equals(tagName)) {
				drugClass.setScId(data);
			} else if ("ParentId".equals(tagName)) {
				drugClass.setParentId(data);
			} else if ("Title".equals(tagName)) {
				drugClass.setTitle(data);
			} else if ("ExistSub".equals(tagName)) {
				drugClass.setExistSub(Integer.parseInt(data));
			}
		}

	}
}
