package neo4j.relationships;

import java.util.Map;

public class relationshipProperties {

	private Map<String, Object> relationshipProperties;

	public relationshipProperties(Map<String, Object> relationshipProperties) {
		this.relationshipProperties = relationshipProperties;
	}

	public relationshipProperties() {
	}


	public Map<String, Object> getRelationshipProperties() {
		return this.relationshipProperties;
	}

	public void setRelationshipProperties(Map<String, Object> relationshipProperties) {
		this.relationshipProperties = relationshipProperties;
	}

}

