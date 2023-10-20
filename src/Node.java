
public class Node {
	int id;
	int X;
	int Y;
	
	public Node(int id, int X, int Y) {
		this.id = id - 1;
		this.X = X;
		this.Y = Y;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getX() {
		return X;
	}
	public void setX(int x) {
		X = x;
	}
	public int getY() {
		return Y;
	}
	public void setY(int y) {
		Y = y;
	}
	
	public double euclidianDistance(Node nodeB) {
	    double distX = Y - nodeB.getY();
	    double distY = X - nodeB.getX();
	    return Math.sqrt(distX*distX + distY*distY);
	}
	
	@Override
	public String toString() {
		return "Node [id=" + id + ", X=" + X + ", Y=" + Y + "]";
	}
}
