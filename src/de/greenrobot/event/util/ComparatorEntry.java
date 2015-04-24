package de.greenrobot.event.util;

import java.util.Comparator;

import com.easemob.chat.EMMessage;

public class ComparatorEntry implements Comparator<EMMessage> {
	@Override
	public int compare(EMMessage lhs, EMMessage rhs) {
		long lhsTime = lhs.getMsgTime();
		long rhsTime = rhs.getMsgTime();
		
		if(lhsTime - rhsTime>0){
			return 1;
		}else{
			return -1;
		}
		
	}

}
