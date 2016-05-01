package com.ap.nn.core;


import com.ap.common.core.ContainerOnList;
import static org.junit.Assert.*;

import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

import com.ap.common.model.EuclideanVector;

public class NetworkOnListTest {
	
    Nucleus nucleus1, nucleus2, nucleus3, nucleus4;
    NucleusContainer layer1, layer2;
	NetworkContainer network;
	private double[] inputVectors;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		nucleus1 = new Nucleus();
		layer1 = new NucleusContainer(ContainerOnList.class, 10, nucleus1);
		
		nucleus2 = new Nucleus();
		nucleus3 = new Nucleus();
		nucleus4 = new Nucleus();
		layer2 = new NucleusContainer(ContainerOnList.class);
		
		network = new NetworkContainer(ContainerOnList.class);
		
		inputVectors = IntStream.range(0, 10).mapToDouble(i -> i).toArray();
		
	}

	//FIXME split test
	@Test
	public void testAddLayer() {
		network.addLayer(layer1);
//		assertEquals(1, network.size());
		assertEquals(layer1,network.getInputLayer());
	}

	/**
	 * Test method for {@link com.ap.nn.core.NetworkContainer#getChildrenList()}.
	 */
	@Test
	public void testGetChildren() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ap.nn.core.NetworkContainer#getOutputs()}.
	 */
	@Test
	public void testGetOutputs() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ap.nn.core.NetworkContainer#getOutputLayer()}.
	 */
	@Test
	public void testGetOutputLayer() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ap.nn.core.NetworkContainer#setOutputLayer(com.ap.nn.core.NucleusContainer)}.
	 */
	@Test
	public void testSetOutputLayer() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ap.nn.core.NetworkContainer#setInOutLayers()}.
	 */
	@Test
	public void testSetInOutLayers() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ap.nn.core.NetworkContainer#getInputLayer()}.
	 */
	@Test
	public void testGetInputLayer() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ap.nn.core.NetworkContainer#setInputLayer(com.ap.nn.core.NucleusContainer)}.
	 */
	@Test
	public void testSetInputLayer() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ap.nn.core.NetworkContainer#calculate()}.
	 */
	@Test
	public void testCalculate() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ap.nn.core.NetworkContainer#getParent()}.
	 */
	@Test
	public void testGetParent() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetInput() {
		testAddLayer();
		network.setInput(new EuclideanVector(inputVectors) );
		double[] array = network.get(0).asStream().mapToDouble(n -> ((Nucleus) n).getInput()).toArray();
		assertArrayEquals(inputVectors, array, Double.MIN_VALUE);
	}

	/**
	 * Test method for {@link com.ap.nn.core.NetworkContainer#toString()}.
	 */
	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ap.nn.core.NetworkContainer#selfConnect(com.ap.nn.core.NucleusContainer, double)}.
	 */
	@Test
	public void testSelfConnect() {
		fail("Not yet implemented");
	}

}
