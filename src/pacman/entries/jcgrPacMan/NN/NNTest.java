/**
 * 
 */
package pacman.entries.jcgrPacMan.NN;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;
import pacman.entries.jcgrPacMan.NN.training.PacManTrainingData;
import pacman.entries.jcgrPacMan.NN.training.TrainingData;
import pacman.entries.jcgrPacMan.NN.training.TrainingSet;

/**
 * 
 * 
 * @author Jacob
 */
public class NNTest
{
	
	static NeuralNetwork nn;

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
//		Test();
		classificationByBackpropagationTest();
	}
	
	private static void Test()
	{
		int numberOfInputs = 21;
		int numberOfOutputs = 5;
		int hiddenLayerNeurons = 10;
		
		nn = new NeuralNetwork();
		
		Neuron bias = new Neuron();
		bias.setOutput(1.0);
		
		DataTuple[] dta = DataSaverLoader.LoadPacManData();
		TrainingSet ts = new TrainingSet();
		for (int i = 0; i < dta.length; i++)
			ts.AddTrainingData(new PacManTrainingData(dta[i], numberOfInputs, numberOfOutputs));
		
		NeuronLayer input = new NeuronLayer(bias);
		for (int i = 0; i < numberOfInputs; i++)
			input.addNeuron(new Neuron());
		
		NeuronLayer h1Layer = new NeuronLayer(input, bias);
		for (int i = 0; i < hiddenLayerNeurons; i++)
			h1Layer.addNeuron(new Neuron());
		
		NeuronLayer output = new NeuronLayer(h1Layer);
		for (int i = 0; i < numberOfOutputs; i++)
			output.addNeuron(new Neuron());
		
		nn.addLayer(input);
		nn.addLayer(h1Layer);
		nn.addLayer(output);
		
		nn.run();
		
		double[] results = nn.getOutput();
		for(double d : results)
			System.out.println(d);
		
		Backpropagator bp = new Backpropagator(nn, 1.0);
		bp.train(ts, 0.05);
//		Backpropagator bp = new Backpropagator(nn, 1.0, ts, 0.05);
	}

	/**
	 * Runs the test from "Classification by Backpropagation" 
	 * Example 9.1 in section 9.2.3.
	 */
	private static void classificationByBackpropagationTest()
	{
		// Initialize nn
		nn = new NeuralNetwork();

		Neuron bias = new Neuron();
		bias.setOutput(1.0);

		// Input neurons
		Neuron x1 = new Neuron();
		x1.setOutput(1.0);
		x1.number = 1;

		Neuron x2 = new Neuron();
		x2.setOutput(0.0);
		x2.number = 2;

		Neuron x3 = new Neuron();
		x3.setOutput(1.0);
		x3.number = 3;

		// Input layer
		NeuronLayer inputLayer = new NeuronLayer(bias);
		inputLayer.addNeuron(x1);
		inputLayer.addNeuron(x2);
		inputLayer.addNeuron(x3);

		// Hidden neurons
		Neuron h1 = new Neuron();
		double[] h1weights =
		{ -0.4, 0.2, 0.4, -0.5 };
		h1.number = 4;

		Neuron h2 = new Neuron();
		double[] h2weights =
		{ 0.2, -0.3, 0.1, 0.2 };
		h2.number = 5;

		// Hidden layer
		NeuronLayer hLayer = new NeuronLayer(inputLayer, bias);
		hLayer.addNeuron(h1, h1weights);
		hLayer.addNeuron(h2, h2weights);

		// Output neuron
		Neuron o1 = new Neuron();
		double[] o1weights =
		{ 0.1, -0.3, -0.2 };
		o1.number = 6;

		// Output layer
		NeuronLayer outputLayer = new NeuronLayer(hLayer);
		outputLayer.addNeuron(o1, o1weights);

		nn.addLayer(inputLayer);
		nn.addLayer(hLayer);
		nn.addLayer(outputLayer);

		nn.run();

		TrainingSet ts = new TrainingSet();
		TrainingData td = new TrainingData(3, 1);
		double[] in =
		{ 1, 0, 1 };
		double[] out =
		{ 1 };
		td.setData(in, out);
		ts.AddTrainingData(td);

		Backpropagator b = new Backpropagator(nn, 0.9);
		b.train(ts, 0.05);
		
		nn.setInputs(in);
		nn.run();
		System.out.println(nn.getOutput()[0]);
//		b.bpTest(nn, 0.9, ts, 0.05);
//		Backpropagator b = new Backpropagator(nn, 0.9, ts, 0);
	}
}
