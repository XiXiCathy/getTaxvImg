package com.pwc.byhttpclient;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.collections4.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import invInfoLogical.GetvImg;
import invInfoLogical.HyAPI;

/**
 * 对HTTPClient的封装
 */
public class HttpClientUtilWithMyX509TrustMananer {

	private static final String ENCODING = "UTF-8";

	private static HttpClient client = null;
	private static SchemeRegistry schemeRegistry; // 协议控制
	private static PoolingClientConnectionManager ccm; // HttpClient连接池(多连接的线程安全的管理器)

	static {
		try {
			/*
			 * 与https请求相关的操作
			 */
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, new TrustManager[] { getMyX509TrustManager() }, new SecureRandom());
			SSLSocketFactory socketFactory = new SSLSocketFactory(sslContext);
			/*
			 * 定义访问协议
			 */
			schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));// http
			schemeRegistry.register(new Scheme("https", 443, socketFactory));// https
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}

		// 连接池管理
		ccm = new PoolingClientConnectionManager(schemeRegistry);
		ccm.setDefaultMaxPerRoute(20);// 每个路由的最大连接数
		ccm.setMaxTotal(400);// 最大总连接数

		HttpParams httpParams = new BasicHttpParams();
		httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000);// 连接超时时间（ms）
		httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);// 操作超时时间（ms）
		httpParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);// 设置http1.1或http1.0

		client = new DefaultHttpClient(ccm, httpParams);// 一个客户端就有一个连接池
	}

	/**
	 * get请求
	 * 
	 * @param url
	 *            请求URL
	 * @param paramMap
	 *            请求参数
	 * @param headerMap
	 *            请求头信息
	 */
	public static String get(String url, Map<String, String> paramMap, Map<String, String> headerMap)
			throws ClientProtocolException, IOException {
		/*
		 * 拼接URL与参数
		 */
		if (MapUtils.isNotEmpty(paramMap)) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			for (String key : paramMap.keySet()) {
				params.add(new BasicNameValuePair(key, paramMap.get(key)));
			}
			String queryString = URLEncodedUtils.format(params, ENCODING);
			if (url.indexOf("?") > -1) {// 存在?，表示这时的URL已经带参数了
				url += "&" + queryString;
			} else {
				url += "?" + queryString;
			}
		}
		System.out.println("url is :" + url);
		HttpGet httpGet = new HttpGet(url);

		/*
		 * 设置头信息
		 */
		if (MapUtils.isNotEmpty(headerMap)) {
			Set<String> keySet = headerMap.keySet();
			for (String key : keySet) {
				httpGet.addHeader(key, headerMap.get(key));
			}
		}

		String result = "";

		HttpResponse response = client.execute(httpGet); // 发出get请求
		StatusLine status = response.getStatusLine(); // 获取返回的状态码
		HttpEntity entity = response.getEntity(); // 获取返回的响应内容
		if (status.getStatusCode() == HttpStatus.SC_OK) { // 200
			result = EntityUtils.toString(entity, ENCODING);
		}

		httpGet.abort();// 中止请求，连接被释放回连接池
		return result;
	}

	/**
	 * post请求
	 * 
	 * @param url
	 *            请求URL
	 * @param paramMap
	 *            请求参数
	 * @param headerMap
	 *            请求头信息
	 */
	public static String post(String url, Map<String, String> paramMap, Map<String, String> headerMap)
			throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(url);
		/*
		 * 处理参数
		 */
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (MapUtils.isNotEmpty(paramMap)) {
			Set<String> keySet = paramMap.keySet();
			for (String key : keySet) {
				params.add(new BasicNameValuePair(key, paramMap.get(key)));
			}
		}

		/*
		 * 设置头信息
		 */
		if (MapUtils.isNotEmpty(headerMap)) {
			Set<String> keySet = headerMap.keySet();
			for (String key : keySet) {
				httpPost.addHeader(key, headerMap.get(key));
			}
		}

		String result = "";

		httpPost.setEntity(new UrlEncodedFormEntity(params, ENCODING));// 设置参数
		HttpResponse response = client.execute(httpPost); // 发出post请求
		StatusLine status = response.getStatusLine(); // 获取返回的状态码
		HttpEntity entity = response.getEntity(); // 获取响应内容
		if (status.getStatusCode() == HttpStatus.SC_OK) {
			result = EntityUtils.toString(entity, ENCODING);
		}

		httpPost.abort();// 中止请求，连接被释放回连接池
		return result;
	}

	/**
	 * 构建自定义信任管理器内部类
	 */
	private static class MyX509TrustManager implements X509TrustManager {
		/**
		 * 检查客户端证书，若不信任，抛出异常
		 */
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		/**
		 * 检查服务端证书，若不信任，抛出异常，反之，若不抛出异常，则表示信任（所以，空方法代表信任所有的服务端证书）
		 */
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		/**
		 * 返回受信任的X509证书数组
		 */
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}

	/**
	 * 为外部类获取内部类提供方法
	 */
	public static MyX509TrustManager getMyX509TrustManager() {
		return new MyX509TrustManager();
	}

	/**
	 * 测试
	 */
	public static void main(String[] args) {

		Date now = new Date();
		String startNow = "" + now.getTime();
		Map<String, String> paras = new HashMap<String, String>();
		
		//湖南
		paras.put("fpdm", "143001720660");
		paras.put("fphm", "01594093");
		paras.put("area", "4300");	
		//广东
		/*
		paras.put("fpdm", "144001724660");
		paras.put("fphm", "01908372");
		paras.put("area", "4400");
		*/
		
		paras.put("r", "0.8153687267850135");
		paras.put("v", "V1.0.06_001");	
		paras.put("nowtime", "");
		paras.put("pulickey", "");
		

		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept", "*/*");
		heads.put("Accept-Encoding", "gzip, deflate, sdch, br");
		heads.put("Accept-Language", "en-US,en;q=0.8");
		heads.put("Connection", "keep-alive");
		String vImgFolder = "C:\\uploadData";
		String TaxSysVer = "V1.0.06_001";

		// 获取验证码图片
		//GetvImg get = new GetvImg(vImgFolder, null, TaxSysVer);
		GetvImg get = new GetvImg();
		String data = "";
		while (data.equals("")) {
			try {
				String url = "https://fpcy.hntax.gov.cn:8083/WebQuery/yzmQuery";
				//String url = "https://fpcy.gd-n-tax.gov.cn/WebQuery/yzmQuery";
				data = HttpClientUtilWithMyX509TrustMananer.get(url,
						paras, heads);
			} catch (Exception e) {
				data = "";
				try {
					Thread.sleep(1000 * 3);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}

		Map<String, String> vImg_map = get.convert2Map(data);
		String vImg_path = get.savevImg(vImg_map.get("key4"), vImg_map.get("key1"));
		System.out.println("vImg_path: " + vImg_path);
		//请求打码平台
		String valicodeStr = "";
		try {
			//识别验证码
			String[] vcode = HyAPI.hyIdentifyCode(vImg_path, 4026);
			valicodeStr = vcode[1];
			int error_vcode_time = 0;
			System.out.println("valicodeStr: " + valicodeStr);
			if(valicodeStr.toUpperCase().startsWith("#")){
				error_vcode_time++;				
				if(error_vcode_time>10){
					System.out.println("验证码超时超过十次，结束运行。");
				}
			}else{
				System.out.println("vCode no error");				
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		//获取发票检验信息
		
		paras = new HashMap<String, String>();
		paras.put("callback", "jQuery");
		//湖南
		
		paras.put("key1", "143001720660");
		paras.put("key2", "01594093");
		paras.put("key3", "20180206");
		paras.put("key4", "254273.5");
		paras.put("area", "4300");
		
		//广东
		/*
		paras.put("fpdm", "144001724660");
		paras.put("fphm", "01908372");
		paras.put("kprq", "20180314");
		paras.put("fpje", "191736.75");
		paras.put("area", "4400");
		*/
		paras.put("fplx", "03");
		paras.put("yzm", valicodeStr);
		paras.put("yzhSj", vImg_map.get("key2"));
		paras.put("index", vImg_map.get("key3"));
		paras.put("pulickey", null);
		paras.put("_", null);
		
		String check_result = "";
		while (check_result.equals("")) {
			try {
				String url = "https://fpcy.hntax.gov.cn:8083/WebQuery/vatQuery";
				//String url = "https://fpcy.gd-n-tax.gov.cn/WebQuery/invQuery";
				check_result = HttpClientUtilWithMyX509TrustMananer.get(url,
						paras, heads);
			} catch (Exception e) {
				check_result = "";
				try {
					Thread.sleep(1000 * 3);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		System.out.println("check_result: " + check_result);

	}
}