package net.ememed.user2.util;

import java.util.Comparator;


import net.ememed.user2.entity.RoomEntry;


public class PinyinComparator  implements Comparator<RoomEntry>{

	public int compare(RoomEntry o1, RoomEntry o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}



}

