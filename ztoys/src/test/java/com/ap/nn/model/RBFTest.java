package com.ap.nn.model;

import java.util.EventObject;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import com.ap.common.core.AbstractEventListener;
import com.ap.common.core.ContainerOnList;
import com.ap.common.model.EuclideanVector;
import com.ap.common.model.VectorInjective;
import com.ap.common.model.VectorInjectiveSpace;
import com.ap.common.util.CommonUtils;
import com.ap.common.function.Gaussian;
import com.ap.common.function.Linear;
import com.ap.common.fx.FxLineChartFactory;
import com.ap.nn.core.DistanceDoubleCollector;
import com.ap.nn.core.NucleusContainer;
import com.ap.nn.core.Network;
import com.ap.nn.core.NetworkContainer;
import com.ap.nn.core.Nucleus;

import javafx.scene.Node;
import javafx.scene.chart.XYChart.Series;

public class RBFTest {

	Network network = new NetworkContainer(ContainerOnList.class);
	
	RBF Rbf = new RBF();
//	ArrayList<VectorInjective> collection = new ArrayList<>();
	private AbstractEventListener listener = new AbstractEventListener() {

		@Override
		public void handleEvent(EventObject event) {
			LMS lr = (LMS) event.getSource();
			CommonUtils.getLogger(getClass()).debug(lr.getCurrentIteration() + ". iteration | Total network error: " + lr.getTotalNetworkError());
		}
	};
	//FIXME load data
	private VectorInjectiveSpace injectiveSpace= new VectorInjectiveSpace(ContainerOnList.class);
	private EuclideanVector inputVector;

	@Before
	public void setUp() throws Exception {
		
		FxLineChartFactory.run();
		
		Random random = new Random();
		IntStream.range(0, 10).forEach(i -> {
			injectiveSpace.add(new VectorInjective(new double[]{i * 0.1}, new double[]{Math.sin(i * 0.1 * 3) + random.nextGaussian() * 0.1}));
		});
		
		Series series = FxLineChartFactory.asSeries(injectiveSpace);
		FxLineChartFactory.add("injectiveSpace", series, 1000);
		((Node) series.nodeProperty().get()).setStyle("-fx-stroke-width: 0;");
		

		NucleusContainer input = new NucleusContainer(ContainerOnList.class, 1, new Nucleus(new Linear()));
		NucleusContainer rbf = new NucleusContainer(ContainerOnList.class, 10, new Nucleus(new DistanceDoubleCollector(), new Gaussian()));
		NucleusContainer output = new NucleusContainer(ContainerOnList.class, 1, new Nucleus(new Linear()));

		network.addLayer(input).addLayer(rbf).addLayer(output);
		Rbf.setNetwork(network);

		Rbf.rate(0.01).maxIterations(7000);
		Rbf.setMaxError(0.0005);
		Rbf.addListener(listener);

	}

	@Test
	public void test() {

		Rbf.learn(injectiveSpace);
		VectorInjectiveSpace resultSpace = new VectorInjectiveSpace(ContainerOnList.class);
		
		double[] outarray = new double[10];
		for(int i = 0; i < 12; i++){
			network.setInput(new EuclideanVector(new double[]{i * 0.1}));
			network.calculate();
			EuclideanVector out = network.getOutputs();
			resultSpace.add(new VectorInjective(new double[]{i * 0.1}, out.asArray()));
		}
		
		Series series = FxLineChartFactory.asSeries(resultSpace);
		FxLineChartFactory.add("resultSpace", series, 5000);
		
	}

}
