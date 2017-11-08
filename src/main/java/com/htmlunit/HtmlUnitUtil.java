package com.htmlunit;


import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HtmlUnitUtil {

	/**
	 * 默认开启javascript
	 * @return
	 */
	public static WebClient getWebClient() {
		return getWebClient(true);
	}

	/**
	 * 构造客户端
	 * @param flag
	 * @return
	 */
	public static WebClient getWebClient(boolean flag) {
		WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);
		//支持https
		webClient.getOptions().setUseInsecureSSL(true);
		// JS解释器启用，默认为true
		webClient.getOptions().setJavaScriptEnabled(flag);
		// 禁用css支持
		webClient.getOptions().setCssEnabled(false);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		// 设置连接超时时间 ，这里是10S。如果为0，则无限期等待
		webClient.getOptions().setTimeout(50000);
		//设置js运行超时时间
		webClient.setJavaScriptTimeout(8000);
		//设置页面等待js响应时间
		webClient.waitForBackgroundJavaScript(500);
		// js运行错误时，是否抛出异常
		webClient.getOptions().setThrowExceptionOnScriptError(false);

		return webClient;
	}

	public static String getXml(WebClient webClient, String url){
		return getXml(webClient, url, true);
	}

	public static String getXml(WebClient webClient, String url, boolean flag){
		HtmlPage htmlPage;
		webClient.getOptions().setJavaScriptEnabled(flag);
		try {
			htmlPage = webClient.getPage(url);
			return htmlPage.asXml();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getXml(String url){
		WebClient webClient = getWebClient();
		HtmlPage htmlPage;
		webClient.getOptions().setJavaScriptEnabled(false);
		try {
			htmlPage = webClient.getPage(url);
			return htmlPage.asXml();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return null;
	}

	/**
	 * 使用一个新的webClient
	 * @param url
	 * @return
	 */
	public static String getXml(String url, boolean flag){
		WebClient webClient = getWebClient();
		HtmlPage htmlPage;
		webClient.getOptions().setJavaScriptEnabled(flag);
		try {
			htmlPage = webClient.getPage(url);
			return htmlPage.asXml();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
