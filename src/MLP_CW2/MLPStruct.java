package MLP_CW2;

public class MLPStruct {
    private double[][] inputHiddenWeights; // Weights between input and hidden layer
    private double[][] hiddenOutputWeights; // Weights between hidden layer and output
    private double[] hiddenBiases; // Biases for hidden layer
    private double[] outputBiases; // Biases for output layer
    
    // Assuming a single hidden layer for simplicity
    public MLPStruct(int inputSize, int hiddenSize, int outputSize) {
        // Initialize weights and biases
        inputHiddenWeights = new double[inputSize][hiddenSize];
        hiddenOutputWeights = new double[hiddenSize][outputSize];
        hiddenBiases = new double[hiddenSize];
        outputBiases = new double[outputSize];
        
        // Randomly initialize weights, biases initialization can be added here
        initializeWeights();
    }
    
    private void initializeWeights() {
        // Initialize input to hidden weights
        for (int i = 0; i < inputHiddenWeights.length; i++) {
            for (int j = 0; j < inputHiddenWeights[i].length; j++) {
                // Subtracting 0.5 to get a range of [-0.5, 0.5], then multiplying by 2 to adjust to range [-1, 1]
                inputHiddenWeights[i][j] = (Math.random() - 0.5) * 2;
            }
        }

        // Initialize hidden to output weights
        for (int i = 0; i < hiddenOutputWeights.length; i++) {
            for (int j = 0; j < hiddenOutputWeights[i].length; j++) {
                // Similar approach for hidden to output weights
                hiddenOutputWeights[i][j] = (Math.random() - 0.5) * 2;
            }
        }

        // Initialize hidden biases
        for (int i = 0; i < hiddenBiases.length; i++) {
            hiddenBiases[i] = (Math.random() - 0.5) * 2;
        }

        // Initialize output biases
        for (int i = 0; i < outputBiases.length; i++) {
            outputBiases[i] = (Math.random() - 0.5) * 2;
        }
    }

    
    public double[] forwardPropagate(double[] inputs) {
        // Calculate inputs to hidden layer
        double[] hiddenInputs = new double[hiddenBiases.length]; // Same size as number of neurons in hidden layer
        for (int i = 0; i < hiddenInputs.length; i++) {
            for (int j = 0; j < inputs.length; j++) {
                hiddenInputs[i] += inputs[j] * inputHiddenWeights[j][i];
            }
            // Add bias
            hiddenInputs[i] += hiddenBiases[i];
        }

        // Apply sigmoid activation function to hidden layer inputs to get hidden outputs
        double[] hiddenOutputs = new double[hiddenInputs.length];
        for (int i = 0; i < hiddenInputs.length; i++) {
            hiddenOutputs[i] = sigmoid(hiddenInputs[i]);
        }

        // Calculate inputs to output layer
        double[] finalInputs = new double[outputBiases.length]; // Same size as number of neurons in output layer
        for (int i = 0; i < finalInputs.length; i++) {
            for (int j = 0; j < hiddenOutputs.length; j++) {
                finalInputs[i] += hiddenOutputs[j] * hiddenOutputWeights[j][i];
            }
            // Add bias
            finalInputs[i] += outputBiases[i];
        }

        // Apply sigmoid activation function to output layer inputs to get final outputs
        double[] finalOutputs = new double[finalInputs.length];
        for (int i = 0; i < finalInputs.length; i++) {
            finalOutputs[i] = sigmoid(finalInputs[i]);
        }

        return finalOutputs;
    }

    // Helper method for sigmoid activation function
    private double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    
    // Add methods for training (including backpropagation) and prediction
}
