package com.fpt.servicecontract.ultils;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportReport<T> {

    public byte[] getItemReport(List<T> items, String format) {

        JasperReport jasperReport;
        byte[] reportContent;

        try {

            jasperReport = (JasperReport) JRLoader.loadObject(ResourceUtils.getFile("item-report.jasper"));
        } catch (FileNotFoundException | JRException e) {
            try {

                File file = ResourceUtils.getFile("classpath:item-report.jrxml");
                jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
                JRSaver.saveObject(jasperReport, "item-report.jasper");
            } catch (FileNotFoundException | JRException ex) {
                throw new RuntimeException(e);
            }
        }


        //Set report data
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(items);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", "Item Report");

        //Fill report
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            switch (format) {
                case "pdf" -> reportContent = JasperExportManager.exportReportToPdf(jasperPrint);
                case "xml" -> reportContent = JasperExportManager.exportReportToXml(jasperPrint).getBytes();
                default -> throw new RuntimeException("Unknown report format");
            }
            return reportContent;
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }
}
