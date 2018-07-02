package AreaTest;

import invInfoBean.InvInfor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import DBUtilsTax.DbHelper;
import DBUtilsTax.IConnectionProvider;
import DBUtilsTax.JdbcProvider;

public class Interact2DB {
	private IConnectionProvider connectionProvider = null;
	private String sourceName;
	private DbHelper dbHelper;
	
	public Interact2DB() throws Exception{
		new Configure().getDBConfig();
		try {
			connectionProvider = (IConnectionProvider) new JdbcProvider("com.microsoft.sqlserver.jdbc.SQLServerDriver",
					"jdbc:sqlserver://" + Configure.db_ip, Configure.db_user_name,
					Configure.db_user_passwd);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			e.toString();
		}
		sourceName = Configure.db_name;
		dbHelper = new DbHelper(connectionProvider, sourceName);
	}
	
	/**
	 * 从数据库中获取需要查验的发票的信息
	 * @return
	 * @throws Exception
	 */
	public List<InvInfor> getBasicInvInfor() throws Exception{
		List<InvInfor> inv_list = new ArrayList<InvInfor>();
		
		//String sql = "select * from InvByArea where ISOPEN is null or ISOPEN=?";
		String sql = "select a.*,b.* from InvByArea a,ChinaTaxDimArea b where a.area = b.areaCode AND (ISOPEN is null or ISOPEN=?)";
		ResultSet rs = dbHelper.query(sql,"");
		
		while(rs.next()){
			InvInfor temp = new InvInfor();
			
			temp.setInvCode(rs.getString("inv_code"));
			temp.setInvNo(rs.getString("inv_num"));
			temp.setInvDate(rs.getString("billing_date"));
			String total_amt = rs.getString("total_levied");
			total_amt = total_amt.replace(",", "").trim();
			temp.setTotalAmt(total_amt);
			
			double total_amount = Double.parseDouble(total_amt);
			DecimalFormat df = new DecimalFormat("#######.00");
			double priceExTax = total_amount/(1 + 0.16);
			temp.setPriceExTax(df.format(priceExTax));
			temp.setArea_code(rs.getString("area"));
			temp.setIp(rs.getString("Ip"));
			temp.setYzmURL(rs.getString("yzmURL"));
			temp.setInvURL(rs.getString("invURL"));
			
			inv_list.add(temp);
		}
		return inv_list;
	}
	
	/**
	 * 通过区域code获取发送请求的链接
	 * @param area_code - 区域编码
	 * @return
	 * @throws Exception
	 */
	public String getRequestUrl(String area_code) throws Exception{
		String url_str = "";
		String sql = "select * from ChinaTaxDimArea where areaCode=?";
		ResultSet rs = dbHelper.query(sql, area_code);
		while(rs.next()){
			url_str = rs.getString("Ip");
			break;
		}
		System.out.println("url_str: " + url_str);
		return url_str;
	}
	
	/**
	 * 将查询结果更新到数据库
	 * @param area_code - 地区编码
	 * @param res_code - 查询结果的编码code
	 * @param res - 完整的查询结果字符串
	 * @return
	 * @throws Exception
	 */
	public int updateCheckRes(String area_code,String res_code,String res) throws Exception{
		String sql = "update ChinaTaxDimArea set ISOPEN=? ,Remark=? where areaCode=?";
		return dbHelper.updatePrepareSQL(sql, res_code,res,area_code);
	}
	
	
	/**
	 * 获取指定某一时期的税率，当正确找到时返回true，否则false
	 * @param keyin_infor
	 * @param incst_couter
	 * @return
	 * @throws Exception
	 */
	public boolean getLastTaxRate(InvInfor keyin_infor, int incst_couter) throws Exception {
		String inv_date = keyin_infor.getInvDate().replace("-", "");
		String sql = "select * from TAX_RATE where end_date <=? order by start_date DESC,end_date DESC";
		ResultSet rs = dbHelper.query(sql, inv_date);
		double tax_rate = 0;//默认税率为16%
		int counter = 0;
		boolean isGet = false;
		while(rs.next()){
			counter++;
			if(counter == incst_couter){
				tax_rate = rs.getDouble("tax_rate");
				isGet = true;
				break;
			}
		}
		System.out.println("current tax rate: " + tax_rate);
		if(isGet){
			String total_amt_str = keyin_infor.getTotalAmt();
			double total_amount = Double.parseDouble(total_amt_str);
			DecimalFormat df = new DecimalFormat("#######.00");
			double priceExTax = total_amount/(1 + tax_rate);
			keyin_infor.setPriceExTax(df.format(priceExTax));
		}
		return isGet;
	}
	
	
	public static void main(String[] args) throws Exception {
		Interact2DB action = new Interact2DB();
		List<InvInfor> inv_list = action.getBasicInvInfor();
		if(inv_list != null){
			System.out.println("Length of inv_list: " + inv_list.size());
		}


	}



}
