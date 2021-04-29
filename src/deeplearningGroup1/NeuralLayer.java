package deeplearningGroup1;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * NeuralLayer holds the weights and biases corresponding to a single layer in a neural network 
 * in {@link Matrix} form. It also provides support in training the neural net by calculating the 
 * partial derivatives of the cost function.
 * @author Steven Rose
 * @version 2.0
 * @see Jarvis
 */
public class NeuralLayer {
	
	/**
	 * A unique string id for each layer of the neural network, useful for differentiating 
	 * between layers when reading log files or console output.
	 */
	private String id;
	
	/**
	 * A {@link Matrix} of the weights for a layer of the neural network, of size [output x input], 
	 * where output is the number of neurons in this layer, and input is the number of neurons in 
	 * the previous layer.
	 */
	private Matrix weights;
	
	/**
	 * A {@link Matrix} of the biases for a layer of the neural network, of size [output x 1], 
	 * where output is the number of neurons in this layer.
	 */
	private Matrix bias;
	
	/**
	 * A {@link Matrix} of the activation values for this layer, calculated based on the input 
	 * from the previous layer.  Used for training the neural net.
	 */
	private Matrix activations;
	
	/**
	 * A {@link Matrix} of the weighted input from the previous layer.  Used for training the neural net.
	 */
	private Matrix weightedInput;
	
	/**
	 * A {@link Matrix} of the error in the output of this layer, defined as how changes in the 
	 * output of this layer affect the output of the neural net as a whole.  Used for training the 
	 * neural net.
	 */
	private Matrix outputError; //error in output of neuron, del C / del Z
	
	/**
	 * A {@link Matrix} of the error in the weights of this layer.  Used for training the neural net.
	 */
	private Matrix weightsError;
	
	/**
	 * A {@link Matrix} of the cost of this layer, ie the difference between the actual output of 
	 * this layer and the expected output.
	 */
	private Matrix cost;
	
	/**
	 * How quickly the neural net learns.  Set experimentally, must be >= 1.
	 */
	private final double modifier = 100;
	
	/**
	 * Creates a new NeuralLayer with an id, with known weights and biases provided.
	 * @param id
	 * 		String id for this layer.
	 * @param weights
	 * 		Matrix of weights for this layer.
	 * @param bias
	 * 		Matrix of biases for this layer.
	 */
	public NeuralLayer(String id, Matrix weights, Matrix bias) {
		this.id = id;
		this.weights = weights;
		this.bias = bias;
		this.activations = new Matrix(bias.getM(), 1);
		this.weightedInput = new Matrix(bias.getM(), 1);
	}
	
	/**
	 * Creates an empty NeuralLayer with an id and the size of the Matrices for the weights and biases.
	 * @param id
	 * 		String id for this layer.
	 * @param weightWidth
	 * 		Width of the matrix for weights (This is equal to the number of inputs to this layer).
	 * @param weightHeight
	 * 		Height of the matrix for weights (This is equal to the number of neurons in this layer).
	 * @param biasHeight
	 * 		height of the matrix for biases (This is equal to the number of neurons in this layer).
	 */
	public NeuralLayer(String id, int weightWidth, int weightHeight, int biasHeight) {
		if (weightHeight != biasHeight)
			throw new RuntimeException("Making a new Neural Layer, Weight and Bias dimensions don't match: weights = " +
					"[" + weightHeight + ", " + weightWidth + "]; bias = [" + biasHeight + ", 1]");
		
		this.id = id;
		double[][] weights = new double[weightHeight][weightWidth];
		double[][] bias = new double[biasHeight][1];
		
		for (int i = 0; i < weightHeight; i++) {
			for (int j = 0; j < weightWidth; j++) {
				weights[i][j] = 20*Math.random() - 10;
			}
		}
		
		for (int i = 0; i < biasHeight; i++) {
			bias[i][0] = 20*Math.random() - 10;
		}
		
		this.weights = new Matrix(weights);
		this.bias = new Matrix(bias);
		this.activations = new Matrix(biasHeight, 1);
		this.weightedInput = this.activations;
	}
	
	/**
	 * Calculates the sigmoid for each neuron in this layer. This ensures the output of each 
	 * neuron is in the range (0,1).
	 * @param input
	 * 		Column Matrix of inputs to this layer.
	 * @return
	 * 		Column matrix of activations for this layer.
	 */
	public Matrix calculateSigmoid(Matrix input) {
		if (weights.getN() != input.getM()) {
			System.out.println("ERROR: sigmoid size incompatibility:\nweights width = " + weights.getN() + ", input height = " + input.getM());
			//System.exit(1);
		}
		Matrix m = weights.multiply(input).plus(bias);
		weightedInput = new Matrix(m.getData());
		for (int i = 0; i < m.getM(); i++) {
			for (int j = 0; j < m.getN(); j++) {
				m.set(i, j, 1/(1+Math.exp(-1*m.get(i, j))));
			}
		}
		activations = new Matrix(m.getData());
		return m;
	}
	
