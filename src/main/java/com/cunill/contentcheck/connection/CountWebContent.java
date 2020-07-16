package com.cunill.contentcheck.connection;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class CountWebContent extends HttpConnection {

	private final String HTTP_PROTOCOL = "http://";
	private final String HTTPS_PROTOCOL = "https://";

	public int countClassElements(String url, String className) {
		url = reviewUrlStructure(url);
		Document doc = recoverGETUrlContent(url);
		if (doc == null) {
			return 0;
		}
		Elements elements = doc.getElementsByClass(className);
		return elements.size();
	}

	private String reviewUrlStructure(String url) {
		if (!url.startsWith(HTTP_PROTOCOL) || !url.startsWith(HTTPS_PROTOCOL)) {
			url = HTTP_PROTOCOL + url;
		}
		return url;
	}
}
