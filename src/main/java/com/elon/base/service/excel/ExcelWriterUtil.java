package com.elon.base.service.excel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Excel数据写入工具类
 *
 * @author neo
 * @since 2025/3/7
 * @version 1.0
 */
public class ExcelWriterUtil {
    private static final Logger LOGGER = LogManager.getLogger(ExcelWriterUtil.class);

    /**
     * 文件路径
     */
    private String filePath = "";

    private SXSSFWorkbook sworkBook = null;

    private Sheet curSheet = null;

    /**
     * 构造方法
     *
     * @param filePath excel文件路径
     * @author neo
     * @since 2025/3/7
     */
    public ExcelWriterUtil(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 往excel中批量写入数据
     * @param sheetNo sheet页编号，从1开始
     * @param startRow 开始写数据的起始行
     * @param dataList 数据列表
     * @return true-写入成功; false-写入失败
     * @throws FileNotFoundException
     */
    public <T> boolean writeData(int sheetNo, int startRow, List<List<T>> dataList) throws FileNotFoundException {
        //1、打开excel文件
        openExcel();

        //2、选择写入数据的sheet页
        boolean result = selectSheet(sheetNo);
        if (!result) {
            return false;
        }

        //3、逐行写入数据
        for(List<T> data : dataList) {
            writeSingleRowData(startRow++, data);
        }

        //4、保存文件，关闭流
        saveExcel();
        return true;
    }

    /**
     * 写一行数据到Excel中
     * @param rowNum 行号
     * @param valueList 一行内容
     * @return true:写成功; false：写失败
     */
    private <T> boolean writeSingleRowData(int rowNum, List<T> valueList)
    {
        Row row = curSheet.getRow(rowNum);
        if (row == null) {
            row = curSheet.createRow(rowNum);
        }

        if (row == null) {
            return false;
        }

        for (int cellNum = 0; cellNum < valueList.size(); ++cellNum) {
            Cell cell = row.getCell(cellNum);
            if (cell == null) {
                cell = row.createCell(cellNum);
            }

            if (cell == null) {
                continue;
            }

            T value = valueList.get(cellNum);
            cell.setCellValue(String.valueOf(value));
        }

        return true;
    }

    /**
     * 保存数据到Excel文件
     */
    private void saveExcel() throws FileNotFoundException {
        FileOutputStream out = new FileOutputStream(filePath);
        try {
            sworkBook.write(out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sworkBook.dispose();
    }


    /**
     * 打开excel文件
     *
     * @author neo
     * @since 2025/3/7
     */
    private void openExcel() throws FileNotFoundException {
        FileInputStream inFile = new FileInputStream(filePath);
        try {
            XSSFWorkbook workBook = new XSSFWorkbook(inFile);
            sworkBook = new SXSSFWorkbook(workBook, 100);
        } catch (Exception e) {
            LOGGER.error("Open excel fail. filePath:{}", filePath);
        }
        finally {
            try {
                inFile.close();
            } catch (IOException e) {
                LOGGER.error("Close file stream fail. filePath:{}", filePath);
            }
        }
    }

    /**
     * 选择指定的Sheet页，作为输入/读取。
     *
     * @param sheetId sheet唯一标示。名称或者索引
     * @return true:获取成功；false:获取失败
     */
    private <T> boolean selectSheet(T sheetId) {
        if (sheetId instanceof String) {
            curSheet = sworkBook.getSheet(sheetId.toString());
            return true;
        } else if (sheetId instanceof Integer) {
            curSheet = sworkBook.getSheetAt(((Integer) sheetId).intValue());
            return true;
        } else {
            return false;
        }
    }
}
