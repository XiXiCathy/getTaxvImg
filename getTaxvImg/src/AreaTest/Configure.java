package AreaTest;

import invInfoBean.InvInfor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configure {

	public static String db_ip;
	public static String db_name;
	public static String db_user_name;
	public static String db_user_passwd;
	private double maxAmount = 2500000;//价税合计超过该值就认为金额过大
	private double minAmount = 100000;//价税合计超过该值就认为金额过小
	
	public Configure() throws IOException{
		
	}
	
	/**
	 * 获取数据库连接信息
	 * @throws FileNotFoundException 
	 */
	public void getDBConfig() throws Exception{
		File  inforFile = new File ("C:\\uploadData\\DBConfigure\\uinvoiceConfig.txt");
		BufferedReader reader = new BufferedReader(new FileReader(inforFile));
		String infor = "" ;
		String tempString = null;
		while ((tempString = reader.readLine()) != null) {
			infor += tempString;
		}
		db_ip = infor.substring(6, infor.indexOf("db_name:"));
		db_name = infor.substring(infor.indexOf("db_name:")+8, infor.indexOf("db_user_name:"));
		db_user_name = infor.substring(infor.indexOf("db_user_name:")+13, infor.indexOf("db_user_password:"));
		db_user_passwd = infor.substring(infor.indexOf("db_user_password:")+17);
		
		System.out.println("dp_ip = " + db_ip);
		reader.close();
	}
	
	/**
	 * 从txt里面读取经销商key in的信息
	 * @return
	 * @throws Exception 
	 */
	public InvInfor getDealerKeyInInvoiceInfor() throws Exception{
		File  inforFile = new File ("C:\\fapiao\\TempInfo.txt");
		//File  inforFile = new File ("C:\\fapiao\\input.txt");
		//BufferedReader reader = new BufferedReader(new FileReader(inforFile));
		InputStreamReader read = new InputStreamReader(new FileInputStream(inforFile),"gbk");
		BufferedReader reader = new BufferedReader(read);
		String infor = "" ;
		String tempString = null;
		
		InvInfor keyin_infor = new InvInfor();
		while ((tempString = reader.readLine()) != null) {
			//infor += tempString;
			if(tempString.indexOf("$") <= 0){
				continue;
			}
			int index = tempString.indexOf("$") + 1;
			String tmp = tempString.substring(0,index);
			infor = tempString.substring(index);
			/*System.out.println("tmp = " + tmp + "\ninfor = " + infor);*/
			//tmp = "InvoiceID$";
			switch(tmp){
			case "InvoiceID$":
			//case "InvoiceID$":
				keyin_infor.setInvoiceID(infor);
				System.out.println("\ninfor = " + infor + " && " + keyin_infor.getInvoiceID());
				break;
			case "TimeStamp$":
				keyin_infor.setTimeStamp(infor);
				break;
			case "ReprotType$":
				keyin_infor.setReprotType(infor);
				break;
			case "SaleType$":
				keyin_infor.setSaleType(infor);
				break;
			case "InvoiceType$":
				keyin_infor.setInvType(infor);
				break;
			case "InvoiceDate$":
				keyin_infor.setInvDate(infor);
				break;
			case "InvoiceCode$":
				keyin_infor.setInvCode(infor);
				break;
			case "InvoiceNo$":
				keyin_infor.setInvNo(infor);
				break;
			case "InvoiceCategory$":
				keyin_infor.setInvCategory(infor);
				break;
			case "InvoiceStatus$":
				keyin_infor.setInvStatus(infor);
				break;
			case "VIN$":
				keyin_infor.setVIN(infor);
				break;
			case "TotalAmt$":
				infor = infor.replace(",", "").trim();
				keyin_infor.setTotalAmt(infor);
				double total_amount = Double.parseDouble(infor);
				/*DecimalFormat df = new DecimalFormat("#######.00");   
				keyin_infor.setPriceExTax(df.format(total_amount/1.17));*/
				if(maxAmount <= Math.abs(total_amount)){
					System.out.println("da");
					keyin_infor.setLargeAmountTip("；价税合计过高");
				}else if(minAmount >= Math.abs(total_amount)){
					keyin_infor.setLargeAmountTip("；价税合计过低");
				}else{
					System.out.println("xiao");
					keyin_infor.setLargeAmountTip("");
				}
				System.out.println("fin:" + keyin_infor.getLargeAmountTip());
				break;
			case "Company$":
				keyin_infor.setCompany(infor);
				break;
			case "CarType$":
				keyin_infor.setCarType(infor);
				break;
			case "DealerNo$":
				keyin_infor.setDealerNo(infor);
				break;
			case "SalesCompany$":
				keyin_infor.setSalesCompany(infor);
				break;
			case "Result$":
				keyin_infor.setResult(infor);
				break;
			case "Remark$":
				keyin_infor.setRemark(infor);
				break;
			case "PictureType$":
				keyin_infor.setPictureType(infor);
				break;
			case "Pending$":
				keyin_infor.setPending(infor);
				break;
			case "Approver$":
				keyin_infor.setApprover(infor);
				break;
			default:
				System.out.println("Dealer Key In Information Error." + tmp + " && infor = " + infor);
				break;
			}
		}
		reader.close();
		return keyin_infor;
	}
	
	
	
	
	/**
	 * 获取存储在Manufactures.txt文件中的厂商的名称
	 * @param args
	 * @throws IOException 
	 * @throws Exception
	 */
	public List<String> getManufacturesName() throws IOException{
		List<String> manufactures = new ArrayList<String>();
		manufactures = getConfigList("C:\\fapiao\\Configuration\\Manufactures.txt");
		
		/*File  inforFile = new File ("C:\\fapiao\\Configuration\\Manufactures.txt");
		//BufferedReader reader = new BufferedReader(new FileReader(inforFile));
		InputStreamReader read = new InputStreamReader(new FileInputStream(inforFile),"gbk");
		BufferedReader reader = new BufferedReader(read);
		String tempString = null;
		
		while ((tempString = reader.readLine()) != null) {
			manufactures.add(tempString);
		}
		reader.close();*/
		return manufactures;
	}
	
	
	/**
	 * 从QualificationIinvoiceBranch.txt文件中获取配置文件中的有开票资格的展厅，子公司，分公司
	 * @param file_path
	 * @return
	 * @throws IOException
	 */
	public List<String> getQualifiedBranch() throws IOException{
		List<String> qualified = new ArrayList<String>();
		qualified = getConfigList("C:\\fapiao\\Configuration\\QualificationIinvoiceBranch.txt");
		return qualified;
	}
	
	
	/**
	 * 从UnqualificationIinvoiceBranch.txt文件中获取配置文件中的无开票资格的展厅，子公司，分公司
	 * @param file_path
	 * @return
	 * @throws IOException
	 */
	public List<String> getUnqualifiedBranch() throws IOException{
		List<String> unqualified = new ArrayList<String>();
		unqualified = getConfigList("C:\\fapiao\\Configuration\\UnqualificationIinvoiceBranch.txt");
		return unqualified;
	}
	
	
	
	
	
	public Map<String, String> getConfigFilesPath() throws IOException{
		File  inforFile = new File ("C:\\fapiao\\Configuration\\ConfigFilesPath.txt");
		InputStreamReader read = new InputStreamReader(new FileInputStream(inforFile),"gbk");
		BufferedReader reader = new BufferedReader(read);
		String infor = "" ;
		String tempString = null;
		Map<String, String> paths_map = new HashMap<String, String>();
		while ((tempString = reader.readLine()) != null) {
			if(tempString.indexOf(",") <= 0){
				continue;
			}
			int index = tempString.indexOf(",");
			String tmp_path_name = tempString.substring(0,index);
			infor = tempString.substring(index+1);
			paths_map.put(tmp_path_name, infor);
			System.out.println("tmp_path_name = " + tmp_path_name + "\ninfor = " + infor);
		}
		reader.close();
		return paths_map;
	}
	
	
	
	
	/**
	 * 读取配置文件，返回一个list
	 * @param file_path
	 * @return
	 * @throws IOException
	 */
	public List<String> getConfigList(String file_path) throws IOException{
		List<String> cand_list = new ArrayList<String>();
		
		File  inforFile = new File (file_path);
		//BufferedReader reader = new BufferedReader(new FileReader(inforFile));
		InputStreamReader read = new InputStreamReader(new FileInputStream(inforFile),"gbk");
		BufferedReader reader = new BufferedReader(read);
		String tempString = null;
		
		while ((tempString = reader.readLine()) != null) {
			cand_list.add(tempString);
		}
		reader.close();
		return cand_list;
	}
	
	public String getLastVcode() throws IOException {
		String last_vcode = "";
		
		File  inforFile = new File ("C:\\fapiao\\vImg\\last_vcode.txt");
		InputStreamReader read = new InputStreamReader(new FileInputStream(inforFile),"gbk");
		BufferedReader reader = new BufferedReader(read);
		String tempString = null;
		while ((tempString = reader.readLine()) != null) {
			last_vcode = tempString;
			return last_vcode;
		}
		reader.close();
		
		return last_vcode;
	}
	
	
	
	public static void main(String[] args) throws Exception{
		Configure config = new Configure();
		//config.getDealerKeyInInvoiceInfor();
		/*Map<String, String> paths_map = new HashMap<String, String>();
		paths_map = config.getConfigFilesPath();
		String PhotoLocation = paths_map.get("PhotoLocation");
		System.out.println("PhotoLocation = " + PhotoLocation);*/
		
		
		String content1 = "hello";
		String content2 = " world!";
		FileWriter writer = new FileWriter("C:\\fapiao\\test_writefile.txt", true);
		writer.write(content1);
		writer.close();
		
		File test = new File("C:\\fapiao\\test_writefile.txt","r");
		test.setReadOnly();
		InputStreamReader read = new InputStreamReader(new FileInputStream(test),"gbk");
		BufferedReader reader = new BufferedReader(read);
		String tempString = null;
		while ((tempString = reader.readLine()) != null) {
			System.out.println(tempString);
		}
		reader.close();
		
		FileWriter writer2 = new FileWriter("C:\\fapiao\\test_writefile.txt", true);
		writer2.write(content2);
		writer2.close();
		
		
		
	}

	
	
}
