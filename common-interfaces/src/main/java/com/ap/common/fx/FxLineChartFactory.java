package com.ap.common.fx;

import com.ap.common.model.VectorInjective;
import com.ap.common.model.VectorInjectiveSpace;
import com.ap.common.fx.FxLineChart.STATE;
import javafx.application.Platform;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public class FxLineChartFactory {

    private static Series theSeries = null;

    public static void add(String name, Series series, long delay) {
        waitIfStarting();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FxLineChart.add(name, series);
                FxLineChart.show();
            }
        });
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void replaceTheOne(String string, VectorInjectiveSpace traceSpace, long delay) {
        if (theSeries != null) {
            remove(theSeries);
        }
        Series series = asSeries(traceSpace);
        add("resultSpace", series, delay);
        theSeries = series;
    }

    public static void remove(Series series) {
        waitIfStarting();
        Platform.runLater(() -> FxLineChart.remove(series));
    }

    public static Series asSeries(VectorInjectiveSpace injectiveSpace) {
        Series series = new XYChart.Series();
        if (injectiveSpace.getInputSize() != 1 && injectiveSpace.getOutputSize() != 1) {
            throw new RuntimeException();
        }
        for (VectorInjective injective : injectiveSpace.asList()) {
            series.getData().add(new XYChart.Data(injective.getInput().get(0), injective.getOutput().get(0)));
        }
        return series;
    }

    /**
     * Start Fx Platform before. Use the #run method
     */
    public static void run() {
        synchronized (FxLineChart.class) {
            if (FxLineChart.state != STATE.STOP)
                return;
            Thread thread = new Thread() {
                @Override
                public void run() {
                    javafx.application.Application.launch(FxLineChart.class);
                }
            };
            thread.start();
            FxLineChart.state = STATE.STARTING;
        }
    }

    public static void run(LineChartConfigHolder config) {
        if (config != null) {
            FxLineChart.applyConfig(config);
        }
        run();
    }

    static final String msg = "Start Fx Platform before. Use the #run method.";

    static void waitIfStarting() {
        while (FxLineChart.state != STATE.STARTED) {
            if (FxLineChart.state == STATE.STOP) {
                throw new RuntimeException(msg);
            } else {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
