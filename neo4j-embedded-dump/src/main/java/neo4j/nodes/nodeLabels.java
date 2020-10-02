package neo4j.nodes;

import java.util.ArrayList;

public class nodeLabels {

	private ArrayList<String> nodeLabels;

	public nodeLabels(ArrayList<String> nodeLabels) {
		this.nodeLabels = nodeLabels;
	}

	public nodeLabels() {
	}

	public ArrayList<String> getNodeLabels() {
		return this.nodeLabels;
	}

	public void setNodeLabels(ArrayList<String> nodeLabels) {
		this.nodeLabels = nodeLabels;
	}

}
