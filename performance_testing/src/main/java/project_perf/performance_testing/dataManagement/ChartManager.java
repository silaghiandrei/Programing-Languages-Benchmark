package project_perf.performance_testing.dataManagement;

import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.*;

public class ChartManager {

    private final String[] colors = {"-fx-stroke: #fa761e;", "-fx-stroke: #33FF57;", "-fx-stroke: #82d4fa;"};
    private final String[] labelStyles = {"-fx-text-fill: #fa761e; -fx-font-size: 20px;", "-fx-text-fill: #33FF57; -fx-font-size: 20px;", "-fx-text-fill: #82d4fa; -fx-font-size: 20px;"};
    public VBox returnLineChart(Integer runsNo, String title, String[] languageName, String fileName, Double[] avgs) {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Test number        ");
        yAxis.setLabel("Microseconds");

        xAxis.setTickLabelFill(Color.WHITE);
        yAxis.setTickLabelFill(Color.WHITE);

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(title);
        lineChart.setCreateSymbols(false);

        // Disable the legend
        lineChart.setLegendVisible(false);

        VBox container = new VBox();
        container.setSpacing(20);
        container.setAlignment(Pos.CENTER);

        HBox averagesBox = new HBox();
        averagesBox.setSpacing(50);
        averagesBox.setAlignment(Pos.CENTER);

        Label[] averageLabels = new Label[3];

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            int outerIndex;
            int innerIndex;

            for (outerIndex = 0; outerIndex < 3; outerIndex++) {
                XYChart.Series<Number, Number> series = new XYChart.Series<>();
                series.setName(languageName[outerIndex]);

                String line = reader.readLine();
                for (innerIndex = 0; innerIndex < runsNo; innerIndex++) {
                    line = reader.readLine();
                    String[] parts = line.split(" ");
                    if (parts.length == 2) {
                        int x = Integer.parseInt(parts[0].trim());
                        double y = Double.parseDouble(parts[1].trim());
                        series.getData().add(new XYChart.Data<>(x, y));
                    }
                }
                line = reader.readLine();
                String[] avgParts = line.split(" ");
                if (avgParts.length == 2) {
                    double avg = Double.parseDouble(avgParts[1].trim());
                    avgs[outerIndex] = avg;

                    averageLabels[outerIndex] = new Label(languageName[outerIndex] + " " + avg + "μs");
                    averageLabels[outerIndex].setStyle(labelStyles[outerIndex]);
                }

                lineChart.getData().add(series);
                series.getNode().setStyle(colors[outerIndex]);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        lineChart.setPrefHeight(500);

        averagesBox.getChildren().addAll(averageLabels[0], averageLabels[1], averageLabels[2]);

        container.getChildren().addAll(lineChart, averagesBox);

        return container;
    }

    public void addLineChartToPane(VBox vBox, VBox chartContainer) {
        chartContainer.setPrefSize(800, 700);

        LineChart<?, ?> lineChart = (LineChart<?, ?>) chartContainer.getChildren().getFirst();
        lineChart.setStyle("-fx-background-color: transparent;");
        lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");
        lineChart.lookup(".chart-horizontal-grid-lines").setStyle("-fx-stroke: transparent;");
        lineChart.lookup(".chart-vertical-grid-lines").setStyle("-fx-stroke: transparent;");
        lineChart.lookup(".chart-title").setStyle("-fx-text-fill: #79A876;");

        vBox.getChildren().add(chartContainer);
    }
}
