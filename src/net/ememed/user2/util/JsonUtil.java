package net.ememed.user2.util;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class JsonUtil {
	private static Gson gson = new Gson();

	public static Object readJson2Object(String json, Class className) {
		Object obj = gson.fromJson(json, className);
		return obj;
	}

	public static List readJson2List(String json, Class className) {
		List lists = new ArrayList();

			JsonParser jsonParser = new JsonParser();
			JsonElement jsonElement = jsonParser.parse(json); // 将json字符串转换成JsonElement
			JsonArray jsonArray = jsonElement.getAsJsonArray(); // 将JsonElement转换成JsonArray
			java.util.Iterator<JsonElement> it = jsonArray.iterator(); // Iterator处理
			while (it.hasNext()) { // 循环
				jsonElement = (JsonElement) it.next(); // 提取JsonElement
				json = jsonElement.toString(); // JsonElement转换成String
				Object obj = gson.fromJson(json, className); // String转化成JavaBean
				lists.add(obj);// 加入List
			}
		
		return lists;
	}
}
