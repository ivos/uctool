package net.sf.uctool.util;

import org.apache.commons.lang.StringEscapeUtils;

public class Escape {

	public static String escape(String value) {
		return StringEscapeUtils.escapeHtml(value);
	}

}
