package neo4j.relationships;

public class relationshipElement {


	relationshipIdentifier relationshipIdentifier;
	relationshipType relationshipType;
	relationshipProperties relationshipProperties;
	long startNodeId;
	long endNodeId;


	public relationshipElement(relationshipIdentifier relationshipIdentifier, relationshipType relationshipType, relationshipProperties relationshipProperties, long startNodeId, long endNodeId) {

		this.relationshipIdentifier = relationshipIdentifier;
		this.relationshipType = relationshipType;
		this.relationshipProperties = relationshipProperties;
		this.startNodeId = startNodeId;
		this.endNodeId = endNodeId;
	}

	public relationshipElement() {
	}

	public relationshipType getRelationshipType() {
		return relationshipType;
	}
	public void setRelationshipType(relationshipType relationshipType) {
		this.relationshipType = relationshipType;
	}
	public relationshipProperties getRelationshipProperties() {
		return relationshipProperties;
	}
	public void setRelationshipProperties(relationshipProperties relationshipProperties) {
		this.relationshipProperties = relationshipProperties;
	}
	public relationshipIdentifier getRelationshipIdentifier() {
		return relationshipIdentifier;
	}
	public void setRelationshipIdentifier(relationshipIdentifier relationshipIdentifier) {
		this.relationshipIdentifier = relationshipIdentifier;
	}

	public long getStartNodeId() {
		return startNodeId;
	}

	public void setStartNodeId(long startNodeId) {
		this.startNodeId = startNodeId;
	}

	public long getEndNodeId() {
		return endNodeId;
	}

	public void setEndNodeId(long endNodeId) {
		this.endNodeId = endNodeId;
	}


}
