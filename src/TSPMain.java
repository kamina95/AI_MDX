import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TSPMain {

	public static double[][] distances;
	public static int[] tour;
	public static ArrayList<Node> nodes;

	public static ArrayList<Node> readFile(String fileName) {

		ArrayList<Node> nodeArr = new ArrayList<>();
		try {
			File myObj = new File(fileName);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				System.out.println(data);
				List<String> nodeData = Arrays.asList(data.trim().split("\\s+"));
				nodeArr.add(new Node(Integer.valueOf(nodeData.get(0)), Integer.valueOf(nodeData.get(1)),
						Integer.valueOf(nodeData.get(2))));
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return nodeArr;
	}

	private static double[][] initDistanceTable() {
		double[][] res = new double[nodes.size()][nodes.size()];
		for (int i = 0; i < nodes.size() - 1; ++i) {
			for (int j = i + 1; j < nodes.size(); ++j) {
				res[i][j] = nodes.get(i).euclidianDistance(nodes.get(j));

				res[j][i] = res[i][j];
			}
		}
		return res;
	}

	public static double getDistance(int[] tour) {
		double sum = 0;

		for (int i = 0; i < tour.length - 1; i++) {
			int a = tour[i]; // <->
			int b = tour[(i + 1)]; // <->
			sum += distances[a][b];
		}

		return sum;
	}

	public static void swapEdges(int a, int b, int c, int d) {
		// Find the positions of a, b, c, and d in the tour
		int aIndex = -1, bIndex = -1, cIndex = -1, dIndex = -1;
		for (int i = 0; i < tour.length; i++) {
			if (tour[i] == a)
				aIndex = i;
			if (tour[i] == b)
				bIndex = i;
			if (tour[i] == c)
				cIndex = i;
			if (tour[i] == d)
				dIndex = i;
		}

		// Reverse the segment between b and c (inclusive of b, exclusive of c)
		int start = bIndex, end = cIndex;
		while (start < end) {
			int temp = tour[start];
			tour[start] = tour[end - 1];
			tour[end - 1] = temp;
			start++;
			end--;
		}
	}

	public static void linKernighan() {
		int maxIterations = 10000;
		int iterationCount = 0;
		boolean improvement = true;
		while (improvement && iterationCount < maxIterations) {
			improvement = false;

			for (int a = 0; a < tour.length - 1; a++) {
				int b = (a + 1) % tour.length;
				for (int c = 0; c < tour.length - 1; c++) {
					int d = (c + 1) % tour.length;

					if (a == c || a == d || b == c || b == d)
						continue;

					if (shouldSwap(a, b, c, d)) {
						swapEdges(tour[a], tour[b], tour[c], tour[d]);
						improvement = true;
					}
				}
			}
			iterationCount++;
		}

	}

	public static void threeOpt() {
		boolean improvement = true;
		while (improvement) {
			improvement = false;

			for (int i = 0; i < tour.length - 2; i++) {
				for (int j = i + 1; j < tour.length - 1; j++) {
					for (int k = j + 1; k < tour.length; k++) {
						
						double currentDist = distances[tour[i]][tour[(i + 1) % tour.length]]
								+ distances[tour[j]][tour[(j + 1) % tour.length]]
								+ distances[tour[k]][tour[(k + 1) % tour.length]];

						double newDist1 = distances[tour[i]][tour[j]] + distances[tour[(i + 1) % tour.length]][tour[k]]
								+ distances[tour[(j + 1) % tour.length]][tour[(k + 1) % tour.length]];

						double newDist2 = distances[tour[i]][tour[k]] + distances[tour[(i + 1) % tour.length]][tour[j]]
								+ distances[tour[(j + 1) % tour.length]][tour[(k + 1) % tour.length]];

						if (newDist1 < currentDist || newDist2 < currentDist) {
							improvement = true;
							if (newDist1 < newDist2) {
								// Reconnect using first configuration
								reconnect(i, (i + 1) % tour.length, j, (j + 1) % tour.length, k, (k + 1) % tour.length);
							} else {
								// Reconnect using second configuration
								reconnect(i, k, (i + 1) % tour.length, j, (j + 1) % tour.length, (k + 1) % tour.length);
							}
						}
					}
				}
			}
		}
	}

	public static void reconnect(int a, int b, int c, int d, int e, int f) {
	    int[] newTour = new int[tour.length];
	    int index = 0;

	    // Ensure provided indices are valid
	    if (a < 0 || a >= tour.length || 
	        b < 0 || b >= tour.length || 
	        c < 0 || c >= tour.length || 
	        d < 0 || d >= tour.length || 
	        e < 0 || e >= tour.length || 
	        f < 0 || f >= tour.length) {
	        return; // Invalid indices, exit the method
	    }

	    // Copy until 'a'
	    for (int i = 0; i <= a && index < tour.length; i++) {
	        newTour[index++] = tour[i];
	    }

	    // Copy from 'b' to 'c'
	    for (int i = b; i != c && index < tour.length; i = (i + 1) % tour.length) {
	        newTour[index++] = tour[i];
	    }

	    // Copy from 'd' to 'e'
	    for (int i = d; i != e && index < tour.length; i = (i + 1) % tour.length) {
	        newTour[index++] = tour[i];
	    }

	    // Copy remaining
	    for (int i = f; i != a && index < tour.length; i = (i + 1) % tour.length) {
	        newTour[index++] = tour[i];
	    }

	    // If there are any remaining positions in the newTour, fill them from the original tour
	    while (index < newTour.length) {
	        newTour[index] = tour[index];
	        index++;
	    }

	    tour = newTour;
	}


	public static boolean shouldSwap(int a, int b, int c, int d) {
		double currentDistance = distances[tour[a]][tour[b]] + distances[tour[c]][tour[d]];
		double newDistance = distances[tour[a]][tour[c]] + distances[tour[b]][tour[d]];
		return newDistance < currentDistance;
	}

	public static void NearNeighbour(ArrayList<Node> nodeList) {
		int[] tempTour = new int[nodeList.size() + 1];
		Node firstNode = nodeList.remove(0);
		ArrayList<Node> tempList = new ArrayList<>();
		tempList.addAll(nodeList);
		Double closestNodeDistance;
		Node nextNode = firstNode;
		Node tempNode = null;
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(firstNode.getId() + "->");
		tempTour[0] = firstNode.getId();
		int i = 1;

		while (!tempList.isEmpty()) {
			closestNodeDistance = Double.MAX_VALUE;
			for (Node possibleNode : tempList) {
				if (nextNode.euclidianDistance(possibleNode) < closestNodeDistance) {
					tempNode = possibleNode;
				}
			}

			stringBuffer.append(tempNode.getId() + "->");
			tempTour[i] = tempNode.getId();
			nextNode = tempNode;
			tempList.remove(nextNode);
			i++;
		}
		tempTour[i] = firstNode.getId();
		stringBuffer.append(firstNode.getId());
		System.out.println(getDistance(tempTour));

		tour = tempTour;
	}

	public static void main(String[] args) {

		nodes = readFile("resources/sample4-22.txt");

		distances = initDistanceTable();

		double startTime = System.nanoTime();

		ArrayList<Node> nodesList = new ArrayList<>();
		nodesList.addAll(nodes);

		NearNeighbour(nodesList);

		linKernighan();
		//threeOpt();

		double finishTime = System.nanoTime();

		System.out.println("Time on ms to complete: " + ((finishTime - startTime) / 1000000));

		for (int e : tour) {
			System.out.print(e + " ");
		}

		System.out.println("\n" + getDistance(tour));
	}
}
