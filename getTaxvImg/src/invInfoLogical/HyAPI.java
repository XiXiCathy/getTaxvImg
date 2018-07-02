package invInfoLogical;

//需要用到jna库，可以用本例子带的jna库，把它导入到项目路径
//或者可以到https://github.com/java-native-access/jna下载
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import java.lang.Thread;

public class HyAPI {
	
	public interface HYPlugin extends Library // 载入dll
	{
		
		//NativeLibrary.addSearchPath("HYDati.dll","C:\\UUWiseConfig\\");
		//HYPlugin INSTANCE = (HYPlugin) Native.loadLibrary(NativeLibrary.addSearchPath("HYDati.dll","C:\\UUWiseConfig\\"), HYPlugin.class);
		HYPlugin INSTANCE = (HYPlugin) Native.loadLibrary("C:\\UUWiseConfig\\HYDati.dll", HYPlugin.class);
		
		public void SetAuthor(String author);// 设置作者账号

		public void EnableLog(String logpath);// 返回剩余题分

		public String QueryBalance();// 返回剩余题分

		public String SendFile(String authcode, String path, int type, int timeout, int priority, String extr_str);// 发送文件

		public String GetAnswer(String tid);// 得到答案
	}

	public static String[] hyIdentifyCode(String path,int type) throws IllegalAccessException {
		// 修改jna默认编码
		System.setProperty("jna.encoding", "GBK");

		HYPlugin.INSTANCE.SetAuthor("pwcbjra2");// 设置作者账号，用来获取返利

		HYPlugin.INSTANCE.EnableLog("dati.log"); // 开启日志文件

		// 发送文件
		String tid = HYPlugin.INSTANCE.SendFile("iTYg5iQUDwKxCp5k",path, type, 60, 1, "");

		if (tid.startsWith("#")) {
			System.out.println("发送失败:" + tid);
		}
		// 打印题号
		System.out.println("得到题号:" + tid);

		String answer = "";
		while (answer.isEmpty()) {
			answer = HYPlugin.INSTANCE.GetAnswer(tid);
			try {
				Thread.currentThread().sleep(1000); // 等待1秒再取答案
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (answer.startsWith("#")) {
			System.out.println("答题失败:" + answer);
		}

		System.out.println("答题答案:" + answer);
		String[] result = new String[2];
		result[0] = tid;
		result[1] = answer;
		return result;

	}
	
	public static void main(String[] args){
		NativeLibrary.addSearchPath("HYDati.dll", "C:\\UUWiseConfig\\");
	}

}
