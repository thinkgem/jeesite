package com.jeesite.test.media;

import com.jeesite.common.media.VideoUtils;

/**
 * 视频工具测试类
 * @author ThinkGem
 * @version 2023-6-9
 */
public class VideoUtilsTest {

	public static void main(String[] args) {
//		VideoUtils.setFfmpegFile("e:/jeesite/tools/video/ffmpeg-4.9/bin/ffmpeg.exe");
		VideoUtils.setFfmpegFile("e:/jeesite/tools/video/libav-10.6-win64/bin/avconv.exe");
		VideoUtils.setMencoderFile("e:/jeesite/tools/video/mencoder-4.9/mencoder.exe");
		VideoUtils.setQtFaststartFile("e:/jeesite/tools/video/qt-faststart/qt-faststart.exe");
		final VideoUtils v = new VideoUtils("d:/dfdf.mp4");
		new Thread(() -> {
			System.out.println("img：转换" + (v.cutPic()?"成功":"失败"));
			System.out.println("file：转换" + (v.convert()?"成功":"失败"));
		}).start();
		System.out.println("inputFile: " + v.getInputFile());
		System.out.println("imageFile: " + v.getImgFile());
		System.out.println("outputFile: " + v.getOutputFile());
	}

}
