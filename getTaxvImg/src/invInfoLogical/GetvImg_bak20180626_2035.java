package invInfoLogical;

import invInfoBean.InvInfor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.*;

import sun.misc.BASE64Encoder;
import net.sf.json.JSONObject;

public class GetvImg_bak20180626_2035 {
	
	String vImg_url;
	String fpdm;
	String sys_ver;
	String nowtime;
	/*String AREA = "4300";
	//String HOST = "https://fpcy.gd-n-tax.gov.cn/WebQuery";//广东省 - 4400
	//String HOST = "https://fpcy.szgs.gov.cn/WebQuery";//广东省深圳市 - 4403
	//String HOST = "https://fpcyweb.tax.sh.gov.cn:1001/WebQuery";//上海市 - 3100
	String HOST = "https://fpcy.hntax.gov.cn:8083/WebQuery";//湖南省 - 4300
	 */	
	
	public GetvImg_bak20180626_2035(String vImg_url_in,String fpdm_in,String sys_ver_in){
		vImg_url = vImg_url_in;
		fpdm = fpdm_in;
		sys_ver = sys_ver_in;
	}

	public static void main(String[] args) {
		

	}
	
	/**
	 * 模拟浏览器发送请求获取验证码图片及需要识别的颜色
	 * @return
	 */
	public String getvImg(String nowtime,String publickey,String areacode,String url_prefix ){
		String cert_folder = "C:/cert/";
		
		String cert1_name = "1.jks";
		String cert1_pwd = "Pwcauto!@#";
		
		String cert2_name = "2.jks";
		String cert2_pwd = "Pwcauto!@#";
		
		String cert_name = "trust.jks";
		String cert_pwd = "Pwcauto!@#";
		
		String cert1_path = cert_folder + cert1_name;
		String cert2_path = cert_folder + cert2_name;
		String cert_path = cert_folder + cert_name;
		
		String cert1 = "";
		String cert2 = "";
		String cert = "";
		String vImg = "";
		
		
		try {
			/************** BEGIN Certificate configuration for use in connection **************/
			SSLContext context = SSLContext.getInstance("SSL");
			KeyStore cert_ks = KeyStore.getInstance("JKS");
			cert_ks.load(new FileInputStream(new File(cert_path)),cert_pwd.toCharArray());
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(cert_ks);
			context.init(null,tmf.getTrustManagers(), null); 
			SSLSocketFactory sslSocketFactory = context.getSocketFactory();
			/************** END Certificate configuration for use in connection **************/

			/************** BEGIN HTTPS Connection **************/
			String r = String.valueOf(Math.random());
			//String url_str = "https://fpcy.gd-n-tax.gov.cn/WebQuery/yzmQuery?"
			String url_str = url_prefix + "/yzmQuery?"
					//+ "callback=" + "jQuery"
					+ "fpdm=" + fpdm
					+ "&r=" + r
					+ "&v=" + sys_ver
					+ "&nowtime=" + nowtime
					+ "&area=" + areacode
					+ "&pulickey=" + publickey
					//+ "&_=" + ""
					;
			//URL url = new URL("https://fpcy.gd-n-tax.gov.cn/WebQuery/yzmQuery"); 
			//URL url = new URL("https://fpcy.gd-n-tax.gov.cn/WebQuery/yzmQuery?fpdm=144001724660&v=V1.0.05_001&nowtime=1528872244257");
			URL url = new URL(url_str);
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
				public boolean verify(String hostname,SSLSession session){
					return true;
				}
			});
			
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(sslSocketFactory);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Content-Type", "application/json;charset=gb2312");
			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestMethod("GET");
			
			String body = "{'fpdm':'" + fpdm + "','r':'" + r + "','v':'" + sys_ver + "'"
					+ ",'nowtime':'" + nowtime + "'"
					+ ",'area':'" + areacode + "'"
					+ ",'publickey':'" + publickey + "'";
			body+= "}"; 
			System.out.println("Payload: " + body);
			/************** END JSON Message to ChinaTax **************/


