package com.gaooh.util;

/**
 * テキスト処理のUtil
 * @author gaooh
 * @date 2008/05/06
 */
public class TextUtil {

	/**
	 * HTML用の改行に変換する
	 * @param value
	 * @return
	 */
	public static String convertHTMLBreak(String value) {
		return value.replaceAll("\r\n", "<br/>");
	}
}
