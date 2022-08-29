package com.Mindelo.VentouraServer.Util;

import java.util.ArrayList;
import java.util.List;

public class CommonUtil {

	/* Reverse a List */
	public static <T> ArrayList<T> reverse(List<T> list) {
		ArrayList<T> theReturn = new ArrayList<T>(list.size());

		for (int i = list.size() - 1; i >= 0; i--)
			theReturn.add(list.get(i));

		return theReturn;
	}
}
