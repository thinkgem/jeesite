package com.jeesite.common.media;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.image.ImageUtils;
import com.jeesite.common.io.FileUtils;
import com.jeesite.common.io.PropertiesUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.lang.TimeUtils;

/**
 * 视频工具类
 * @author ThinkGem
 * @version 2015-5-13
 */
public class VideoUtils {

	private static final Logger log = LoggerFactory.getLogger(VideoUtils.class);
	private static String ffmpegFile; 		// ffmpeg.exe所放的路径
	private static String mencoderFile;		//  mencoder.exe所放的路径
	private static String qtFaststartFile;	//  qt-faststart.exe所放的路径
	
	private String inputFile = "";				// 需转换的原始文件名称
	private String inputFileExtension = "";		// 当前文件的文件后缀
	
	private String outputFile = "";				// 输出文件名称
	private String outputFileExtension = "mp4";	// 最终转换的文件格式 mp4 flv
	
	private String imgFile = "";				// 生成缩略图文件名
	private String imgFileExtension = "jpg";	// 生成视频图片截图后缀 jpg gif
	
	private String width = null;//"800";		// 视频默认宽高
	private String height = null;//"600";		// 视频默认宽高
	
	private boolean status = false; 	// 是否正常状态

	/**
	 * 构造函数
	 * @param inputFile 需要转换视频文件的绝对路径和名称
	 */
	public VideoUtils(String inputFile) {
		this(inputFile, null, null);
	}
	
	/**
	 * 构造函数
	 * @param inputFile 需要转换视频文件的绝对路径和名称
	 * @param outputFile 视频文件转换后的输出文件路径和名称
	 * @param imgFile 视频文件截图的图片路径和名称
	 */
	public VideoUtils(String inputFile, String outputFile, String imgFile) {
		this.inputFile = FileUtils.path(inputFile);
		this.inputFileExtension = FileUtils.getFileExtension(inputFile);
		this.outputFile = outputFile != null ? FileUtils.path(outputFile) : inputFile + "." + outputFileExtension;
		this.imgFile = imgFile != null ? imgFile : inputFile + "." + imgFileExtension;
		this.status = checkfile(inputFile);
	}

	/**
	 * 构造函数
	 * @param inputFile 需要转换视频文件的绝对路径和名称
	 * @param outputFile 视频文件转换后的输出文件路径和名称
	 * @param imgFile 视频文件截图的图片路径和名称
	 * @param width 转换后视频和图片的宽度
	 * @param height 转换后视频和图片的高度
	 */
	public VideoUtils(String inputFile, String outputFile, String imgFile, String width, String height){
		this(inputFile, outputFile, imgFile);
		this.width = width;
		this.height = height;
	}

	/**
	 * 检查文件格式。根据文件格式 分类解析
	 * @return int 0：ffmpag；1：mencoder；0：不支持的格式
	 */
	private int checkContentType() {
		// ffmpeg 能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv，rm，rmvb等）
		if (StringUtils.inString(inputFileExtension, "avi", "mpg", "wmv", "3gp", "mov",
				"mp4", "asf", "asx", "flv", "rm", "rmvb")) {
			return 0;
		}
		// 对ffmpeg无法解析的文件格式(wmv9，等), 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.
		else if (StringUtils.inString(inputFileExtension, "wmv9")) {
			return 1;
		}
		return 9;
	}

	/**
	 * 截取图片
	 * @return boolean  
	 */
	public boolean cutPic() {
		long startTime = System.currentTimeMillis(); // 获取开始时间
		boolean statusTemp = status;
		if (statusTemp) {
			statusTemp = processFfmpegCutpic(inputFile, outputFile);
			try {
				File imgfile = new File(imgFile);
				if (imgfile.exists()){
					ImageUtils.thumbnails(imgfile, 800, 600, null);
				}else{
					statusTemp = false;
				}
			} catch (Exception e) {
				statusTemp = false;
				log.error("视频剪切图片失败", e);
			}
		}
		log.debug("视频剪切图片" + (statusTemp ? "成功" : "失败") + "，用时：" + TimeUtils.formatDateAgo(System.currentTimeMillis() - startTime));
		return statusTemp;
	}

