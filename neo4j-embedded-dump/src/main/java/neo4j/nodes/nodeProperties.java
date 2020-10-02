package neo4j.nodes;

import java.util.Map;

public class nodeProperties {

	private Map<String, Object> nodeProperties;

	public nodeProperties(Map<String, Object> nodeProperties) {
		this.nodeProperties = nodeProperties;
	}

	public nodeProperties() {
	}


	public Map<String, Object> getNodeProperties() {
		return this.nodeProperties;
	}

	public void setNodeProperties(Map<String, Object> nodeProperties) {
		this.nodeProperties = nodeProperties;
	}

}

