package invInfoLogical;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ImageOperation {
	public static final String diskPath = "C:/fapiao/";     // 图片临时文件路径
	//public static final String resultPath = "C:/fapiao/SecuritCode/vimg_qiche.png";
	public static final String resultPath = "C:/fapiao/vImg/";

	public static final Map<String, Color> colorMap = new HashMap<String,Color>();
	public static final Map<String, String> tipsMap = new HashMap<String,String>();
	static{
		colorMap.put("00", Color.BLACK);
		colorMap.put("01", Color.RED);
		colorMap.put("02", Color.YELLOW);
		colorMap.put("03", Color.BLUE);
		tipsMap.put("00", "输入验证码");
		tipsMap.put("01", "选择红色验证码");
		tipsMap.put("02", "选择黄色验证码");
		tipsMap.put("03", "选择蓝色验证码");
	}


	public static void main(String[] args) throws Exception {
		run("蓝色","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");
	}
	public static String run(String color,String imageBase64) throws Exception {
		String code_img_path = "";
		long startTime = System.nanoTime();
		createFolder(diskPath);
	 	String yzmImagePath = encode64Image(imageBase64);

		String tipsImagePath = generateImageTips(tipsMap.get(color),colorMap.get(color));
		code_img_path = composeImage(color,tipsImagePath, yzmImagePath);//function内包含判断，
		deleteFile(new String[]{yzmImagePath,tipsImagePath});
		System.out.println("共消耗"+((System.nanoTime()-startTime)/1000000000)+"秒");
		return code_img_path;
	}

	// 生成文件路径
	public static boolean createFolder(String folderPath){
		if(StringUtils.isBlank(folderPath)){
			return false;
		}
		File file = new File(folderPath);
		if(!file.exists()){
			file.mkdirs();
		}
		return true;
	}



	// 解码base64图片，并保存
	public static String encode64Image(String imageBase64){

		if(StringUtils.isBlank(imageBase64)){
			return null;
		}
		String fileName = getRandomString(15);
		byte[] decode = Base64.decodeBase64(imageBase64);
		int length = decode.length;
		for(int i=0;i<length;++i){
			if(decode[i] < 0){
				decode[i] += 256;
			}
		}
		OutputStream out = null;
		try {
			out = new FileOutputStream(new File(diskPath+fileName));
			try {
				out.write(decode);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return diskPath+fileName;

	}

	// 生成提示文字图片
	public static String generateImageTips(String tips,Color color) throws Exception{
		int width = 90;
		int height = 35;
		String fileName = getRandomString(15);
		File file = new File(diskPath+fileName);

		Font font = new Font("Serif", Font.BOLD, 10);
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = (Graphics2D)bi.getGraphics();
		g2.setBackground(new Color(192,192,192));
		g2.clearRect(0, 0, width, height);
		g2.setPaint(color);
		FontRenderContext context = g2.getFontRenderContext();
		Rectangle2D bounds = font.getStringBounds(tips, context);
		double x = (width - bounds.getWidth()) / 2;
		double y = (height - bounds.getHeight()) / 2;
		double ascent = -bounds.getY();
		double baseY = y + ascent;

		g2.drawString(tips, (int)x, (int)baseY);
		ImageIO.write(bi, "png", file);

		return diskPath+fileName;
	}

	// 合成图片
	public static String composeImage(String color, String img1Path, String img2Path){
		String code_img_path = resultPath + System.currentTimeMillis() + ".png";
		try {
			//第一张图片
			File  fileOne  =  new  File(img1Path);
			BufferedImage  imageOne = ImageIO.read(fileOne);
			int  width  =  imageOne.getWidth();
			int  height  =  imageOne.getHeight();
			int[]  imageArrayOne  =  new  int[width*height];
			imageArrayOne  =  imageOne.getRGB(0,0,width,height,imageArrayOne,0,width);

			//第二张图片
			File  fileTwo  =  new  File(img2Path);
			BufferedImage  imageTwo  =  ImageIO.read(fileTwo);
			int width2 = imageTwo.getWidth();
			int height2 = imageTwo.getHeight();
			int[]   ImageArrayTwo  =  new  int[width2*height2];
			ImageArrayTwo  =  imageTwo.getRGB(0,0,width,height,ImageArrayTwo,0,width);

			//新图片
			BufferedImage imageNew  =  new  BufferedImage(width,height*2,BufferedImage.TYPE_INT_RGB);
			imageNew.setRGB(0,0,width,height,imageArrayOne,0,width);
			imageNew.setRGB(0,height,width,height,ImageArrayTwo,0,width);
			File  outFile  =  new  File(code_img_path);
			if (color!="00"){
				//File  outFile  =  new  File(resultPath);
				ImageIO.write(imageNew,  "png",  outFile);
			} else {
				//File outFile = new File( resultPath );
				ImageIO.write( imageTwo,"png",outFile );
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
		return code_img_path;

	}

	// 删除无用的图片
	public static boolean deleteFile(String[] paths){
		if(paths == null || paths.length == 0){
			return false;
		}
		File file = null;
		for(String p:paths){
			file = new File(p);
			file.delete();
		}
		return true;
	}

	// 生成图片临时文件名
	public static String getRandomString(int length) { //length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
}
