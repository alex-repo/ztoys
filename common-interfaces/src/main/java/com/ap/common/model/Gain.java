package com.ap.common.model;

import java.io.Serializable;
import java.util.Random;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class Gain implements DoubleSupplier, DoubleConsumer, Serializable {

	private double value;

	private transient double delta;

	public Gain() {
		this.value = Math.random() - 0.5d;
		this.setDelta(0);
	}

	public Gain(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public void randomize() {
		this.value = Math.random() - 0.5d;
	}

	public void randomize(double min, double max) {
		this.value = min + Math.random() * (max - min);
	}

	public void randomize(Random generator) {
		this.value = generator.nextDouble();
	}

	public double getDelta() {
		return delta;
	}

	public Gain setDelta(double delta) {
		this.delta = delta;
		return this;
	}

	@Override
	public void accept(double value) {
		this.value = value;
	}

	@Override
	public double getAsDouble() {
		return value;
	}

	public Gain inc(double delta) {
		this.value += delta;
		return this;
	}

	public Gain dec(double delta) {
		this.value -= delta;
		return this;
	}

}
