package com.ap.common.model;

import com.ap.common.core.ContainerOnList;
import static org.junit.Assert.*;

import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

public class ClusterSpaceTest {

	private VectorInjectiveSpace injectiveSpace = new VectorInjectiveSpace(ContainerOnList.class);
	
	@Before
	public void setUp() throws Exception {
		
		IntStream.range(0, 15).forEach(i -> {
//			collection.add(new VectorInjective(new double[]{random.nextDouble()}, new double[]{random.nextDouble()}));
			injectiveSpace.add(new VectorInjective(new double[]{i}, new double[]{i}));
		});

	}

	/**
	 * Test method for {@link com.ap.common.model.ClusterSpace#ClusterSpace(com.ap.common.model.VectorInjectiveSpace)}.
	 */
	@Test
	public void testClusterSpaceVectorInjectiveSpace() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ap.common.model.ClusterSpace#ClusterSpace(com.ap.common.model.VectorInjectiveSpace, int)}.
	 */
	@Test
	public void testClusterSpaceVectorInjectiveSpaceInt() {
		ClusterSpace cs = new ClusterSpace(ContainerOnList.class, injectiveSpace, 2);
		fail("Not yet implemented");
		assertEquals(2, cs.size());
	}

	/**
	 * Test method for {@link com.ap.common.model.ClusterSpace#init(com.ap.common.model.VectorInjectiveSpace)}.
	 */
	@Test
	public void testInit() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ap.common.model.ClusterSpace#randomizeCentroids()}.
	 */
	@Test
	public void testRandomizeCentroids() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.ap.common.model.ClusterSpace#doClustering()}.
	 */
	@Test
	public void testDoClustering() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testGetNearestCluster(){
		fail("Not yet implemented");
	}

}
