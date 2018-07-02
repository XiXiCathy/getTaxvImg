package invInfoBean;

public class DimArea {
	private String code;//省份城市编号
    private String sfmc;//省份名称
    private String Ip;//省份IP地址
    private String address;//省份IP地址
    private String yzmURL;//省份查验证码的地址的后缀
    private String invURL;//地区差详细发票信息地址的后缀

    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
    public void setSfmc(String sfmc){
        this.sfmc = sfmc;
    }
    public String getSfmc(){
        return this.sfmc;
    }
    public void setIp(String Ip){
        this.Ip = Ip;
    }
    public String getIp(){
        return this.Ip;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public String getAddress(){
        return this.address;
    }
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
}