	/**
	 * Calculates the error in the output of this layer (how far off expected the actual output 
	 * is). Used for training the neural net.
	 * @param delC_A
	 * 		The partial derivative of the total cost function with respect to the activations of 
	 * this layer.
	 * @return
	 * 		The error in the output for this layer.
	 */
	public Matrix calculateError(Matrix delC_A) {
		Matrix z = weightedInput;
		Matrix delA_Z = new Matrix(z.getM(), z.getN());
		double x;
		for (int i = 0; i < z.getM(); i++) {
			x = Math.exp(-1*z.get(i, 0));
			delA_Z.set(i, 0, x/Math.pow(1+x, 2));
		}
		outputError = delC_A.elementMultiply(delA_Z);
		return outputError;
	}
	
	/**
	 * Calculates the error in the output of this layer (how far off expected the actual output 
	 * is). Used for training the neural net.
	 * @param weightsNext
	 * 		The weights for the next layer in the neural net.
	 * @param errorNext
	 * 		The output error of the next layer in the neural net.
	 * @return
	 */
	public Matrix calculateError(Matrix weightsNext, Matrix errorNext) {
		Matrix z = weightedInput;
		Matrix delA_Z = new Matrix(z.getM(), 1);
		double x;
		for (int i = 0; i < z.getM(); i++) {
			x = Math.exp(-1*z.get(i, 0));
			delA_Z.set(i, 0, x/Math.pow(1+x, 2));
		}
		outputError = weightsNext.transpose().multiply(errorNext).elementMultiply(delA_Z);
		return outputError;
	}
	
	/**
	 * Calculates the error in the weights of this layer (how far off expected the actual values 
	 * are).  User for training the neural net.
	 * @param outputPrevious
	 * 		The output of the previous layer in the neural net.
	 * @return
	 * 		The error in the weights for this layer.
	 */
	public Matrix calculateWeightError(Matrix outputPrevious) {
		Matrix m = new Matrix(weights.getM(), weights.getN());
		for (int i = 0; i < m.getM(); i++)
			for (int j = 0; j < m.getN(); j++)
				m.set(i, j, outputPrevious.get(j, 0) * outputError.get(i, 0));
		
		weightsError = m;
		return m;
	}
	
	/**
	 * Calculates the cost for this layer using a quadratic cost function.
	 * @param desiredOutputs
	 * 		The desired output values for this layer.
	 * @return
	 * 		The cost of the error in the outputs for this layer.
	 */
	public Matrix calculateCost(Matrix desiredOutputs) {
		cost = desiredOutputs.minus(activations).pow(2); //quadratic cost function
		return cost;
	}
	
	/**
	 * Changes the bias Matrix by a small change multipled by the {@link NeuralLayer#modifier}
	 * @param delta
	 * 		The small change to the bias.
	 */
	public void changeBias(Matrix delta) {
		//delta.fixNaN();
		bias = bias.plus(delta.multiply(modifier));
	}
	
	/**
	 * Changes the weights Matrix by a small change multipled by the {@link NeuralLayer#modifier}
	 * @param delta
	 * 		The small change to the bias.
	 */
	public void changeWeights(Matrix delta) {
		//delta.fixNaN();
		weights = weights.plus(delta.multiply(modifier));
	}
	
	/**
	 * Returns the Matrix of weights for this layer.
	 * @return
	 * 		Matrix of weights.
	 */
	public Matrix getWeights() {
		return weights;
	}
	
	/**
	 * Returns the Matrix of biases for this layer.
	 * @return
	 * 		Matrix of biases.
	 */
	public Matrix getBias() {
		return bias;
	}
	
	/**
	 * Returns the Matrix of activations for this layer.
	 * @return
	 * 		Matrix of activations.
	 */
	public Matrix getActivations() {
		return activations;
	}
	
	/**
	 * Returns the Matrix of the weighted input for this layer.
	 * @return
	 * 		Matrix of the weighted input.
	 */
	public Matrix getWeightedInput() {
		return weightedInput;
	}
	
	/**
	 * Returns the Matrix of the bias error for this layer.
	 * @return
	 * 		Matrix of the bias error.
	 */
	public Matrix getBiasError() {
		return outputError;
	}
	
	/**
	 * Returns the Matrix of the weights error for this layer.
	 * @return
	 * 		Matrix of the weights error.
	 */
	public Matrix getWeightsError() {
		return weightsError;
	}

	/**
	 * Prints the id and the weights and bias Matrices to the console and writes them 
	 * to the log file if {@link Jarvis#debugging} is set to true.
	 */
	public void print() {
		if (Jarvis.debugging) {
			log("id: " + id);
			log("weights:");
			weights.print();
			log("biases:");
			bias.print();
		}
	}

	/**
	 * Prints all of the data for this neural layer to the console and writes it to the 
	 * log file if {@link Jarvis#debugging} is set to true.
	 */
	public void printAll() {
		if (Jarvis.debugging) {
			print();
			log("weighted inputs:");
			weightedInput.print();
			log("activations:");
			activations.print();
			log("bias error:");
			outputError.print();
			log("weights error:");
			weightsError.print();
			if (cost != null) {
				log("cost:");
				cost.print();
			}
		}
	}

	/**
	 * Prints a String to the console and writes it to a log file if {@link Jarvis#debugging} 
	 * is set to true.
	 * @param msg
	 * 		Message to be printed.
	 */
	private void log(String msg) {
		if (Jarvis.debugging) {
			System.out.println(msg);
			PrintWriter out;
			try {
				out = new PrintWriter(new FileWriter("resources/log_file.txt", true));
				out.println(msg);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
