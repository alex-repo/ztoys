package com.ap.common.fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class FxLineChart extends Application {
    private static Stage stage;

    private static LineChart<Number, Number> lineChart;

    private static LineChartConfigHolder config = new LineChartConfigHolder();
    static {
        for (Property property : Property.values()) {
            config.getConfig().put(property, property.name);
        }
    }

    public enum Property {
        TITLE("Title"),
        CHART_TITLE("Chart Title"),
        INPUT_LABEL("Input Label"),
        OUTPUT_LABEL("Output Label");

        private final String name;

        Property(final String string) {
            this.name = string;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public enum STATE {
        STOP, STARTING, STARTED
    }

    static volatile STATE state = STATE.STOP;
    private NumberAxis input;
    private NumberAxis output;

    public static void show() {
        stage.show();
    }

    public static void add(String name, XYChart.Series series) {
        series.setName(name);
        lineChart.getData().add(series);
    }

    public static void remove(XYChart.Series series) {
        lineChart.getData().remove(series);
    }

    @Override
    public void init() throws Exception {
        applyConfig(config);
        input = new NumberAxis();
        output = new NumberAxis();
        lineChart = new LineChart<>(input, output);
        lineChart.setLegendVisible(false);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle(config.getConfig().get(Property.TITLE));
        input.setLabel(config.getConfig().get(Property.INPUT_LABEL));
        output.setLabel(config.getConfig().get(Property.OUTPUT_LABEL));
        lineChart.setTitle(config.getConfig().get(Property.CHART_TITLE));
        Scene scene = new Scene(lineChart, 800, 600);
        stage.setScene(scene);
        state = STATE.STARTED;
    }

    public static void applyConfig(LineChartConfigHolder conf) {
        String value;
        for (Property property : Property.values()) {
            if ((value = conf.getConfig().get(property)) != null) {
                config.getConfig().put(property, value);
            }
        }
    }
}
