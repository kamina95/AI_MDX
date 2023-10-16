import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TSPMain {

	public static ArrayList<Node> readFile(String fileName) {

		ArrayList<Node> nodeArr = new ArrayList<>();
		try {
			File myObj = new File(fileName);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				System.out.println(data);
				List<String> nodeData = Arrays.asList(data.trim().split(" "));
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
	
	public static void NearNeighbour(ArrayList<Node> nodes) {
		 Node firstNode =nodes.remove(0);
		 Double closestNodeDistance;
		 Node nextNode = firstNode;
		 Node tempNode = null;
		 Double totalDistance = 0.0;
		 StringBuffer stringBuffer = new StringBuffer();
		 stringBuffer.append(firstNode.getId() + "->");
		 
		 while(!nodes.isEmpty()) {
			 closestNodeDistance = Double.MAX_VALUE;
			 for(Node possibleNode : nodes) {
				 if(nextNode.euclidianDistance(possibleNode) < closestNodeDistance) {
					 tempNode = possibleNode;
				 }
			 }
			 
			 totalDistance = totalDistance + nextNode.euclidianDistance(tempNode);
			 stringBuffer.append(tempNode.getId() + "->");
			 nextNode = tempNode;
			 nodes.remove(nextNode);
		 }
		 
		 stringBuffer.append(firstNode.getId());
		 totalDistance = totalDistance + nextNode.euclidianDistance(firstNode);
		 
		System.out.println(stringBuffer.toString() +"\nTotal Distance: " + totalDistance );
	}

	public static void main(String[] args) {
		
		ArrayList<Node> nodeArr =readFile("resources/train1.txt");
		NearNeighbour(nodeArr);
		
//		for(Node e : nodeArr) {
//			System.out.println(e.toString());
//		}

	}
}
