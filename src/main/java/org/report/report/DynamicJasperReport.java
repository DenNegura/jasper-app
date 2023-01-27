package org.report.report;

import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.chart.BarChartBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabRowGroupBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import org.report.repository.DataReader;

import java.awt.*;
import java.sql.Connection;
import java.util.Date;

import static net.sf.dynamicreports.report.builder.DynamicReports.cht;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.ctab;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

public class DynamicJasperReport {


    private static Connection openConnection() {
        DataReader dataReader = new DataReader();
        return dataReader.getConnection();
    }

    public static void generate() throws DRException {

        Connection connection = openConnection();

        StyleBuilder columnStyle = stl.style()
                .setBorder(stl.pen1Point())
                .setTextAlignment(HorizontalTextAlignment.LEFT, VerticalTextAlignment.TOP)
                .setTopPadding(2)
                .setLeftPadding(2);
        StyleBuilder headerStyle = stl.style(columnStyle).setBackgroundColor(Color.decode("#7BBCC9"));
        StyleBuilder titleStyle = stl.style().setFontSize(22).bold();

        TextColumnBuilder<String> columnCountry = col.column("Country", "country", type.stringType())
                .setTitleFixedHeight(28)
                .setFixedWidth(130)
                .setHeight(28);
        TextColumnBuilder<String> columnHoliday = col.column("Holiday", "name", type.stringType())
                .setTitleFixedHeight(28)
                .setFixedWidth(150)
                .setHeight(28);
        TextColumnBuilder<Date> columnDate = col.column("Date", "date", type.dateType())
                .setTitleFixedHeight(28)
                .setPattern("M/d/yyyy")
                .setFixedWidth(150)
                .setHeight(28);
        TextColumnBuilder<String> columnMonth = col.column("Month", "month", type.stringType());
        TextColumnBuilder<Integer> columnCountHolidays =
                col.column("Count holidays", "count", type.integerType());


        CrosstabColumnGroupBuilder<Integer> columnDateGroup =
                ctab.columnGroup("month", Integer.class)
                        .setHeaderHeight(20)
                        .setHeaderStyle(columnStyle)
                        .setTotalHeaderStyle(columnStyle);

        CrosstabRowGroupBuilder<String> rowCountryGroup =
                ctab.rowGroup("country", String.class)
                        .setHeaderWidth(60)
                        .setHeaderStyle(columnStyle)
                        .setTotalHeaderStyle(columnStyle);

        CrosstabBuilder crassTab = ctab.crosstab()
                .columnGroups(columnDateGroup)
                .rowGroups(rowCountryGroup)
                .measures(ctab.measure("name", String.class, Calculation.COUNT))
                .setCellStyle(columnStyle)
                .setGrandTotalStyle(columnStyle)
                .setCellWidth(30)
                .setCellHeight(20);

        BarChartBuilder barChart = cht.barChart()
                .setTitle("Holidays per month")
                .setTitleFont(stl.font().bold())
                .setCategory(columnMonth)
                .setUseSeriesAsCategory(true)
                .series(cht.serie(columnCountHolidays))
                .setDataSource("select count(name) as count, extract(month from date) as month " +
                        "from tab_holiday group by extract(month from date) " +
                        "order by extract(month from date)", connection)
                .setCategoryAxisFormat(cht.axisFormat().setLabel("Months"));

        report()
                .columns(columnCountry, columnHoliday, columnDate)
                .sortBy(columnDate)
                .title(cmp.horizontalList().add(
                                cmp.text("Holidays").setStyle(titleStyle).setHeight(80),
                                cmp.image("C:\\Users\\crme004\\Pictures\\index.png")
                                        .setFixedDimension(165, 50)))
                .summary(crassTab, barChart)
                .summaryOnANewPage()
                .setDataSource("select country, name, date, extract(month from date) as month " +
                        "from tab_holiday where extract(year from date) = 2017;", connection)
                .setColumnStyle(columnStyle)
                .setColumnTitleStyle(headerStyle)
                .setPageMargin(DynamicReports.margin(20))
                .show();
    }
}