	/**
	 * 转换视频
	 * @return boolean  
	 */
	public boolean convert() {
		long startTime = System.currentTimeMillis(); // 获取开始时间
		boolean statusTemp = status;
		int type = checkContentType();
		String tempFile = outputFile + ".tmp";
		if (statusTemp && type == 0) {
			log.debug("使用ffmpage进行视频转换");
			statusTemp = processFfmpeg(inputFile, tempFile);
		} else if (statusTemp && type == 1) {
			log.debug("使用mencoder进行视频转换");
			statusTemp = processMencoder(inputFile, tempFile);
		}
		if (statusTemp){
			log.debug("将mp4视频的元数据信息转到视频第一帧");
			statusTemp = processQtFaststart(tempFile, outputFile);
		}
		log.debug("删除临时文件");
		FileUtils.deleteFile(tempFile);
		log.debug("视频转换" + (statusTemp ? "成功" : "失败") + "，用时：" + TimeUtils.formatDateAgo(System.currentTimeMillis() - startTime));
		return statusTemp;
	}

	/**
	 * 检查文件是否存在
	 * @param inputFile
	 * @return boolean
	 */
	public boolean checkfile(String inputFile) {
		File file = new File(inputFile);
		if (!file.isFile() || !file.exists()) {
			log.warn("文件不存在！");
			return false;
		}
		return true;
	}

	/**
	 * ffmpeg 截取缩略图
	 * @param inputFile
	 * @return boolean  
	 */
	public boolean processFfmpegCutpic(String inputFile, String outputFile) {
		List<String> command = new java.util.ArrayList<String>();
		command.add(getFfmpegFile());
		command.add("-i");
		command.add(inputFile);
		if ((imgFileExtension.toLowerCase()).equals("gif")) {
			command.add("-vframes");
			command.add("30");
			command.add("-f");
			command.add("gif");
		} else {
			command.add("-ss");
			command.add("4");
			command.add("-t");
			command.add("0.001");
			command.add("-f");
			command.add("image2");
		}
//		if (StringUtils.isNotBlank(width) && StringUtils.isNotBlank(height)){
//			command.add("-s");
//			command.add((width + "x" + height));
//		}
		command.add("-y");
		command.add(imgFile);
		return process(command);
	}
	
	/**
	 * ffmpeg能解析转换视频
	 * @param inputFile （asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
	 * @return boolean  
	 */
	private boolean processFfmpeg(String inputFile, String outputFile) {
		List<String> command = new java.util.ArrayList<String>();
		command.add(getFfmpegFile());
		command.add("-i");
		command.add(inputFile);
		command.add("-f");
		command.add(outputFileExtension);
		command.add("-c:v");
		command.add("libx264");
		command.add("-b:v");
		command.add("600k");
		command.add("-g");
		command.add("300");
		command.add("-bf");
		command.add("2");
		command.add("-c:a");
		command.add("aac");
		command.add("-strict");
		command.add("experimental");
		command.add("-ac");
		command.add("1");
		command.add("-ar");
		command.add("44100"); // 11025 22050 32000 44100
		command.add("-r");
		command.add("29.97");
		command.add("-qscale");
		command.add("6");
		if (StringUtils.isNotBlank(width) && StringUtils.isNotBlank(height)){
			command.add("-s");
			command.add((width + "x" + height));
		}
		command.add("-y");
		command.add(outputFile);
		return process(command);
	}

	/**
	 * 直接转换不需要转成avi在转换
	 * @return boolean  
	 */
	private boolean processMencoder(String inputFile, String outputFile) {
		List<String> command = new ArrayList<String>();
		command.add(getMencoderFile());
		command.add(inputFile);
		command.add("-oac");
		command.add("mp3lame");
		command.add("-lameopts");
		command.add("aq=7:vbr=2:q=6");
		command.add("-srate");
		command.add("44100");
		if (StringUtils.isNotBlank(width) && StringUtils.isNotBlank(height)){
			command.add("-vf");
			command.add(("scale=" + width + ":" + height + ",harddup"));
		}
		command.add("-ovc");
		command.add("xvid");
		command.add("-xvidencopts");
		command.add("fixed_quant=8");
		command.add("-of");
		command.add("lavf");
		command.add("-o");
		command.add(outputFile);
		return process(command);
	}

	/**
	 * 将mp4视频的元数据信息转到视频第一帧
	 * @return boolean  
	 */
	private boolean processQtFaststart(String inputFile, String outputFile) {
		List<String> command = new ArrayList<String>();
		command.add(getQtFaststartFile());
		command.add(inputFile);
		command.add(outputFile);
		return process(command);
	}

