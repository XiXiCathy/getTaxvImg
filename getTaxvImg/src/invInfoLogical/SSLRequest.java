package invInfoLogical;
import javax.net.ssl.*; 

import java.io.*; 
import java.net.URL; 
import java.security.*; 
import java.security.cert.CertificateException; 
import java.util.UUID;
 
public class SSLRequest 
{ 
	String username="";
	String userdir="";
	String role="";
	String preview="";
	
	public SSLRequest(String name,String dir,String role,String preview)
	{
		this.username=name;
		this.userdir=dir;
		this.role=role;
		this.preview = preview;
	}
	
	public String getUrl() 
	{ 	
		String xrfkey = getUUId(); //Xrfkey to prevent cross-site issues 
		//('v') Generate a random 16-digit UUID for xrfkey
		
//		String host = "60.205.151.173"; //Enter the Qlik Sense Server hostname here 
		String host = ""; //Enter the Qlik Sense Server hostname here
		String vproxy = "ticket"; //Enter the prefix for the virtual proxy configured in Qlik Sense Steps Step 1
		//('v') In our case, this is it.
		
		String target_page = "";
		String str="";
		
		try 
		{
			/************** BEGIN Certificate Acquisition **************/ 
			/*String certFolder = "c:\\QSC\\"; //This is a folder reference to the location of the jks files used for securing ReST communication 
			String proxyCert = certFolder + "client.jks"; //Reference to the client jks file which includes the client certificate with private key 
			String proxyCertPass="Pwcauto!@#"; //This is the password to access the Java Key Store information 
			String rootCert = certFolder + "root.jks"; //Reference to the root certificate for the client cert. Required in this example because Qlik Sense certs are used. 
			String rootCertPass = "Pwcauto!@#"; //This is the password to access the Java Key Store information
*/			/************** END Certificate Acquisition **************/
 
			String certFolder = "";
			String proxyCertPass = "";
			String rootCertPass = "";
			
			try {
				Configuration config = new Configuration();
				try {
					config.getServerConfigInfor();
					
					host = config.server_ip;
					//target_page = config.target_page;
					//target_page=config.getTargetpage(role);
					target_page=config.getPage(role,preview);
					certFolder = config.certification_path;
					proxyCertPass = config.proxyCertPass;
					rootCertPass = config.rootCertPass;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			String proxyCert = certFolder + "client.jks";
			String rootCert = certFolder + "root.jks";
			System.out.println("begin get url3");
			//('v') In this block, nothing needs to be changed.
			/************** BEGIN Certificate configuration for use in connection **************/
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(new File(proxyCert)), proxyCertPass.toCharArray()); 
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()); 
			kmf.init(ks, proxyCertPass.toCharArray()); 
			SSLContext context = SSLContext.getInstance("SSL"); 
			KeyStore ksTrust = KeyStore.getInstance("JKS"); 
			ksTrust.load(new FileInputStream(rootCert), rootCertPass.toCharArray()); 
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()); 
			tmf.init(ksTrust); 
			context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null); 
			SSLSocketFactory sslSocketFactory = context.getSocketFactory();
			/************** END Certificate configuration for use in connection **************/


			/************** BEGIN HTTPS Connection **************/
			System.out.println("Browsing to: " + "https://" + host + ":4243/qps/" + vproxy + "/ticket?xrfkey=" + xrfkey); 
			URL url = new URL("https://" + host + ":4243/qps/" + vproxy + "/ticket?xrfkey=" + xrfkey); 
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
			{
	            public boolean verify(String hostname, SSLSession session)
	            {
	               return true;//('v') Turn off the verification.
	            }
	        });
			HttpsURLConnection connection = (HttpsURLConnection ) url.openConnection();	
			connection.setSSLSocketFactory(sslSocketFactory); 
			connection.setRequestProperty("x-qlik-xrfkey", xrfkey); 
			connection.setDoOutput(true); 
			connection.setDoInput(true); 
			connection.setRequestProperty("Content-Type","application/json"); 
			connection.setRequestProperty("Accept", "application/json"); 
			connection.setRequestMethod("POST");
			/************** BEGIN JSON Message to Qlik Sense Proxy API **************/


			String body = "{ 'UserId':'" + username + "','UserDirectory':'" + userdir +"',";
			body+= "'Attributes': [],"; 
//			body+="'TargetUri':'http://"+host+"/ticket/hub',";
			body+="'TargetUri':'http://"+host+"/ticket/"+ target_page +"',";
			body+= "}"; 
			System.out.println("Payload: " + body);
			/************** END JSON Message to Qlik Sense Proxy API **************/


			OutputStreamWriter wr= new OutputStreamWriter(connection.getOutputStream()); 
			wr.write(body); 
			wr.flush(); //Get the response from the QPS BufferedReader 
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream())); 
			StringBuilder builder = new StringBuilder(); 
			String inputLine; 
			while ((inputLine = in.readLine()) != null) 
			{ 
				builder.append(inputLine); 
			} 
			in.close(); 
			String data = builder.toString(); 
			str=data;
			System.out.println("The response from the server is: " + data);
			wr.close();
			connection.disconnect();
			/************** END HTTPS Connection **************/
		} 
		catch (KeyStoreException e) { e.printStackTrace(); } 
		catch (IOException e) { e.printStackTrace(); } 
		catch (CertificateException e) { e.printStackTrace(); } 
		catch (NoSuchAlgorithmException e) { e.printStackTrace(); } 
		catch (UnrecoverableKeyException e) { e.printStackTrace(); } 
		catch (KeyManagementException e) { e.printStackTrace(); } 
		//('v') Make the needed json here.
		String ticket=str.split(",")[3].substring(10);
//		String finalUrl="http://60.205.151.173/ticket/hub/?qlikTicket="+ticket;
		String finalUrl="http://" + host + "/ticket/"+ target_page +"?qlikTicket="+ticket;
		finalUrl=finalUrl.substring(0,finalUrl.length()-1);
		//return "{\"status\":\"success\",\"url\":\""+finalUrl+"\",\"ticket\":\""+ticket+"}";
		//return finalUrl;
		return ticket.substring(0,ticket.length()-1);
	} 

	
	 public static String getUUId() {
         return UUID.randomUUID().toString().replaceAll("-","").substring(0,16);
     }
	

}