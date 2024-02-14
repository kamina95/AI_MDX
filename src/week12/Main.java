package week12;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
//		printDigits(readFile("digits.csv"));
		createDatasets(readFile("resources/cw2Dataset2.csv"),readFile("resources/cw2Dataset1.csv"));
	}
	
	public static void createDatasets(int[][] trainData, int[][] testData) {
		double total = 0;
		double correct = 0;
		for(int[] trainRow : trainData ) {
			System.out.print(closestEuclidean(trainRow, testData));
			if(closestEuclidean(trainRow, testData) == trainRow[64]) {
				System.out.println( "  -> Is the correct resutl.");
				correct++;
			}else {
				System.out.println( "  -> Is incorrcect, should be: " + trainRow[64]);
			}
			total++;
		}
		System.out.println("Total : " + total + " Correct : " + correct + " percentaje = " + (correct/total) * 100);
	}
	
	
	public static int closestEuclidean(int[] digit, int[][] testData) {
		double distance = Double.MAX_VALUE;
		int closestDigit = 0;
		for(int[] test : testData) {
			if(euclideanDistance(digit, test) < distance) {
				distance = euclideanDistance(digit, test);
				closestDigit = test[64];
			}
		}
		
		return closestDigit;
	}
	
	public static double euclideanDistance(int[] digit, int[] trainData) {
		double total = 0;
		for(int i = 0; i< 64; i++) {
			total = total + ((digit[i] - trainData[i]) * (digit[i] - trainData[i]));
		}
		
		return Math.sqrt(total);
	}
	
	public static void printDigits(int[][] digitsPoints) {
		for(int[] row : digitsPoints) {
			int pos = 0;
			for(int i = 0; i < 8 ; i++) {
				for(int j = 0; j < 8 ; j++) {
					System.out.print(printPoint(row[pos]) + " ");
					pos++;
				}
				System.out.println();
			}
			System.out.println(row[64] + "\n\n");
		}
	}
	
	public static String printPoint(int num) {
		
		if(num == 0) 
			return " ";	
		if(num <5) 
			return ".";		
		if(num < 10) 
			return "x";
		
		return "X";
	}

	
	public static int[][] readFile(String filePath) {
		ArrayList<int[]> digitsFromFile =  new ArrayList<>();
		
	    try {
	        File myObj = new File(filePath);
	        Scanner myReader = new Scanner(myObj);
	        while (myReader.hasNextLine()) {
	          String data = myReader.nextLine();
	          String[] points =  data.split(",");
	          int[] rowPoints = new int[65];
	          for(int i = 0; i < points.length; i++) {
	        	  rowPoints[i] = Integer.valueOf(points[i]);
	          }
	          digitsFromFile.add(rowPoints);
	        }
	        myReader.close();
	      } catch (FileNotFoundException e) {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	      }
	    
	    int[][] dataset = new int[digitsFromFile.size()][65];
	    for(int i = 0; i<digitsFromFile.size(); i++) {
	    	dataset[i] = digitsFromFile.get(i);
	    }
		return dataset;
	}
}