	/**
	 * 执行命令
	 * @param command
	 * @return boolean  
	 */
	private boolean process(List<String> command) {
		try {
			log.debug(ListUtils.convertToString(command, " "));
//			Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
			Process process = Runtime.getRuntime().exec(command.toArray(new String[command.size()]));
			new PrintErrorReader(process.getErrorStream()).start();
			new PrintInputStream(process.getInputStream()).start();
			process.waitFor();
			return true;
		} catch (Exception e) {
			if (StringUtils.contains(e.getMessage(), "CreateProcess error=2")){
				log.error("缺少视频转换工具，请配置video.ffmpegFile相关参数。" + e.getMessage());
			}else{
				log.error(e.getMessage(), e);
			}
			return false;
		}
	}

	public static String getFfmpegFile() {
		if (ffmpegFile == null){
			ffmpegFile = PropertiesUtils.getInstance().getProperty("video.ffmpegFile");
		}
		return ffmpegFile;
	}

	public static void setFfmpegFile(String ffmpegFile) {
		VideoUtils.ffmpegFile = ffmpegFile;
	}

	public static String getMencoderFile() {
		if (mencoderFile == null){
			mencoderFile = PropertiesUtils.getInstance().getProperty("video.mencoderFile");
		}
		return mencoderFile;
	}

	public static void setMencoderFile(String mencoderFile) {
		VideoUtils.mencoderFile = mencoderFile;
	}

	public static String getQtFaststartFile() {
		if (qtFaststartFile == null){
			qtFaststartFile = PropertiesUtils.getInstance().getProperty("video.qtFaststartFile");
		}
		return qtFaststartFile;
	}

	public static void setQtFaststartFile(String qtFaststartFile) {
		VideoUtils.qtFaststartFile = qtFaststartFile;
	}

	public String getInputFile() {
		return inputFile;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = FileUtils.path(inputFile);
	}

	public String getInputFileExtension() {
		return inputFileExtension;
	}

	public void setInputFileExtension(String inputFileExtension) {
		this.inputFileExtension = inputFileExtension;
	}
	
	public String getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = FileUtils.path(outputFile);
	}

	public String getOutputFileExtension() {
		return outputFileExtension;
	}

	public void setOutputFileExtension(String outputFileExtension) {
		this.outputFileExtension = outputFileExtension;
	}

	public String getImgFile() {
		return imgFile;
	}

	public void setImgFile(String imgFile) {
		this.imgFile = imgFile;
	}

	public String getImgFileExtension() {
		return imgFileExtension;
	}

	public void setImgFileExtension(String imgFileExtension) {
		this.imgFileExtension = imgFileExtension;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	class PrintInputStream extends Thread {
		java.io.InputStream __is = null;

		public PrintInputStream(java.io.InputStream is) {
			__is = is;
		}

		@Override
		public void run() {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(__is));
				String line = null;
				while ((line = br.readLine()) != null) {
					log.debug(line);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class PrintErrorReader extends Thread {
		java.io.InputStream __is = null;

		public PrintErrorReader(java.io.InputStream is) {
			__is = is;
		}

		@Override
		public void run() {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(__is));
				String line = null;
				while ((line = br.readLine()) != null) {
					log.error(line);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

//	public static void main(String[] args) {
////		VideoUtils.setFfmpegFile("d:/tools/video/ffmpeg-4.9/bin/ffmpeg.exe");
//		VideoUtils.setFfmpegFile("d:/tools/video/libav-10.6-win64/bin/avconv.exe");
//		VideoUtils.setMencoderFile("d:/tools/video/mencoder-4.9/mencoder.exe");
//		VideoUtils.setQtFaststartFile("d:/tools/video/qt-faststart/qt-faststart.exe");
//		final VideoUtils v = new VideoUtils("e:/Users/Administrator/Desktop/mp4/20150001.mp4");
////		new Thread(new Runnable() {
////			@Override
////			public void run() {
//				System.out.println("img：转换" + (v.cutPic()?"成功":"失败"));
//				System.out.println("file：转换" + (v.convert()?"成功":"失败"));
////			}
////		}).start();
//		System.out.println("inputFile: " + v.getInputFile());
//		System.out.println("imageFile: " + v.getImgFile());
//		System.out.println("outputFile: " + v.getOutputFile());
//	}

}
