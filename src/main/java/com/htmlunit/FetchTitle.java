package com.htmlunit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class FetchTitle {

	public static void main(String[] args) {
		String url = "http://mp.weixin.qq.com/s?__biz=MzI2OTU3MDI3NQ==&mid=2247483989&idx=1&sn=552a88aa1341da761810fdff8e1fd043&chksm=eadf1bd9dda892cf9d69c80cb24b08eebbc5dab022d53ce94b7549d1bbc4c398fae95249dcf6&mpshare=1&scene=1&srcid=1106GXsgJKsBrdyoPZ6X3GEn#rd";

		Document document = Jsoup.parse(HtmlUnitUtil.getXml(url, true));
		Element element = document.select("title").first();
		System.out.println(element.text());
	}

}
