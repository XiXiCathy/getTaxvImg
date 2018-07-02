package invInfoBean;

public class InvInfor {
	private String InvoiceID;//发票ID
	private String TimeStamp;//审批时间
	private String ReprotType;//上报类型
	private String SaleType;//销售类型
	private String InvType;//发票类型
	private String InvDate;//开票日期
	private String InvCode;//发票代码
	private String InvNo;//发票号码
	private String InvCategory;//发票种类
	private String InvStatus;//发票状态
	private String VIN;//车架号
	private String TotalAmt;//价税合计
	private String PriceExTax;//不含税价
	private String Company;//公司
	private String CarType;//车型
	private String DealerNo;//经销商编号
	private String SalesCompany;//销货单位名称
	private String Result;//审批员本次判定结果
	private String Remark;//更改理由
	private String result_type;//标志位，区别是查验过程中的记录还是审批后的记录结果,0-查验过程中的记录，1-审批后的记录
	private String PictureType;//从发票上传系统中获取的发票图片存储的类型
	private String Pending;//发票上传系统中的pending原因
	private String Approver;//登录发票上传系统审批员账号
	private String LargeAmountTip;//是否金额过大，如果金额过大，该属性值为“；价税合计过高”，金额过小时，“；价税合计过低”，否则值为空串
	
	private String dealer_name;//销货单位名称
	private String taxplayer_id;//纳税人识别号
	private String check_result;//发票查验结果
	private String shot_url;//查验截图URL
	private String ocr_inv_code;//OCR识别结果发票代码
	private String ocr_inv_num;//OCR识别发票号码
	private String inv_id;//发票ID
	private String apv_time;//审批时间
	private String final_result;//最终程序对该条记录的审批结果
	
	private String nowtime;
	private String publickey;
	private String area_code;//所在地区编号
	private String ip;//需要请求的IP
	private String yzmURL;//省份查验证码的地址的后缀
    private String invURL;//地区差详细发票信息地址的后缀
	
