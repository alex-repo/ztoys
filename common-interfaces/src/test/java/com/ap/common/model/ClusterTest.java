package com.ap.common.model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

public class ClusterTest {

	
	static private int SIZE;
	Cluster cluster = new Cluster();
	Cluster cluster1 = new Cluster();
	EuclideanVector euclideanVector = new EuclideanVector(SIZE);
	EuclideanVector euclideanVector1 = new EuclideanVector(SIZE);
	EuclideanVector euclideanVector2 = new EuclideanVector(SIZE);
	EuclideanVector euclideanVector3 = new EuclideanVector(SIZE);
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testCentroid() {
		euclideanVector.set(new Double[]{0.,1.});
		euclideanVector1.set(new Double[]{0.,3.});
		cluster.add(euclideanVector);
		cluster.add(euclideanVector1);
		EuclideanVector centroid = cluster.centroid();
		assertArrayEquals(new Double[]{0.,2.}, centroid.toArray());
	}

	@Test
	public void testEqualsObject() {

		euclideanVector.set(new Double[]{0.,1.});
		euclideanVector1.set(new Double[]{0.,3.});
		euclideanVector2.set(new Double[]{0.,0.});
		euclideanVector3.set(new Double[]{0.,4.});
		
		cluster.add(euclideanVector);
		cluster.add(euclideanVector1);
		cluster1.add(euclideanVector2);
		cluster1.add(euclideanVector3);
		
		assertTrue(cluster.equals(cluster1));

	}

	@Test
	public void testAdd() {
		assertTrue(cluster.add(euclideanVector));
		assertFalse(cluster.add(euclideanVector));
	}

	@Test
	public void testKNearestNeighbours() {
		euclideanVector.set(new Double[]{0.,1.});
		euclideanVector1.set(new Double[]{0.,2.});
		euclideanVector2.set(new Double[]{0.,3.});
		euclideanVector3.set(new Double[]{0.,4.});
		
		cluster.add(euclideanVector1);
		cluster.add(euclideanVector2);
		cluster.add(euclideanVector3);
		
		EuclideanVector[] array = cluster.kNearestNeighbours(euclideanVector, 2);

		assertArrayEquals(new EuclideanVector[]{euclideanVector1, euclideanVector2}, array);
	}

}
