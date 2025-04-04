package com.elon.base.service.file;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件操作基础工具类
 *
 * @author neo
 * @version 2023-12-16
 */
@Component
public class FileUtil {
    private static final Logger LOGGER = LogManager.getLogger(FileUtil.class);

    /**
     * 读取 resource下文件内容
     *
     * @param resourceFilePath resource下文件相对路径. 例如: dbscript/123.sql
     * @return 文件内容列表
     */
    public List<String> readFileInResource(String resourceFilePath) {
        List<String> lines = new ArrayList<>();
        InputStream inputStream = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource(resourceFilePath);
            inputStream = classPathResource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (Exception e) {
            LOGGER.error("Read file fail. resourceFilePath:" + resourceFilePath, e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.error("Close file fail.", e);
                }
            }
        }

        return lines;
    }
}
