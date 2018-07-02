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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.*;

import sun.misc.BASE64Encoder;
import net.sf.json.JSONObject;

public class GetvImg {
	private static final String SYSVer = "V1.0.06_001";//国税局发票系统版本
	/*String vImg_url;
	String fpdm;
	String sys_ver;
	String nowtime;
	String AREA = "4300";
	//String HOST = "https://fpcy.gd-n-tax.gov.cn/WebQuery";//广东省 - 4400
	//String HOST = "https://fpcy.szgs.gov.cn/WebQuery";//广东省深圳市 - 4403
	//String HOST = "https://fpcyweb.tax.sh.gov.cn:1001/WebQuery";//上海市 - 3100
	String HOST = "https://fpcy.hntax.gov.cn:8083/WebQuery";//湖南省 - 4300
	 
	
	public GetvImg(String vImg_url_in,String fpdm_in,String sys_ver_in){
		vImg_url = vImg_url_in;
		fpdm = fpdm_in;
		sys_ver = sys_ver_in;
	}*/	

	public static void main(String[] args) {
		

	}
	
	
	/**
	 * 通过发票代码按照一定的逻辑得出发票所属的区域的areacode
	 * @param inv_code - 发票代码
	 * @return area_code
	 */
	public String getAreaCode(String inv_code){
		String area_code = "";
		
		if(inv_code.length() == 12){
			area_code = inv_code.substring(1,5);
		}else{
			area_code = inv_code.substring(0,4);
		}
		
		List<String> city_code_list = new ArrayList<String>();
		city_code_list.add("2102");
		city_code_list.add("3302");
		city_code_list.add("3502");
		city_code_list.add("3702");
		city_code_list.add("4403");
		
		for(int i = 0;i<city_code_list.size();i++){
			if(area_code.equals(city_code_list.get(i))){
				area_code = area_code.substring(0,2) + "00";
				break;
			}
		}
		
		System.out.println("area_code: " + area_code);
		return area_code;
	}
	
	/**
	 * 模拟浏览器发送请求获取验证码图片及需要识别的颜色
	 * @param fpdm
	 * @param nowtime
	 * @param publickey
	 * @return 获取验证码的信息的json字符串
	 */
	public String getvImg(InvInfor inv,String nowtime,String publickey,String areacode,String url_prefix){
		String vImg_infor = "";
		
		String r = String.valueOf(Math.random());
		//String url_str = "https://fpcy.gd-n-tax.gov.cn/WebQuery/yzmQuery?"
		String url_str = url_prefix + "/yzmQuery?"
				//+ "callback=" + "jQuery"
				+ "fpdm=" + inv.getInvCode()
				+ "&fphm=" + inv.getInvNo()
				+ "&r=" + r
				+ "&v=" + SYSVer
				+ "&nowtime=" + nowtime
				//+ "&area=" + "4400"
				+ "&area=" + areacode
				+ "&pulickey=" + publickey
				//+ "&_=" + ""
				;
		System.out.println("url_str1: " + url_str);
		Map<String,String> args_map = new HashMap<String,String>();
		
		//args_map.put("callback","jQuery");
		args_map.put("fpdm",inv.getInvCode());
		args_map.put("fphm",inv.getInvNo());
		args_map.put("r",r);
		args_map.put("v",SYSVer);
		args_map.put("nowtime",nowtime);
		//args_map.put("area","4400");
		args_map.put("area",areacode);
		args_map.put("pulickey",publickey);
		JSONObject json = JSONObject.fromObject(args_map);
		String args_str = json.toString();
		
		GetvImg get = new GetvImg();
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
	 * @param inv - 发票基本信息的JavaBean
	 * @param fplx - 发票类型代码，03 - 机动车销售发票
	 * @param yzm - 国税局网站的验证码
	 * @param vImg_reply - 发送请求获取到验证码的map
	 * @return
	 */
	public String getInvInfor(InvInfor inv,String fplx,String yzm,Map<String,String> vImg_reply,String url_prefix){
		String inv_infor = "";
		
		//String url_str = "https://fpcy.gd-n-tax.gov.cn/WebQuery/invQuery?"
		/*String url_str = url_prefix + "/invQuery?"
				+ "callback=" + "jQuery"
				+ "&fpdm=" + inv.getInvCode()
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
				;*/
		String url_str = url_prefix + "/vatQuery?"
				+ "callback=" + "jQuery"
				+ "&key1=" + inv.getInvCode()
				+ "&key2=" + inv.getInvNo()
				+ "&key3=" + inv.getInvDate()
				+ "&key4=" + inv.getPriceExTax()
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
		/*args_map.put("fpdm",inv.getInvCode());
		args_map.put("fphm",inv.getInvNo());
		args_map.put("kprq",inv.getInvDate());
		args_map.put("fpje",inv.getPriceExTax());*/
		args_map.put("key1",inv.getInvCode());
		args_map.put("key2",inv.getInvNo());
		args_map.put("key3",inv.getInvDate());
		args_map.put("key4",inv.getPriceExTax());
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
		GetvImg get = new GetvImg();
		inv_infor = get.sendRequest(url_str,args_str);
		System.out.println("inv_infor: " + inv_infor);
		
		return inv_infor;
	}
	
	
	 private static class TrustAnyTrustManager implements X509TrustManager {
	    
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }
    
    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
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
			if (conn instanceof HttpsURLConnection)  {
	        	SSLContext sc = SSLContext.getInstance("SSL");
	        	sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
	        	((HttpsURLConnection) conn).setSSLSocketFactory(sc.getSocketFactory());
	        	((HttpsURLConnection) conn).setHostnameVerifier(new TrustAnyHostnameVerifier());
	        }
	        //conn.connect();
	        //System.out.println(conn.getResponseCode());

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
