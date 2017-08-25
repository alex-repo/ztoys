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

import com.ap.common.model.EuclideanVector;
import com.ap.common.model.VectorInjective;
import com.ap.common.model.VectorInjectiveSpace;
import com.ap.nn.model.Iterative;

import java.util.Iterator;

//FIXME comment
/**
 * Base class for all unsupervised learning algorithms.
 * 
 */
abstract public class UnsupervisedLearning extends Iterative {
	
	/**
	 * The class fingerprint that is set to indicate serialization 
	 * compatibility with a previous version of the class
	 */	
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new unsupervised learning rule
	 */
	public UnsupervisedLearning() {
		super();
	}


	/**
	 * This method does one learning epoch for the unsupervised learning rules.
	 * It iterates through the training set and trains network weights for each
	 * element
	 * 
	 * @param trainingSet
	 *            training set for training network
	 */
         @Override
	public void epoch(VectorInjectiveSpace trainingSet) {
		Iterator<VectorInjective> iterator = trainingSet.iterator();
		while (iterator.hasNext() && !isStopped()) {
			VectorInjective trainingSetRow = iterator.next();
			learnPattern(trainingSetRow);
		}
	}	
	
	/**
	 * Trains network with the pattern from the specified training element
	 * 
	 * @param DataSetItem
	 *            unsupervised training element which contains network input
	 */
	protected void learnPattern(VectorInjective trainingElement) {
		EuclideanVector input = trainingElement.getInput();
		getNetwork().setInput(input);
		getNetwork().calculate();
		this.updateNetworkWeights();
	}



	/**
	 * This method implements the gain adjustment
	 */
	abstract protected void updateNetworkWeights();

}