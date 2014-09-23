/**
 * 
 */
package com.simple.web.devpia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * http://www.vogella.com/tutorials/ApacheHttpClient/article.html
 * 
 * @author hothead
 */
public class DevpiaService {

	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0";
	private static final String DEVPIA_LOGIN_URL = "https://secure.devpia.com/Login/DoLogin.aspx";
	private HttpClient client;

	public DevpiaService() {
		client = HttpClientBuilder.create().build();
		CookieHandler.setDefault(new CookieManager());
	}

	public void destroy() {
		HttpClientUtils.closeQuietly(client);
	}

	/**
	 * return Set-Cookie headers
	 */
	public Header[] loginDevpia(String id, String password)
			throws UnsupportedEncodingException, IOException,
			ClientProtocolException {
		HttpPost post = new HttpPost(DEVPIA_LOGIN_URL);
		// add header
		post.setHeader("User-Agent", USER_AGENT);

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("flID", id));
		urlParameters.add(new BasicNameValuePair("flPASS", password));

		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(urlParameters);
		post.setEntity(entity);

		String body = new BufferedReader(new InputStreamReader(
				entity.getContent())).readLine();
		System.out.println("post body : " + body);

		HttpResponse response = client.execute(post);

		String respBody = getBodyFromResponse(response);
		System.out.println(respBody);

		return response.getHeaders("Set-Cookie");
	}

	public String sendMessageWithNewLine(String message, Header[] cookie,
			String sender, String receiver) throws Exception {
		String url = "http://mblog.devpia.com/mymblog/Message.aspx?mm="
				+ sender;

		Document doc = Jsoup.connect(url).get();
		Elements aspnetFormElem = doc.select("#aspnetForm");
		Elements inputs = aspnetFormElem.select("input");
		Element textArea = aspnetFormElem.select("textarea").get(0);

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters
				.add(new BasicNameValuePair(textArea.attr("name"), message));
		urlParameters.add(new BasicNameValuePair("__EVENTTARGET",
				"ctl00$ContentPlaceHolder1$Button3"));
		urlParameters.add(new BasicNameValuePair("ctl00$rbtnVisible", "1"));
		urlParameters.add(new BasicNameValuePair("ctl00$rbtnTaget", "1"));

		for (int i = 0; i < inputs.size(); i++) {
			Element inputElem = inputs.get(i);
			String key = inputElem.attr("name");
			String value = inputElem.attr("value");
			String type = inputElem.attr("type");
			if (StringUtils.equals(type, "hidden")
					|| StringUtils.equals(type, "text")) {
				if(StringUtils.contains(key, "txtReceiptID")) {
					urlParameters.add(new BasicNameValuePair(key, receiver));
				} else {
					urlParameters.add(new BasicNameValuePair(key, value));
				}
			}
		}

		HttpPost post = new HttpPost(url);

		// add header
		post.setHeader("Host", "mblog.devpia.com");
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		post.setHeader("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");
//		post.setHeader("Accept-Charset", "EUC-KR");
		for (Header header : cookie) {
			post.setHeader(header);
		}
		post.setHeader("Connection", "keep-alive");
		post.setHeader("Referer", url);
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");

		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(urlParameters,
				"utf-8");
		post.setEntity(entity);
		System.out.println(entity.toString());

		String body = new BufferedReader(new InputStreamReader(
				entity.getContent())).readLine();
		System.out.println("post body : " + body);

		HttpResponse response = client.execute(post);

		String result = getBodyFromResponse(response);
		HttpClientUtils.closeQuietly(response);

		return result;
	}

	@Deprecated
	public String sendMessage(String message, Header[] cookie, String receiver)
			throws IOException, ClientProtocolException {

		String messageKey = getKey(client, receiver);
		HttpPost post = new HttpPost(
				"http://www.devpia.com/usercontrols/Pagelet/MessageReturn.aspx?Returnid="
						+ receiver);

		// add header
		post.setHeader("Host", "www.devpia.com");
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		post.setHeader("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4");
		post.setHeader("Accept-Charset", "EUC-KR");
		for (Header header : cookie) {
			post.setHeader(header);
		}
		post.setHeader("Connection", "keep-alive");
		post.setHeader("Referer",
				"http://www.devpia.com/usercontrols/Pagelet/MessageReturn.aspx?Returnid="
						+ receiver);
		post.setHeader("Content-Type", "application/x-www-form-urlencoded");

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("__VIEWSTATE", messageKey));
		urlParameters.add(new BasicNameValuePair("txtMessage", message));
		urlParameters.add(new BasicNameValuePair("Button3.x", "101"));
		urlParameters.add(new BasicNameValuePair("Button3.y", "22"));

		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(urlParameters,
				"euc-kr");
		post.setEntity(entity);

		String body = new BufferedReader(new InputStreamReader(
				entity.getContent())).readLine();
		System.out.println("post body : " + body);

		HttpResponse response = client.execute(post);

		String result = getBodyFromResponse(response);
		HttpClientUtils.closeQuietly(response);

		return result;
	}

	private String getKey(HttpClient client, String userId) throws IOException,
			ClientProtocolException {
		String sendMsgURL = "http://www.devpia.com/usercontrols/Pagelet/MessageReturn.aspx?Returnid="
				+ userId;

		HttpGet request = new HttpGet(sendMsgURL);
		// add request header
		request.addHeader("User-Agent", USER_AGENT);
		HttpResponse response = client.execute(request);

		String messageKey = getMessageKeyFromBody(this
				.getBodyFromResponse(response));
		return messageKey;
	}

	protected String getMessageKeyFromBody(String line) {
		int startIndex = line
				.indexOf("<input type=\"hidden\" name=\"__VIEWSTATE");
		// if(startIndex < 0) {
		// continue;
		// }
		int endIndex = line.indexOf("/>", startIndex);
		System.out.printf("(%d, %d)\n", startIndex, endIndex);
		String input = line.substring(startIndex, endIndex);

		int valueIndex = input.indexOf("value=\"") + "value=\"".length();
		int valueEndIndex = input.lastIndexOf("\"");
		String value = input.substring(valueIndex, valueEndIndex);
		System.out.println("__VIEWSTATE : " + value);
		return value;
	}

	private String getBodyFromResponse(HttpResponse response)
			throws IOException {
		System.out.println("Response Code : "
				+ response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		return result.toString();
	}
}
