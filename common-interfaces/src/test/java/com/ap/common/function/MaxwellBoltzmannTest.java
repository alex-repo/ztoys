package com.ap.common.function;

import com.ap.common.core.ContainerOnList;
import com.ap.common.fx.FxLineChartFactory;
import com.ap.common.model.VectorInjective;
import com.ap.common.model.VectorInjectiveSpace;
import javafx.scene.chart.XYChart.Series;
import org.junit.Before;
import org.junit.Test;

public class MaxwellBoltzmannTest {

    MaxwellBoltzmann maxwellBoltzmann = new MaxwellBoltzmann();

    @Before
    public void setUp() throws Exception {
        FxLineChartFactory.run();
    }

    @Test
    public void testApply() {
        VectorInjectiveSpace fSpace = new VectorInjectiveSpace(ContainerOnList.class);
        VectorInjectiveSpace dSpace = new VectorInjectiveSpace(ContainerOnList.class);
        for (int i = -5; i < 30; i++) {
            double v = i * 0.1;
            double f = maxwellBoltzmann.apply(v).getAsDouble();
            double d = maxwellBoltzmann.derivative(v).getAsDouble();
            fSpace.add(new VectorInjective(new double[]{v}, new double[]{f}));
            dSpace.add(new VectorInjective(new double[]{v}, new double[]{d}));
        }
        Series fseries = FxLineChartFactory.asSeries(fSpace);
        Series dseries = FxLineChartFactory.asSeries(dSpace);
        FxLineChartFactory.add("f", fseries, 1000);
        FxLineChartFactory.add("d", dseries, 10000);
    }
}
