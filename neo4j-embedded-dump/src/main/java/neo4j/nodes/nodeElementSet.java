package neo4j.nodes;

import java.util.ArrayList;

public class nodeElementSet {

	ArrayList<nodeElement> nodeElementSet;

	public nodeElementSet(ArrayList<nodeElement> nodeElementSet) {

		this.nodeElementSet = nodeElementSet;
	}

	public nodeElementSet() {

		this.nodeElementSet = new ArrayList<nodeElement>();
	}

	public ArrayList<nodeElement> getNodeElementSet() {
		return nodeElementSet;
	}
	public void setNodeElementSet(ArrayList<nodeElement> nodeElementSet) {
		this.nodeElementSet = nodeElementSet;
	}



}
