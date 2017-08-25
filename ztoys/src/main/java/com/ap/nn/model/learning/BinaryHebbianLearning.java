/**
 * Copyright 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ap.nn.model.learning;

import com.ap.nn.core.Nucleus;
import com.ap.nn.core.Sinapse;
//FIXME comment
/**
 * Hebbian-like learning algorithm used for Hopfield network. Works with [0, 1] values
 */
public class BinaryHebbianLearning extends UnsupervisedHebbianLearning {
		
	/**
	 * The class fingerprint that is set to indicate serialization
	 * compatibility with a previous version of the class.
	 */	
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new instance of BinaryHebbianLearning
	 */
	public BinaryHebbianLearning() {
		super();
	}
	
	/**
	 * This method implements weights update procedure for the single neuron
	 * 
	 * @param neuron
	 *            neuron to update weights
	 */
	@Override
	protected void updateNeuronWeights(Nucleus neuron) {
		double output = neuron.getOutput();
		for (Sinapse connection : neuron.getDendrites()) {
			double input = connection.getInput();

			if (((input>0) && (output>0)) || ((input<=0) && (output<=0))) {
				connection.getWeight().inc(this.learningRate);
			} else {
				connection.getWeight().dec(this.learningRate);
			}
		}
	}	
	
}
