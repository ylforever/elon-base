package com.elon.base.service.excel;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStrings;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * Excel读取公共类。
 *
 * @author neo
 * @version 2018年7月7日
 */
public class ExcelReaderUtil extends ExcelAbstract {
    private static final Logger LOGGER = LogManager.getLogger(ExcelAbstract.class);

    /**
     * 提取列名称的正则表达式
     */
    private static final String DISTILL_COLUMN_REG = "^([A-Z]{1,})";

    /**
     * 读取excel的每一行记录。map的key是列号(A、B、C...), value是单元格的值。如果单元格是空，则没有值。
     */
    private List<Map<String, String>> dataList = new ArrayList<>();

    /**
     * 读取指定名称的sheet页数据
     *
     * @param filePath excel文件路径
     * @param sheetName sheet页名称
     * @author neo
     */
    public void readOneSheetByName(String filePath, String sheetName) {
        OPCPackage pkg = null;
        InputStream sheet = null;
        try {
            pkg = OPCPackage.open(filePath);
            XSSFReader r = new XSSFReader(pkg);
            SharedStrings sst = r.getSharedStringsTable();
            XMLReader parser = getSheetParser(sst);

            // 根据 rId# 或 rSheet# 查找sheet
            XSSFReader.SheetIterator iterator = (XSSFReader.SheetIterator)r.getSheetsData();
            while (iterator.hasNext()) {
                sheet = iterator.next();
                String sn = iterator.getSheetName();
                if (sheetName.equals(sn)) {
                    break;
                }
            }

            if (sheet == null) {
                LOGGER.error("Invalid sheet name. sheetName:" + sheetName);
                return;
            }
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
        } catch (Exception e1) {
            LOGGER.error("Read excel fail. filePath:" + filePath);
        } finally {
            try {
                if (sheet != null) {
                    sheet.close();
                }
                if (pkg != null) {
                    pkg.close();
                }
            } catch (Exception e2) {
                LOGGER.error("Close excel fail. filePath:" + filePath);
            }
        }
    }

    /**
     * 读取指定ID编码的sheet页数据
     *
     * @param filePath excel文件路径
     * @param sheetId sheet页ID编码, 从1开始
     * @author neo
     */
    public void readOneSheetById(String filePath, int sheetId) {
        readOneSheet(filePath, "rId" + sheetId);
    }

    /**
     * 读取Excel指定sheet页的数据。注意：如果excel的sheet页删除过，sheetNum会发生变化.
     *
     * @param filePath 文件路径
     * @param sheetId sheet页编号. 例如：rId2。
     */
    private void readOneSheet(String filePath, String sheetId) {
        OPCPackage pkg = null;
        InputStream sheet = null;
        try {
            pkg = OPCPackage.open(filePath);
            XSSFReader r = new XSSFReader(pkg);
            SharedStrings sst = r.getSharedStringsTable();
            XMLReader parser = getSheetParser(sst);

            // 根据 rId# 或 rSheet# 查找sheet
            sheet = r.getSheet(sheetId);
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
        } catch (Exception e1) {
            LOGGER.error("Read excel fail. filePath:" + filePath);
        } finally {
            try {
                if (sheet != null) {
                    sheet.close();
                }
                if (pkg != null) {
                    pkg.close();
                }
            } catch (Exception e2) {
                LOGGER.error("Close excel fail. filePath:" + filePath);
            }
        }
    }

    @Override
    public void optRows(int curRow, Map<String, String> rowValueMap) {

        Map<String, String> dataMap = new HashMap<>();
        rowValueMap.forEach((k,v)->dataMap.put(removeNum(k), v));
        dataList.add(dataMap);
    }

    /**
     * 日期数字转换为字符串。
     *
     * @param dateNum excel中存储日期的数字
     * @return 格式化后的字符串形式
     */
    public static String dateNum2Str(String dateNum) {
        Date date = DateUtil.getJavaDate(Double.parseDouble(dateNum));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    /**
     * 删除单元格名称中的数字，只保留列号。
     * @param cellName 单元格名称。如:A1
     * @return 列号。如：A
     */
    private String removeNum(String cellName) {
        Pattern pattern = Pattern.compile(DISTILL_COLUMN_REG);
        Matcher m = pattern.matcher(cellName);
        if (m.find()) {
            return m.group(1);
        }

        return "";
    }

    public List<Map<String, String>> getDataList() {
        return dataList;
    }
}

