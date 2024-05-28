package com.jeesite.common.ueditor.upload;

import com.jeesite.common.config.Global;
import com.jeesite.common.image.ImageUtils;
import com.jeesite.common.io.FileUtils;
import com.jeesite.common.media.VideoUtils;
import com.jeesite.common.ueditor.PathFormat;
import com.jeesite.common.ueditor.define.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BinaryUploader {

    public static final State save(HttpServletRequest request, Map<String, Object> conf) {
        String contentType = request.getContentType();
        if (!("POST".equals(request.getMethod()) && contentType != null
                && contentType.startsWith("multipart/"))) {
            return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
        }
        try {
            MultipartFile file = null;
            if (request instanceof MultipartHttpServletRequest) {
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                Iterator<String> it = multiRequest.getFileNames();
                while (it.hasNext()) {
                    MultipartFile f = multiRequest.getFile(it.next());
                    if (f != null && !f.isEmpty() && f.getOriginalFilename() != null) {
                        file = f;
                    }
                    break;
                }
            }
            if (file == null) {
                return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
            }

            String savePath = (String) conf.get("savePath");
            String originFileName = file.getOriginalFilename();
            String suffix = FileType.getSuffixByFilename(originFileName);

            originFileName = originFileName.substring(0,
                    originFileName.length() - suffix.length());
            savePath = savePath + suffix;

            long maxSize = ((Long) conf.get("maxSize")).longValue();

            if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
                return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
            }

            savePath = PathFormat.parse(savePath, originFileName);

            String physicalPath = FileUtils.path(conf.get("rootPath") + savePath);

            InputStream is = null;
            State storageState = null;
            try {
                is = file.getInputStream();
                storageState = StorageManager.saveFileByInputStream(is, physicalPath, maxSize);
            } finally {
                if (is != null) {
                    is.close();
                }
            }

            if (storageState != null && storageState.isSuccess()) {
                int actionCode = ((Integer) conf.get("actionCode")).intValue();
                String ctxPath = Global.getCtxPath(); // ThinkGem 修正上传图片后返回无contextpath问题

                // 上传图片后，进行图片压缩
                if (actionCode == ActionMap.UPLOAD_IMAGE) {

                    // 如果开启了压缩图片
                    if ((Boolean) conf.get("imageCompressEnable")) {
                        Integer maxWidth = (Integer) conf.get("imageCompressBorder");
                        ImageUtils.thumbnails(new File(physicalPath), maxWidth, -1, null);
                    }

                }

                // 上传成功后 转换格式 按照新的视频格式 返回前台 ThinkGem
                else if (actionCode == ActionMap.UPLOAD_VIDEO) {
                    final VideoUtils v = new VideoUtils(physicalPath);
                    // 先截图
                    if (v.cutPic()) {
                        // 开启进程，在转换视频文件
                        Thread thread = new Thread("video-convert") {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(5000);
                                    v.convert();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        thread.setDaemon(true);
                        thread.start();
                        storageState.putInfo("url", ctxPath + PathFormat.format(savePath) + "." + v.getOutputFileExtension());
                        storageState.putInfo("type", "." + v.getOutputFileExtension());
                        storageState.putInfo("original", originFileName + "." + v.getInputFileExtension());

                        // Ueditor编辑器上传文件完成后调用事件
                        StorageManager.uploadFileSuccess(physicalPath, storageState);

                        return storageState;
                    }
                }
                storageState.putInfo("url", ctxPath + PathFormat.format(savePath));
                storageState.putInfo("type", suffix);
                storageState.putInfo("original", originFileName + suffix);

                // UEditor上传文件成功后调用事件
                StorageManager.uploadFileSuccess(physicalPath, storageState);
            }
            return storageState;
        } catch (IOException e) {
            return new BaseState(false, AppInfo.IO_ERROR);
        }
    }

    private static boolean validType(String type, String[] allowTypes) {
        List<String> list = Arrays.asList(allowTypes);
        return list.contains(type);
    }

}
