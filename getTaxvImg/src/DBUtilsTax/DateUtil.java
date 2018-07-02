package DBUtilsTax;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static String getNowStr() {
		Date nowdate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = dateFormat.format(nowdate);
		return dateString;
	}
	
	public static String getDateFormat(Date dateStr) {
		
		String result = dateStr.toString();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			result = dateFormat.format(dateStr);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
		
	}
	
}
