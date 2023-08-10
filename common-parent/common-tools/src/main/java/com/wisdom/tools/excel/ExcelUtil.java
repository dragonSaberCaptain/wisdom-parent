/*
 * Copyright (c) 2023 dragonSaberCaptain.All rights reserved.
 * 当前项目名:wisdom-parent
 * 当前模块名:common-tools
 * 当前文件的权限定名:com.wisdom.tools.excel.ExcelUtil
 * 当前文件的名称:ExcelUtil.java
 * 当前文件的类名:ExcelUtil
 * 上一次文件修改的日期时间:2023/8/10 下午4:27
 *
 */

package com.wisdom.tools.excel;


import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class ExcelUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
    //每页数据总量
    private static final int SHEETSIZE = 5000;
    // 下拉限制开始行
    private static final int FROMROW = 1;
    // 下拉限制结束行行
    private static final int ENDROW = 1000;

    public static void main(String[] args) throws Exception {
        File file = new File("D:\\data\\file\\excel1.xls");
        FileOutputStream outputStream = new FileOutputStream(file);
//        HSSFWorkbook workbook = new HSSFWorkbook(); //----xls
//        XSSFWorkbook workbook = new XSSFWorkbook(); //----xlsx
        SXSSFWorkbook workbook = new SXSSFWorkbook(); //----xlsx

        Sheet excelSheet = workbook.createSheet("excel");

        //demo 单独下拉列表
//        addValidationToSheet(workbook, excelSheet, Arrays.asList("百度", "阿里"), "D", FROMROW, ENDROW);

        //demo 级联下拉列表
        Map<String, List<String>> data = new HashMap<>();
        data.put("百度系列", Arrays.asList("百度地图", "百度知道", "百度音乐"));
        data.put("阿里系列", Arrays.asList("淘宝", "支付宝", "钉钉"));
        addValidationToSheet(workbook, excelSheet, data, "B", "C", FROMROW, ENDROW);

        //demo 自动填充
        Map<String, String> kvs = new HashMap<>();
        kvs.put("百度系列", "www.baidu.com");
        kvs.put("阿里系列", "www.taobao.com");
        addAutoMatchValidationToSheet(workbook, excelSheet, kvs, "B", "D", FROMROW, ENDROW);

        // 隐藏存储下拉列表数据的sheet；可以注释掉该行以便查看、理解存储格式
        //hideTempDataSheet(workbook, 1);

        workbook.write(outputStream);
        outputStream.close();
    }

    /**
     * excel 导出
     *
     * @param excelData    导出数据
     * @param outputStream 输出流
     * @author created by captain on 2021-07-22 10:15:44
     */
    public static void listToExecl(ExcelData excelData, OutputStream outputStream) {
        Workbook workbook = createWorkbook(excelData.getFileType());

        // 设置单元格格式为文本格式
        CellStyle textStyle = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@"));
        try {
            if (excelData.isTemplate()) {
                Sheet sheet = workbook.createSheet();
                Row row = sheet.createRow(0);

                List<String> firstCell = excelData.getFirstCell();
                List<ExcelTemplateData> dataOptions = excelData.getDataOptions();

                Map<String, String> tmpMap = new HashMap<>();

                for (int j = 0; j < firstCell.size(); j++) {
                    String tmpCellValue = String.valueOf(firstCell.get(j));
                    //获取对应的字母
                    String strCol = excelColIndexToStr(j);

                    //设置单元格格式为"文本"
                    sheet.setDefaultColumnStyle(j, textStyle);

                    // 在sheet的第一行中，添加字段名
                    Cell cell = row.createCell(j);
                    cell.setCellValue(tmpCellValue);

                    tmpMap.put(tmpCellValue, strCol);
                }

                for (String tmpCellValue : firstCell) {
                    String strCol = tmpMap.get(tmpCellValue);
                    //该列存在下拉
                    List<ExcelTemplateData> dataList = dataOptions.stream().filter(sub -> sub.getKey().equals(tmpCellValue)).collect(Collectors.toList());
                    for (ExcelTemplateData templateData : dataList) {
                        //判断是普通下拉还是级联下拉
                        if ("3".equals(templateData.getOptionsType())) {
                            addAutoMatchValidationToSheet(workbook, sheet, templateData.getOptionsData(), strCol, tmpMap.get(templateData.getSubKeyName()));
                        } else if ("2".equals(templateData.getOptionsType())) {
                            addValidationToSheet(workbook, sheet, templateData.getOptionsData(), strCol, tmpMap.get(templateData.getSubKeyName()));
                        } else if ("1".equals(templateData.getOptionsType())) {
                            addValidationToSheet(workbook, sheet, templateData.getOptionsData(), strCol);
                        }
                    }
                }

                //隐藏所有存储下拉列表的工作表名带hide的sheet
                hideTempDataSheet(workbook);
            } else {
                List<Object> data = excelData.getData();
                Map<String, String> fields = excelData.getFields();

                // 根据data计算有多少页sheet
                int pages = data.size() / SHEETSIZE;
                if (data.size() % SHEETSIZE > 0) {
                    pages += 1;
                }
                logger.info("导出的excel总的sheet页数:" + pages);

                // 添加数据
                for (int i = 0; i < pages; i++) {
                    int rowNum = 0;
                    // 计算每页的起始数据和结束数据
                    int startIndex = i * SHEETSIZE;
                    int endIndex = Math.min((i + 1) * SHEETSIZE - 1, data.size());

                    // 创建每页，并创建第一行
                    Sheet sheet = workbook.createSheet();

                    Row row = sheet.createRow(rowNum);

                    List<String> cnTitles = new ArrayList<>();
                    List<String> enTitles = new ArrayList<>();
                    if (fields != null) {
                        // 提取表格的字段名（英文字段名是为了对照中文字段名的）
                        cnTitles = new ArrayList<>(fields.keySet());
                        enTitles = new ArrayList<>(fields.values());
                    }
                    //取出第一个对象
                    Object item = data.get(0);
                    if (cnTitles.size() > 0) {
                        // 在每页sheet的第一行中，添加字段名
                        for (int f = 0; f < cnTitles.size(); f++) {
                            Cell cell = row.createCell(f);
                            cell.setCellValue(cnTitles.get(f));
                        }
                    } else {
                        Field[] declaredFields = item.getClass().getDeclaredFields();
                        for (int k = 0; k < declaredFields.length; k++) {
                            declaredFields[k].setAccessible(true);
                            Cell cell = row.createCell(k);
                            cell.setCellValue(declaredFields[k].getName());
                        }
                    }
                    rowNum += 1;

                    // 将数据添加进表格
                    for (int j = startIndex; j < endIndex; j++) {
                        row = sheet.createRow(rowNum);

                        item = data.get(j);
                        if (item instanceof Map) {
                            for (int h = 0; h < enTitles.size(); h++) {
                                Map<String, Object> map = (Map) item;
                                Object o = map.get(enTitles.get(h));
                                String value = o == null ? "" : String.valueOf(o);

                                //设置单元格格式为"文本"
                                sheet.setDefaultColumnStyle(h, textStyle);

                                Cell cell = row.createCell(h);
                                cell.setCellValue(value);
                            }
                        } else {
                            Field[] declaredFields = item.getClass().getDeclaredFields();
                            if (enTitles.size() > 0) {
                                for (int k = 0; k < enTitles.size(); k++) {
                                    String subEn = enTitles.get(k);
                                    for (Field declaredField : declaredFields) {
                                        declaredField.setAccessible(true);
                                        String subName = declaredField.getName().toLowerCase();
                                        if (subEn.equalsIgnoreCase(subName)) {
                                            Object o = declaredField.get(item);
                                            String value = o == null ? "" : String.valueOf(o);

                                            //设置单元格格式为"文本"
                                            sheet.setDefaultColumnStyle(k, textStyle);

                                            Cell cell = row.createCell(k);
                                            cell.setCellValue(value);
                                        }
                                    }
                                }
                            } else {
                                for (int k = 0; k < declaredFields.length; k++) {
                                    declaredFields[k].setAccessible(true);
                                    String subName = declaredFields[k].getName().toLowerCase();
                                    if (enTitles.contains(subName)) {
                                        Object o = declaredFields[k].get(item);
                                        String value = o == null ? "" : String.valueOf(o);

                                        //设置单元格格式为"文本"
                                        sheet.setDefaultColumnStyle(k, textStyle);

                                        Cell cell = row.createCell(k);
                                        cell.setCellValue(value);
                                    }
                                }
                            }
                        }
                        rowNum += 1;
                    }
                }
            }
            // 将创建好的数据写入输出流
            workbook.write(outputStream);
        } catch (Exception e) {
            logger.error("ExcelUtil--listToExecl", e);
            e.printStackTrace();
        } finally {
            try {
                // 关闭workbook
                workbook.close();
                // 关闭输出流
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                logger.error("IO异常,关闭失败", e);
                e.printStackTrace();
            }
        }
    }

    public static <T> List<T> execlToList(ExcelData excelData, Class<T> entityClazz, String fileFullPath) {
        return execlToList(excelData, entityClazz, new File(fileFullPath));
    }

    public static <T> List<T> execlToList(ExcelData excelData, Class<T> entityClazz, File file) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return execlToList(excelData, entityClazz, inputStream);
    }

    /**
     * excel 导入
     *
     * @param excelData   导入数据
     * @param entityClazz 要转换的类
     * @param inputStream 输入流
     * @return java.util.List<T>
     * @author created by captain on 2021-07-22 10:16:51
     */
    public static <T> List<T> execlToList(ExcelData excelData, Class<T> entityClazz, InputStream inputStream) {
        Workbook workbook = createWorkbook(excelData.getFileType(), inputStream);
        List<T> resultList = new ArrayList<>();
        try {
            // 得到excel中sheet总数
            int sheetcount = workbook.getNumberOfSheets();
            if (sheetcount == 0) {
                logger.error("Excel文件中没有任何数据");
                throw new Exception("Excel文件中没有任何数据");
            }

            Map<String, String> fields = excelData.getFields();

            // 数据的导出
            for (int i = 0; i < sheetcount; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                if (workbook instanceof SXSSFWorkbook) {
                    XSSFWorkbook xssfWorkbook = ((SXSSFWorkbook) workbook).getXSSFWorkbook();
                    sheet = xssfWorkbook.getSheetAt(i);
                }
                if (sheet == null) {
                    continue;
                }
                //跳过隐藏的下拉Sheet
                if (sheet.getSheetName().contains("hide")) {
                    continue;
                }
                Row firstRow = sheet.getRow(excelData.getStartRowIndex());

                int cellLength = firstRow.getLastCellNum();
                List<String> excelFieldNameList = new ArrayList<>();

                LinkedHashMap<String, Integer> colMap = new LinkedHashMap<>();

                //获取Excel中的列名
                for (int f = 0; f < cellLength; f++) {
                    Cell cell = firstRow.getCell(f);
                    String fieldName = String.valueOf(cell).trim();
                    if (!"".equals(fieldName)) {
                        excelFieldNameList.add(fieldName);
                        //将列名和列号放入Map中,这样通过列名就可以拿到列号
                        colMap.put(fieldName, f);
                    }
                }

                //判断需要的字段在Excel中是否都存在
                for (String cnName : fields.keySet()) {
                    if (!excelFieldNameList.contains(cnName)) {
                        //如果有列名不存在，则抛出异常，提示错误
                        logger.error("Excel中缺少必要的字段，或字段名称有误:" + cnName);
                        throw new Exception("Excel中缺少必要的字段，或字段名称有误:" + cnName);
                    }
                }

                // 将sheet转换为list
                for (int j = excelData.getStartRowIndex() + 1; j <= sheet.getLastRowNum(); j++) {
                    Row row = sheet.getRow(j);

                    //先判断当前行是否有数据
                    boolean isNull = true;
                    for (int k = row.getFirstCellNum(); k < row.getLastCellNum(); k++) {
                        Cell cell = row.getCell(k);
                        Object cellValue = getCellValue(cell);
                        if (null != cellValue && !"".equals(cellValue)) {
                            isNull = false;
                            break;
                        }
                    }
                    if (isNull) {
                        break;
                    }

                    // 根据泛型创建实体类
                    T entity = entityClazz.getDeclaredConstructor().newInstance();
                    // 给对象中的字段赋值
                    for (Map.Entry<String, String> entry : fields.entrySet()) {
                        // 获取中文字段名
                        String cnNormalName = entry.getKey();
                        // 获取英文字段名
                        String enNormalName = entry.getValue();
                        // 根据中文字段名获取列号
                        int colIndex = colMap.get(cnNormalName);
                        Cell cell = row.getCell(colIndex);
                        // 获取当前单元格中的内容
                        Object tmpValue = getCellValue(cell);
                        if (null == tmpValue || "".equals(tmpValue)) {
                            continue;
                        }
                        // 给对象赋值
                        setFieldValueByName(enNormalName, tmpValue, entity);
                    }
                    resultList.add(entity);
                }
            }
        } catch (Exception e) {
            logger.error("ExcelUtil--execlToList", e);
            e.printStackTrace();
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                logger.error("IO异常,关闭失败", e);
                e.printStackTrace();
            }
        }
        return resultList;
    }

    public static Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        Object tmpValue;
        switch (cell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    tmpValue = cell.getDateCellValue();
                } else {
                    tmpValue = doubleToString(cell);
                }
                break;
            case BOOLEAN:
                tmpValue = cell.getBooleanCellValue();
                break;
            case FORMULA:
                tmpValue = cell.getStringCellValue();
                break;
            default:
                tmpValue = cell.getStringCellValue();
                break;
        }
        return tmpValue;
    }

    public static String doubleToString(Cell cell) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 不显示千位分割符，否则显示结果会变成类似1,234,567,890
        numberFormat.setGroupingUsed(false);
        numberFormat.setMaximumFractionDigits(16); //设置小数部分最大位数
        return numberFormat.format(cell.getNumericCellValue());
    }

    /**
     * 根据字段名给对象的字段赋值
     *
     * @param fieldName  字段名
     * @param fieldValue 字段值
     * @param obj        对象
     * @author created by captain on 2021-07-22 10:17:49
     */
    private static void setFieldValueByName(String fieldName, Object fieldValue, Object obj) {
        Field field = getFieldByName(fieldName, obj.getClass());
        if (field == null) {
            logger.error(obj.getClass().getSimpleName() + "类不存在字段名 " + fieldName);
            return;
        }
        field.setAccessible(true);
        // 获取字段类型
        Class<?> fieldType = field.getType();

        String tmpValue = String.valueOf(fieldValue);

        try {
            // 根据字段类型给字段赋值
            if ((Byte.TYPE == fieldType) || (Byte.class == fieldType)) {
                field.set(obj, Byte.parseByte(tmpValue));
            } else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
                field.set(obj, Short.valueOf(tmpValue));
            } else if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
                field.set(obj, Integer.parseInt(tmpValue));
            } else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
                field.set(obj, Long.valueOf(tmpValue));
            } else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
                field.set(obj, Float.valueOf(tmpValue));
            } else if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
                field.set(obj, Double.valueOf(tmpValue));
            } else if (Character.TYPE == fieldType || Character.class == fieldType) {
                field.set(obj, tmpValue.charAt(0));
            } else if ((Boolean.TYPE == fieldType) || (Boolean.class == fieldType)) {
                field.set(obj, Boolean.parseBoolean(tmpValue));
            } else if (Date.class == fieldType) {
                try {
                    field.set(obj, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tmpValue));
                } catch (ParseException e) {
                    logger.info("Date类型的常规格式转换错误,尝试yyyyMMddHHmmss格式转换");
                    field.set(obj, new SimpleDateFormat("yyyyMMddHHmmss").parse(tmpValue));
                }
            } else if (ZonedDateTime.class == fieldType) {
                try {
                    field.set(obj, ZonedDateTime.parse(tmpValue, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault())));
                } catch (DateTimeParseException e) {
                    logger.info("ZonedDateTime类型常规格式转换错误,尝试yyyyMMddHHmmss格式转换");
                    field.set(obj, ZonedDateTime.parse(tmpValue, DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneId.systemDefault())));
                }
            } else if (LocalDateTime.class == fieldType) {
                try {
                    field.set(obj, LocalDateTime.parse(tmpValue, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                } catch (DateTimeParseException e) {
                    logger.info("LocalDateTime类型常规格式转换错误,尝试yyyyMMddHHmmss格式转换");
                    field.set(obj, LocalDateTime.parse(tmpValue, DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
                }
            } else if (String.class == fieldType) {
                field.set(obj, tmpValue);
            } else {
                field.set(obj, fieldValue);
            }
        } catch (Exception e) {
            logger.error("ExcelUtil--setFieldValueByName", e);
            e.printStackTrace();
        }
    }

    /**
     * 根据字段名获取字段
     *
     * @param fieldName 字段名
     * @param clazz     包含该字段的类
     * @return java.lang.reflect.Field 字段
     * @author created by captain on 2021-07-22 10:18:33
     */
    private static Field getFieldByName(String fieldName, Class<?> clazz) {
        // 拿到本类的所有字段
        Field[] selfFields = clazz.getDeclaredFields();

        // 如果本类中存在该字段，则返回
        for (Field field : selfFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        // 否则，查看父类中是否存在此字段，如果有则返回
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null && superClazz != Object.class) {
            return getFieldByName(fieldName, superClazz);
        }
        return null;
    }

    /**
     * 给sheet页，添加下拉列表
     *
     * @param workbook    excel文件，用于添加Name
     * @param targetSheet 级联列表所在sheet页
     * @param options     级联数据 例如：['百度','阿里巴巴']
     * @param column      下拉列表所在列 从'A'开始
     * @param fromRow     下拉限制开始行
     * @param endRow      下拉限制结束行
     * @author created by captain on 2021-07-22 10:19:26
     */
    public static void addValidationToSheet(Workbook workbook, Sheet targetSheet, Object options, String column, int fromRow, int endRow) {
        String hiddenSheetName = "hideSheet" + workbook.getNumberOfSheets();
        Sheet optionsSheet = workbook.createSheet(hiddenSheetName);
        String listFormula = column + "_hideParent";

        if (options instanceof List) {
            List tmpList = (List) options;
            int rowIndex = 0;
            for (Object option : tmpList) {
                Row row = optionsSheet.createRow(rowIndex++);
                Cell cell = row.createCell(0);
                cell.setCellValue(String.valueOf(option));
            }
            if (workbook.getName(listFormula) == null) {
                createName(workbook, listFormula, hiddenSheetName + "!$A$1:$A$" + tmpList.size());
            }
            int firstCol = getCharSumByStr(column) - 'A';
            int lastCol = getCharSumByStr(column) - 'A';

            addValidationData(targetSheet, listFormula, fromRow, endRow, firstCol, lastCol);
        }
    }

    public static void addValidationToSheet(Workbook workbook, Sheet targetSheet, Object options, String column) {
        addValidationToSheet(workbook, targetSheet, options, column, FROMROW, ENDROW);
    }

    /**
     * 给sheet页  添加级联下拉列表
     *
     * @param workbook    excel文件，用于添加Name
     * @param targetSheet 当前sheet页
     * @param optionsData 要添加的下拉列表内容,keys 是下拉列表1中的内容，每个Map.Entry.Value 是对应的级联下拉列表内容
     * @param keyColumn   下拉列表1位置
     * @param valueColumn 级联下拉列表位置
     * @param fromRow     级联限制开始行
     * @param endRow      级联限制结束行
     * @author created by captain on 2021-07-22 10:20:47
     */
    public static void addValidationToSheet(Workbook workbook, Sheet targetSheet, Object optionsData, String keyColumn, String valueColumn, int fromRow, int endRow) {
        String hiddenSheetName = "hideSheet" + workbook.getNumberOfSheets();
        Sheet hiddenSheet = workbook.createSheet(hiddenSheetName);
        List<Object> firstLevelItems = new ArrayList<>();

        if (optionsData instanceof Map) {
            Map<String, List<Object>> options = (Map) optionsData;
            int rowIndex = 0;
            for (Map.Entry<String, List<Object>> entry : options.entrySet()) {
                String entryKey = entry.getKey();
                firstLevelItems.add(entryKey);
                List<Object> children = entry.getValue();

                int columnIndex = 0;
                Row row = hiddenSheet.createRow(rowIndex++);
                Cell cell;

                for (Object child : children) {
                    cell = row.createCell(columnIndex++);
                    cell.setCellValue(String.valueOf(child));
                }

                char lastChildrenColumn = 'A';
                if (children.size() != 0) {
                    lastChildrenColumn = (char) ((int) 'A' + children.size() - 1);
                }
                createName(workbook, entryKey, hiddenSheetName + "!$A$" + rowIndex + ":$" + lastChildrenColumn + "$" + rowIndex);

                int firstCol = getCharSumByStr(valueColumn) - 'A';
                int lastCol = getCharSumByStr(valueColumn) - 'A';

                String listFormula;
                if (targetSheet instanceof HSSFSheet) {
                    listFormula = "INDIRECT(CONCATENATE(\"_\",$" + keyColumn + "1))";
                } else {
                    listFormula = "INDIRECT(CONCATENATE(\"_\",$" + keyColumn + "2))";
                }
                addValidationData(targetSheet, listFormula, fromRow, endRow, firstCol, lastCol);
            }
            addValidationToSheet(workbook, targetSheet, firstLevelItems, keyColumn, fromRow, endRow);
        }
    }

    public static void addValidationToSheet(Workbook workbook, Sheet targetSheet, Object optionsData, String keyColumn, String subCol) {
        addValidationToSheet(workbook, targetSheet, optionsData, keyColumn, subCol, FROMROW, ENDROW);
    }

    /**
     * 根据用户在keyColumn选择的key, 自动填充value到valueColumn
     *
     * @param workbook    excel文件，用于添加Name
     * @param targetSheet 当前sheet页
     * @param optionsData 匹配关系 {'百度','www.baidu.com'},{'淘宝','www.taobao.com'}
     * @param keyColumn   要匹配的列（例如 网站中文名称）
     * @param valueColumn 匹配到的内容列（例如 网址）
     * @param fromRow     下拉限制开始行
     * @param endRow      下拉限制结束行
     * @author created by captain on 2021-07-22 10:21:34
     */
    public static void addAutoMatchValidationToSheet(Workbook workbook, Sheet targetSheet, Object optionsData, String keyColumn, String valueColumn, int fromRow, int endRow) {
        String hiddenSheetName = "hideAutoSheet" + workbook.getNumberOfSheets();
        Sheet hiddenSheet = workbook.createSheet(hiddenSheetName);

        if (optionsData instanceof Map) {
            Map<String, Object> options = (Map) optionsData;
            // init the search region(A and B columns in hiddenSheet)
            int rowIndex = 0;
            for (Map.Entry<String, Object> kv : options.entrySet()) {
                Row totalSheetRow = hiddenSheet.createRow(rowIndex++);

                Cell cell = totalSheetRow.createCell(0);
                cell.setCellValue(kv.getKey());

                cell = totalSheetRow.createCell(1);
                cell.setCellValue(String.valueOf(kv.getValue()));
            }

            for (int i = fromRow; i <= endRow; i++) {
                Row totalSheetRow = targetSheet.getRow(i);
                if (totalSheetRow == null) {
                    totalSheetRow = targetSheet.createRow(i);
                }

                int colIndex = getCharSumByStr(valueColumn) - 'A';

                Cell cell = totalSheetRow.getCell(colIndex);
                if (cell == null) {
                    cell = totalSheetRow.createCell(colIndex);
                }

                String keyCell = keyColumn + (i + 1);
                String formula = String.format("IF(ISNA(VLOOKUP(%s,%s!A:B,2,0)),\"\",VLOOKUP(%s,%s!A:B,2,0))", keyCell, hiddenSheetName, keyCell, hiddenSheetName);
                cell.setCellFormula(formula);
            }
            // init the keyColumn as comboList
            addValidationToSheet(workbook, targetSheet, new ArrayList<>(options.keySet()), keyColumn, fromRow, endRow);
        }
    }

    public static void addAutoMatchValidationToSheet(Workbook workbook, Sheet targetSheet, Object optionsData, String keyColumn, String valueColumn) {
        addAutoMatchValidationToSheet(workbook, targetSheet, optionsData, keyColumn, valueColumn, FROMROW, ENDROW);
    }

    private static void addValidationData(Sheet targetSheet, String listFormula, int fromRow, int endRow, int firstCol, int lastCol) {
        DataValidationHelper helper = targetSheet.getDataValidationHelper();
        DataValidationConstraint provConstraint = helper.createFormulaListConstraint(listFormula);
        CellRangeAddressList provRangeAddressList = new CellRangeAddressList(fromRow, endRow, firstCol, lastCol);
        DataValidation provinceDataValidation = helper.createValidation(provConstraint, provRangeAddressList);
        targetSheet.addValidationData(provinceDataValidation);
    }

    /**
     * 添加name
     *
     * @param workbook 要添加的excel
     * @param nameName 要添加的名字
     * @param formula  公式
     * @return org.apache.poi.ss.usermodel.Name
     * @author created by captian on 2021-07-22 10:22:06
     */
    private static Name createName(Workbook workbook, String nameName, String formula) {
        nameName = formatNameName(nameName);
        Name name = workbook.createName();
        name.setNameName(nameName);
        name.setRefersToFormula(formula);
        return name;
    }

    /**
     * 隐藏excel中的sheet页
     *
     * @param workbook 要隐藏的excel
     * @param start    需要隐藏的 sheet开始索引
     * @author created by captian on 2021-07-22 10:23:42
     */
    private static void hideTempDataSheet(Workbook workbook, int start) {
        for (int i = start; i < workbook.getNumberOfSheets(); i++) {
            workbook.setSheetHidden(i, true);
        }
    }

    /**
     * 遍历并隐藏工作表名带hide的表
     *
     * @param workbook 要隐藏的excel
     * @author created by captain on 2021-07-19 10:02:45
     */
    private static void hideTempDataSheet(Workbook workbook) {
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            if (sheet.getSheetName().contains("hide")) {
                workbook.setSheetHidden(i, true);
            }
        }
    }

    /**
     * 不可数字开头
     *
     * @param name 名称
     * @return java.lang.String
     * @author created by captain on 2021-07-22 10:24:41
     */
    static String formatNameName(String name) {
        name = name.replaceAll(" ", "").replaceAll("-", "_").replaceAll(":", ".");
        String ch = "hideParent";
        if (!name.contains(ch)) {
            name = "_" + name;
        }
        return name;
    }

    /**
     * excel 列下标转字母
     *
     * @param columnIndex 要转换的下标
     * @return java.lang.String
     * @author created by captain on 2021-07-22 10:25:04
     */
    public static String excelColIndexToStr(int columnIndex) {
        if (columnIndex < 0) {
            return null;
        }
        StringBuilder columnStr = new StringBuilder();
        do {
            if (columnStr.length() > 0) {
                columnIndex--;
            }
            columnStr.insert(0, ((char) (columnIndex % 26 + (int) 'A')));
            columnIndex = ((columnIndex - columnIndex % 26) / 26);
        } while (columnIndex > 0);
        return columnStr.toString();
    }

    /**
     * excel 求字符串中所有字符之和
     *
     * @param colStr 要计算的字符串
     * @return int
     * @author created by captain on 2021-07-22 10:25:56
     */
    public static int getCharSumByStr(String colStr) {
        int sum = 0;
        for (int i = 0; i < colStr.length(); i++) {
            char tmpChar = colStr.charAt(i);
            sum += tmpChar;
        }
        return sum;
    }

    /**
     * excel 列字母转列下标
     *
     * @param colStr 要转换的列字母
     * @return int
     * @author created by captain on 2021-07-22 10:26:50
     */
    public static int excelColStrToNum(String colStr) {
        int num = 0;
        int result = 0;
        for (int i = 0; i < colStr.length(); i++) {
            char ch = colStr.charAt(colStr.length() - i - 1);
            num = (ch - 'A' + 1);
            num *= Math.pow(26, i);
            result += num;
        }
        return result;
    }

    public static Workbook createWorkbook(String fileType) {
        return createWorkbook(fileType, null);
    }

    /**
     * 根据excel类型创建workbook
     *
     * @param fileType 文件类型
     * @param in       输入流
     * @return org.apache.poi.ss.usermodel.Workbook
     * @author created by captain on 2021-07-22 10:29:17
     */
    public static Workbook createWorkbook(String fileType, InputStream in) {
        Workbook workbook = null;
        try {
            if (fileType.contains("xlsx")) {
                if (in != null) {
                    workbook = new SXSSFWorkbook(new XSSFWorkbook(in), -1);
                } else {
                    workbook = new SXSSFWorkbook(-1);
                }
            } else if (fileType.contains("xls")) {
                if (in != null) {
                    workbook = new HSSFWorkbook(in);
                } else {
                    workbook = new HSSFWorkbook();
                }
            }
            if (workbook == null) {
                logger.error("文件格式不正确,workbook创建失败");
                throw new Exception("文件格式不正确,workbook创建失败");
            }
        } catch (Exception e) {
            logger.error("ExcelUtil--createWorkbook", e);
            e.printStackTrace();
        }
        return workbook;
    }

    /**
     * 给输出流设置响应头参数
     *
     * @param fullFileName 输出流名称
     * @return java.io.OutputStream
     * @author created by captain on 2021-07-22 10:30:32
     */
    public static OutputStream getOutputStream(String fullFileName, HttpServletResponse response) {
        //设置response的属性
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/msexcel");
        response.addHeader("Access-Control-Allow-Origin", "*");
        OutputStream outputStream = null;
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fullFileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream;
    }

    /**
     * 隐藏列
     *
     * @param sheet         要隐藏的sheet页
     * @param hiddenColumns 要隐藏的列
     */
    protected void hideColumns(Sheet sheet, List<Integer> hiddenColumns) {
        if (null != hiddenColumns && hiddenColumns.size() > 0) {
            for (Integer hiddenColumn : hiddenColumns) {
                sheet.setColumnHidden(hiddenColumn, true);
            }
        }
    }
}
