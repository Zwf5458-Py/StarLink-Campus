package com.starlink.campus.common.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ExcelExportUtil {

    /**
     * 通用 Excel 导出
     * @param data 数据列表，每行为一个 Map
     * @param headers 表头列表（对应 Map 的 key）
     * @param headerLabels 表头显示名
     * @param sheetName Sheet 名称
     * @param fileName 文件名
     * @param response HTTP 响应
     */
    public static void export(List<Map<String, Object>> data, 
                              List<String> headers, 
                              List<String> headerLabels,
                              String sheetName, 
                              String fileName,
                              HttpServletResponse response) throws IOException {
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);
        
        // 创建表头样式
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        
        // 写入表头
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headerLabels.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headerLabels.get(i));
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 5000);
        }
        
        // 写入数据
        for (int rowIndex = 0; rowIndex < data.size(); rowIndex++) {
            Row row = sheet.createRow(rowIndex + 1);
            Map<String, Object> rowData = data.get(rowIndex);
            for (int colIndex = 0; colIndex < headers.size(); colIndex++) {
                Cell cell = row.createCell(colIndex);
                Object value = rowData.get(headers.get(colIndex));
                cell.setCellValue(value != null ? value.toString() : "");
            }
        }
        
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + ".xlsx");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
