package io.github.thefive40.tienda_front.controller.navigation;

import io.github.thefive40.tienda_front.controller.auth.LoginController;
import io.github.thefive40.tienda_front.model.dto.ClientDTO;
import io.github.thefive40.tienda_front.service.UserService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
public class StatisticsController implements Initializable {


    @FXML
    private Label averageValue;

    @FXML
    private Label totalSalesValue;

    @FXML
    private Button exportButton;

    @FXML
    private ListView<String> topProductsList;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private Button applyFilterButton;

    @FXML
    private Label activeCustomersValue;

    @FXML
    private Button updateButton;

    @FXML
    private Label bestCategory;

    @FXML
    private Label conversionRateValue;

    @FXML
    private ChoiceBox<String> periodChoiceBox;

    @Autowired
    private ApplicationContext context;
    @Autowired
    private UserService userService;

    @FXML
    void handleExport(ActionEvent event) {
        // Mostrar un FileChooser para seleccionar la ubicación de guardado
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Archivo Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos Excel (*.xlsx)", "*.xlsx"));

        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet dataSheet = workbook.createSheet("Datos de Gráfica");
                XSSFSheet chartSheet = (XSSFSheet) workbook.createSheet("Gráfica de Ventas");

                exportChartData(dataSheet);

                drawBarChart(chartSheet, dataSheet);

                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                    System.out.println("Archivo Excel con gráfica exportado con éxito en: " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void drawBarChart( XSSFSheet chartSheet, Sheet dataSheet) {
        XSSFDrawing drawing = chartSheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 1, 1, 15, 25);

        XSSFChart chart = drawing.createChart(anchor);
        chart.setTitleText("Distribución de Ventas");

        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis( AxisPosition.BOTTOM);
        bottomAxis.setTitle("Categorías");

        var leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setTitle("Porcentaje");

        XDDFDataSource<String> categories = XDDFDataSourcesFactory.fromStringCellRange( (XSSFSheet) dataSheet,
                new CellRangeAddress (1, dataSheet.getLastRowNum(), 0, 0));
        XDDFNumericalDataSource<Double> values1 = XDDFDataSourcesFactory.fromNumericCellRange( (XSSFSheet) dataSheet,
                new CellRangeAddress(1, dataSheet.getLastRowNum(), 1, 1));
        XDDFNumericalDataSource<Double> values2 = XDDFDataSourcesFactory.fromNumericCellRange( (XSSFSheet) dataSheet,
                new CellRangeAddress(1, dataSheet.getLastRowNum(), 2, 2));

        XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
        XDDFChartData.Series series1 = data.addSeries(categories, values1);
        series1.setTitle("Probabilidad de Comprar", null);

        XDDFChartData.Series series2 = data.addSeries(categories, values2);
        series2.setTitle("Probabilidad de no Comprar", null);

        chart.plot(data);

        XDDFBarChartData barData = (XDDFBarChartData) data;
        barData.setBarDirection(BarDirection.COL);
    }
    private void exportChartData(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Categoría");
        headerRow.createCell(1).setCellValue("Probabilidad de Comprar");
        headerRow.createCell(2).setCellValue("Probabilidad de no Comprar");

        ObservableList<XYChart.Series<String, Number>> seriesList = barChart.getData();

        int rowNum = 1;
        for (int i = 0; i < seriesList.size(); i++) {
            XYChart.Series<String, Number> series = seriesList.get(i);
            for (XYChart.Data<String, Number> data : series.getData()) {
                Row row = sheet.getRow(rowNum);
                if (row == null) row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue(data.getXValue());
                row.createCell(i + 1).setCellValue(data.getYValue().doubleValue());

                rowNum++;
            }
            rowNum = 1;
        }
    }

    @Override
    public void initialize ( URL url, ResourceBundle resourceBundle ) {
        var listClient = userService.findAll ( );
        int clientActive = (int) listClient.stream ( ).filter ( ClientDTO::isStatus ).count ( );
        HashMap<String, Integer> productCategory = new HashMap<> ( );
        AtomicInteger total = new AtomicInteger ( 0 );
        AtomicInteger totalVentas = new AtomicInteger ( 0 );
        HashMap<String, Integer> products = new HashMap<> (  );
        listClient.forEach ( client -> {
            if (!client.getOrders ( ).isEmpty ( )) {
                client.getOrders ( ).forEach ( order -> {
                    order.getDetailOrder ( ).forEach ( detail -> {
                        String productName = detail.getProduct ().getName ();
                        if(products.containsKey ( productName )){
                            products.put ( productName, products.get ( productName )+1 );
                        }else{
                            products.put ( productName ,0);
                        }
                        String category = detail.getProduct ( ).getDescription ( );
                        productCategory.put ( category, productCategory.getOrDefault ( category, 0 ) + 1 );
                        total.addAndGet ( 1 );
                    } );
                } );
                totalVentas.set ( totalVentas.get ( ) + 1 );
            } else {

            }
        } );
        int max = productCategory.values ( ).stream ( ).max ( Comparator.naturalOrder ( ) ).orElseThrow ( );
        AtomicReference<String> bestCategory = new AtomicReference<> ( "" );
        productCategory.forEach ( ( key, value ) -> {
            if (value == max) {
                bestCategory.set ( key );
            }
        } );
        this.bestCategory.setText ( bestCategory.get ( ) );
        this.totalSalesValue.setText ( totalVentas.get ( ) + "" );
        XYChart.Series<String, Number> series1 = new XYChart.Series<> ( );
        series1.setName ( "Probabilidad de Comprar" );

        productCategory.forEach ( ( category, count ) -> {
            double probabilityOfBuying = (double) count / total.get ( ) * 100;
            series1.getData ( ).add ( new XYChart.Data<> ( category, probabilityOfBuying ) );
        } );
        Map<String, Integer> sortedByValue = products.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) // Ordenar de mayor a menor
                .collect( Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        final int limit = 5;
        final AtomicInteger countLimit = new AtomicInteger(0);
        sortedByValue.forEach((key, value) -> {
            if (countLimit.getAndIncrement() < limit) {
                topProductsList.getItems ().add ( key );
            }
        });

        barChart.getData ( ).add ( series1 );

        XYChart.Series<String, Number> series2 = new XYChart.Series<> ( );
        series2.setName ( "Probabilidad de no Comprar" );

        productCategory.forEach ( ( category, count ) -> {
            double probabilityOfNotBuying = 100 - ((double) count / total.get ( ) * 100);
            series2.getData ( ).add ( new XYChart.Data<> ( category, probabilityOfNotBuying ) );
        } );

        conversionRateValue.setText(String.format("%.3f", ((double) totalVentas.get() / clientActive)) + " ");
        activeCustomersValue.setText ( clientActive + "");
        barChart.getData ( ).add ( series2 );
        averageValue.setText ( ( Math.round ( (double) (total.get ( ) / productCategory.size ( ))) + "%" ));
    }
}
