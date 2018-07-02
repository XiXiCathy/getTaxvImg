package invInfoLogical;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Configuration {
	
	/*public static String server_ip;
	public static String certification_path;
	public static String target_page;*/
	public String server_ip;
	public String certification_path;
	public String target_page;
	public String proxyCertPass;
	public String rootCertPass;
	
	public Configuration() throws IOException{
		
	}
	
	public String getTargetpage(String role) throws Exception{
		File  inforFile = new File ("C:\\uploadData\\BAICSenseLoginAPP\\Role.txt");
		BufferedReader reader = new BufferedReader(new FileReader(inforFile));
		String tempString = null;
		String result="";
		while ((tempString = reader.readLine()) != null) {
			
			String[] role_arr = tempString.split(",");
			if(role_arr[0].equals(role)){
				result=  role_arr[1];
				break;
			}
		}
		
		reader.close();
		return result;
	}
	
	/**
	 * 根据登录用户的role及preview参数获取最终的目标页面
	 * @param role
	 * @param preview
	 * @return
	 * @throws Exception
	 */
	public String getPage(String role,String preview) throws Exception{
		String result="";
		String final_role = role;
		if(preview.equals("true")){
			final_role = role + "_" + "preview";
		}else if(preview.trim().length() > 0){
			//当preview不为true时，取C:\\uploadData\\BAICSenseLoginAPP\\Role.txt文件中的第一个字段为role_preview的行中的页面
			final_role = role + "_" + preview;
		}
		System.out.println("final_role = " + final_role);
		result = getTargetpage(final_role);
		return result;
	}
	
	//读取数据库相关信�?
	public void getServerConfigInfor() throws Exception{
		File  inforFile = new File ("C:\\uploadData\\BAICSenseLoginAPP\\GetTicketConfig.txt");
		BufferedReader reader = new BufferedReader(new FileReader(inforFile));
		String infor = "" ;
		String tempString = null;
		while ((tempString = reader.readLine()) != null) {
			infor += tempString;
		}
		System.out.println("infor = "+infor);
		server_ip = infor.substring("server_ip:".length(), infor.indexOf("certification_path:"));
		certification_path = infor.substring(infor.indexOf("certification_path:")+"certification_path:".length(), infor.indexOf("target_page:"));
		//target_page = infor.substring(infor.indexOf("target_page:")+"target_page:".length(),infor.indexOf("proxyCertPass:"));
		
		proxyCertPass = infor.substring(infor.indexOf("proxyCertPass:")+"proxyCertPass:".length(),infor.indexOf("rootCertPass:"));
		rootCertPass = infor.substring(infor.indexOf("rootCertPass:")+"rootCertPass:".length());
		
		reader.close();
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration config = new Configuration();
		config.getServerConfigInfor();
		System.out.println("server_ip = " + config.server_ip);
		System.out.println("certification_path = " + config.certification_path);
		System.out.println("target_page = " + config.target_page);
		System.out.println("proxyCertPass = " + config.proxyCertPass);
		System.out.println("rootCertPass = " + config.rootCertPass);
		
		String proxyCert = config.certification_path + "client.jks";
		String rootCert = config.certification_path + "root.jks";
		String finalUrl="http://"+ config.server_ip+"/ticket/"+ config.target_page +"/?qlikTicket="+"ticket";
		
		String body = "{ 'UserId':'" + "user1" + "','UserDirectory':'" + "userdir" +"',";
		body+= "'Attributes': [],"; 
		body+="'TargetUri':'http://"+config.server_ip+"/ticket/"+ config.target_page +"',";
		body+= "}"; 
		System.out.println("Payload: " + body);
		
		System.out.println("proxyCert = " + proxyCert);
		System.out.println("rootCert = " + rootCert);
		System.out.println("finalUrl = " + finalUrl);
		
		System.out.println("*****************user list***********************");
	}

}
