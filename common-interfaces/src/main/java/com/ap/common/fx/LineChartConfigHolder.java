package com.ap.common.fx;

import com.ap.common.fx.FxLineChart.Property;

import java.util.HashMap;

public class LineChartConfigHolder {

    private final HashMap<Property, String> properties = new HashMap<>();

    public HashMap<Property, String> getConfig() {
        return properties;
    }
}
