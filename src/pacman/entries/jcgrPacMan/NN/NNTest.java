/**
 * 
 */
package pacman.entries.jcgrPacMan.NN;

import pacman.entries.jcgrPacMan.NN.training.TrainingData;
import pacman.entries.jcgrPacMan.NN.training.TrainingSet;
import pacman.entries.jcgrPacMan.NNPacMan.NNPacMan;

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
		NNPacMan.newcontroller();
		System.out.println("Done training");
//		System.out.println("=====");
//		AND();
//		System.out.println("=====");
//		XOR();
//		System.out.println("=====");
//		Test();
//		classificationByBackpropagationTest();
	}
	
	private static void AND()
	{
		nn = new NeuralNetwork("AND");

		Neuron bias = new Neuron();
		bias.setOutput(1.0);
		
		Neuron i1 = new Neuron();
		i1.number = 0;
		Neuron i2 = new Neuron();
		i2.number = 1;
		
		Neuron o1 = new Neuron();
		o1.number = 9;
		
		NeuronLayer inputLayer = new NeuronLayer(bias);
		inputLayer.addNeuron(i1);
		inputLayer.addNeuron(i2);
		
		NeuronLayer outputLayer = new NeuronLayer(inputLayer);
		outputLayer.addNeuron(o1);
		
		nn.addLayer(inputLayer);
		nn.addLayer(outputLayer);
		nn.activate();

		double[] in1 = {0, 0};
		double[] in2 = {0, 1};
		double[] in3 = {1, 0};
		double[] in4 = {1, 1};

		double[] out1 = {0.95};
		double[] out2 = {0.05};
		
		TrainingSet ts = new TrainingSet();
		TrainingData td1 = new TrainingData(2, 1);
		td1.setData(in1, out2);

		TrainingData td2 = new TrainingData(2, 1);
		td2.setData(in2, out2);

		TrainingData td3 = new TrainingData(2, 1);
		td3.setData(in3, out2);

		TrainingData td4 = new TrainingData(2, 1);
		td4.setData(in4, out1);

		ts.AddTrainingData(td1);
		ts.AddTrainingData(td2);
		ts.AddTrainingData(td3);
		ts.AddTrainingData(td4);
		
		Backpropagator bp = new Backpropagator(nn);
		bp.train(ts);
		
		nn.setInputs(in1);
		nn.activate();
		double result = nn.getOutput()[0];
		System.out.println("AND (0, 0) = " + result);
		
		nn.setInputs(in2);
		nn.activate();
		result = nn.getOutput()[0];
		System.out.println("AND (1, 0) = " + result);
		
		nn.setInputs(in3);
		nn.activate();
		result = nn.getOutput()[0];
		System.out.println("AND (0, 1) = " + result);
		
		nn.setInputs(in4);
		nn.activate();
		result = nn.getOutput()[0];
		System.out.println("AND (1, 1) = " + result);
	}
	
	private static void XOR()
	{
		nn = new NeuralNetwork("XOR");

		Neuron bias = new Neuron();
		bias.setOutput(1.0);

		Neuron i1 = new Neuron();
		i1.number = 0;
		Neuron i2 = new Neuron();
		i2.number = 1;

		Neuron h1 = new Neuron();
		h1.number = 4;
		Neuron h2 = new Neuron();
		h2.number = 5;
		
		Neuron o1 = new Neuron();
		o1.number = 9;
		
		NeuronLayer inputLayer = new NeuronLayer(bias);
		inputLayer.addNeuron(i1);
		inputLayer.addNeuron(i2);

		NeuronLayer h1Layer = new NeuronLayer(inputLayer, bias);
		h1Layer.addNeuron(h1);
		h1Layer.addNeuron(h2);
		
		NeuronLayer outputLayer = new NeuronLayer(h1Layer);
		outputLayer.addNeuron(o1);
		
		nn.addLayer(inputLayer);
		nn.addLayer(h1Layer);
		nn.addLayer(outputLayer);
		nn.activate();

		double[] in1 = {0, 0};
		double[] in2 = {0, 1};
		double[] in3 = {1, 0};
		double[] in4 = {1, 1};

		double[] out1 = {0.95};
		double[] out2 = {0.05};
		
		TrainingSet ts = new TrainingSet();
		TrainingData td1 = new TrainingData(2, 1);
		td1.setData(in1, out2);

		TrainingData td2 = new TrainingData(2, 1);
		td2.setData(in2, out1);

		TrainingData td3 = new TrainingData(2, 1);
		td3.setData(in3, out1);

		TrainingData td4 = new TrainingData(2, 1);
		td4.setData(in4, out2);

		ts.AddTrainingData(td1);
		ts.AddTrainingData(td2);
		ts.AddTrainingData(td3);
		ts.AddTrainingData(td4);
	
		Backpropagator bp = new Backpropagator(nn);
		bp.train(ts);
		
		nn.setInputs(in1);
		nn.activate();
		double result = nn.getOutput()[0];
		System.out.println("XOR (0, 0) = " + result);
		
		nn.setInputs(in2);
		nn.activate();
		result = nn.getOutput()[0];
		System.out.println("XOR (1, 0) = " + result);
		
		nn.setInputs(in3);
		nn.activate();
		result = nn.getOutput()[0];
		System.out.println("XOR (0, 1) = " + result);
		
		nn.setInputs(in4);
		nn.activate();
		result = nn.getOutput()[0];
		System.out.println("XOR (1, 1) = " + result);
	}

	/**
	 * Runs the test from "Classification by Backpropagation" 
	 * Example 9.1 in section 9.2.3.
	 */
	private static void classificationByBackpropagationTest()
	{
		// Initialize nn
		nn = new NeuralNetwork("cBBT");

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

		nn.activate();

		TrainingSet ts = new TrainingSet();
		TrainingData td = new TrainingData(3, 1);
		double[] in =
		{ 1, 0, 1 };
		double[] out =
		{ 1 };
		td.setData(in, out);
		ts.AddTrainingData(td);

		Backpropagator bp = new Backpropagator(nn);
		bp.train(ts);
		
		nn.setInputs(in);
		nn.activate();
		System.out.println(nn.getOutput()[0]);
	}
}
