package neo4j.relationships;

public class relationshipIdentifier {

	private long relationshipId;

	public relationshipIdentifier(long relationshipId) {
		this.relationshipId = relationshipId;
	}

	public relationshipIdentifier() {
	}

	public long getRelationshipId() {
		return this.relationshipId;
	}

	public void setRelationshipId(long relationshipId) {
		this.relationshipId = relationshipId;
	}

}
