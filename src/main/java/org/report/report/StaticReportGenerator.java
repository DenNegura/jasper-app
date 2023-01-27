package org.report.report;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import org.report.domain.Holiday;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;


public class StaticReportGenerator {

    private static final String REPORT_FOLDER = "C:\\Users\\crme004\\Documents\\";

    private static final String REPORT_NAME = "Blank_report_1.jasper";


    public static void generateUseResultSet(ResultSet resultSetOfHolidays, String reportTitle) throws JRException {
        generate(new JRResultSetDataSource(resultSetOfHolidays), reportTitle);
    }

    public static void generateUseList(List<Holiday> listOfHolidays, String reportTitle) throws JRException {
        generate(new JRBeanCollectionDataSource(listOfHolidays), reportTitle);
    }

    public static void generateUseMap(List<Map<String, ?>> listOfMapsOfHolidays, String reportTitle) throws JRException {
        generate(new JRMapCollectionDataSource(listOfMapsOfHolidays), reportTitle);
    }

    private static void generate(JRDataSource dataSource, String reportTitle) throws JRException {
        JasperFillManager.fillReportToFile(REPORT_FOLDER + REPORT_NAME,
                REPORT_FOLDER + reportTitle + ".jrprint", null, dataSource);
    }
}
