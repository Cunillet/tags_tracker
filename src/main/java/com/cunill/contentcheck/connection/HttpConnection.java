package com.cunill.contentcheck.connection;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HttpConnection {

	protected final Log log = LogFactory.getLog(this.getClass());
	protected CloseableHttpClient httpClient;
	protected HttpClientContext context;

	public void setCommonConfiguration() {
		httpClient = HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
				.setRedirectStrategy(new LaxRedirectStrategy()).build();
	}

	public void setCommonConfigurationWithProxy(String proxyAddress, int proxyPort, String proxyUser, String proxyPwd,
			String proxyDomain) {
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(proxyAddress, proxyPort, AuthScope.ANY_HOST, "ntlm"),
				new NTCredentials(proxyUser, proxyPwd, "", proxyDomain));
		HttpHost proxy = new HttpHost(proxyAddress, proxyPort);
		CookieStore cookieStore = new BasicCookieStore();
		DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
		httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).setRoutePlanner(routePlanner)
				.setDefaultCookieStore(cookieStore).setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
				.setRedirectStrategy(new LaxRedirectStrategy()).build();
	}

	public void closeConnection() {
		try {
			httpClient.close();
		} catch (IOException e) {
			log.error("Error closing connection");
		}
	}

	public Document stringToDocument(String responseString) {
		return Jsoup.parse(responseString);
	}
	

	protected Document recoverGETUrlContent(final String url) {

		HttpGet get = new HttpGet(url);
		get.addHeader("User-Agent", "Mozilla/5.0");
		int httpCode = 0;
		Document docRes = null;
		String entityResponse = "";

		try {
			HttpResponse resp = httpClient.execute(get);
			httpCode = resp.getStatusLine().getStatusCode();
			HttpEntity entity = resp.getEntity();
			entityResponse = EntityUtils.toString(entity, "UTF-8");
			Thread.sleep(100);
		} catch (Exception e) {
			log.error("ERROR || recoverGETUrlContent: " + url);
			return null;
		}

		if (httpCode == 200) {
			docRes = stringToDocument(entityResponse);
			return docRes;
		}
		return null;
	}
}
