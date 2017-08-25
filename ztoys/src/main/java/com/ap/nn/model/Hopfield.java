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

package com.ap.nn.model;

import com.ap.nn.core.NetworkContainer;
import com.ap.nn.core.Nucleus;
import com.ap.nn.core.NucleusContainer;
import com.ap.nn.model.learning.BinaryHebbianLearning;
//FIXME comment
/**
 * Hopfield neural network.
 * Notes: try to use [1, -1] activation levels, sign as transfer function, or real numbers for activation
 */

public class Hopfield extends NetworkContainer {
	
	public Hopfield(int nucleusCount) {
		createNetwork(nucleusCount, new InputOutput());
	}

	public Hopfield(int nucleusCount, Nucleus nuclearGenome) {
		createNetwork(nucleusCount, nuclearGenome);
	}

	private void createNetwork(int nucleusCount, Nucleus nuclearGenome) {

		NucleusContainer layer = new NucleusContainer(nucleusCount, nuclearGenome);

		// createLayer full connectivity in layer
		selfConnect(layer, 0.1);

		// add layer to network
		addLayer(layer);

		// set input and output cells for this network
		initInputOutput();

		// set Hopfield learning rule for this network
		//this.setLearningRule(new HopfieldLearning(this));	
		this.setLearning(new BinaryHebbianLearning());			
	}
	
	/**
	 * Sets default input and output neurons for network (first layer as input,
	 * last as output)
	 */
//from	public class NeuralNetworkFactory {
//	public static void setDefaultIO(NeuralNetwork nnet) {
//               ArrayList<Neuron> inputNeuronsList = new ArrayList<>();
//                LayerContainer firstLayer = nnet.getLayerAt(0);
//                for (Neuron neuron : firstLayer.getNeurons() ) {
//                    if (!(neuron instanceof BiasNeuron)) {  // dont set input to bias neurons
//                        inputNeuronsList.add(neuron);
//                    }
//                }
//
//                Neuron[] inputNeurons = new Neuron[inputNeuronsList.size()];
//                inputNeurons = inputNeuronsList.toArray(inputNeurons);
//		Neuron[] outputNeurons = ((LayerContainer) nnet.getLayerAt(nnet.getLayersCount()-1)).getNeurons();
//
//		nnet.setInputNeurons(inputNeurons);
//		nnet.setOutputNeurons(outputNeurons); 
//	}


}
