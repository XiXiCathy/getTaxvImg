package invInfoLogical;

import invInfoBean.InvInfor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class GetvImgTest {
	static String vImgFolder = "C:\\uploadData";
	static String Fplx = "03";
	static String TaxSysVer = "V1.0.06_001";
	public static String console_log_path = "C:\\fapiao_logs\\consoleLog\\";

	public static void main(String[] args) {
		InvInfor inv = new InvInfor();
		/*//area = 4400 - 佛山
		inv.setInvCode("144001724660");
		inv.setInvNo("01908342");
		inv.setInvDate("20180312");
		inv.setPriceExTax("229059.83");*/
		
		/*//area = 4400 - 佛山
		inv.setInvCode("144001724660");
		inv.setInvNo("01908372");
		inv.setInvDate("20180314");
		inv.setPriceExTax("191736.75");
		inv.setArea_code("4400");
		inv.setIp("https://fpcy.gd-n-tax.gov.cn/WebQuery");*/
		
		//area=4300 - 湖南株洲
		inv.setInvCode("143001720660");
		inv.setInvNo("01594093");
		inv.setInvDate("20180206");
		inv.setPriceExTax("254273.5");
		inv.setArea_code("4300");
		inv.setIp("https://fpcy.hntax.gov.cn:8083/WebQuery");
		
		/*//area=3702 - 青岛
		inv.setInvCode("137021722016");
		inv.setInvNo("00441207");
		inv.setInvDate("20180320");
		inv.setPriceExTax("231367.52");*/
		
		/*//area=4400 - 珠海
		inv.setInvCode("144001824660");
		inv.setInvNo("00253429");
		inv.setInvDate("20180326");
		inv.setPriceExTax("368290.60");*/
		
		/*//area=3100 - 上海
		inv.setInvCode("131001720661");
		inv.setInvNo("00846441");
		inv.setInvDate("20180326");
		inv.setPriceExTax("247008.55");*/
		
		/* * //area=4403 - 深圳
		inv.setInvCode("144031824160");
		inv.setInvNo("00033718");
		inv.setInvDate("20180322");
		inv.setPriceExTax("388461.54");*/
		
		/*//areaCode=3500 - 福建南平
		inv.setInvCode("135071720001");
		inv.setInvNo("00083371");
		inv.setInvDate("20180201");
		inv.setPriceExTax("264865.81");
		inv.setArea_code("3500");
		inv.setIp("https://fpcyweb.fj-n-tax.gov.cn/WebQuery");*/
		
		
		GetvImg get = new GetvImg();
		/*int i = 0;
		while(i < 5){
			System.out.println("i: " + i);
			get.readPK();
			i++;
		}*/
		
		
		//获取验证码图片
		//String data = get.getvImg("","");
		String data = get.getvImg(inv,inv.getNowtime(),inv.getPublickey(),inv.getArea_code(),inv.getIp());
		Map<String,String> vImg_map = get.convert2Map(data);
		String vImg_path = get.savevImg(vImg_map.get("key4"), vImg_map.get("key1"));
		System.out.println("vImg_path: " + vImg_path);
		
		try {
			//识别验证码
			String[] vcode = HyAPI.hyIdentifyCode(vImg_path, 4026);
			String valicodeStr = vcode[1];
			int error_vcode_time = 0;
			System.out.println("valicodeStr: " + valicodeStr);
			if(valicodeStr.toUpperCase().startsWith("#")){
				error_vcode_time++;
				
				if(error_vcode_time>10){
					System.out.println("验证码超时超过十次，结束运行。");
				}
			}else{
				System.out.println("vCode no error");
				
				//获取发票检验信息
				String check_result = get.getInvInfor(inv,Fplx,valicodeStr,vImg_map,inv.getIp());
				System.out.println("check_result: " + check_result);
				writeLog(check_result);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		

	}
	
	/**
	 * 写日志，日志文件名：log_日期串.txt
	 * 日志格式“日志时间：日志内容”
	 * @param log_content - 日志内容
	 */
	public static void writeLog(String log_content){
		Date nowdate=new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = dateFormat.format(nowdate);
		String dateString1 = dateFormat1.format(nowdate);
		writeFile(dateString1 + ":" + log_content,console_log_path,"log_" + dateString + ".txt",true);
	}
	
	
	/**
	 * 写文件
	 * @param message - 需要写的文件内容
	 * @param path - 文件路径
	 * @param file_name - 文件名称
	 * @param isAppend - 写文件时是清空还是追加，true表示追加，false表示清空
	 */
	public static void writeFile(String message,String path,String file_name,boolean isAppend){
		try {

			String content = message+"\r\n";
			File f = new File(path);
			if (!f.exists()) {
				f.mkdirs();
			}
			path = path + file_name;
			File file = new File(path);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter writer = new FileWriter(path, isAppend);
			writer.write(content);
			writer.close();
//			   FileWriter fw = new FileWriter(file.getAbsoluteFile());
//			   BufferedWriter bw = new BufferedWriter(fw);
//			   bw.write(content);
//			   bw.close();

			System.out.println("write file Done");

		} catch (IOException e) {
			//writeException(e);
			e.printStackTrace();

		}
	}
	
	public static String mainCheck(InvInfor inv,String url_prefix) {		
		GetvImg get = new GetvImg();
		//获取验证码图片
		String data = get.getvImg(inv,inv.getNowtime(),inv.getPublickey(),inv.getArea_code(),url_prefix);
		//String data = get.getvImg(inv.getNowtime(),inv.getPublickey());
		Map<String,String> vImg_map = get.convert2Map(data);
		String vImg_path = get.savevImg(vImg_map.get("key4"), vImg_map.get("key1"));
		System.out.println("vImg_path: " + vImg_path);
		
		try {
			//识别验证码
			String[] vcode = HyAPI.hyIdentifyCode(vImg_path, 4026);
			String valicodeStr = vcode[1];
			int error_vcode_time = 0;
			System.out.println("valicodeStr: " + valicodeStr);
			if(valicodeStr.toUpperCase().startsWith("#")){
				error_vcode_time++;
				
				if(error_vcode_time>10){
					System.out.println("验证码超时超过十次，结束运行。");
				}
			}else{
				System.out.println("vCode no error");
				
				//获取发票检验信息
				String res = get.getInvInfor(inv,Fplx,valicodeStr,vImg_map,url_prefix);
				System.out.println("res: " + res);
				return res;
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
