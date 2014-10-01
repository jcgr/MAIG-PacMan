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
		System.out.println("=====");
		AND();
		System.out.println("=====");
		XOR();
		System.out.println("=====");
//		Test();
//		classificationByBackpropagationTest();
	}
	
	private static void AND()
	{
		int numberOfInputs = 2;
		int numberOfOutputs = 1;
		
		nn = new NeuralNetwork();

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
		nn.feedForward();

		double[] in1 = {0, 0};
		double[] in2 = {0, 1};
		double[] in3 = {1, 0};
		double[] in4 = {1, 1};

		double[] out1 = {1};
		double[] out2 = {0};
		
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
		
		Backpropagator bp = new Backpropagator(nn, 1.0);
		bp.train(ts, 0.05);
		
		nn.setInputs(in1);
		nn.feedForward();
		double result = nn.getOutput()[0];
		System.out.println("AND (0, 0) = " + result);
		
		nn.setInputs(in2);
		nn.feedForward();
		result = nn.getOutput()[0];
		System.out.println("AND (1, 0) = " + result);
		
		nn.setInputs(in3);
		nn.feedForward();
		result = nn.getOutput()[0];
		System.out.println("AND (0, 1) = " + result);
		
		nn.setInputs(in4);
		nn.feedForward();
		result = nn.getOutput()[0];
		System.out.println("AND (1, 1) = " + result);
	}
	
	private static void XOR()
	{
		int numberOfInputs = 2;
		int h1Neurons = 2;
		int numberOfOutputs = 1;
		
		nn = new NeuralNetwork();

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
		nn.feedForward();

		double[] in1 = {0, 0};
		double[] in2 = {0, 1};
		double[] in3 = {1, 0};
		double[] in4 = {1, 1};

		double[] out1 = {1};
		double[] out2 = {0};
		
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
		
		Backpropagator bp = new Backpropagator(nn, 1.0);
//		bp.log = true;
		bp.train(ts, 0.05);
		
		nn.setInputs(in1);
		nn.feedForward();
		double result = nn.getOutput()[0];
		System.out.println("XOR (0, 0) = " + result);
		
		nn.setInputs(in2);
		nn.feedForward();
		result = nn.getOutput()[0];
		System.out.println("XOR (1, 0) = " + result);
		
		nn.setInputs(in3);
		nn.feedForward();
		result = nn.getOutput()[0];
		System.out.println("XOR (0, 1) = " + result);
		
		nn.setInputs(in4);
		nn.feedForward();
		result = nn.getOutput()[0];
		System.out.println("XOR (1, 1) = " + result);
	}
	
	private static void Test()
	{
		int numberOfInputs = 14;
		int numberOfOutputs = 5;
		int h1LayerNeurons = 3;
		int h2LayerNeurons = 3;
		
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
		for (int i = 0; i < h1LayerNeurons; i++)
			h1Layer.addNeuron(new Neuron());
		
		NeuronLayer h2Layer = new NeuronLayer(input, bias);
		for (int i = 0; i < h2LayerNeurons; i++)
			h2Layer.addNeuron(new Neuron());
		
		NeuronLayer output = new NeuronLayer(h1Layer);
		for (int i = 0; i < numberOfOutputs; i++)
			output.addNeuron(new Neuron());
		
		nn.addLayer(input);
		nn.addLayer(h1Layer);
		nn.addLayer(output);
		
		nn.feedForward();
		
//		double[] results = nn.getOutput();
//		for(double d : results)
//			System.out.println(d);
		
		Backpropagator bp = new Backpropagator(nn, 1.0);
		bp.train(ts, 0.05);
		
		//nn.setInputs();
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

		nn.feedForward();

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
		nn.feedForward();
		System.out.println(nn.getOutput()[0]);
//		b.bpTest(nn, 0.9, ts, 0.05);
//		Backpropagator b = new Backpropagator(nn, 0.9, ts, 0);
	}
}
