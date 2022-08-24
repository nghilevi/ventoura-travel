package com.Mindelo.VentouraServer.Util;



public class HtmlUtil {
	
	/**
	 * Delete unused html tag, text beween startMark and endMark will be deleted
	 * @param body an entire html body
	 * @param startMark 
	 * @param endMark
	 * @return trimmedHtmlBody
	 */
	public static String optimizeHtmlByDeleteRangeMark(String body, String startMark, String endMark){
		StringBuffer trimedBody = new StringBuffer(body);
		String tempSubstring;
		int startPoint = 0;
		int endPoint = 0;
		while((startPoint = trimedBody.indexOf(startMark)) != -1){
			tempSubstring = trimedBody.substring(startPoint);
			if((endPoint =  startPoint + tempSubstring.indexOf(endMark)) < trimedBody.length())
			trimedBody.delete(startPoint, endPoint + 1);
			if(startPoint >= endPoint + 1){
				break;
			}
		}
		return trimedBody.toString();
	}
	
	/**
	 *  all the specified slice will be deleted from Html body
	 * @param body
	 * @param tag
	 * @return trimmed body
	 */
	public static String optimizeHtmlByDeleteSpecificSlice(String body, String slice){
		StringBuffer trimedBody = new StringBuffer(body);
		int startPoint = 0;
		while((startPoint = trimedBody.indexOf(slice)) != -1){
			trimedBody.delete(startPoint, startPoint + slice.length());
		}
		return trimedBody.toString();
	}
}
