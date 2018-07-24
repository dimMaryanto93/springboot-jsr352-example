package com.maryato.dimas.example.config.items;

import com.maryato.dimas.example.models.Penduduk;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@StepScope
@Component
public class DataPendudukExcelItemWriter implements ItemWriter<Penduduk> {

    private static final String FILE_NAME = new StringBuilder(System.getProperty("user.home"))
            .append(File.separator).append("Downloads").append(File.separator).append("penduduk").toString();
    private static final String[] HEADERS =
            {"NIK", "Nama Lengkap", "Tanggal Lahir"};

    private String outputFilename;
    private Workbook workbook;
    private CellStyle dataCellStyle;
    private int currRow = 0;
    private Sheet sheet;

    private void addHeaders(Sheet sheet) {

        Workbook wb = sheet.getWorkbook();

        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();

        font.setFontHeightInPoints((short) 10);
        font.setFontName("Arial");
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(font);

        Row row = sheet.createRow(2);
        int col = 0;

        for (String header : HEADERS) {
            Cell cell = row.createCell(col);
            cell.setCellValue(header);
            cell.setCellStyle(style);
            col++;
        }
        currRow++;
    }

    private void addTitleToSheet(Sheet sheet) {

        Workbook wb = sheet.getWorkbook();

        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();

        font.setFontHeightInPoints((short) 14);
        font.setFontName("Arial");
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(font);

        Row row = sheet.createRow(currRow);
        row.setHeightInPoints(16);

//        String currDate = DateFormatUtils.format(Calendar.getInstance(),
//                DateFormatUtils.ISO_DATETIME_FORMAT.getPattern());

        Cell cell = row.createCell(0, Cell.CELL_TYPE_STRING);
        cell.setCellValue("Stock Data as of " + new Date());
        cell.setCellStyle(style);

        CellRangeAddress range = new CellRangeAddress(0, 0, 0, 7);
        sheet.addMergedRegion(range);
        currRow++;

    }

    @AfterStep
    public void afterStep(StepExecution stepExecution) throws IOException {
        FileOutputStream fos = new FileOutputStream(outputFilename);
        workbook.write(fos);
        fos.close();
    }

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("Calling beforeStep");

        String dateTime = new Date().toString();
        outputFilename = FILE_NAME + "_" + dateTime + ".xlsx";

        workbook = new SXSSFWorkbook(100);
        this.sheet = workbook.createSheet("Testing");
        sheet.createFreezePane(0, 3, 0, 3);
        sheet.setDefaultColumnWidth(20);

        addTitleToSheet(sheet);
        currRow++;
        addHeaders(sheet);
        initDataStyle();

    }

    private void initDataStyle() {
        dataCellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();

        font.setFontHeightInPoints((short) 10);
        font.setFontName("Arial");
        dataCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        dataCellStyle.setFont(font);
    }

    @Override
    public void write(List<? extends Penduduk> items) throws Exception {

        Sheet sheet = workbook.getSheetAt(0) != null ? workbook.getSheetAt(0) : workbook.createSheet();
        for (Penduduk data : items) {
            currRow++;
            Row row = sheet.createRow(currRow);
            createStringCell(row, data.getNik(), 0);
            createStringCell(row, data.getNamaLengkap(), 1);
            createStringCell(row, data.getTanggalLahir(), 2);
        }
    }

    private void createStringCell(Row row, String val, int col) {
        Cell cell = row.createCell(col);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue(val);
    }

    private void createNumericCell(Row row, Double val, int col) {
        Cell cell = row.createCell(col);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(val);
    }
}
