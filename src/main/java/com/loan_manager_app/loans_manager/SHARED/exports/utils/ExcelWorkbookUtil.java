package com.loan_manager_app.loans_manager.SHARED.exports.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public final class ExcelWorkbookUtil {

    private ExcelWorkbookUtil() {
    }

    public static byte[] createWorkbook(
            String sheetName,
            String[] headers,
            List<Object[]> rows
    ) {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet(sheetName);
            //Header style

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 11);

            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // Currency style

            CellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.setDataFormat(
                    workbook.createDataFormat()
                            .getFormat("#,##0.00")
            );

            // Date style
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(
                    workbook.createDataFormat()
                            .getFormat("yyyy-MM-dd")
            );

            // DateTime style
            CellStyle dateTimeStyle = workbook.createCellStyle();
            dateTimeStyle.setDataFormat(
                    workbook.createDataFormat()
                            .getFormat("yyyy-MM-dd HH:mm:ss")
            );

            // Create headers
            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Create data rows
            for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
                Row row = sheet.createRow(rowIndex + 1);
                Object[] rowData = rows.get(rowIndex);
                for (int columnIndex = 0;
                     columnIndex < rowData.length;
                     columnIndex++) {

                    Object value = rowData[columnIndex];
                    Cell cell = row.createCell(columnIndex);

                    if (value == null) {
                        cell.setCellValue("");
                    }

                    else if (value instanceof String string) {
                        cell.setCellValue(string);
                    }

                    else if (value instanceof Long number) {
                        cell.setCellValue(number);
                    }

                    else if (value instanceof Integer number) {
                        cell.setCellValue(number);
                    }

                    else if (value instanceof Double number) {
                        cell.setCellValue(number);
                    }

                    else if (value instanceof BigDecimal number) {
                        cell.setCellValue(number.doubleValue());
                        cell.setCellStyle(currencyStyle);
                    }

                    else if (value instanceof LocalDate date) {
                        cell.setCellValue(date);
                        cell.setCellStyle(dateStyle);
                    }

                    else if (value instanceof LocalDateTime dateTime) {
                        cell.setCellValue(dateTime);
                        cell.setCellStyle(dateTimeStyle);
                    }

                    else if (value instanceof Boolean bool) {
                        cell.setCellValue(bool);
                    }

                    else {
                        cell.setCellValue(value.toString());
                    }
                }
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);

                // Prevent extremely wide columns
                if (sheet.getColumnWidth(i) > 15000) {
                    sheet.setColumnWidth(i, 15000);
                }
            }

            // Freeze header
            sheet.createFreezePane(0, 1);

            // Add filter
            if (!rows.isEmpty()) {
                sheet.setAutoFilter(
                        new org.apache.poi.ss.util.CellRangeAddress(
                                0,
                                rows.size(),
                                0,
                                headers.length - 1
                        )
                );
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to generate Excel file",
                    e
            );
        }
    }
}
