package MLP_CW2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MLP {
	
    // Main method to test the network (pseudo-code)
    public static void main(String[] args) {
        int inputSize = 64; // Example for 8x8 input images
        int hiddenSize = 30; // Example size
        int outputSize = 10; // For binary classification
        SimpleMLP mlp = new SimpleMLP(inputSize, hiddenSize, outputSize);
        
        double[][] data = readFile("resources/cw2Dataset1.csv");
        
        
        // Assume trainingInputs and trainingTargets are initialized and loaded with your data
        double[][] trainingInputs = readFile("resources/cw2Dataset1.csv"); // Your input data
        double[][] trainingTargets = readFile("resources/cw2Dataset2.csv"); // Your target data
//        
//       	for(int i = 0; i< getTargets(trainingInputs).length; i++) {
//    		for(int j = 0; j< getTargets(trainingInputs)[i].length; j++){
//    			System.out.print(" " + getTargets(trainingInputs)[i][j]);
//    		}
//    		System.out.println("");
//    	}
        
        mlp.train(getData(trainingInputs), getTargets(trainingInputs), 1000); // Example: 1000 epochs
    }

    public static double[][] getTargets(double[][] data){
    	double[][] targets = new double[data.length][10];
    	for(int row = 0; row< data.length; row++) {
    		for(int i = 0; i<10; i++) {
    			if(data[row][64] == i) {
    				targets[row][i] = 1;
    			}else {
    				targets[row][i] = 0;
    			}
    		}
    	}
    	
    	return targets;
    }
    
    public static double[][] getData(double[][] data) {
    	double[][] justData = new double[data.length][64];
    	for(int i = 0; i< data.length; i++) {
    		for(int j = 0; j< 64; j++){
    			justData[i][j] = data[i][j]/ 16;
    		}
    	}
    	return justData;
    }

	public static double[][] readFile(String filePath) {
		ArrayList<double[]> digitsFromFile = new ArrayList<>();

		try {
			File myObj = new File(filePath);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				String[] points = data.split(",");
				double[] rowPoints = new double[65];
				for (int i = 0; i < points.length; i++) {
					rowPoints[i] = Integer.valueOf(points[i]);
				}
				digitsFromFile.add(rowPoints);
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		double[][] dataset = new double[digitsFromFile.size()][65];
		for (int i = 0; i < digitsFromFile.size(); i++) {
			dataset[i] = digitsFromFile.get(i);
		}
		return dataset;
	}
}
