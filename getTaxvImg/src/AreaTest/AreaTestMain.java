package AreaTest;

import invInfoBean.InvInfor;
import invInfoLogical.GetvImg;
import invInfoLogical.GetvImgTest;

import java.util.List;
import java.util.Map;

public class AreaTestMain {

	public static void main(String[] args) throws Exception {
		Interact2DB action = new Interact2DB();
		List<InvInfor> inv_list = action.getBasicInvInfor();
		String url_prefix = "";
		String res = "";
		String res_code = "";
		for(int i=0;i<inv_list.size();i++){
			InvInfor inv = inv_list.get(i);
			//url_str = action.getRequestUrl(inv.getArea_code());
			url_prefix = inv.getIp() + "/WebQuery";
			try{
			res = GetvImgTest.mainCheck(inv, url_prefix);
			GetvImg get = new GetvImg();
			Map<String,String> inv_map = get.convert2Map(res);
			res_code = inv_map.get("key1");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				action.updateCheckRes(inv.getArea_code(), res_code, res);
				continue;
			}
		}

	}

}
