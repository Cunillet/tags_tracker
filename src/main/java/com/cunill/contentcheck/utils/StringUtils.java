package com.cunill.contentcheck.utils;


/**
 * Utility class to operate with strings.
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

	public static final String NEW_LINE = "\r\n";
	public static final String LINE_SEPARATOR = "-------------------------------";

	public static String getSafeArrayValue(String[] array, int pos) {
		if (array.length > pos) {
			return array[pos].replaceAll("\"", "");
		} else {
			return "";
		}
	}
}