    public String getYzmURL() {
		return yzmURL;
	}
	public void setYzmURL(String yzmURL) {
		this.yzmURL = yzmURL;
	}
	public String getInvURL() {
		return invURL;
	}
	public void setInvURL(String invURL) {
		this.invURL = invURL;
	}
	public String getArea_code() {
		return area_code;
	}
	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getInvoiceID() {
		return InvoiceID;
	}
	public void setInvoiceID(String invoiceID) {
		InvoiceID = invoiceID;
	}
	public String getTimeStamp() {
		return TimeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		TimeStamp = timeStamp;
	}
	public String getReprotType() {
		return ReprotType;
	}
	public void setReprotType(String reprotType) {
		ReprotType = reprotType;
	}
	public String getSaleType() {
		return SaleType;
	}
	public void setSaleType(String saleType) {
		SaleType = saleType;
	}
	public String getInvType() {
		return InvType;
	}
	public void setInvType(String invType) {
		InvType = invType;
	}
	public String getInvDate() {
		return InvDate;
	}
	public void setInvDate(String invDate) {
		InvDate = invDate;
	}
	public String getInvCode() {
		return InvCode;
	}
	public void setInvCode(String invCode) {
		InvCode = invCode;
	}
	public String getInvNo() {
		return InvNo;
	}
	public void setInvNo(String invNo) {
		InvNo = invNo;
	}
	public String getInvCategory() {
		return InvCategory;
	}
	public void setInvCategory(String invCategory) {
		InvCategory = invCategory;
	}
	public String getInvStatus() {
		return InvStatus;
	}
	public void setInvStatus(String invStatus) {
		InvStatus = invStatus;
	}
	public String getVIN() {
		return VIN;
	}
	public void setVIN(String vIN) {
		VIN = vIN;
	}
	public String getTotalAmt() {
		return TotalAmt;
	}
	public void setTotalAmt(String totalAmt) {
		TotalAmt = totalAmt;
	}
	public String getPriceExTax() {
		return PriceExTax;
	}
	public void setPriceExTax(String priceExTax) {
		PriceExTax = priceExTax;
	}
	public String getCompany() {
		return Company;
	}
	public void setCompany(String company) {
		Company = company;
	}
	public String getCarType() {
		return CarType;
	}
	public void setCarType(String carType) {
		CarType = carType;
	}
	public String getDealerNo() {
		return DealerNo;
	}
	public void setDealerNo(String dealerNo) {
		DealerNo = dealerNo;
	}
	public String getSalesCompany() {
		return SalesCompany;
	}
	public void setSalesCompany(String salesCompany) {
		SalesCompany = salesCompany;
	}
	public String getResult() {
		return Result;
	}
	public void setResult(String result) {
		Result = result;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public String getResult_type() {
		return result_type;
	}
	public void setResult_type(String result_type) {
		this.result_type = result_type;
	}
	public String getPictureType() {
		return PictureType;
	}
	public void setPictureType(String PictureType) {
		this.PictureType = PictureType;
	}
	public String getPending() {
		return Pending;
	}
	public void setPending(String pending) {
		Pending = pending;
	}
	public String getApprover() {
		return Approver;
	}
	public void setApprover(String approver) {
		Approver = approver;
	}
	public String getLargeAmountTip() {
		return LargeAmountTip;
	}
	public void setLargeAmountTip(String largeAmountTip) {
		LargeAmountTip = largeAmountTip;
	}
	

	
	public String getDealer_name() {
		return dealer_name;
	}
	public void setDealer_name(String dealer_name) {
		this.dealer_name = dealer_name;
	}
	public String getTaxplayer_id() {
		return taxplayer_id;
	}
	public void setTaxplayer_id(String taxplayer_id) {
		this.taxplayer_id = taxplayer_id;
	}
	public String getCheck_result() {
		return check_result;
	}
	public void setCheck_result(String check_result) {
		this.check_result = check_result;
	}
	public String getShot_url() {
		return shot_url;
	}
	public void setShot_url(String shot_url) {
		this.shot_url = shot_url;
	}
	public String getOcr_inv_code() {
		return ocr_inv_code;
	}
	public void setOcr_inv_code(String ocr_inv_code) {
		this.ocr_inv_code = ocr_inv_code;
	}
	public String getOcr_inv_num() {
		return ocr_inv_num;
	}
	public void setOcr_inv_num(String ocr_inv_num) {
		this.ocr_inv_num = ocr_inv_num;
	}
	public String getInv_id() {
		return inv_id;
	}
	public void setInv_id(String inv_id) {
		this.inv_id = inv_id;
	}
	public String getApv_time() {
		return apv_time;
	}
	public void setApv_time(String apv_time) {
		this.apv_time = apv_time;
	}
	public String getFinal_result() {
		return final_result;
	}
	public void setFinal_result(String final_result) {
		this.final_result = final_result;
	}
	public String getNowtime() {
		return nowtime;
	}
	public void setNowtime(String nowtime) {
		this.nowtime = nowtime;
	}
	public String getPublickey() {
		return publickey;
	}
	public void setPublickey(String publickey) {
		this.publickey = publickey;
	}

	/*
	String invoiceID = "";
	String timeStamp = "";
	String reprotType = "";
	String saleType = "";
	String invoiceType = "";
	String invoiceDate = "";
	String invoiceCode = "";
	String invoiceNo = "";
	String invoiceCategory = "";
	String invoiceStatus = "";
	String VIN = "";
	String totalAmt = "";
	String company = "";
	String carType = "";
	String dealerNo = "";
	String salesCompany = "";
	String result = "";
	String remark = "";
		
	keyin_infor.setInvoiceID(invoiceID);
	keyin_infor.setTimeStamp(timeStamp);
	keyin_infor.setReprotType(reprotType);
	keyin_infor.setSaleType(saleType);
	keyin_infor.setInvoiceType(invoiceType);
	keyin_infor.setInvoiceDate(invoiceDate);
	keyin_infor.setInvoiceCode(invoiceCode);
	keyin_infor.setInvoiceNo(invoiceNo);
	keyin_infor.setInvoiceCategory(invoiceCategory);
	keyin_infor.setInvoiceStatus(invoiceStatus);
	keyin_infor.setVIN(VIN);
	keyin_infor.setTotalAmt(totalAmt);
	keyin_infor.setCompany(company);
	keyin_infor.setCarType(carType);
	keyin_infor.setDealerNo(dealerNo);
	keyin_infor.setSalesCompany(salesCompany);
	keyin_infor.setResult(result);
	keyin_infor.setRemark(remark);
	*/
	
}
