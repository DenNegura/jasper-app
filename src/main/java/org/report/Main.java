package org.report;

import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRException;
import org.report.report.DynamicJasperReport;
import org.report.report.StaticReportGenerator;
import org.report.repository.DataReader;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, JRException, DRException {
        generateByExistingReportTemplate();
//        generateUsingDynamicJasperReport();
    }

    public static void generateByExistingReportTemplate() throws SQLException, JRException {
        DataReader dataReader = new DataReader();

        StaticReportGenerator.generateUseList(dataReader.findAllToList(), "ListReportHoliday");
        StaticReportGenerator.generateUseResultSet(dataReader.findAllToResultSet(), "ResultSetReportHoliday");
        StaticReportGenerator.generateUseMap(dataReader.findAllToListOfMaps(), "MapReportHoliday");

        dataReader.closeConnection();
    }

    public static void generateUsingDynamicJasperReport() throws DRException, SQLException {
        DynamicJasperReport.generate();
    }
}