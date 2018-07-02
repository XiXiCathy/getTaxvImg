package AreaTest;

import invInfoBean.InvInfor;
import invInfoLogical.GetvImg;
import invInfoLogical.GetvImgTest;
import invInfoLogical.HyAPI;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pwc.byhttpclient.HttpClientUtilWithMyX509TrustMananer;

public class AreaTestMainByHttpClient {
	private static final String SYSVer = "V1.0.06_001";//国税局发票系统版本

	public static void main(String[] args) throws Exception {
		Interact2DB action = new Interact2DB();
		List<InvInfor> inv_list = action.getBasicInvInfor();
		//String url_prefix = "";
		String res = "";
		String res_code = "";
		for(int i=0;i<inv_list.size();i++){
			InvInfor inv = inv_list.get(i);
			//url_str = action.getRequestUrl(inv.getArea_code());
			//url_prefix = inv.getIp() + "/WebQuery";
			try{
			res = new AreaTestMainByHttpClient().httpClientCheckTest(inv);
			GetvImg get = new GetvImg();
			//Map<String,String> inv_map = get.convert2Map(res);
			//res_code = inv_map.get("key1");
			res = res.substring(0,100);
			}catch(Exception e){
				e.printStackTrace();
				//res = res.substring(0,50);
			}finally{
				action.updateCheckRes(inv.getArea_code(), res_code, res);
				continue;
			}
		}

	}
	
	
	
	public String httpClientCheckTest(InvInfor inv) {

		Date now = new Date();
		String startNow = "" + now.getTime();
		Map<String, String> paras = new HashMap<String, String>();
		
		//湖南
		paras.put("fpdm", inv.getInvCode());
		paras.put("fphm", inv.getInvNo());
		paras.put("area", inv.getArea_code());	
		//广东
		/*
		paras.put("fpdm", "144001724660");
		paras.put("fphm", "01908372");
		paras.put("area", "4400");
		*/
		
		paras.put("r", String.valueOf(Math.random()));
		paras.put("v", SYSVer);	
		paras.put("nowtime", "");
		paras.put("pulickey", "");
		

		Map<String, String> heads = new HashMap<String, String>();
		heads.put("Accept", "*/*");
		heads.put("Accept-Encoding", "gzip, deflate, sdch, br");
		heads.put("Accept-Language", "en-US,en;q=0.8");
		heads.put("Connection", "keep-alive");

		// 获取验证码图片
		//GetvImg get = new GetvImg(vImgFolder, null, TaxSysVer);
		GetvImg get = new GetvImg();
		String data = "";
		//while (data.equals("")) {
			try {
				String url = inv.getIp() + inv.getYzmURL();
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
		//}

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
		
		if(inv.getInvURL().contains("/WebQuery/invQuery")){
			paras.put("fpdm", inv.getInvCode());
			paras.put("fphm", inv.getInvNo());
			paras.put("kprq", inv.getInvDate());
			paras.put("fpje", inv.getPriceExTax());
			
		}else{
			paras.put("key1", inv.getInvCode());
			paras.put("key2", inv.getInvNo());
			paras.put("key3", inv.getInvDate());
			paras.put("key4", inv.getPriceExTax());
		}
		paras.put("area", inv.getArea_code());
		
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
		//while (check_result.equals("")) {
			try {
				String url = inv.getIp() + inv.getInvURL();
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
		//}
		System.out.println("check_result: " + check_result);
		return check_result;

	}

}
