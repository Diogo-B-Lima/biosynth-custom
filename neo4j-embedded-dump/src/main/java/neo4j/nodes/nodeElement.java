package neo4j.nodes;

public class nodeElement {

	nodeIdentifier nodeIdentifier;
	nodeLabels nodeLabels;
	nodeProperties nodeProperties;



	public nodeElement(nodeIdentifier nodeIdentifier, nodeLabels nodeLabels, nodeProperties nodeProperties) {

		this.nodeIdentifier = nodeIdentifier;
		this.nodeLabels = nodeLabels;
		this.nodeProperties = nodeProperties;
	}

	public nodeElement() {
	}

	public nodeLabels getNodeLabels() {
		return nodeLabels;
	}
	public void setNodeLabels(nodeLabels nodeLabels) {
		this.nodeLabels = nodeLabels;
	}
	public nodeProperties getNodeProperties() {
		return nodeProperties;
	}
	public void setNodeProperties(nodeProperties nodeProperties) {
		this.nodeProperties = nodeProperties;
	}
	public nodeIdentifier getNodeIdentifier() {
		return nodeIdentifier;
	}
	public void setNodeIdentifier(nodeIdentifier nodeIdentifier) {
		this.nodeIdentifier = nodeIdentifier;
	}



}
