package com.elon.base.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件基本操作工具类
 *
 * @author neo
 * @since 2025-03-06
 */
public class FileUtil {
    private static final Logger LOGGER = LogManager.getLogger(FileUtil.class);

    /** 
     * 删除文件
     *
     * @param filePath 文件路径
     * @author neo
     * @since 2025/3/11
     */ 
    public static void deleteFile(String filePath){
        try {
            Path path = Paths.get(filePath);
            Files.deleteIfExists(path);
        } catch (Exception e) {
            LOGGER.error("Delete file fail. filePath:{}", filePath);
        }
    }

    /**
     * 下载本地文件。将文件内容写到http响应结果
     *
     * @param fileLocalPath 文件在本地磁盘的路径
     * @param fileName 文件名称(下载到客户端显示的名称)
     * @param response http响应
     * @author neo
     */
    public static void downloadLocalFile(String fileLocalPath, String fileName, HttpServletResponse response){
        // 设置文件名称
        try {
            response.setContentType("application/vnd.ms-excel; charset=UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));  // 附件下载
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 读入文件内容
         */
        byte[] fileContent = new byte[100];
        BufferedInputStream inputStream = null;
        BufferedOutputStream outputStream = null;
        try {
            File file = new File(fileLocalPath);
            inputStream = new BufferedInputStream(new FileInputStream(file));
            outputStream = new BufferedOutputStream(response.getOutputStream());

            // 读取文件能用写入响应结构的输出流
            int length = 0;
            while ((length = inputStream.read(fileContent)) != -1){
                outputStream.write(fileContent, 0, length);
            }
            outputStream.flush();
        } catch (Exception e) {
            LOGGER.error("Read file fail. fileLocalPath:{}", fileLocalPath, e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }

            } catch (Exception e) {
                LOGGER.error("Close file fail. fileLocalPath:{}", fileLocalPath, e);
            }
        }
    }
}
