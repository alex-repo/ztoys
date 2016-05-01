package com.ap.nn.core;

import com.ap.common.core.ContainerOnList;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LayerTest {
    Nucleus nucleus1, nucleus2, nucleus3, nucleus4;
    NucleusContainer layer1, layer2;

	@Before
	public void setUp() throws Exception {
		layer1 = new NucleusContainer(ContainerOnList.class);
		layer2 = new NucleusContainer(ContainerOnList.class);
		
		nucleus1 = new Nucleus();
		nucleus2 = new Nucleus();
		nucleus3 = new Nucleus();
		nucleus4 = new Nucleus();
	}

	/**
	 * Test method for {@link com.ap.nn.core.NucleusContainer#addNucleus(com.ap.nn.core.Nucleus)}.
	 */
	@Test
	public void testAddNucleusNucleus() {
		layer1.add(nucleus1);
		layer1.add(nucleus2);
		assertEquals(nucleus2, layer1.get(1));
	}

	/**
	 * Test method for {@link com.ap.nn.core.NucleusContainer#setNucleus(int, com.ap.nn.core.Nucleus)}.
	 */
	@Test
	public void testSetNucleus() {
		testAddNucleusNucleus();
		layer1.set(0, nucleus4);
		assertEquals(nucleus4, layer1.get(0));
	}

	/**
	 * Test method for {@link com.ap.nn.core.NucleusContainer#remove(com.ap.nn.core.Nucleus)}.
	 */
	@Test
	public void testRemoveNucleus() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ap.nn.core.NucleusContainer#remove(int)}.
	 */
	@Test
	public void testRemoveInt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ap.nn.core.NucleusContainer#calculate()}.
	 */
	@Test
	public void testCalculate() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ap.nn.core.NucleusContainer#reset()}.
	 */
	@Test
	public void testReset() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ap.nn.core.NucleusContainer#initGains(double)}.
	 */
	@Test
	public void testInitGains() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ap.nn.core.NucleusContainer#createSinapse(com.ap.nn.core.Nucleus, com.ap.nn.core.Nucleus, com.ap.common.model.Gain)}.
	 */
	@Test
	public void testCreateSinapse() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ap.nn.core.NucleusContainer#addInput(com.ap.nn.core.Nucleus)}.
	 */
	@Test
	public void testAddInput() {
		fail("Not yet implemented");
	}

	@Test
	public void testConnect() {
		layer1.add(nucleus1);
		layer1.add(nucleus2);
		layer2.add(nucleus3);
		layer2.add(nucleus4);
		layer1.connect(layer2);
		
		int sum = layer2.asStream().flatMap(n -> n.getChildrenList().stream()).mapToInt(s -> 1).sum();

		assertEquals(4, sum);
	}
	
	@Test
	public void testDirectConnectLayerLayerDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testDirectConnectLayerLayer() {
		fail("Not yet implemented");
	}



}
