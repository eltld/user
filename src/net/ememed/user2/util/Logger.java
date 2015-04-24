package net.ememed.user2.util;

import android.util.Log;

public class Logger {
	
	private static String appName = "ememed";
	
	public static boolean debugFlag = false;
	
	public static void dout(String str) {
		if (debugFlag) {
			Log.d(appName, "Line:"+getLineNumber()+" : str>>>>>>>>>" + str);
		}
	}

	public static void dout(Class context, String str2) {
		if (debugFlag) {
			Log.d(context.getSimpleName(), "Line:"+getLineNumber()+" str:>>>>>>>>>>>>>" + str2);
		}
	}
	
	public static void iout(String str) {
		if (debugFlag) {
			Log.i(appName, str);
		}
	}
	
	public static void iout(String tag, String str) {
		if (debugFlag) {
			Log.i(tag, str);
		}
	}
	private static int getLineNumber() {  
        return Thread.currentThread().getStackTrace()[5].getLineNumber();  
    }  
	
}
