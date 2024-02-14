package MLP_CW2;

public class SimpleMLP {
    private double[][] inputHiddenWeights;
    private double[][] hiddenOutputWeights;
    private double[] hiddenBiases;
    private double[] outputBiases;
    private int inputSize;
    private int hiddenSize;
    private int outputSize;
    private double learningRate = 0.1;
    double[] hiddenInputs = new double[hiddenSize];
    double[] hiddenOutputs = new double[hiddenSize];

    public SimpleMLP(int inputSize, int hiddenSize, int outputSize) {
        this.inputSize = inputSize;
        this.hiddenSize = hiddenSize;
        this.outputSize = outputSize;
        this.inputHiddenWeights = new double[inputSize][hiddenSize];
        this.hiddenOutputWeights = new double[hiddenSize][outputSize];
        this.hiddenBiases = new double[hiddenSize];
        this.outputBiases = new double[outputSize];
        initializeWeights();
    }

    private void initializeWeights() {
        // Initialize weights with small random values
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                inputHiddenWeights[i][j] = Math.random() - 0.5;
            }
        }
        for (int i = 0; i < hiddenSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                hiddenOutputWeights[i][j] = Math.random() - 0.5;
            }
            hiddenBiases[i] = Math.random() - 0.5;
        }
        for (int i = 0; i < outputSize; i++) {
            outputBiases[i] = Math.random() - 0.5;
        }
    }

    private double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    private double sigmoidDerivative(double output) {
        return output * (1.0 - output);
    }

    public double[] forwardPropagate(double[] inputs) {
        hiddenInputs = new double[hiddenSize];
        hiddenOutputs = new double[hiddenSize];
        double[] finalOutputs = new double[outputSize];

        // Calculate inputs to hidden layer
        for (int i = 0; i < hiddenSize; i++) {
            for (int j = 0; j < inputSize; j++) {
                hiddenInputs[i] += inputs[j] * inputHiddenWeights[j][i];
            }
            hiddenInputs[i] += hiddenBiases[i];
            hiddenOutputs[i] = sigmoid(hiddenInputs[i]);
        }

        // Calculate inputs to output layer
        for (int i = 0; i < outputSize; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                finalOutputs[i] += hiddenOutputs[j] * hiddenOutputWeights[j][i];
            }
            finalOutputs[i] += outputBiases[i];
            finalOutputs[i] = sigmoid(finalOutputs[i]);
        }

        return finalOutputs;
    }
    
    
//weight(old) + learning rate * output error * output(neurons i) * output(neurons i+1) * ( 1 - output(neurons i+1) )
    public void backpropagate(double[] inputs, double[] targetOutputs, double[] actualOutputs) {
        // Calculate output layer error
        double[] outputErrors = new double[outputSize]; // outputSize is now 10
        for (int i = 0; i < outputSize; i++) {
            outputErrors[i] = (targetOutputs[i] - actualOutputs[i]) * sigmoidDerivative(actualOutputs[i]);
        }

        // Calculate hidden layer error
        double[] hiddenErrors = new double[hiddenSize];
        for (int i = 0; i < hiddenSize; i++) {
            hiddenErrors[i] = 0; // Initialize to ensure clean slate for accumulation
            for (int j = 0; j < outputSize; j++) {
                hiddenErrors[i] += outputErrors[j] * hiddenOutputWeights[i][j];
            }
            hiddenErrors[i] *= sigmoidDerivative(hiddenOutputs[i]); // Assuming hiddenOutputs is accessible
        }

        // Update weights and biases for hidden to output layer
        for (int i = 0; i < hiddenSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                hiddenOutputWeights[i][j] -= learningRate * outputErrors[j] * hiddenOutputs[i];
            }
        }
        // Update output biases
        for (int i = 0; i < outputSize; i++) {
            outputBiases[i] -= learningRate * outputErrors[i];
        }

        // Update weights and biases for input to hidden layer
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                inputHiddenWeights[i][j] -= learningRate * hiddenErrors[j] * inputs[i];
            }
        }
        // Update hidden biases
        for (int i = 0; i < hiddenSize; i++) {
            hiddenBiases[i] -= learningRate * hiddenErrors[i];
        }
    }


    public void train(double[][] trainingInputs, double[][] trainingTargets, int epochs) {
        for (int epoch = 0; epoch < epochs; epoch++) {
//            double loss = 0;
            for (int i = 0; i < trainingInputs.length; i++) {
                double[] outputs = forwardPropagate(trainingInputs[i]);
                backpropagate(trainingInputs[i], trainingTargets[i], outputs);
                // Optionally, accumulate loss here for logging
            }

            // Calculate and log accuracy after each epoch
            double accuracy = calculateAccuracy(trainingInputs, trainingTargets);
            System.out.println("Epoch " + (epoch + 1) + "/" + epochs + " - Accuracy: " + accuracy + "%");
        }
    }

    
    private double calculateAccuracy(double[][] inputs, double[][] targets) {
        int correctPredictions = 0;
        for (int i = 0; i < inputs.length; i++) {
            double[] predictedOutputs = forwardPropagate(inputs[i]);
            int predicted = maxIndex(predictedOutputs);
            int actual = maxIndex(targets[i]);

            if (predicted == actual) {
                correctPredictions++;
            }
        }
        return (double) correctPredictions / inputs.length * 100; // Returns accuracy as a percentage
    }

    private int maxIndex(double[] array) {
        int maxIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }


}
