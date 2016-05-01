/**
 * 
 * Copyright 2015 alex
 * 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package com.ap.nn.model;

import com.ap.common.core.DoubleCollector;
import com.ap.common.function.DifferentiableFunction;
import com.ap.nn.core.Nucleus;
import com.ap.nn.core.Sinapse;

public class ThresholdNucleus extends Nucleus {

	private double threshold = 0;

	public double getThreshold() {
		return threshold;
	}

	public ThresholdNucleus setThreshold(double threshold) {
		this.threshold = threshold;
		return this;
	}

	public ThresholdNucleus(DoubleCollector<Sinapse> dendritesCollector, DifferentiableFunction axonFunction) {
		super(dendritesCollector,axonFunction);
		threshold = Math.random(); 
	}

	public ThresholdNucleus(DifferentiableFunction axonFunction) {
		super(axonFunction);				
	}

	@Override
	public void calculate() {
		if (this.hasInputs()) {
			setInput(getChildrenList().stream().collect(getDendritesCollector()));
		}
		setOutput(getAxonFunction().apply(getInput() - threshold).getAsDouble());
	}
}
