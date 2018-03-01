package org.rta.rules.utils;

import org.springframework.stereotype.Component;

@Component
public class NumberParser {

	
	public static double getRoundNextTen(double inputValue) {
		int i = (int) Math.ceil(inputValue);
		int module = i % 10;
		if (module == 0)
			return Math.ceil(inputValue);
		int y = i / 10;
		double nextTenValue = ++y * 10;
		return nextTenValue;
	}
}
