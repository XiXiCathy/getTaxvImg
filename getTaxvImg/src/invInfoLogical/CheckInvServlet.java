package invInfoLogical;

import invInfoBean.InvInfor;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CheckInvServlet
 */
@WebServlet("/CheckInv")
public class CheckInvServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckInvServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		String fpdm = request.getParameter("fpdm");
		String InvNo = request.getParameter("InvNo");
		String InvDate = request.getParameter("InvDate");
		String PriceExTax = request.getParameter("PriceExTax");
		String nowtime = request.getParameter("nowtime");
		String publickey = request.getParameter("publickey");
		System.out.println("fpdm: " + fpdm 
				+ " && InvNo: " + InvNo 
				+ " && InvDate: " + InvDate 
				+ " && PriceExTax: " + PriceExTax 
				+ " && nowtime: " + nowtime 
				+ " && publickey: " + publickey);
		
		InvInfor inv = new InvInfor();
		inv.setInvCode(fpdm);
		inv.setInvNo(InvNo);
		inv.setInvDate(InvDate);
		inv.setPriceExTax(PriceExTax);
		inv.setNowtime(nowtime);
		inv.setPublickey(publickey);
		
		new GetvImgTest().mainCheck(inv,"");
		
	}

}