			OutputStreamWriter outwr = new OutputStreamWriter(conn.getOutputStream());
			outwr.write(body); 
			outwr.flush(); //Get the response from the QPS BufferedReader 
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
			StringBuilder builder = new StringBuilder(); 
			String inputLine; 
			while ((inputLine = in.readLine()) != null) 
			{ 
				builder.append(inputLine); 
			} 
			in.close(); 
			String data = builder.toString(); 
			vImg = data;
			System.out.println("The response from the server is: " + data);
			outwr.close();
			conn.disconnect();			
			
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		
		return vImg;
	}
	
	/**
	 * 模拟浏览器发送请求获取验证码图片及需要识别的颜色
	 * @param get
	 * @param nowtime
	 * @param publickey
	 * @return 获取验证码的信息的json字符串
	 */
	public String getvImg(GetvImg_bak20180626_2035 get,String nowtime,String publickey,String areacode,String url_prefix){
		String vImg_infor = "";
		
		String r = String.valueOf(Math.random());
		//String url_str = "https://fpcy.gd-n-tax.gov.cn/WebQuery/yzmQuery?"
		String url_str = url_prefix + "/yzmQuery?"
				//+ "callback=" + "jQuery"
				+ "fpdm=" + fpdm
				+ "&r=" + r
				+ "&v=" + sys_ver
				+ "&nowtime=" + nowtime
				//+ "&area=" + "4400"
				+ "&area=" + areacode
				+ "&pulickey=" + publickey
				//+ "&_=" + ""
				;
		System.out.println("url_str1: " + url_str);
		Map<String,String> args_map = new HashMap<String,String>();
		
		//args_map.put("callback","jQuery");
		args_map.put("fpdm",fpdm);
		args_map.put("r",r);
		args_map.put("v",sys_ver);
		args_map.put("nowtime",nowtime);
		//args_map.put("area","4400");
		args_map.put("area",areacode);
		args_map.put("pulickey",publickey);
		JSONObject json = JSONObject.fromObject(args_map);
		String args_str = json.toString();
		
		vImg_infor = get.sendRequest(url_str,args_str);
		System.out.println("vImg_infor: " + vImg_infor);
		
		return vImg_infor;
	}
	
	
	
	/**
	 * 将请求获取到的json字符串数据转成Map，并返回
	 * @param data - 国税局返回的json字符串，可以转成json对象后再转成Map
	 * @return map对象
	 */
	public Map<String,String> convert2Map(String data){
		JSONObject json = JSONObject.fromObject(data);
		Map<String,String> vImg_map = new HashMap<String,String>();
		vImg_map = (Map<String,String>)json;
		for(String key : vImg_map.keySet()){
			System.out.println(key + " : " + vImg_map.get(key));
		}
		
		return vImg_map;
	}
	
	
	/**
	 * 将请求得到的验证码数据转成带有提示的图片存储到指定路径下，并返回存储路径
	 * @param txtColor - 需要输入的验证码的颜色提示
	 * @param img_data - 验证码图片的base64的编码数据
	 * @return 返回验证码的存储路径
	 */
	public String savevImg(String txtColor,String img_data){
		String code_img_path = "";
		try {
			code_img_path = ImageOperation.run(txtColor,img_data);//得到带有颜色及颜色提示的验证码图片
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return code_img_path;
	}
	
	/**
	 * 构造获取发票信息所需请求的参数，并发送请求，返回国税局中发票信息
	 * @param get - 类GetvImg的实例，用来调取方法
	 * @param inv - 发票基本信息的JavaBean
	 * @param fplx - 发票类型代码，03 - 机动车销售发票
	 * @param yzm - 国税局网站的验证码
	 * @param vImg_reply - 发送请求获取到验证码的map
	 * @return
	 */
	public String getInvInfor(GetvImg_bak20180626_2035 get,InvInfor inv,String fplx,String yzm,Map<String,String> vImg_reply,String url_prefix){
		String inv_infor = "";
		
		//String url_str = "https://fpcy.gd-n-tax.gov.cn/WebQuery/invQuery?"
		String url_str = url_prefix + "/invQuery?"
				+ "callback=" + "jQuery"
				+ "&fpdm=" + fpdm
				+ "&fphm=" + inv.getInvNo()
				+ "&kprq=" + inv.getInvDate()
				+ "&fpje=" + inv.getPriceExTax()
				+ "&fplx=" + fplx
				+ "&yzm=" + yzm
				+ "&yzhSj=" + vImg_reply.get("key2")
				+ "&index=" + vImg_reply.get("key3")
				//+ "&area=" + "4400"
				+ "&area=" + inv.getArea_code()
				+ "&pulickey=" + ""
				+ "&_=" + ""
				;
		System.out.println("url_str2: " + url_str);
		Map<String,String> args_map = new HashMap<String,String>();
		args_map.put("callback","jQuery");
		args_map.put("fpdm",inv.getInvCode());
		args_map.put("fphm",inv.getInvNo());
		args_map.put("kprq",inv.getInvDate());
		args_map.put("fpje",inv.getPriceExTax());
		args_map.put("fplx",fplx);
		args_map.put("yzm",yzm);
		args_map.put("yzhSj",vImg_reply.get("key2"));
		args_map.put("index",vImg_reply.get("key3"));
		//args_map.put("area","4400");
		args_map.put("area",inv.getArea_code());
		args_map.put("pulickey","");
		args_map.put("_",String.valueOf(System.currentTimeMillis()-1000));
		JSONObject json = JSONObject.fromObject(args_map);
		String args_str = json.toString();
		
		
		/*String args_str = "{'callback':'jQuery'"
				+ ",'fpdm':'" + inv.getInvCode() + "'"
				+ ",'fphm':'" + inv.getInvNo() + "'"
				+ ",'kprq':'" + inv.getInvDate() + "'"
				+ ",'fpje':'" + inv.getPriceExTax() + "'"
				+ ",'fplx':'" + fplx + "'"
				+ ",'yzm':'" + yzm + "'"
				+ ",'yzhSj':'" + yzhSj + "'"
				+ ",'index':'" + index + "'"
				+ ",'area':'" + fpdm + "'"
				+ ",'pulickey':'" + fpdm + "'"
				+ ",'_':'" + fpdm + "'";
		*/
		
		inv_infor = get.sendRequest(url_str,args_str);
		System.out.println("inv_infor: " + inv_infor);
		
		return inv_infor;
	}
	
	/**
	 * 专门发送请求，并返回请求结果的字符串
	 * @param url_str - 请求的地址串
	 * @param args_str - 发送请求时需要参数组成的json字符串
	 * @return
	 */
	public String sendRequest(String url_str,String args_str){
		String cert_folder = "C:/cert/";
		
		String cert1_name = "1.jks";
		String cert1_pwd = "Pwcauto!@#";
		
		String cert2_name = "2.jks";
		String cert2_pwd = "Pwcauto!@#";
		
		String cert_name = "trust.jks";
		String cert_pwd = "Pwcauto!@#";
		
		String cert1_path = cert_folder + cert1_name;
		String cert2_path = cert_folder + cert2_name;
		String cert_path = cert_folder + cert_name;
		
		String cert1 = "";
		String cert2 = "";
		String cert = "";
		String reply = "";
		
		
		try {
			/************** BEGIN Certificate configuration for use in connection **************/
			SSLContext context = SSLContext.getInstance("SSL");
			KeyStore cert_ks = KeyStore.getInstance("JKS");
			cert_ks.load(new FileInputStream(new File(cert_path)),cert_pwd.toCharArray());
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(cert_ks);
			context.init(null,tmf.getTrustManagers(), null); 
			SSLSocketFactory sslSocketFactory = context.getSocketFactory();
			/************** END Certificate configuration for use in connection **************/

			/************** BEGIN HTTPS Connection **************/
			URL url = new URL(url_str); 
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
				public boolean verify(String hostname,SSLSession session){
					return true;
				}
			});
			System.setProperty("javax.net.ssl.trustStore", cert_path);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(sslSocketFactory);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Content-Type", "application/json;charset=gb2312");
			conn.setRequestProperty("Accept", "*/*");
			conn.setRequestMethod("GET");
			
			String body = args_str;
			System.out.println("Payload: " + body);
			/************** END JSON Message to ChinaTax **************/

			OutputStreamWriter outwr = new OutputStreamWriter(conn.getOutputStream());
			outwr.write(body); 
			outwr.flush(); //Get the response from the QPS BufferedReader 
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"gb2312")); 
			StringBuilder builder = new StringBuilder(); 
			String inputLine; 
			while ((inputLine = in.readLine()) != null) 
			{ 
				builder.append(inputLine); 
			} 
			in.close(); 
			String data = builder.toString(); 
			reply = data;
			System.out.println("The response from the server is: " + data);
			outwr.close();
			conn.disconnect();			
			
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		
		return reply;
	}
	
	public String readPK(){
		String cert_path = "C:/cert/trust.cer";
		String pk_str = "";
		//java 读取证书
		try {
			CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
			FileInputStream bais=new FileInputStream(cert_path);  
			X509Certificate Cert = (X509Certificate)certificatefactory.generateCertificate(bais);  
			PublicKey pk = Cert.getPublicKey();  
			BASE64Encoder bse=new BASE64Encoder();  
			pk_str = bse.encode(pk.getEncoded());
			System.out.println("pk: " + pk_str);  
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return pk_str;  
		
	}
	

}
