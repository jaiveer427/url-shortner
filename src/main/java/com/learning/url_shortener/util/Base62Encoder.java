package com.learning.url_shortener.util;



public class Base62Encoder {

	private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static String encode(long number) {

		if (number == 0) {
			return "0";
		}

		StringBuilder result = new StringBuilder();

		while (number > 0) {

			int remainder = (int) (number % 62);

			result.append(BASE62.charAt(remainder));

			number = number / 62;
		}

		return result.reverse().toString();
	}

	public static long decode(String shortCode) {

		long result = 0;

		for (char c : shortCode.toCharArray()) {

			int value = BASE62.indexOf(c);

			if (value == -1) {
				throw new IllegalArgumentException("Invalid code");
			}

			result = result * 62 + value;
		}

		return result;
	}

}
