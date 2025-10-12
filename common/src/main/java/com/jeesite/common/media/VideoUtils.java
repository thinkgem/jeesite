package com.jeesite.common.media;

import com.jeesite.common.image.ImageUtils;
import com.jeesite.common.io.FileUtils;
import com.jeesite.common.io.PropertiesUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.lang.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 视频工具类
 * @author ThinkGem
 * @version 2015-5-13
 */
public class VideoUtils {

	private static final Logger logger = LoggerFactory.getLogger(VideoUtils.class);
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
				logger.error("视频剪切图片失败", e);
			}
		}
		logger.debug("视频剪切图片" + (statusTemp ? "成功" : "失败") + "，用时：" + TimeUtils.formatTime(System.currentTimeMillis() - startTime));
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
			logger.debug("使用ffmpage进行视频转换");
			statusTemp = processFfmpeg(inputFile, tempFile);
		} else if (statusTemp && type == 1) {
			logger.debug("使用mencoder进行视频转换");
			statusTemp = processMencoder(inputFile, tempFile);
		}
		if (statusTemp){
			logger.debug("将mp4视频的元数据信息转到视频第一帧");
			statusTemp = processQtFaststart(tempFile, outputFile);
		}
		logger.debug("删除临时文件");
		FileUtils.deleteFile(tempFile);
		logger.debug("视频转换{}，用时：{}", statusTemp ? "成功" : "失败", TimeUtils.formatTime(System.currentTimeMillis() - startTime));
		return statusTemp;
	}

	/**
	 * 检查文件是否存在
	 * @return boolean
	 */
	public boolean checkfile(String inputFile) {
		File file = new File(inputFile);
		if (!file.isFile() || !file.exists()) {
			logger.warn("文件不存在！");
			return false;
		}
		return true;
	}

	/**
	 * ffmpeg 截取缩略图
	 * @return boolean
	 */
	public boolean processFfmpegCutpic(String inputFile, String outputFile) {
		List<String> command = new java.util.ArrayList<String>();
		command.add(getFfmpegFile());
		command.add("-i");
		command.add(inputFile);
		if ("gif".equalsIgnoreCase(imgFileExtension)) {
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
	 * @return boolean
	 */
	private boolean process(List<String> command) {
		try {
			logger.debug(StringUtils.join(command, StringUtils.SPACE));
//			Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
			Process process = Runtime.getRuntime().exec(command.toArray(new String[0]));
			new PrintErrorReader(process.getErrorStream()).start();
			new PrintInputStream(process.getInputStream()).start();
			process.waitFor();
			return true;
		} catch (Exception e) {
			if (StringUtils.contains(e.getMessage(), "CreateProcess error=2")){
				logger.error("缺少视频转换工具，请配置video.ffmpegFile相关参数。{}", e.getMessage());
			}else{
				logger.error(e.getMessage(), e);
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

	static class PrintInputStream extends Thread {
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
					logger.debug(line);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	static class PrintErrorReader extends Thread {
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
					logger.error(line);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

}
